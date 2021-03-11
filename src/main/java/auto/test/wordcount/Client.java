package auto.test.wordcount;

import auto.test.wordcount.executor.Executor;
import auto.test.wordcount.judge.*;
import auto.test.wordcount.model.TestCase;
import auto.test.wordcount.report.ReportData;
import auto.test.wordcount.report.WordCountReportData;
import auto.test.wordcount.utils.ClassUtils;
import auto.test.wordcount.utils.FileUtil;
import auto.test.wordcount.utils.GitUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static auto.test.wordcount.utils.CSVUtil.exportToCSV;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/2
 * @since
 */
public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);


    /**
     * 在download文件夹下新建一个以当前时间戳为文件名的文件夹，然后把项目克隆到这个目录
     * 返回克隆后，仓库的绝对路径地址：
     * 比如：D:\\git\\download\\1614926251715\\
     * git地址是：https://github.com/kofyou/PersonalProject-Java
     * 则调用这个方法，会在D:\\git\\download\\1614926251715\\目录下生成一个 PersonalProject-Java仓库
     * 返回：D:\\git\\download\\1614926251715\\PersonalProject-Java 这个路径
     *
     * @param url git地址 注意：必须是公有仓库!!
     * @return 克隆后的绝对路径
     */
    public static String clone(String url) {
        while (true) {
            try {
                File downloadFolder = new File("download");
                if (!downloadFolder.exists()) {
                    downloadFolder.mkdir();
                }
                // 以当前时间戳新建一个文件夹，防止冲突
                String subFolder = String.valueOf(System.currentTimeMillis());
                FileUtil.createFolder(downloadFolder.getAbsolutePath(), subFolder);
                String allSourceCodePath = downloadFolder.getAbsolutePath() + File.separator + subFolder;
                GitUtil.cloneRepo(url, allSourceCodePath, false);
                String repo = allSourceCodePath + File.separator + url.replace(".git", "").substring(url.lastIndexOf("/") + 1);
                log.info("clone to local folder {}", repo);
                return repo;
            } catch (Throwable e) {
                log.error("clone {} , error {}", url, e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // 克隆代码仓库
        // 由于网络原因，clone经常失败，可以先手动下载，如果要自动下载，则把needPath = true
        String repo = preparePath(false);

        // 默认测试用例的数量
        int testCaseNum = 10;

        // 如果用例准备好了，请返回准备好的用例信息
        Map<String, TestCase> testCases = generateTestCases(repo, testCaseNum);

        // 遍历仓库下的所有学生学号命名的文件夹，在这些文件夹下面建好一个output文件夹，用于存放学生程序的输出结果文件
        generateOutput(repo, testCaseNum);

        // Key为学号，Value是该学号学生的代码路径
        Map<String, String> src = generateSrc(repo);


        Judge judge = new WordCountJudge();
        List<JudgeResult> results = new ArrayList<>();
        for (String studentId : src.keySet()) {
            // main方法所在文件
            Executor executor;
            try {
                executor = findExecutor(src.get(studentId));
            } catch (Exception e) {
                log.error("not executor found studentId: {}", studentId);
                continue;
            }
            String mainFunFile = mainFunctionFilesLocation(src.get(studentId), executor.mainFile());
            executor.compile(mainFunFile);
            Map<String, String> commitMessage = commitMessageOfPerStudent(repo, studentId);
            int commitTimes = commitMessage.size();
            String commitDetails = format(commitMessage);
            JudgeResult judgeResult = new JudgeResult(studentId, new ArrayList<>(), String.valueOf(commitTimes), commitDetails);
            for (String caseId : testCases.keySet()) {
                //储存用例地址和答案地址
                TestCase testCase = testCases.get(caseId);
                String testCaseLocation = testCase.getCaseLocation();
                String answerLocation = testCase.getAnswerLocation();
                String outputPath = findOutput(src.get(studentId), caseId);
                log.info("开始执行 学号为 {} 的作业 执行的测试用例为： {} ", studentId, caseId);
                long runtime = executor.exec(mainFunFile, testCaseLocation + " " + outputPath);
                log.info("学号为 {} 的作业 执行  {} 用例完毕， 执行时间为 {}ms 接下来开始测评...", studentId, caseId, runtime);
                Result result = judge.judge(outputPath, answerLocation);
                log.info("学号为 {} 的作业 测评结果是：{}", studentId, result);

                JudgeItem judgeItem = new JudgeItem(String.valueOf(result.getScore()), String.valueOf(runtime));
                judgeResult.getScore().add(judgeItem);
            }
            results.add(judgeResult);
        }
        // export to csv
        ReportData reportData = new WordCountReportData(results);
        // 导出到CSV
        exportToCSV(reportData, generateResultPath(repo));
    }

    private static String format(Map<String, String> commitMessage) {
        return JSON.toJSON(commitMessage).toString();
    }

    private static Map<String, String> commitMessageOfPerStudent(String repo, String studentId) {
        List<String> history = GitUtil.history(repo, studentId);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < history.size(); i++) {
            map.put(String.valueOf(i), history.get(i));
        }
        return map;
    }

    /**
     * 程序在执行caseId后对应的输出位置是哪里
     *
     * @param src
     * @param caseId
     * @return
     */
    private static String findOutput(String src, String caseId) {
        String parent = cn.hutool.core.io.FileUtil.getParent(src, 1);
        return new File(new File(parent, "output"), caseId + ".txt").getAbsolutePath();
    }


    private static Executor findExecutor(String srcLocation) throws Exception {
        // srcLocation : C:\git\WordCountAutoTest\download\1614954391268\PersonalProject-Java\890177\src
        // 需要遍历srcLocation目录下的文件，找到主执行函数所在的文件的文件名，这个文件名配置在每个Executor的mainFile()方法里面
        List<String> sources = cn.hutool.core.io.FileUtil.listFileNames(srcLocation);
        Executor executor = null;
        Set<Class<?>> classes;
        try {
            classes = ClassUtils.getClasses("auto.test.wordcount.executor.impl");
        } catch (IOException e) {
            log.error("find executors error {}", e.getMessage());
            throw new Exception("fail to load executors");
        }
        for (Class<?> aClass : classes) {
            try {
                final String mainFile = (String) aClass.getMethod("mainFile").invoke(aClass.newInstance());
                if (oneOf(mainFile, sources)) {
                    executor = (Executor) aClass.newInstance();
                    break;
                }
            } catch (Exception e) {
                log.warn("find executor.mainfile error");
                break;
            }

        }
        if (null == executor) {
            log.error("not found executor");
            throw new Exception("not found executor");
        }
        return executor;
    }

    private static boolean oneOf(String mainFile, List<String> sources) {
        return sources.contains(mainFile);
    }

    // main方法所在路径
    public static String mainFunctionFilesLocation(String src, String mainFile) {
        return new File(src, mainFile).getAbsolutePath();
    }

    private static Map<String, String> generateSrc(String repo) {
        Map<String, String> src = new HashMap<>();
        List<String> allFolders = FileUtil.listFolders(repo);
        for (String folder : allFolders) {
            src.put(new File(folder).getName(), new File(folder, "src").getAbsolutePath());
        }
        return src;
    }

    // repo : C:\git\WordCountAutoTest\download\1614954391268\PersonalProject-Java
    // 则生成测试用例的文件夹为 ： C:\git\WordCountAutoTest\download\1614954391268\cases
    // 对应答案的文件夹为：C:\git\WordCountAutoTest\download\1614954391268\answers
    private static Map<String, TestCase> generateTestCases(String repo, int testCaseNum) {
        String parent = cn.hutool.core.io.FileUtil.getParent(repo, 1);
        WordCountTestCasesGenerator generator = new WordCountTestCasesGenerator(testCaseNum, parent, 1000000, 100);
        return generator.getTestCases();
    }

    private static void generateOutput(String repo, int testCaseNum) {
        List<String> subFolders = FileUtil.listFolders(repo);
        for (String sub : subFolders) {
            File f = new File(sub, "output");
            if (!f.exists()) {
                f.mkdir();
            }
            for (int i = 1; i <= testCaseNum; i++) {
                try {
                    new File(f.getAbsolutePath(), i + ".txt").createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static String preparePath(boolean needClone) {
        String repo;
        if (needClone) {
            repo = clone("https://github.com/kofyou/PersonalProject-Java.git");
        } else {
            // 手动下载，指定下载仓库的目录
            repo = "D:\\git\\WordCountAutoTest\\download\\1615421924089\\PersonalProject-Java";
        }
        return repo;
    }

    private static String generateResultPath(String repo) {
        // repo : D:\\git\\download\\1614926251715\\PersonalProject-Java
        // -> D:\\git\\download\\1614926251715\\
        return repo.substring(0, repo.lastIndexOf("\\")) + File.separator + "result" + File.separator + "result.csv";
    }
}
