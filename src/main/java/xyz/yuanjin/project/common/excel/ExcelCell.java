package xyz.yuanjin.project.common.excel;

import java.lang.annotation.*;

/**
 * @author yuanjin
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCell {
    
    String name();

    int col();
}
