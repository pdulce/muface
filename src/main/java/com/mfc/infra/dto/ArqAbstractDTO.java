package com.mfc.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public abstract class ArqAbstractDTO implements IArqDTO {

    @JsonIgnore
    public abstract Map<String, String> getMapaConversion();

    @JsonIgnore
    public abstract List<String> getModelMongoEntities();

    @JsonIgnore
    public abstract List<String> getModelJPAEntities();

    @JsonIgnore
    public abstract String getJPAEntidadPrincipal();

    @JsonIgnore
    public abstract String getMongoEntidadPrincipal();

    @JsonIgnore
    private static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error de mapeo:: el campo " + fieldName + " no existe en ");
        }
    }

    @JsonIgnore
    public static void incluirEnDTO(Object entity, IArqDTO dto) {
        try {
            //IArqDTO dto = (IArqDTO) dtoClass.getDeclaredConstructor().newInstance();
            if (dto.getMapaConversion() == null || dto.getMapaConversion().isEmpty()) {
                for (Field entityField : entity.getClass().getDeclaredFields()) {
                    entityField.setAccessible(true);
                    Object value = entityField.get(entity);
                    for (Field dtoField : dto.getClass().getDeclaredFields()) {
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
                    Field dtoField = getFieldByName(dto.getClass(), dtoFieldName);

                    if (entityField != null && dtoField != null) {
                        entityField.setAccessible(true);
                        dtoField.setAccessible(true);
                        Object value = entityField.get(entity);
                        if (dtoField.getType().equals(entityField.getType())) {
                            dtoField.set(dto, value);
                        } else if (value != null){
                            if (dtoField.getType().getSimpleName().contentEquals("String")) {
                                dtoField.set(dto, value.toString());
                            } else if (dtoField.getType().getSimpleName().contentEquals("Long")) {
                                dtoField.set(dto, Long.valueOf(value.toString()));
                            } else if (dtoField.getType().getSimpleName().contentEquals("Integer")) {
                                dtoField.set(dto, Long.valueOf(value.toString()));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in method::convertToDTO:: converting entity to DTO", e);
        }
    }

    @JsonIgnore
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

                        Object value = dtoField.get(dto);
                        if (entityField.getType().equals(dtoField.getType())) {
                            entityField.set(entity, value);
                        } else if (value != null){
                            if (entityField.getType().getSimpleName().contentEquals("String")) {
                                entityField.set(entity, value.toString());
                            } else if (entityField.getType().getSimpleName().contentEquals("Long")) {
                                entityField.set(entity, Long.valueOf(value.toString()));
                            } else if (entityField.getType().getSimpleName().contentEquals("Integer")) {
                                entityField.set(entity, Long.valueOf(value.toString()));
                            }
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
