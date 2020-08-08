package dto;

import xyz.yuanjin.project.common.annotation.ExcelColumn;
import xyz.yuanjin.project.common.enums.ExcelCellEnum;

import java.util.Date;

public class ExcelTestDTO {
    @ExcelColumn(value = "第一列", cell = ExcelCellEnum.A)
    private String col1;

    @ExcelColumn(value = "第二列", cell = ExcelCellEnum.B)
    private String col2;

    @ExcelColumn(value = "第三列", cell = ExcelCellEnum.C)
    private Date col3;

    @ExcelColumn(value = "第三列", cell = ExcelCellEnum.D)
    private Double col4;

    @ExcelColumn(value = "第三列", cell = ExcelCellEnum.E)
    private Integer col5;

    public Double getCol4() {
        return col4;
    }

    public void setCol4(Double col4) {
        this.col4 = col4;
    }

    public Integer getCol5() {
        return col5;
    }

    public void setCol5(Integer col5) {
        this.col5 = col5;
    }

    public Date getCol3() {
        return col3;
    }

    public void setCol3(Date col3) {
        this.col3 = col3;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }
}
