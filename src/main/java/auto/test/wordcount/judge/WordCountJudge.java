package auto.test.wordcount.judge;

import auto.test.wordcount.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

import static auto.test.wordcount.utils.FileUtil.content;

/**
 * wordcount作业的Judge
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/4
 * @since
 */
public class WordCountJudge implements Judge {
    private static final Logger log = LoggerFactory.getLogger(WordCountJudge.class);

    /**
     * 评分，参考：WordCountTester#checkValid 方法
     *
     * @param resultPath 结果文件路径
     * @param answerPath 答案文件路径
     * @return
     */
    @Override
    public Result judge(String resultPath, String answerPath) {
        Result result = new Result();
        result.result(resultPath).answer(answerPath);
        try {
            int i = checkValid(resultPath, answerPath);
            result.score(i);
        } catch (IOException e) {
            log.error("fail to check :{} {}", resultPath, e.getMessage());
            result.score(-1);
        }
        return result;
    }


    //Overview: 将标程standard生成的result文件和用户生成的result文件进行对比，完全一致则
    public static int checkValid(String standardPath, String filePath) throws FileNotFoundException {
        int count = 0;
        InputStreamReader isrStandard = new InputStreamReader(new FileInputStream(standardPath));
        BufferedReader buffStandard = new BufferedReader(isrStandard);

        InputStreamReader isrTest = new InputStreamReader(new FileInputStream(filePath));
        BufferedReader buffTest = new BufferedReader(isrTest);

        String strStandard, strTest;
        try {
            strStandard = buffStandard.readLine();
            strTest = buffTest.readLine();
            if (strStandard.equals(strTest)) { //第一行characters: n是相同的
                count += 1; //得1分
            }
            strStandard = buffStandard.readLine();
            strTest = buffTest.readLine();
            if (strStandard.equals(strTest)) { //第二行words: m是相同的
                count += 2; //得2分
            }
            strStandard = buffStandard.readLine();
            strTest = buffTest.readLine();
            if (strStandard.equals(strTest)) { //第三行lines: m是相同的
                count += 2; //得2分
            }
            boolean checkMain = true; //检查核心部分是否正确(求词频、求词组、求特定出现量的单词)
            while ((strStandard = buffStandard.readLine()) != null) {
                strTest = buffTest.readLine();
                if (!strStandard.equals(strTest)) {
                    checkMain = false;
                    break;
                }
            }
            if (checkMain) { //核心部分占15分
                count += 15;
            }
            return count;
        } catch (Exception e) {
            log.error("execute error found {} {}", e.getMessage(), standardPath);
            return -1; //其它错误
        }
    }
}

