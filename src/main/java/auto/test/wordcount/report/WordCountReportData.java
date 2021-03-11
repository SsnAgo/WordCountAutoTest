package auto.test.wordcount.report;

import auto.test.wordcount.judge.JudgeItem;
import auto.test.wordcount.judge.JudgeResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/5
 * @since
 */

public class WordCountReportData implements ReportData {
    private final List<JudgeResult> results;

    public WordCountReportData(List<JudgeResult> results) {
        this.results = results;
    }

    @Override
    public String[] headers() {
        if (results.isEmpty()) {
            throw new RuntimeException("export data is empty");
        }
        final long count = results.get(0).getScore().size();
        final String[] headers = new String[(int) (count * 2 + 4)];
        headers[0] = "StudentNo";
        headers[1] = "Score";
        int index = 2;
        for (int i = 1; i <= count; index++) {
            if (index % 2 == 0) {
                headers[index] = "Score" + i;
            } else {
                headers[index] = "Time" + i;
                i++;
            }
        }
        // 学生的提交次数
        headers[index++] = "commit_times";
        // 提交详情
        headers[index] = "commit_details";
        return headers;
    }

    @Override
    public List<List<String>> records() {
        List<List<String>> records = new ArrayList<>();

        for (JudgeResult result : results) {
            List<String> record = new ArrayList<>();
            record.add(result.getStudentNo());
            final Double scores = result.getScore().stream().mapToDouble(x -> Double.parseDouble(x.getScore())).sum();
            record.add(String.valueOf(scores));
            for (JudgeItem judgeItem : result.getScore()) {
                record.add(judgeItem.getScore());
                record.add(judgeItem.getTime());

            }
            record.add(result.getCommitTimes());
            record.add(result.getCommitDetails());
            records.add(record);
        }
        return records;
    }


}
