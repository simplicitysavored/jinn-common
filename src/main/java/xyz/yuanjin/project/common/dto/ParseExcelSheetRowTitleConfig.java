package xyz.yuanjin.project.common.dto;

import java.util.List;

/**
 * @author yuanjin
 */
public class ParseExcelSheetRowTitleConfig {
    private Integer rowNum;
    private List<ParseExcelSheetCellConfig> cellConfigList;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public List<ParseExcelSheetCellConfig> getCellConfigList() {
        return cellConfigList;
    }

    public void setCellConfigList(List<ParseExcelSheetCellConfig> cellConfigList) {
        this.cellConfigList = cellConfigList;
    }
}
