import org.junit.Test;
import xyz.yuanjin.project.common.util.FileUtil;

import java.io.File;

public class PatternTest {
    @Test
    public void video() {
        String fileName = "xxx.3gp";
        System.out.format("%s is Video: %s\n", fileName, FileUtil.isVideo(new File(fileName)));
        fileName = "xxx.3GP";
        System.out.format("%s is Video: %s\n", fileName, FileUtil.isVideo(new File(fileName)));
    }
}
