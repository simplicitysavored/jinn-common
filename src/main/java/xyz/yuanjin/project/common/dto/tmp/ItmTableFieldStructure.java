package xyz.yuanjin.project.common.dto.tmp;

import lombok.Data;
import xyz.yuanjin.project.common.annotation.ExcelColumn;
import xyz.yuanjin.project.common.enums.ExcelCellEnum;

@Data
public class ItmTableFieldStructure {

    @ExcelColumn(value = "管理批次", cell = ExcelCellEnum.A)
    private String batchNo;

    @ExcelColumn(value = "子系统", cell = ExcelCellEnum.B)
    private String subSys;

    @ExcelColumn(value = "开发中心", cell = ExcelCellEnum.C)
    private String developCenter;

    @ExcelColumn(value = "库表编号", cell = ExcelCellEnum.D)
    private String databaseTableNo;

    @ExcelColumn(value = "字段编号", cell = ExcelCellEnum.E)
    private String tableFieldNo;

    @ExcelColumn(value = "平台", cell = ExcelCellEnum.F)
    private String platform;

    @ExcelColumn(value = "数据表英文名", cell = ExcelCellEnum.G)
    private String tableEnglishName;

    @ExcelColumn(value = "数据表中文名", cell = ExcelCellEnum.H)
    private String tableChineseName;

    @ExcelColumn(value = "字段英文名", cell = ExcelCellEnum.I)
    private String tableFieldEnglishName;

    @ExcelColumn(value = "字段中文名", cell = ExcelCellEnum.J)
    private String tableFieldChineseName;

    @ExcelColumn(value = "数据字典编号", cell = ExcelCellEnum.K)
    private String dataItemNo;

    @ExcelColumn(value = "数据类型", cell = ExcelCellEnum.L)
    private String dataType;

    @ExcelColumn(value = "字段长度", cell = ExcelCellEnum.M)
    private String dataLength;

    @ExcelColumn(value = "有效祛痣番位", cell = ExcelCellEnum.N)
    private String x1;

    @ExcelColumn(value = "是否为主键", cell = ExcelCellEnum.O)
    private String ifPrimaryKey;

    @ExcelColumn(value = "是否敏感信息", cell = ExcelCellEnum.P)
    private String x2;

    @ExcelColumn(value = "敏信息种类", cell = ExcelCellEnum.Q)
    private String x3;

    @ExcelColumn(value = "是否为空", cell = ExcelCellEnum.R)
    private String ifEmpty;

    @ExcelColumn(value = "是否来源D模型", cell = ExcelCellEnum.S)
    private String x4;

    @ExcelColumn(value = "实体", cell = ExcelCellEnum.T)
    private String x5;

    @ExcelColumn(value = "是否有业务XX", cell = ExcelCellEnum.U)
    private String x6;

    @ExcelColumn(value = "C模型尸体XX", cell = ExcelCellEnum.V)
    private String x7;

    @ExcelColumn(value = "C模型XXX", cell = ExcelCellEnum.W)
    private String x8;

    @ExcelColumn(value = "期次", cell = ExcelCellEnum.X)
    private String x9;

    @ExcelColumn(value = "上线日期", cell = ExcelCellEnum.Y)
    private String onlineDate;

    @ExcelColumn(value = "是否提供数XX", cell = ExcelCellEnum.Z)
    private String x11;

    @ExcelColumn(value = "变更原因", cell = ExcelCellEnum.AA)
    private String modifyReason;

    @ExcelColumn(value = "联系人及电话", cell = ExcelCellEnum.AB)
    private String contactPersonAndPhone;

    @ExcelColumn(value = "状态", cell = ExcelCellEnum.AC)
    private String status;
}
