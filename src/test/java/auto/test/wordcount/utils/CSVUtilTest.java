package auto.test.wordcount.utils;

import auto.test.wordcount.ReportData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
                List<String> names = new ArrayList<>();
                names.add("zhangsan");
                names.add("lisi");
                names.add("wangwu");
                names.add("xiaojiang");
                List<String> scores = new ArrayList<>();
                scores.add("3.3");
                scores.add("3.33");
                scores.add("3.13");
                scores.add("3.43");
                List<List<String>> result = new ArrayList<>();
                result.add(names);
                result.add(scores);
                return result;
            }
        };
        CSVUtil.exportToCSV(reportData, "D:\\git2\\result.csv");
    }
}