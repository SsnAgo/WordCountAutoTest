package auto.test.wordcount.utils;

import auto.test.wordcount.judge.Judge;
import auto.test.wordcount.judge.JudgeItem;
import auto.test.wordcount.judge.JudgeResult;
import auto.test.wordcount.report.ReportData;
import auto.test.wordcount.report.WordCountReportData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVUtilTest {

    /**
     * name	score
     * zhangsan
     * lisi
     * wangwu
     * xiaojiang
     * 3.3
     * 3.33
     * 3.13
     * 3.43
     */
    @Test
    public void exportToCSV() {
        ReportData reportData = new ReportData() {
            @Override
            public String[] headers() {
                return new String[]{"name", "score"};
            }

            @Override
            public List<List<String>> records() {
                final List<List<String>> records = new ArrayList<>();
                records.add(Arrays.asList("zhangsan", "3.3"));
                records.add(Arrays.asList("lisi", "3.33"));
                records.add(Arrays.asList("wangwu", "3.13"));
                records.add(Arrays.asList("xiaojiang", "3.43"));
                return records;
            }
        };
        CSVUtil.exportToCSV(reportData, "D:\\git2\\result.csv");
    }

    @Test
    public void testWordCountReport(){
        List<JudgeResult> results = new ArrayList<>();
        final JudgeResult result1 = new JudgeResult("2111111", Arrays.asList(new JudgeItem("100", "22"), new JudgeItem("200", "11")));
        final JudgeResult result2 = new JudgeResult("22222222", Arrays.asList(new JudgeItem("11", "11"), new JudgeItem("11", "11")));
        results.add(result1);
        results.add(result2);
        final WordCountReportData wordCountReportData = new WordCountReportData(results);
        CSVUtil.exportToCSV(wordCountReportData, "D:\\result.csv");
    }
}