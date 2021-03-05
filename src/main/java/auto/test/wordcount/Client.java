package auto.test.wordcount;

import auto.test.wordcount.executor.Executor;
import auto.test.wordcount.executor.JavaExecutor;
import auto.test.wordcount.judge.Judge;
import auto.test.wordcount.judge.WordCountJudge;
import auto.test.wordcount.utils.CSVUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/2
 * @since
 */
public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        // TODO 以下注释的方法是clone仓库，由于github不稳定，clone时好时坏，以下代码在网络通顺的时候是没问题的
        //  所以先假设代码clone下来了，
        // 目录结构  download/1614926251715/PersonalProject-Java/学号/src/WordCount.java
        /*File downloadFolder = new File("download");
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }
        String subFolder = String.valueOf(System.currentTimeMillis());

        FileUtil.createFolder(downloadFolder.getAbsolutePath(), subFolder);
        String allSourceCodePath = downloadFolder.getAbsolutePath() + File.separator + subFolder;
        String url = "https://github.com/GreyZeng/wordcount.git";
        GitUtil.cloneRepo(url, allSourceCodePath, false);*/

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


        // 导出到CSV
        String resultPath = "D:\\git\\WordCountAutoTest\\download\\1614926251715" + File.separator + "result.csv";
        ReportData reportData = new ReportData() {
            @Override
            public String[] headers() {
                return new String[]{"name", "score"};
            }

            @Override
            public List<List<String>> records() {
                final List<List<String>> records = new ArrayList<>();
                // TODO 这里需要考虑通过率
                records.add(Arrays.asList(studentId, result.isPass() ? "100" : "0"));
                return records;
            }
        };
        CSVUtil.exportToCSV(reportData, resultPath);


    }
}
