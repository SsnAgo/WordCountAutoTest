package auto.test.wordcount.executor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static auto.test.wordcount.utils.CmdUtil.cmd;

/**
 * Python执行程序
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/5
 * @since
 */
public class PythonExecutor implements Executor {
    @Override
    public void compile(String src) {

    }

    @Override
    public List<String> suffix() {
        return Collections.singletonList("py");
    }

    @Override
    public void exec(String src, String input) {
        try {
            if (null != input && !input.trim().equals("")) {
                cmd("python " + src + " " + input);
            } else {
                cmd("python " + src);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
