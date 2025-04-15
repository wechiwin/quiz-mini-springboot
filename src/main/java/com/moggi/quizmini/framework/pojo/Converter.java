package com.moggi.quizmini.framework.pojo;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class Converter<Dto, Entity> implements IConvert<Dto, Entity> {
    Class<Dto> clazzDto;
    Class<Entity> clazzEntity;

    public Converter(Class<Dto> clazzDto, Class<Entity> clazzEntity) {
        this.clazzDto = clazzDto;
        this.clazzEntity = clazzEntity;
    }

    @Override
    public Dto createDto() {
        try {
            return clazzDto.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Entity createEntity() {
        try {
            return clazzEntity.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


}
