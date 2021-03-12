package xyz.yuanjin.project.common.dto;

/**
 * @author yuanjin
 */
public class ParseExcelSheetConfig {
    private String name;
    private String javaClass;
    private String javaField;
    private Integer dataStartRow;
    private ParseExcelSheetRowTitleConfig rowTitleConfig;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public Integer getDataStartRow() {
        return dataStartRow;
    }

    public void setDataStartRow(Integer dataStartRow) {
        this.dataStartRow = dataStartRow;
    }

    public ParseExcelSheetRowTitleConfig getRowTitleConfig() {
        return rowTitleConfig;
    }

    public void setRowTitleConfig(ParseExcelSheetRowTitleConfig rowTitleConfig) {
        this.rowTitleConfig = rowTitleConfig;
    }

    public String getJavaField() {
        return javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }
}
