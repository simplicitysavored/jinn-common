package xyz.yuanjin.project.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.yuanjin.project.common.annotation.ExcelColumn;
import xyz.yuanjin.project.common.enums.DateFormatEnum;
import xyz.yuanjin.project.common.enums.ExcelCellEnum;
import xyz.yuanjin.project.common.exception.ExcelParseErrorException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
    private static final String XLS = ".xls";
    private static final String XLSX = ".xlsx";

    public static <T> void write(List<T> list, File file) throws ExcelParseErrorException {
        Workbook workbook = null;
        if (file.getName().endsWith(XLS)) {
            workbook = new HSSFWorkbook();
        } else if (file.getName().endsWith(XLSX)) {
            workbook = new XSSFWorkbook();
        } else {
            throw new ExcelParseErrorException("Excel文件解析失败 | 不是xls或xlsx");
        }

        // 这里之后可以自定义sheet名称
        Sheet sheet = workbook.createSheet();
        Field[] fields = createFirstRowAndReturnFields(list.get(0).getClass(), sheet);

        for (int rowIndex = 0; rowIndex < list.size(); ) {
            T t = list.get(rowIndex++);

            Row row = sheet.createRow(rowIndex);
            for (Field field : fields) {
                field.setAccessible(true);
                ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                if (null != excelColumn) {
                    Cell cell = row.createCell(excelColumn.cell().getValue());
                    cell.setCellType(CellType.STRING);
                    try {
                        if (Date.class.equals(field.getType())) {
                            DateFormat df = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_1.getValue());
                            cell.setCellValue(df.format(field.get(t)));
                        } else {
                            cell.setCellValue(String.valueOf(field.get(t)));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new ExcelParseErrorException("Excel写入失败");
                    }
                }
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            throw new ExcelParseErrorException("Excel写入失败");
        }

    }

    private static <T> Field[] createFirstRowAndReturnFields(Class<T> clz, Sheet sheet) {
        Row firstRow = sheet.createRow(0);
        Field[] fields = clz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (null != excelColumn) {
                Cell cell = firstRow.createCell(excelColumn.cell().getValue());
                cell.setCellType(CellType.STRING);
                cell.setCellValue(excelColumn.value());
            }
        }
        return fields;
    }

    private static <T> void createFirstRow(Class<T> clz, Sheet sheet) {
        createFirstRowAndReturnFields(clz, sheet);
    }

    public static <T> List<T> read(Class<T> clz, String path) throws ExcelParseErrorException {
        File file = new File(path);
        return read(clz, file);
    }

    public static <T> List<T> read(Class<T> clz, File file) throws ExcelParseErrorException {
        if (!(file.getName().endsWith(XLS) || file.getName().endsWith(XLSX))) {
            throw new ExcelParseErrorException("Excel文件解析失败 | 不是xls或xlsx");
        }
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (IOException | InvalidFormatException e) {
            throw new ExcelParseErrorException("Excel对象初始化失败");
        }

        Sheet sheet = workbook.getSheetAt(0);

        int numberOfRows = sheet.getPhysicalNumberOfRows();

        Field[] fields = clz.getDeclaredFields();

        List<T> list = new ArrayList<>(numberOfRows);

        for (int rowIndex = 1; rowIndex < numberOfRows; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (null != row) {
                T t = null;
                try {
                    t = clz.newInstance();
                } catch (Exception e) {
                    throw new ExcelParseErrorException("对象实例化失败");
                }
                for (Field field : fields) {
                    field.setAccessible(true);
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                    if (excelColumn != null) {
                        ExcelCellEnum cellEnum = excelColumn.cell();
                        int cellIndex = cellEnum.getValue();
                        Cell cell = row.getCell(cellIndex);

                        try {
                            if (Integer.class.equals(field.getType())) {
                                cell.setCellType(CellType.STRING);
                                field.set(t, Integer.parseInt(cell.getStringCellValue()));
                            } else if (Double.class.equals(field.getType())) {
                                cell.setCellType(CellType.NUMERIC);
                                field.set(t, cell.getNumericCellValue());
                            } else if (Date.class.equals(field.getType())) {
                                cell.setCellType(CellType.STRING);
                                DateFormat df = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_1.getValue());
                                field.set(t, df.parse(cell.getStringCellValue()));
                            } else {
                                cell.setCellType(CellType.STRING);
                                field.set(t, cell.getStringCellValue());
                            }
                        } catch (Exception e) {
                            throw new ExcelParseErrorException("第" + (rowIndex + 1) + "行" + cellEnum.name() + "列格式错误, 期待格式为 " + field.getType().toString() + " | " + cell);
                        }
                    }
                }
                list.add(t);
            }
        }

        return list;

    }


}
