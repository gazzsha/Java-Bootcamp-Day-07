package edu.school21.manager;

import edu.school21.annotation.OrmColumn;
import edu.school21.annotation.OrmColumnId;
import edu.school21.annotation.OrmEntity;
import edu.school21.entity.User;
import edu.school21.utils.Types;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.reflections.Reflections;

import javax.lang.model.element.Element;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ORMManager {
    private final DataSource dataSource;

    public ORMManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T findByID(Long id, Class<T> aClass) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T object = null;
        try (Connection connection = dataSource.getConnection()) {
            OrmEntity declaredAnnotation = aClass.getDeclaredAnnotation(OrmEntity.class);
            final String tableName = declaredAnnotation.table();
            final String SQLQuery = "select * from " + tableName + " where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(preparedStatement);
            while (resultSet.next()) {
                object = aClass.getDeclaredConstructor().newInstance();
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    Annotation[] annotations = field.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof OrmColumn) {
                            Field declaredField = object.getClass().getDeclaredField(field.getName());
                            declaredField.setAccessible(true);
                            declaredField.set(object, resultSet.getObject(((OrmColumn) annotation).name()));
                        }
                        if (annotation instanceof OrmColumnId) {
                            Field declaredField = object.getClass().getDeclaredField(field.getName());
                            declaredField.setAccessible(true);
                            declaredField.set(object, resultSet.getObject("id"));
                        }
                    }
                }
            }
        }
        return object;
    }


    public void save(Object entity) throws SQLException, NoSuchFieldException, IllegalAccessException {
        OrmEntity declaredAnnotation = entity.getClass().getDeclaredAnnotation(OrmEntity.class);
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        List<String> fieldsInTable = new ArrayList<>();
        for (Field field : declaredFields) {
            OrmColumn annotation = field.getAnnotation(OrmColumn.class);
            if (annotation != null) fieldsInTable.add(annotation.name());
        }
        StringBuilder SQLQuery = new StringBuilder("insert into " + declaredAnnotation.table() + " (");
        for (String valuesName : fieldsInTable) {
            SQLQuery.append(valuesName).append(",");
        }
        SQLQuery.replace(SQLQuery.length() - 1, SQLQuery.length(), ")");
        SQLQuery.append(" values (");
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof OrmColumn) {
                    Field fieldInClass = entity.getClass().getDeclaredField(field.getName());
                    fieldInClass.setAccessible(true);
                    if (fieldInClass.getGenericType() == String.class) {
                        SQLQuery.append("'");
                        SQLQuery.append(fieldInClass.get(entity));
                        SQLQuery.append("'");
                    } else SQLQuery.append(fieldInClass.get(entity));
                    SQLQuery.append(",");
                }
            }
        }
        SQLQuery.replace(SQLQuery.length() - 1, SQLQuery.length(), ")");
        try (Connection connection = dataSource.getConnection()) {
            System.out.println(SQLQuery.toString());
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.toString());
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
        }
    }

    public void update(Object entity) throws IllegalAccessException, NoSuchFieldException, SQLException {
        OrmEntity declaredAnnotation = entity.getClass().getDeclaredAnnotation(OrmEntity.class);
        Map<String,Object> values = new HashMap<>();
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof OrmColumn) {
                    Field fieldInClass = entity.getClass().getDeclaredField(field.getName());
                    fieldInClass.setAccessible(true);
                    values.put(((OrmColumn) annotation).name(),fieldInClass.get(entity));
                }
            }
        }
        StringBuilder SQLQuery = new StringBuilder("update ");
        SQLQuery.append(declaredAnnotation.table()).append(" ").append("set ");
        for (Map.Entry<String,Object> map : values.entrySet()) {
            SQLQuery.append(map.getKey()).append(" = ");
            if (map.getValue() instanceof String) {
                SQLQuery.append("'").append(map.getValue()).append("',");
            } else
                SQLQuery.append(map.getValue()).append(",");
        }
        SQLQuery.replace(SQLQuery.length() - 1, SQLQuery.length(), " ");
        SQLQuery.append("where id = ");
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        SQLQuery.append(field.get(entity));
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.toString());
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
        }
    }
}
