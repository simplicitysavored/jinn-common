package xyz.yuanjin.project.common.dto;

import java.util.List;

/**
 * @author yuanjin
 */
public class ParseExcelConfig {
    private String wrapperClass;
    private List<ParseExcelSheetConfig> parseExcelSheetConfigList;

    public String getWrapperClass() {
        return wrapperClass;
    }

    public void setWrapperClass(String wrapperClass) {
        this.wrapperClass = wrapperClass;
    }

    public List<ParseExcelSheetConfig> getParseExcelSheetList() {
        return parseExcelSheetConfigList;
    }

    public void setParseExcelSheetList(List<ParseExcelSheetConfig> parseExcelSheetConfigList) {
        this.parseExcelSheetConfigList = parseExcelSheetConfigList;
    }
}
