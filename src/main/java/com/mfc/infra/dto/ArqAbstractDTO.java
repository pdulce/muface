package com.mfc.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class ArqAbstractDTO implements IArqDTO {

    @JsonIgnore
    public abstract Map<String, String> getMapaConversion();


    private static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error de mapeo:: el campo " + fieldName + " no existe en ");
        }
    }

    public static <T, D> D convertToDTO(T entity, Class<D> dtoClass) {
        try {
            IArqDTO dto = (IArqDTO) dtoClass.getDeclaredConstructor().newInstance();
            if (dto.getMapaConversion() == null || dto.getMapaConversion().isEmpty()) {
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
            } else {
                for (Map.Entry<String, String> entry : dto.getMapaConversion().entrySet()) {
                    String entityFieldName = entry.getValue();
                    String dtoFieldName = entry.getKey();

                    Field entityField = getFieldByName(entity.getClass(), entityFieldName);
                    Field dtoField = getFieldByName(dtoClass, dtoFieldName);

                    if (entityField != null && dtoField != null) {
                        entityField.setAccessible(true);
                        dtoField.setAccessible(true);

                        if (dtoField.getType().equals(entityField.getType())) {
                            Object value = entityField.get(entity);
                            dtoField.set(dto, value);
                        }
                    }
                }
            }
            return (D) dto;
        } catch (Exception e) {
            throw new RuntimeException("Error in method::convertToDTO:: converting entity to DTO", e);
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
                for (Map.Entry<String, String> entry : dto.getMapaConversion().entrySet()) {
                    String entityFieldName = entry.getValue();
                    String dtoFieldName = entry.getKey();

                    Field entityField = getFieldByName(entityClass, entityFieldName);
                    Field dtoField = getFieldByName(dto.getClass(), dtoFieldName);

                    if (entityField != null && dtoField != null) {
                        dtoField.setAccessible(true);
                        entityField.setAccessible(true);

                        if (entityField.getType().equals(dtoField.getType())) {
                            Object value = dtoField.get(dto);
                            entityField.set(entity, value);
                        }
                    }
                }
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error in method::convertToEntity:: converting DTO to entity", e);
        }
    }

}
