package auto.test.wordcount;

import auto.test.wordcount.judge.WordCountJudge;
import auto.test.wordcount.judge.WordCountJudge2;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author <a href="mailto:18965375150@163.com">siberia0015</a>
 * @date 2021/3/6
 * @since
 */
public class CasesTest {
    /**
     * 测试生成随机题目和答案
     */
    @Test
    public void casesTest(){
        int num = 5;
        WordCountJudge2 test = new WordCountJudge2("", "", num);
        System.out.println(test.getAnswer());
    }
}
