package com.example.eventhunter.repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class DocumentModelMapper<T> {
    private final Class<T> clazz;

    public DocumentModelMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getModel(Map<String, Object> data) throws NoSuchMethodException {
        try {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            Constructor<T> noArgConstructor = null;
            for (Constructor<?> constructor : declaredConstructors) {
                if (constructor.getParameterTypes().length == 0) {
                    noArgConstructor = (Constructor<T>) constructor;
                    break;
                }
            }

            if (noArgConstructor == null) {
                throw new NoSuchMethodException("No No-Arg Constructor Present");
            }

            T model = noArgConstructor.newInstance();

            Class<?> superClass = clazz;

            do {
                Field[] declaredFields = superClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    String fieldName = field.getName();

                    if (data.containsKey(fieldName)) {
                        field.setAccessible(true);

                        try {
                            field.set(model, data.get(fieldName));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            // field in map has the same as the one in the instance, but is of different type
                        }
                    }
                }

                superClass = superClass.getSuperclass();

            } while (superClass != null);

            return model;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> createMap(T model) {
        Map<String, Object> dataMap = new HashMap<>();

        Class<?> superClass = clazz;

        do {
            Field[] declaredFields = superClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }

                String fieldName = field.getName();

                try {
                    field.setAccessible(true);
                    dataMap.put(fieldName, field.get(model));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

            superClass = superClass.getSuperclass();
        } while (superClass != null);

        return dataMap;
    }
}
