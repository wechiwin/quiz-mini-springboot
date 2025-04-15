package com.moggi.quizmini.framework.dao.query;

import java.lang.annotation.*;

/**
 * 标记在QueryDTO上的注解，声明排序方式等参数
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QuerySort {

    /**
     * 默认排序字段，由于是后端开发人员指定的，所以不再校验合法性，需要谨慎
     */
    String[] defaultField() default {};

    /**
     * 如果有默认排序字段，按照此升降序排列
     */
    Sort.Order.ORDER[] defaultOrder() default {};

    // /**
    //  * 允许web前端按照DTO中的字段进行来排序
    //  */
    // Class<? extends BaseDTO>[] customClass() default {};

    /**
     * 允许web前端按照此处声明的字段列表进行排序
     * <p>
     * 1 默认会根据驼峰转下划线
     * 2 如果需要重命名字段或者增加前缀，可以使用"|"分割。样例customField={"roleName|e.role_name","roleType"}
     * 3 如果希望使用常量，则使用一个字符串，以逗号分割且不加空格。样例customField="roleName|e.role_name,roleType"
     */
    String[] customField() default {};

    /**
     * 允许最多多少个字段排序
     */
    int maxCustomSort() default 2;

}
