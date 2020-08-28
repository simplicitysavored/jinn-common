import dto.TableStructure;
import org.junit.Test;
import xyz.yuanjin.project.common.exception.ExcelParseErrorException;
import xyz.yuanjin.project.common.util.ExcelUtil;

import java.util.List;

public class PacteraExcelUtilTest {

    @Test
    public void test1() throws ExcelParseErrorException {
        String path = "/Users/yuanjin/Documents/pactera/sample/数据模型结构.xlsx";

        List<TableStructure> list = ExcelUtil.read(TableStructure.class, path, 1);

        System.out.println(list.size());


    }
}
