package xyz.yuanjin.project.common.excel;

import java.lang.annotation.*;

/**
 * @author yuanjin
 */

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheet {

    String name();

    int start() default 0;

    int end() default -1;
}
