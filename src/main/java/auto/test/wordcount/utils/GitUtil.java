package auto.test.wordcount.utils;


import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Git的相关操作
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/2
 * @since
 */
public class GitUtil {
    private static final Logger log = LoggerFactory.getLogger(GitUtil.class);

    /**
     * 克隆url到指定目录
     * eg
     * url：https://github.com/GreyZeng/WordCountAutoTest.git
     * localPath: C:/git/download
     * 则执行完毕后，会在C盘git目录下的download目录下，
     * 如果pathAsRoot参数为false，则会有一个文件夹名称为WordCountAutoTest的仓库
     * 如果pathAsRoot参数为true，则WordCountAutoTest目录不会生成，只会把里面所有的内容放在C:/git/download目录下
     * 不需要考虑目录存在与否的情况，假设给的localPath是合法的，由调用这个API的地方来判断
     *
     * @param url        git地址
     * @param localPath  clone后文件存放的地址
     * @param pathAsRoot clone后的文件是否以localPath为根目录
     *                   如果为true，则clone后的不会生成对应的文件夹，并以localPath作为仓库根目录
     *                   如果为false，则clone后的文件夹会保留下来
     * @return 是否克隆成功, 成功为true，不成功为false
     */
    public static boolean cloneRepo(String url, String localPath, boolean pathAsRoot) {
        if (!pathAsRoot) {
            String repoName = url.substring(url.lastIndexOf("/") + 1).replace(".git", "");
            FileUtil.createFolder(localPath, repoName);
            localPath = localPath + File.separator + repoName;
        }
        File file = new File(localPath);
        if (file.exists()) {
            FileUtil.deleteFile(file);
        }
        log.info("开始下载: {}", url);
        CloneCommand cloneCommand = Git.cloneRepository().setURI(url);
        try {
            cloneCommand.setDirectory(new File(localPath)).setTimeout(100).call();
        } catch (GitAPIException e) {
            log.error("下载错误: {} {}", url, e.getMessage());
            return false;
        }
        log.info("下载完成: {}", url);
        return true;
    }


}
