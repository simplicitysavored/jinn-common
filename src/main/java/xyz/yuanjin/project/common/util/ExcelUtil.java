package xyz.yuanjin.project.common.util;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import xyz.yuanjin.project.common.annotation.ExcelColumn;
import xyz.yuanjin.project.common.dto.ParseExcelConfig;
import xyz.yuanjin.project.common.dto.ParseExcelSheetConfig;
import xyz.yuanjin.project.common.dto.ParseExcelSheetCellConfig;
import xyz.yuanjin.project.common.dto.ParseExcelSheetRowTitleConfig;
import xyz.yuanjin.project.common.enums.DateFormatEnum;
import xyz.yuanjin.project.common.enums.ExcelCellEnum;
import xyz.yuanjin.project.common.exception.ExcelParseErrorException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuanjin
 */
public class ExcelUtil {
    private static final String XLS = ".xls";
    private static final String XLSX = ".xlsx";

    public static <T> void write(List<T> list, File file) throws ExcelParseErrorException {
        Workbook workbook;
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

    public static <T> void writeBig(List<T> list, File file) throws ExcelParseErrorException {

        SXSSFWorkbook workbook = new SXSSFWorkbook(100);

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
                    //cell.setCellType(CellType.STRING);
                    try {
                        if (Date.class.equals(field.getType())) {
                            DateFormat df = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_1.getValue());
                            cell.setCellValue(df.format(field.get(t)));
                        }
                        if (Double.class.equals(field.getType())) {
                            cell.setCellType(CellType.NUMERIC);
                            // 没有小树则只显示整数
                            double tmpVal = Double.parseDouble(String.valueOf(field.get(t)));
                            DataFormat format = workbook.createDataFormat();
                            if (tmpVal - (int) tmpVal == 0) {
                                cell.getCellStyle().setDataFormat(format.getFormat("#"));
                                int intVal = (int) tmpVal;
                                cell.setCellValue(intVal);
                            } else {
                                cell.setCellValue(tmpVal);
                            }
                        }
                        if (Integer.class.equals(field.getType())) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(Integer.parseInt(String.valueOf(field.get(t))));
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
            FileUtil.createFileIfNotExists(file.getAbsolutePath());

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.dispose();
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
        return read(clz, file, 0);
    }

    public static <T> List<T> read(Class<T> clz, String path, int sheetAt) throws ExcelParseErrorException {
        File file = new File(path);
        return read(clz, file, sheetAt);
    }

    public static <T> List<T> read(Class<T> clz, File file) throws ExcelParseErrorException {
        return read(clz, file, 0);

    }

    /**
     * 读取Excel
     *
     * @param clz     解析的类（需要使用{@link ExcelCellEnum}标记）
     * @param file    Excel文件
     * @param sheetAt sheet编号（第一个为0）
     * @param <T>     范型
     * @return List
     * @throws ExcelParseErrorException 异常
     */
    public static <T> List<T> read(Class<T> clz, File file, int sheetAt) throws ExcelParseErrorException {
        Workbook workbook = getWorkbook(file);

        Sheet sheet = workbook.getSheetAt(sheetAt);

        int numberOfRows = sheet.getPhysicalNumberOfRows();

        Field[] fields = clz.getDeclaredFields();

        List<T> list = new ArrayList<>(numberOfRows);

        for (int rowIndex = 1; rowIndex < numberOfRows; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (null != row) {
                T t;
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
                        if (null == cell) {
                            continue;
                        }

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

    private static Workbook getWorkbook(File file) throws ExcelParseErrorException {
        if (!(file.getName().endsWith(XLS) || file.getName().endsWith(XLSX))) {
            throw new ExcelParseErrorException("Excel文件解析失败 | 不是xls或xlsx");
        }
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file);
        } catch (IOException | InvalidFormatException e) {
            throw new ExcelParseErrorException("Excel对象初始化失败");
        }
        return workbook;
    }

    /**
     * 根据配置解析Excel文件
     *
     * @param sourcePath Excel文件路径
     * @param xmlCfgPath Excel解析配置文件路径
     * @return Excel解析结果对象
     */
    public static Object readByConfig(String sourcePath, String xmlCfgPath) throws IOException, DocumentException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ParseExcelConfig excelConfig = parseExcelConfig(xmlCfgPath);

        Class<?> clz = Class.forName(excelConfig.getWrapperClass());
        Object ret = clz.newInstance();

        Field[] sheetFields = clz.getDeclaredFields();
        Map<String, Field> sheetFieldNameMap = Arrays.stream(sheetFields).collect(Collectors.toMap(Field::getName, field -> field));

        System.out.printf("正在初始化Workbook ｜ %s\n", sourcePath);
        try (Workbook workbook = getWorkbook(new File(sourcePath))) {
            System.out.printf("完成初始化Workbook ｜ %s\n", sourcePath);

            for (ParseExcelSheetConfig sheetConfig : excelConfig.getParseExcelSheetList()) {
                List<Object> tmpList = new ArrayList<>();

                Sheet sheet = workbook.getSheet(sheetConfig.getName());

                int totalRowNum = sheet.getPhysicalNumberOfRows();
                for (int rowNum = sheetConfig.getDataStartRow(); rowNum < totalRowNum; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (null == row) {
                        System.err.printf("%s - 第%s行为空\n", sheetConfig.getName(), rowNum);
                        continue;
                    }
                    Class<?> tmpClz = Class.forName(sheetConfig.getJavaClass());
                    Object tmpItem = tmpClz.newInstance();

                    List<ParseExcelSheetCellConfig> cellConfigList = sheetConfig.getRowTitleConfig().getCellConfigList();
                    for (ParseExcelSheetCellConfig cellConfig : cellConfigList) {
                        Cell cell = row.getCell(cellConfig.getColNum());
                        if (null == cell) {
                            System.err.printf("%s - 第%s行，第%s列，单元格为空\n", sheetConfig.getName(), row.getRowNum(), cellConfig.getColNum());
                            continue;
                        }
                        parseCellValue(cell, cellConfig, tmpItem);
                    }
                    tmpList.add(tmpItem);
                }

                Field field = sheetFieldNameMap.get(sheetConfig.getJavaField());
                if (null == field) {
                    System.err.printf("没有找到字段：%s\n", sheetConfig.getJavaField());
                    continue;
                }
                field.setAccessible(true);
                field.set(ret, tmpList);
            }
        } catch (ExcelParseErrorException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 解析单元格的内容
     *
     * @param targetObject 存储单元格值的对象
     * @param cell         单元格
     * @throws IllegalAccessException 异常
     */
    private static void parseCellValue(Cell cell, ParseExcelSheetCellConfig cellConfig, Object targetObject) throws IllegalAccessException, NoSuchFieldException {
        Field targetField = targetObject.getClass().getDeclaredField(cellConfig.getJavaField());
        targetField.setAccessible(true);
        if (String.class.getName().equals(targetField.getType().getName())) {
            cell.setCellType(CellType.STRING);
            targetField.set(targetObject, cell.getStringCellValue());
        } else if (Double.class.getName().equals(targetField.getType().getName())) {
            targetField.setDouble(targetObject, cell.getNumericCellValue());
        } else if (Integer.class.getName().equals(targetField.getType().getName())) {
            cell.setCellType(CellType.STRING);
            targetField.set(targetObject, Integer.valueOf(cell.getStringCellValue()));
        } else if (Date.class.getName().equals(targetField.getType().getName())) {
            targetField.set(targetObject, cell.getDateCellValue());
        } else if (BigDecimal.class.getName().equals(targetField.getType().getName())) {
            cell.setCellType(CellType.NUMERIC);
            targetField.set(targetObject, BigDecimal.valueOf(cell.getNumericCellValue()));
        }

        // 默认值
        String defaultValue = cellConfig.getDefaultValue();
        if (null != defaultValue) {
            if (null == cell.getStringCellValue() || cell.getStringCellValue().length() == 0) {
                targetField.setAccessible(true);
                targetField.set(targetObject, defaultValue);
            }
        }
    }

    /**
     * 解析Excel的配置
     *
     * @param xmlCfgPath Excel的xml配置
     * @return xml配置对象
     */
    private static ParseExcelConfig parseExcelConfig(String xmlCfgPath) throws FileNotFoundException, DocumentException {
        File xmlFile = new File(xmlCfgPath);
        if (!xmlFile.exists()) {
            throw new FileNotFoundException();
        }

        SAXReader reader = new SAXReader();
        Document document = reader.read(xmlFile);

        Element rootElement = document.getRootElement();

        ParseExcelConfig excelConfig = new ParseExcelConfig();
        excelConfig.setWrapperClass(rootElement.attributeValue("wrapperClass"));
        excelConfig.setParseExcelSheetList(new ArrayList<>());
        List<ParseExcelSheetConfig> excelSheetConfigList = excelConfig.getParseExcelSheetList();

        List<Element> sheetElList = rootElement.elements("sheet");
        sheetElList.forEach(sheetEl -> {
            String sheetName = sheetEl.attributeValue("name");
            String javaClass = sheetEl.attributeValue("javaClass");
            Integer dataStart = Integer.parseInt(sheetEl.attributeValue("dataStartRow")) - 1;
            String javaField = sheetEl.attributeValue("javaField");

            ParseExcelSheetConfig sheetConfig = new ParseExcelSheetConfig();
            sheetConfig.setName(sheetName);
            sheetConfig.setJavaClass(javaClass);
            sheetConfig.setJavaField(javaField);
            sheetConfig.setDataStartRow(dataStart);
            sheetConfig.setRowTitleConfig(new ParseExcelSheetRowTitleConfig());

            Element rowTitleEl = sheetEl.element("rowTitle");
            Integer rowNum = Integer.parseInt(rowTitleEl.attributeValue("rowNum")) - 1;

            ParseExcelSheetRowTitleConfig rowTitleConfig = sheetConfig.getRowTitleConfig();
            rowTitleConfig.setRowNum(rowNum);
            rowTitleConfig.setCellConfigList(new ArrayList<>());
            List<ParseExcelSheetCellConfig> cellConfigList = rowTitleConfig.getCellConfigList();

            List<Element> cellElList = rowTitleEl.elements("cell");
            cellElList.forEach(cellEl -> {
                int colNum;
                Attribute colTagAttr = cellEl.attribute("colTag");
                if (null == colTagAttr) {
                    colNum = Integer.parseInt(cellEl.attributeValue("colNum")) - 1;
                } else {
                    colNum = ExcelCellEnum.getValue(colTagAttr.getStringValue());
                }

                String cellJavaField = cellEl.attributeValue("javaField");
                String desc = cellEl.attributeValue("desc");
                String defaultValue = cellEl.attributeValue("defaultValue");
                String format = cellEl.attributeValue("format");

                ParseExcelSheetCellConfig cellConfig = new ParseExcelSheetCellConfig();
                cellConfig.setColNum(colNum);
                cellConfig.setJavaField(cellJavaField);
                cellConfig.setDesc(desc);
                cellConfig.setDefaultValue(defaultValue);
                cellConfig.setFormat(format);
                cellConfigList.add(cellConfig);

            });

            excelSheetConfigList.add(sheetConfig);
        });

        return excelConfig;
    }

}
