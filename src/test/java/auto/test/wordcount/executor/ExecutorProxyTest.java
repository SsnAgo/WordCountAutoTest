package auto.test.wordcount.executor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * 动态代理 获得运行时间
 */
public class ExecutorProxyTest {
    private static final Logger log = LoggerFactory.getLogger(ExecutorProxyTest.class);

    @Test
    public void exec() {
        Executor executor = new JavaExecutor();
        ExecutorProxy executorProxy = new ExecutorProxy(executor);
        Executor proxyInstance = (Executor) Proxy.newProxyInstance(executor.getClass().getClassLoader(),
                new Class<?>[]{Executor.class}, executorProxy);

        // Executor m = (Executor) Proxy.newProxyInstance(Executor.class.getClassLoader(),
        //         new Class[]{Executor.class},
        //         new ExecutorProxy(executor)
        // );
        executor.compile("C:\\git\\wordcount\\src\\WordCount.java");
        proxyInstance.exec("C:\\git\\wordcount\\src\\WordCount.java", "C:\\git\\rural.txt");
        log.info("程序耗时：" + executorProxy.getRuntime());
    }
}
