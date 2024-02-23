package com.moggi.quizmini.framework.pojo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface IConvert<Dto, Entity> {

    /**
     * Entity转Dto
     *
     * @param entity
     * @return
     */
    default Dto toDto(Entity entity) {
        if (entity == null) {
            return null;
        }
        Dto dto = this.createDto();
        this.copyProperties(entity, dto);
        return dto;
    }

    /***
     * Dto转Entity
     *
     * @param dto
     * @return
     */
    default Entity toEntity(Dto dto) {
        if (dto == null) {
            return null;
        }
        Entity entity = this.createEntity();
        this.copyProperties(dto, entity);
        return entity;
    }

    default Entity toEntityIncluding(Dto dto, String... props) {
        if (dto == null) {
            return null;
        }
        Entity entity = this.createEntity();
        this.copyPropertiesIncluding(dto, entity, props);
        return entity;
    }

    default Entity toEntityExcluding(Dto dto, String... props) {
        if (dto == null) {
            return null;
        }
        Entity entity = this.createEntity();
        this.copyPropertiesExcluding(dto, entity, props);
        return entity;
    }

    /**
     * Entity列表转Dto列表<br>
     *
     * @param entities
     * @return 如果为空，始终返回长度为0的列表
     */
    default List<Dto> toDtoList(List entities) {
        List<Dto> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entities)) {
            for (Object entity : entities) {
                result.add(this.toDto((Entity) entity));
            }
        }
        return result;
    }

    /**
     * Dto列表转Entity列表
     *
     * @param dtos
     * @return 如果为空，始终返回长度为0的列表
     */
    default List<Entity> toEntityList(List dtos) {
        List<Entity> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtos)) {
            for (Object dto : dtos) {
                result.add(this.toEntity((Dto) dto));
            }
        }
        return result;
    }

    /**
     * Dto列表转Entity列表，根据黑名单
     *
     * @param dtos
     * @return 如果为空，始终返回长度为0的列表
     */
    default List<Entity> toEntityListExcluding(List dtos, String... props) {
        List<Entity> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtos)) {
            for (Object dto : dtos) {
                result.add(this.toEntityExcluding((Dto) dto, props));
            }
        }
        return result;
    }

    /**
     * Dto列表转Entity列表，根据白名单
     *
     * @param dtos
     * @return 如果为空，始终返回长度为0的列表
     */
    default List<Entity> toEntityListIncluding(List dtos, String... props) {
        List<Entity> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtos)) {
            for (Object dto : dtos) {
                result.add(this.toEntityIncluding((Dto) dto, props));
            }
        }
        return result;
    }

    /**
     * 创建一个Dto对象
     *
     * @return
     */
    Dto createDto();

    /**
     * 创建一个Entity对象
     *
     * @return
     */
    Entity createEntity();

    /**
     * 默认的拷贝
     *
     * @param src
     * @param desc
     */
    default void copyProperties(Object src, Object desc) {
        BeanUtils.copyProperties(src, desc);
    }

    /**
     * BeanUtils默认提供黑名单复制，
     *
     * @param src
     * @param trg
     * @param props 忽略的字段
     */
    default void copyPropertiesExcluding(Object src, Object trg, String... props) {
        BeanUtils.copyProperties(src, trg, props);
    }

    default void copyPropertiesExcluding(Object src, Object trg, Collection<String> props) {
        String[] ignores = new String[props.size()];
        props.toArray(ignores);
        BeanUtils.copyProperties(src, trg, ignores);
    }

    default void copyPropertiesIncluding(Object src, Object trg, String... props) {
        copyPropertiesIncluding(src, trg, Arrays.asList(props));
    }

    /**
     * 复制白名单属性值到目标对象
     *
     * @param src
     * @param trg
     * @param props
     */
    default void copyPropertiesIncluding(Object src, Object trg, Collection<String> props) {
        // 以下方法可以使用BeanUtils的缓存，性能更高，
        PropertyDescriptor srcProp;
        Object value;
        Method writeMethod;
        for (PropertyDescriptor p : BeanUtils.getPropertyDescriptors(trg.getClass())) {
            try {
                if (props.contains(p.getName())) {
                    srcProp = BeanUtils.getPropertyDescriptor(src.getClass(), p.getName());
                    if (srcProp == null) {
                        continue;
                    }
                    value = srcProp.getReadMethod().invoke(src);
                    writeMethod = p.getWriteMethod();
                    if (writeMethod != null) {
                        writeMethod.invoke(trg, value);
                    }
                }
            } catch (Throwable ex) {
                throw new FatalBeanException(
                        "Could not copy property '" + p.getName() + "' from source to target", ex);
            }
        }
    }

    /**
     * 只复制非空属性到目标对象
     *
     * @param src
     * @param trg
     */
    default void copyPropertiesForNotNullVal(Object src, Object trg) {
        PropertyDescriptor srcProp;
        Object value;
        Method writeMethod;
        for (PropertyDescriptor p : BeanUtils.getPropertyDescriptors(trg.getClass())) {
            try {
                srcProp = BeanUtils.getPropertyDescriptor(src.getClass(), p.getName());
                if (srcProp == null) {
                    continue;
                }
                value = srcProp.getReadMethod().invoke(src);
                if (value != null) {
                    writeMethod = p.getWriteMethod();
                    if (writeMethod != null) {
                        writeMethod.invoke(trg, value);
                    }
                }
            } catch (Throwable ex) {
                throw new FatalBeanException(
                        "Could not copy property '" + p.getName() + "' from source to target", ex);
            }
        }
    }

}
