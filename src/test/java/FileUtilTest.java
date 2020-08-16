import org.junit.Test;
import xyz.yuanjin.project.common.util.FileUtil;

import java.io.IOException;

public class FileUtilTest {
    @Test
    public void test() {
        String name = "application.properties";

        try {
            String mes = FileUtil.readFromClasspath(name);
            System.out.println(mes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
