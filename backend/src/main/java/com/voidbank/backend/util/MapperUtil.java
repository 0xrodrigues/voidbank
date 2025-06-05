package com.voidbank.backend.util;

import java.lang.reflect.Field;

public class MapperUtil {

    public static <S, T> T map(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();

            Field[] sourceFields = source.getClass().getDeclaredFields();
            Field[] targetFields = targetClass.getDeclaredFields();

            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true);

                for (Field targetField : targetFields) {
                    if (targetField.getName().equals(sourceField.getName())
                            && targetField.getType().isAssignableFrom(sourceField.getType())) {

                        targetField.setAccessible(true);
                        targetField.set(target, sourceField.get(source));
                        break;
                    }
                }
            }

            return target;

        } catch (Exception e) {
            throw new RuntimeException("Failed to map object", e);
        }
    }
}
