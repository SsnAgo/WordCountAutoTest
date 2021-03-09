package auto.test.wordcount.judge;

import java.util.List;

/**
 * @author <a href="mailTo:5239604@qq.com">coderPlus-tr</a>
 * @date 2021/3/5
 */

public class JudgeResult {
    private String studentNo;
    private List<JudgeItem> score;

    public JudgeResult(String studentNo, List<JudgeItem> score) {
        this.studentNo = studentNo;
        this.score = score;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public List<JudgeItem> getScore() {
        return score;
    }

    public void setScore(List<JudgeItem> score) {
        this.score = score;
    }
}
