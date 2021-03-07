package auto.test.wordcount;

import auto.test.wordcount.executor.Executor;
import auto.test.wordcount.executor.ExecutorProxy;
import auto.test.wordcount.executor.JavaExecutor;
import auto.test.wordcount.judge.*;
import auto.test.wordcount.report.ReportData;
import auto.test.wordcount.report.WordCountReportData;
import auto.test.wordcount.utils.ClassUtils;
import auto.test.wordcount.utils.FileUtil;
import auto.test.wordcount.utils.GitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static auto.test.wordcount.utils.CSVUtil.exportToCSV;
import static java.lang.reflect.Proxy.newProxyInstance;

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
        try {
            File downloadFolder = new File("download");
            if (!downloadFolder.exists()) {
                downloadFolder.mkdir();
            }
            String subFolder = String.valueOf(System.currentTimeMillis());
            FileUtil.createFolder(downloadFolder.getAbsolutePath(), subFolder);
            String allSourceCodePath = downloadFolder.getAbsolutePath() + File.separator + subFolder;
            GitUtil.cloneRepo(url, allSourceCodePath, false);
            return allSourceCodePath + File.separator + url.replace(".git", "").substring(url.lastIndexOf("/") + 1);
        } catch (Exception e) {
            log.error("clone {} , error {}", url, e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {

        // 克隆代码仓库
        // 由于网络原因，clone经常失败，可以先手动下载，如果要自动下载，则把needPath = true
        String repo = preparePath(false);
        if (repo == null) {
            log.error("请设置仓库目录");
            return;
        }

        // 默认测试用例的数量
        int testCaseNum = 5;

        // 如果用例准备好了，请返回准备好的用例信息
        Map<String, Map<String, String>> testCases = generateTestCases(repo, testCaseNum);

        // 遍历仓库下的所有学生学号命名的文件夹，在这些文件夹下面建好一个output文件夹，用于存放学生程序的输出结果文件
        generateOutput(repo, testCaseNum);

        // Key为学号，Value是该学号学生的代码路径
        Map<String, String> src = generateSrc(repo);
        // 根据src目录获取对应的Executor，默认是Java Executor


        Judge judge = new WordCountJudge();
        List<JudgeResult> results = new ArrayList<>();
        for (String studentId : src.keySet()) {
            // main方法所在文件
            Executor executor = null;
            try {
                executor = findExecutor(src.get(studentId));
            } catch (Exception e) {
                log.error("not executor found studentId: {}", studentId);
                continue;
            }
            String mainFunFile = mainFunctionFilesLocation(src.get(studentId));
            executor.compile(mainFunFile);


            JudgeResult judgeResult = new JudgeResult(studentId, new ArrayList<>());

            for (String caseId : testCases.keySet()) {
                // TODO 以下代码需要优化！！！！！
                // 重新设计testCases的数据结构
                // 不应该用Map<String,Map<String,String>>
                // 用Map<String, Node> 会比较方便
                // 其中Node存答案地址，和测试用例的地址即可
                Map<String, String> map = testCases.get(caseId);
                Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                String testCaseLocation = null;
                String answerLocation = null;
                // iterator有且仅有一条记录
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    testCaseLocation = next.getKey();
                    answerLocation = next.getValue();
                }
                String outputPath = findOutput(src.get(studentId), caseId);
                executor.exec(mainFunFile, testCaseLocation + " " + outputPath);

                // TODO
                // 这里的数据结构设计也可以优化
                Result result = judge.judge(outputPath, answerLocation);
                JudgeItem judgeItem = new JudgeItem(String.valueOf(result.getScore()), "32");
                judgeResult.getScore().add(judgeItem);
            }
            results.add(judgeResult);
        }

        // export to csv
        ReportData reportData = new WordCountReportData(results);
        // 导出到CSV
        exportToCSV(reportData, generateResultPath(repo));
    }


    private static String findOutput(String src, String caseId) {
        String parent = cn.hutool.core.io.FileUtil.getParent(src, 1);
        return new File(new File(parent, "output"), caseId + ".txt").getAbsolutePath();
    }

    // 查看srcLocation的后缀名来选择用哪个Executor
    private static Executor findExecutor(String srcLocation) throws Exception {
        // FIXME
        // srcLocation : C:\git\WordCountAutoTest\download\1614954391268\PersonalProject-Java\890177\src
        // 需要遍历srcLocation目录下的文件，看下是以什么结尾的
        String suffix = srcLocation.substring(srcLocation.lastIndexOf(".") + 1);
        Executor executor = null;
        Set<Class<?>> classes;
        try {
            classes = ClassUtils.getClasses("auto.test.wordcount.executor");
        } catch (IOException e) {
            classes = new HashSet<>();
            log.error("find executors error");
        }
        out:
        for (Class<?> aClass : classes) {
            try {
                final Method getSuffix = aClass.getMethod("suffix");
                final List<String> suffixs = (List<String>) getSuffix.invoke(aClass.newInstance());
                for (String s : suffixs) {
                    if (s.equals(suffix)) {
                        executor = (Executor) aClass.newInstance();
                        break out;
                    }
                }
            } catch (Exception e) {
                log.warn("find executor.suffix error");
                break;
            }

        }
        if (null == executor) {
            log.error("not found executor");
            throw new Exception("not found executor");
        }
        ExecutorProxy executorProxy = new ExecutorProxy(executor);
        // 动态代理获取运行时间
        return (Executor) newProxyInstance(executor.getClass().getClassLoader(), new Class<?>[]{Executor.class}, executorProxy);
    }

    // main方法所在路径
    public static String mainFunctionFilesLocation(String src) {
        return new File(src, "WordCount.java").getAbsolutePath();
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
    private static Map<String, Map<String, String>> generateTestCases(String repo, int testCaseNum) {
        // TODO
        String parent = cn.hutool.core.io.FileUtil.getParent(repo, 1);
        WordCountTestCasesGenerator generator = new WordCountTestCasesGenerator(testCaseNum, parent);
        return generator.getTestCases();
       /* Map<String, Map<String, String>> map = new HashMap<>();
        String cases = "C:\\git\\WordCountAutoTest\\download\\1614954391268\\cases\\";
        String answers = "C:\\git\\WordCountAutoTest\\download\\1614954391268\\answers\\";
        for (int i = 1; i <= testCaseNum; i++) {
            Map<String, String> item = new HashMap<>();
            item.put(cases + String.valueOf(i) + ".txt", answers + String.valueOf(i) + ".txt");
            map.put(String.valueOf(i), item);
        }
        return map;*/
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
        String repo = "";
        if (needClone) {
            repo = clone("https://github.com/kofyou/PersonalProject-Java.git");
        } else {
            // 手动下载，指定下载仓库的目录
            repo = "C:\\git\\WordCountAutoTest\\download\\1614954391268\\PersonalProject-Java";
        }
        if (repo == null) {
            log.error("fail to clone project!!!!");
            return null;
        }
        FileUtil.deleteFile(new File(repo, ".git"));
        FileUtil.deleteFile(new File(repo, "example"));
        return repo;
    }


    private static String generateResultPath(String repo) {
        // repo : D:\\git\\download\\1614926251715\\PersonalProject-Java
        // -> D:\\git\\download\\1614926251715\\
        return repo.substring(0, repo.lastIndexOf("\\")) + File.separator + "result" + File.separator + "result.csv";
    }
}
