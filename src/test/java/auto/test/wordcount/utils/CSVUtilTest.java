package auto.test.wordcount.utils;

import auto.test.wordcount.judge.JudgeItem;
import auto.test.wordcount.judge.JudgeResult;
import auto.test.wordcount.report.WordCountReportData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVUtilTest {


    @Test
    public void testWordCountReport() {
        List<JudgeResult> results = new ArrayList<>();
        final JudgeResult result1 = new JudgeResult("2111111", Arrays.asList(new JudgeItem("100", "22"), new JudgeItem("200", "11")), "10", "ddd");
        final JudgeResult result2 = new JudgeResult("22222222", Arrays.asList(new JudgeItem("11", "11"), new JudgeItem("11", "11")), "12", "dasdfas");
        results.add(result1);
        results.add(result2);
        final WordCountReportData wordCountReportData = new WordCountReportData(results);
        CSVUtil.exportToCSV(wordCountReportData, "D:\\result.csv");
    }
}