package auto.test.wordcount.executor.impl;

import auto.test.wordcount.executor.Executor;

/**
 * NodeJS执行程序
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/2
 * @since
 */
public class NodeJSExecutor implements Executor {
    @Override
    public void compile(String src) {

    }

    @Override
    public long exec(String src, String input) {
        // TODO
        return -1;
    }

    @Override
    public String mainFile() {
        return "main.js";
    }
}
