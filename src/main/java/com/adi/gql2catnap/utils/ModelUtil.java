package com.adi.gql2catnap.utils;

import com.github.gregwhitaker.catnap.core.model.DefaultListBackedModel;
import com.github.gregwhitaker.catnap.core.model.DefaultMapBackedModel;
import com.github.gregwhitaker.catnap.core.model.ListBackedModel;
import com.github.gregwhitaker.catnap.core.model.MapBackedModel;
import com.github.gregwhitaker.catnap.core.model.Model;
import com.github.gregwhitaker.catnap.core.query.model.Query;
import com.github.gregwhitaker.catnap.core.query.processor.Property;
import com.github.gregwhitaker.catnap.core.query.processor.QueryProcessor;
import com.github.gregwhitaker.catnap.core.query.processor.QueryProcessorFactory;
import com.github.gregwhitaker.catnap.core.util.ClassUtil;

import java.util.List;
import java.util.Map;

public class ModelUtil {

    private ModelUtil() {
    }

    public static <T> Model<T> build(Object instance, Query query) {
        Model<T> result = null;

        if (instance instanceof Iterable<?>) {
            result = new DefaultListBackedModel();
            buildList((Iterable<?>) instance, query, (ListBackedModel<?>) result);
        } else if (instance instanceof Map<?, ?>) {
            result = new DefaultMapBackedModel();
            buildMap((Map<?, ?>) instance, query, (MapBackedModel<?>) result);
        } else {
            result = new DefaultMapBackedModel();
            buildObject(instance, query, (MapBackedModel<?>) result);
        }

        return result;
    }

    private static void buildList(Iterable<?> instance, Query query, ListBackedModel<?> result) {
        if (instance != null) {
            for (Object item : instance) {
                if (item == null || ClassUtil.isPrimitiveType(item.getClass())) {
                    result.addValue(item);
                } else {
                    //Complex type - We need to go deeper!
                    buildObject(item, query, result.createChildMap());
                }
            }
        }
    }

    /**
     * @param instance
     * @param query
     * @param result
     */
    private static void buildMap(Map<?, ?> instance, Query query, MapBackedModel<?> result) {
        if (instance != null) {
            for (Map.Entry entry : instance.entrySet()) {
                if (entry.getValue() == null || ClassUtil.isPrimitiveType(entry.getValue().getClass())) {
                    result.addValue(entry.getKey().toString(), entry.getValue());
                } else {
                    buildObject(entry.getValue(), query, result.createChildMap(entry.getKey().toString()));
                }
            }
        }
    }

    /**
     * @param instance
     * @param query
     * @param result
     * @param <T>
     */
    private static <T> void buildObject(T instance, Query query, MapBackedModel<?> result) {
        if (instance != null) {
            Class<T> instanceClazz = ClassUtil.loadClass(instance);
            filterObject(instance, instanceClazz, query, result);
        }
    }

    /**
     * @param instance
     * @param instanceClazz
     * @param query
     * @param result
     * @param <T>
     */
    private static <T> void filterObject(T instance, Class<T> instanceClazz, Query query, MapBackedModel<?> result) {
        QueryProcessor queryProcessor = QueryProcessorFactory.createQueryProcessor(query, instanceClazz);
        List<Property<T>> properties = queryProcessor.process(query, instance, instanceClazz);

        for (Property<T> property : properties) {
            Object value = property.getValue();

            if (value == null) {
                continue;
            }

            String name = property.getRenderName();

            if (property.isPrimitive()) {
                result.addValue(name, value);
            } else {
                //Recursively filtering nested subqueries
                Query subQuery = null;

                if (query != null) {
                    subQuery = query.getSubquery(name);
                }

                if (Iterable.class.isAssignableFrom(value.getClass())) {
                    buildList((Iterable<?>) value, subQuery, result.createChildList(name));
                } else if (Map.class.isAssignableFrom(value.getClass())) {
                    buildMap((Map<?, ?>) value, subQuery, result.createChildMap(name));
                } else {
                    buildObject(value, subQuery, result.createChildMap(name));
                }
            }
        }
    }
}
