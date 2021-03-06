package auto.test.wordcount.judge;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class WordCountJudgeTest {

    @Test
    public void checkValid() throws FileNotFoundException {
        String standardPath = "C:\\git\\WordCountAutoTest\\download\\1614954341207\\stds\\std.txt";
        String filePath = "C:\\git\\WordCountAutoTest\\download\\1614954341207\\stds\\student.txt";
        int score = WordCountJudge.checkValid(standardPath, filePath);
        System.out.println(score);
    }
}