package auto.test.wordcount.judge;

import auto.test.wordcount.DataGenerator;
import auto.test.wordcount.utils.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:18965375150@163.com">siberia0015</a>
 * @date 2021/3/6
 * @since
 */
public class WordCountJudge2 {
    private String srcPath;
    private String studentNo;
    private int nums;
    private Map<String, Map<String, String>> answer;
    public Map<String, Map<String, String>> getAnswer() {return answer;}
    public WordCountJudge2(String srcPath, String studentNo, int nums){
        this.srcPath = srcPath;
        this.studentNo = studentNo;
        this.nums = nums;
        answer  = testCases(nums);
    }

    private Map<String, Map<String, String>> testCases(int nums){
        // TODO
        // 生成nums数量的题目
        // config的key是题目编号，value的key是题目位置，value的value是答案的位置
        // 题目和答案位置一律放在某个写死的路径里面
        // 目前先只管生成题目，答案一律为空文件

        // 生成目录
        String stamp = String.valueOf(System.currentTimeMillis());
        File caseFolder = new File("download\\" + stamp + "\\cases\\");
        if (!caseFolder.exists()) {
            caseFolder.mkdirs();
        }
        File answerFolder = new File("download\\" + stamp + "\\answers\\");
        if (!answerFolder.exists()) {
            answerFolder.mkdirs();
        }
        String casePath = "download\\" + stamp + "\\cases\\";
        String answerPath = "download\\" + stamp + "\\answers\\";
        int maxLength = 1000000;
        int minLength = 100;
        Map<String, Map<String,String>> config = new HashMap<>(nums);
        int count = 1;
        while (count <= nums) {
            Map<String, String> question = new HashMap<>();
            question.put(casePath+count+".txt", answerPath+count+".txt");
            config.put(count + "", question);
            // 写入题目文件
            int length = (int)(1+Math.random()*(maxLength-minLength+1)); //生成随机长度
            String content = DataGenerator.generateContent(length); //生成随机字符串
            BufferedWriter bw;
            try{
                // String encoding = "UTF-8";
                File file = new File(casePath+count+".txt");
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
            File file = new File(answerPath+count+".txt");
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
