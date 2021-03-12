import dto.BatchProjectDTO;
import dto.ExcelTestDTO;
import org.dom4j.DocumentException;
import org.junit.Test;
import xyz.yuanjin.project.common.exception.ExcelParseErrorException;
import xyz.yuanjin.project.common.util.ExcelUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelUtilTest {

    @Test
    public void readByConfig() throws IOException, DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String rootPath = System.getProperty("user.dir");
        String configPath = rootPath + File.separator + "src/test/resources/excelParseConfig.xml";
        String excelPath = rootPath + File.separator + "src/test/resources/跑批供数相关-v2021.0304.xlsx";

        Object tmpObj = ExcelUtil.readByConfig(excelPath, configPath);

        BatchProjectDTO dto = (BatchProjectDTO) tmpObj;

        System.out.println(dto);


    }


    List<ExcelTestDTO> list;

    public void readTest() throws Exception {
        File file = new File("C:\\Users\\yuanj\\Desktop\\TEST.xls");

        list = ExcelUtil.read(ExcelTestDTO.class, file);

        Class<ExcelTestDTO> clz = ExcelTestDTO.class;
        Field[] fields = clz.getDeclaredFields();
        for (ExcelTestDTO item : list) {
            for (Field field : fields) {
                field.setAccessible(true);
                System.out.print(field.get(item));
                System.out.print("\t");
            }
            System.out.println();
        }

    }

    @Test
    public void writeTest() throws ExcelParseErrorException {
        File file = new File("C:\\Users\\yuanj\\Desktop\\TEST002.xlsx");

        ExcelUtil.write(list, file);
    }
}
