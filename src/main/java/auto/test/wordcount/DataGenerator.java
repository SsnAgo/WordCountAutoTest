package auto.test.wordcount;

import java.util.Random;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/3
 * @since
 */
public class DataGenerator {
    /**
     * 生成指定长度的随机字符串
     *
     * @param len
     * @return
     */
    public static String generateContent(int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(generateASCII());
        }
        return builder.toString();
    }


    // Math.random() [0,1) ->
    // TODO 概率是否需要调整；单词重复率低。 @kofyou
    private static char generateASCII() {
        if (Math.random() > 0.3) {
            // 70%的概率生成大小写字母
            if (Math.random() > 0.5) {
                // 生成大写字母
                // A-Z: 65 ~ 90
                return (char) (new Random().nextInt(26) + 65);
            } else {
                // 生成小写字母
                // a-z: 97 ~ 122
                return (char) (new Random().nextInt(26) + 97);
            }
        }
        // 30%的概率是其他字符
        int noneAlpha = (int) (Math.random() * 127);
        double ctrlChance = (double) noneAlpha / (double) 31;
        if (ctrlChance < 0.2) {
            noneAlpha = (int) ('\r');
        } else if (ctrlChance < 0.4) {
            noneAlpha = (int) ('\n');
        } else if (ctrlChance < 1) {
            noneAlpha = (int) ('\t');
        }
        return (char) (noneAlpha);
    }

}
