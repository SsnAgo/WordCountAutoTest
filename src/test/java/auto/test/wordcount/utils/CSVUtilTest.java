package auto.test.wordcount.utils;

import auto.test.wordcount.ReportData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
}