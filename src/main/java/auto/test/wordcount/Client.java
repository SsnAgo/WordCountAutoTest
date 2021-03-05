package auto.test.wordcount;

import auto.test.wordcount.executor.Executor;
import auto.test.wordcount.executor.JavaExecutor;
import auto.test.wordcount.judge.Judge;
import auto.test.wordcount.judge.JudgeItem;
import auto.test.wordcount.judge.JudgeResult;
import auto.test.wordcount.judge.WordCountJudge;
import auto.test.wordcount.report.ReportData;
import auto.test.wordcount.report.WordCountReportData;
import auto.test.wordcount.utils.CSVUtil;
import auto.test.wordcount.utils.FileUtil;
import auto.test.wordcount.utils.GitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String repo = clone("https://github.com/kofyou/PersonalProject-Java.git");
        if (repo == null) {
            log.error("fail to clone project!!!!");
            return;
        }
        // TODO
        // 遍历repo这个绝对路径下的所有文件夹，拿到学生的学号信息


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
        executor.compile(mainFile);
        executor.exec(mainFile, case1 + " " + outputPath);


        // Judge代码
        Judge judge = new WordCountJudge();
        Result result = judge.judge(outputPath, answer1);

        // TODO judge to result
        List<JudgeResult> results = new ArrayList<>();
        ReportData reportData = new WordCountReportData(results);
        // 导出到CSV
        exportToCSV(reportData, generateResultPath(repo));
    }


    private static String generateResultPath(String repo) {
        // repo : D:\\git\\download\\1614926251715\\PersonalProject-Java
        // -> D:\\git\\download\\1614926251715\\
        return repo.substring(0, repo.lastIndexOf("\\")) + File.separator + "result" + File.separator + "result.csv";
    }
}
