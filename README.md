## 本测试程序参考北航的自动测试程序

https://github.com/ZhaoYi1031/WordCountAutoTest

## 说明

项目进行了一些调整，采用maven来管理，这样依赖的管理和项目目录会清晰一些，由于原先的代码不太适用我们的作业要求，所以我们需要对代码进行一些改造

改造之前，由于我们需要先抽取出一些API，这些API或多或少在原项目中都有实现，我们可以作为参考

目前这边想到功能大约有这些：

1. 从Git克隆项目到本地
2. 生成测试数据
    - 按照指定的配置（字符数量，行数量，单词数量），生成对应的测试txt文件，并把这些txt存放到指定的位置（通过配置文件指定）
    - 把这个txt对应的答案文件存在指定目录下
3. 编译并执行同学们的代码，执行的输入文件就是第2步中的txt文件，生成的测试结果和第二步中的答案进行对比
4. 将每个同学执行的用例通过数记录下来，存入csv文件中。

具体API

## DataGenerator 数据生成器 已完成

## FileUtil 已完成

## GitUtil 已完成

## CSVUtil 已完成

用法见：CSVUtilTest

## Executor接口，用于执行程序并测试

```java
public interface Executor {
    /**
     * 执行程序
     *
     * @param src    源码目录
     * @param input  测试用例
     */
    void exec(String src, String input);
}
```

不同语言实现这个接口即可

## Judge接口， 用于判断测试用例的通过与否

```java
public interface Judge {
    /**
     * 评判方法
     *
     * @param result 结果文件，可以是内容，也可以是路径，看各个题目要求定
     * @param answer 答案文件
     * @return
     */
    Result judge(String result, String answer);
}
```


