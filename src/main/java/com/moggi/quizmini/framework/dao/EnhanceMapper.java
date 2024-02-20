package com.moggi.quizmini.framework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 增强Mapper
 */
public interface EnhanceMapper<T> extends BaseMapper<T> {

    /**
     * 自定义批量插入
     * 如果要自动填充，@Param(xx) xx参数名必须是 list/collection/array 3个的其中之一
     */
    int insertBatch(@Param("list") List<T> list);

    /**
     * 自定义批量更新，条件为主键
     * 如果要自动填充，@Param(xx) xx参数名必须是 list/collection/array 3个的其中之一
     */
    int updateBatch(@Param("list") List<T> list);

    // /**
    //  * 批量修改
    //  *
    //  * @param entityList
    //  * @return
    //  */
    // int updateBatchIds(@Param("list") List<T> entityList);
    //
    // /**
    //  * 批量新增
    //  *
    //  * @param entityList
    //  * @return
    //  */
    // int insertBatch(@Param("list") List<T> entityList);

}
