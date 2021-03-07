package auto.test.wordcount;

import auto.test.wordcount.executor.Executor;
import auto.test.wordcount.executor.ExecutorProxy;
import auto.test.wordcount.executor.JavaExecutor;
import auto.test.wordcount.judge.Judge;
import auto.test.wordcount.judge.JudgeResult;
import auto.test.wordcount.judge.WordCountJudge;
import auto.test.wordcount.judge.WordCountTestCasesGenerator;
import auto.test.wordcount.report.ReportData;
import auto.test.wordcount.report.WordCountReportData;
import auto.test.wordcount.utils.FileUtil;
import auto.test.wordcount.utils.GitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
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

        for (String studentId : src.keySet()) {
            // main方法所在文件
            Executor executor = findExecutor(src.get(studentId));
            String mainFunFile = mainFunctionFilesLocation(src.get(studentId));
            executor.compile(mainFunFile);
            for (String caseId : testCases.keySet()) {
                // TODO 以下代码需要优化！！！！！
                Map<String, String> map = testCases.get(caseId);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                Iterator<Map.Entry<String, String>> iterator = entries.iterator();
                String key = null;
                String answer;
                // iterator有且仅有一条记录
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    key = next.getKey();
                    answer = next.getValue();
                }
                executor.exec(mainFunFile, key + " " + findOutput(src.get(studentId), caseId));
            }
        }



       /*
        Map<String, Map<String, String>> answer = new HashMap<>();

        List<JudgeResult> results = new ArrayList<>();
       // TODO
        // studentId 根据PersonalProject-Java目录下名称生成
        String studentId = "890177";

        // TODO
        // D:\git\WordCountAutoTest\download\1614926251715 我们自动创建
        // PersonalProject-Java\890177\src  这个目录是作业的规范目录
        String srcPath = "D:\\git\\WordCountAutoTest\\download\\1614926251715\\PersonalProject-Java\\890177\\src";
        String mainFile = srcPath + "\\WordCount.java";


        // TODO 如果有时间多，可以考虑用DataGenerator来生成测试数据, 用对数器来生成答案
        // 为下一步做准备

        // TODO 如果上一步搞定了，那么这两个目录下的数据可以随机生成，
        // 如果上一步时间不够，可以将tests目录和stds目录我们先规定死，测试数据也预先规定死
        String tests = "D:\\git\\WordCountAutoTest\\archives\\tests";
        String stds = "D:\\git\\WordCountAutoTest\\archives\\stds";
        // 以上为准备信息

        // 开始测试一个用例
        String case1 = tests + File.separator + "rural.txt";
        String answer1 = stds + File.separator + "std1.txt";
        String outputPath = srcPath + "\\output.txt";
        // 执行代码

        Executor executor = new JavaExecutor();
        ExecutorProxy executorProxy = new ExecutorProxy(executor);
        // 动态代理获取运行时间
        Executor proxyInstance = (Executor) Proxy.newProxyInstance(executor.getClass().getClassLoader(),
                new Class<?>[]{Executor.class}, executorProxy);
        proxyInstance.compile(mainFile);
        proxyInstance.exec(mainFile, case1 + " " + outputPath);
        // 程序运行时间
        Long runtime = executorProxy.getRuntime();
        log.info("程序运行时间 -- > " + runtime);


        // Judge代码
        Judge judge = new WordCountJudge();
        Result result = judge.judge(outputPath, answer1);

        // TODO judge to result

        ReportData reportData = new WordCountReportData(results);
        // 导出到CSV
        exportToCSV(reportData, generateResultPath(repo));*/
    }

    private static String findOutput(String src, String caseId) {
        String parent = cn.hutool.core.io.FileUtil.getParent(src, 1);
        return new File(new File(parent, "output"), caseId + ".txt").getAbsolutePath();
    }

    // TODO
    // 查看srcLocation的后缀名来选择用哪个Executor
    // 默认先用JavaExecutor
    private static Executor findExecutor(String srcLocation) {
        Executor executor = new JavaExecutor();
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
        /*String parent = cn.hutool.core.io.FileUtil.getParent(repo, 1);
        WordCountTestCasesGenerator generator = new WordCountTestCasesGenerator(testCaseNum, parent);
        return generator.getTestCases();*/
        Map<String, Map<String, String>> map = new HashMap<>();
        String cases = "C:\\git\\WordCountAutoTest\\download\\1614954391268\\cases\\";
        String answers = "C:\\git\\WordCountAutoTest\\download\\1614954391268\\answers\\";
        for (int i = 1; i <= testCaseNum; i++) {
            Map<String, String> item = new HashMap<>();
            item.put(cases + String.valueOf(i) + ".txt", answers + String.valueOf(i) + ".txt");
            map.put(String.valueOf(i), item);
        }
        return map;
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
        return repo;
    }


    private static String generateResultPath(String repo) {
        // repo : D:\\git\\download\\1614926251715\\PersonalProject-Java
        // -> D:\\git\\download\\1614926251715\\
        return repo.substring(0, repo.lastIndexOf("\\")) + File.separator + "result" + File.separator + "result.csv";
    }
}
