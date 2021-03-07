package auto.test.wordcount.judge;

import auto.test.wordcount.DataGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成wordcount程序的测试用例
 *
 * @author <a href="mailto:18965375150@163.com">siberia0015</a>
 * @date 2021/3/6
 * @since
 */
public class WordCountTestCasesGenerator {
    private int nums;
    private String repo;
    private Map<String, Map<String, String>> testCases;

    public Map<String, Map<String, String>> getTestCases() {
        return testCases;
    }

    public WordCountTestCasesGenerator(int nums, String repo) {
        this.nums = nums;
        this.repo = repo;
        this.testCases = testCases();
    }

    // TODO 如果用例和答案准备好了，请返回准备好的用例和答案信息，不需要重新新建测试用例和答案
    private Map<String, Map<String, String>> testCases() {
        // 生成nums数量的题目
        // config的key是题目编号，value的key是题目位置，value的value是答案的位置
        // 题目和答案位置一律放在某个写死的路径里面
        // 目前先只管生成题目，答案一律为空文件

        // 生成目录
        File root = new File(repo);
        if (!root.exists()) {
            root.mkdir();
        }

        File caseFolder = new File(root.getAbsolutePath(), "cases");// new File("download\\" + stamp + "\\cases\\");
        if (!caseFolder.exists()) {
            caseFolder.mkdirs();
        }
        File answerFolder = new File(root.getAbsolutePath(), "answers");
        if (!answerFolder.exists()) {
            answerFolder.mkdirs();
        }
        int maxLength = 1000000;
        int minLength = 100;
        Map<String, Map<String, String>> config = new HashMap<>(nums);
        int count = 1;
        while (count <= nums) {
            Map<String, String> question = new HashMap<>();
            question.put(caseFolder + File.separator + count + ".txt", answerFolder + File.separator + count + ".txt");
            config.put(count + "", question);
            // 写入题目文件
            int length = (int) (1 + Math.random() * (maxLength - minLength + 1)); //生成随机长度
            String content = DataGenerator.generateContent(length); //生成随机字符串
            BufferedWriter bw;
            try {
                // String encoding = "UTF-8";
                File file = new File(caseFolder, count + ".txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //写入答案文件
            File file = new File(answerFolder, count + ".txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }

        return config;
    }

}
