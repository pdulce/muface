package com.mfc.infra.domain;

import java.lang.reflect.Field;

public class ArqDTOConverter {

    public static <T, D> D convertToDTO(T entity, Class<D> dtoClass) {
        try {
            IArqDTO dto = (IArqDTO) dtoClass.getDeclaredConstructor().newInstance();
            if (dto.getMapaConversion() == null || dto.getMapaConversion().isEmpty()) {
                //TODO
            } else {
                for (Field entityField : entity.getClass().getDeclaredFields()) {
                    entityField.setAccessible(true);
                    Object value = entityField.get(entity);
                    for (Field dtoField : dtoClass.getDeclaredFields()) {
                        dtoField.setAccessible(true);
                        if (dtoField.getName().equals(entityField.getName()) &&
                                dtoField.getType().equals(entityField.getType())) {
                            dtoField.set(dto, value);
                            break;
                        }
                    }
                }
            }
            return (D) dto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting entity to DTO", e);
        }
    }

    public static <T, D> T convertToEntity(D dtoObj, Class<T> entityClass) {
        try {
            IArqDTO dto = (IArqDTO) dtoObj;
            T entity = entityClass.getDeclaredConstructor().newInstance();
            if (dto.getMapaConversion() == null || dto.getMapaConversion().isEmpty()) {
                for (Field dtoField : dto.getClass().getDeclaredFields()) {
                    dtoField.setAccessible(true);
                    Object value = dtoField.get(dto);
                    for (Field entityField : entityClass.getDeclaredFields()) {
                        entityField.setAccessible(true);
                        if (entityField.getName().equals(dtoField.getName()) &&
                                entityField.getType().equals(dtoField.getType())) {
                            entityField.set(entity, value);
                            break;
                        }
                    }
                }
            } else {
                //TODO
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error converting DTO to entity", e);
        }
    }

}

