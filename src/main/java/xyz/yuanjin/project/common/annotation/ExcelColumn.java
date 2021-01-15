package xyz.yuanjin.project.common.annotation;

import xyz.yuanjin.project.common.enums.ExcelCellEnum;

import java.lang.annotation.*;

/**
 * @author yuanjin
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    /**
     * 列明
     *
     * @return {String}
     */
    String value() default "";

    /**
     * 单元格所属列
     *
     * @return {String}
     */
    ExcelCellEnum cell();

}
