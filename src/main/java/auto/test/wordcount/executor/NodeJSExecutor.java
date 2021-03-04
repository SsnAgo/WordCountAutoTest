package auto.test.wordcount.executor;

import java.io.IOException;

import static auto.test.wordcount.utils.CmdUtil.cmd;
import static auto.test.wordcount.utils.FileUtil.getFolder;

/**
 * NodeJS执行程序
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/2
 * @since
 */
public class NodeJSExecutor implements Executor {
    @Override
    public void exec(String src, String input) {
        // TODO
        try {
            cmd("node " + src + " " +  input.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
