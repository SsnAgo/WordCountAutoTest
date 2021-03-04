package auto.test.wordcount.executor;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/2
 * @since
 */
public interface Executor {
    /**
     * 编译程序
     *
     * @param src    源码目录
     */
    void compile(String src);
    /**
     * 执行程序
     *
     * @param src    源码目录
     * @param input  测试用例
     */
    void exec(String src, String input);
}
