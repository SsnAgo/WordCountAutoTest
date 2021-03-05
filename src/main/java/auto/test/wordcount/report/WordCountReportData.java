package auto.test.wordcount.report;

import auto.test.wordcount.judge.JudgeItem;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/5
 * @since
 */
// TODO 需要适配表格，参考scores.csv
public class WordCountReportData implements ReportData {
    private Map<String, List<JudgeItem>> map;

    public WordCountReportData(Map<String, List<JudgeItem>> map) {
        this.map = map;
    }

    @Override
    public String[] headers() {
        return new String[0];
    }

    @Override
    public List<List<String>> records() {
        return null;
    }


}
