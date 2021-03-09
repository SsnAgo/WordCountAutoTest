package auto.test.wordcount;

import cn.hutool.core.io.FileUtil;

import java.util.List;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/8
 * @since
 */
public class T {
    public static void main(String[] args) {
        List<String> strings = FileUtil.listFileNames("D:\\git\\WordCountAutoTest\\download\\1614954391268\\PersonalProject-Java\\890177\\src");
        for (int i = 0; i < strings.size(); i++) {
            System.out.println(FileUtil.getSuffix(strings.get(i)));
        }

    }
}
