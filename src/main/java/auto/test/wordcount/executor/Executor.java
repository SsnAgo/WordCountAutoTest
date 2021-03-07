package auto.test.wordcount.executor;

import java.util.List;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/2
 * @since
 */
public interface Executor {
    /**
     * 编译程序
     *
     * @param src 源码目录
     */
    void compile(String src);

    /**
     * 执行程序
     *
     * @param src  源码目录
     * @param args 命令参数
     */
    void exec(String src, String args);

    /**
     * 返回语言源代码的后缀
     * @return 后缀
     */
     List<String> suffix();
}
