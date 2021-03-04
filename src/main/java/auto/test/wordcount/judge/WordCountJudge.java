package auto.test.wordcount.judge;

import auto.test.wordcount.Result;
import auto.test.wordcount.utils.FileUtil;

import java.io.IOException;

import static auto.test.wordcount.utils.FileUtil.isSameContent;

/**
 * wordcount作业的Judge
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/4
 * @since
 */
public class WordCountJudge implements Judge {
    /**
     * @param resultPath 结果文件路径
     * @param answerPath 答案文件路径
     * @return
     */
    @Override
    public Result judge(String resultPath, String answerPath) {
        Result result = new Result();
        result.result(resultPath).answer(answerPath);
        try {
            result.pass(isSameContent(resultPath, answerPath, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            result.pass(false);
        }
        return result;
    }
}

