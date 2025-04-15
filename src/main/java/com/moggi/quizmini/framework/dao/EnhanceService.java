package com.moggi.quizmini.framework.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moggi.quizmini.framework.dao.query.WrapperBuilderUtil;
import com.moggi.quizmini.framework.pojo.Converter;
import com.moggi.quizmini.framework.pojo.IConvert;
import com.moggi.quizmini.framework.pojo.QueryDTO;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;

@Slf4j
public class EnhanceService<M extends BaseMapper<Entity>, Dto, Entity>
        extends ServiceImpl<M, Entity>
        implements IEnhanceService<Entity>, IConvert<Dto, Entity> {

    Class<Dto> clazzDto = null;
    Class<Entity> clazzEntity = null;
    private Converter<Dto, Entity> converter = null;

    public EnhanceService() {
        try {
            /*
            getGenericSuperclass() 获取超类
            ParameterizedType 是 Type 的子接口
            Java 中的类型系统在反射中是通过 java.lang.reflect.Type 来表示的，Type 是一个顶层接口，它的几个常见子接口有：
            ParameterizedType 是 Java 反射中表示“带泛型参数的类型”的对象。它允许我们在运行时获取泛型的真实类型参数。
             */
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            this.clazzDto = (Class) pt.getActualTypeArguments()[1]; // 获取泛型的第二个参数Dto
            this.clazzEntity = (Class) pt.getActualTypeArguments()[2]; // 获取泛型的第三个参数entity
            if (this.clazzDto != null && this.clazzEntity != null) {
                this.converter = new Converter<>(this.clazzDto, this.clazzEntity);
            }
        } catch (Exception exception) {
            log.info("EnhanceService init failed", exception);
        }
    }

    public Converter<Dto, Entity> getConverter() {
        return this.converter;
    }

    public Dto createDto() {
        if (this.converter == null) {
            throw this.buildParamNotSetException();
        } else {
            return this.converter.createDto();
        }
    }

    public Entity createEntity() {
        if (this.converter == null) {
            throw this.buildParamNotSetException();
        } else {
            return this.converter.createEntity();
        }
    }

    private RuntimeException buildParamNotSetException() {
        return new RuntimeException(this.getClass().getName() + "'s <Dto,Entity> not set.");
    }

    /**
     * 获取查询 wrapper，供子类或增强方法使用
     */
    public QueryWrapper<Entity> getWrapper(QueryDTO queryDto) {
        return WrapperBuilderUtil.buildQueryWrapper(getEntityClass(), queryDto);
    }

}
