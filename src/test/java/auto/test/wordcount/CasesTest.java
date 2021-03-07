package auto.test.wordcount;

import auto.test.wordcount.judge.WordCountTestCasesGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;

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
        File tmp = new File("download");
        tmp.mkdir();

        WordCountTestCasesGenerator test = new WordCountTestCasesGenerator(num,tmp.getAbsolutePath());
        System.out.println(test.getTestCases());
    }
    /**
     * 测试
     */
    @Test
    public void test(){

        System.out.println("");
    }
}
