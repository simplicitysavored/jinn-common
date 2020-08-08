import dto.ExcelTestDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xyz.yuanjin.project.common.exception.ExcelParseErrorException;
import xyz.yuanjin.project.common.util.ExcelUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelUtilTest {
    List<ExcelTestDTO> list;

    @Before
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
