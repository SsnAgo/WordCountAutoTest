package auto.test.wordcount.executor;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class JavaExecutorTest {

    @Test
    public void exec() {
        Executor executor = new JavaExecutor();

        Executor m = (Executor) Proxy.newProxyInstance(Executor.class.getClassLoader(),
                new Class[]{Executor.class},
                new ExecutorProxy(executor)
        );
        executor.compile("C:\\git\\wordcount\\src\\WordCount.java");
        m.exec("C:\\git\\wordcount\\src\\WordCount.java", "C:\\git\\rural.txt");
    }
}