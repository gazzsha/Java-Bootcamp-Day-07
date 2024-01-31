package edu.school21.manager;

import edu.school21.annotation.OrmColumn;
import edu.school21.annotation.OrmColumnId;
import edu.school21.annotation.OrmEntity;
import edu.school21.entity.User;
import edu.school21.utils.Types;
import org.checkerframework.checker.units.qual.A;
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

    public <T> T findByID(Long id,Class<T> aClass) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T object = null;
      try (Connection connection = dataSource.getConnection()) {
          OrmEntity declaredAnnotation = aClass.getDeclaredAnnotation(OrmEntity.class);
          final String tableName = declaredAnnotation.table();
          final String SQLQuery = "select * from " + tableName + " where id = ?";
          PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);
          preparedStatement.setLong(1,id);
          ResultSet resultSet = preparedStatement.executeQuery();
          System.out.println(preparedStatement);
          while (resultSet.next()) {
              object = aClass.getDeclaredConstructor().newInstance();
              Field[] declaredFields = aClass.getDeclaredFields();
              for (Field field : declaredFields) {
                  OrmColumn annotationsByType = field.getAnnotations(OrmColumn.class);
                  if (annotationsByType != null ) {
                      Field declaredField = object.getClass().getDeclaredField(field.getName());
                      declaredField.setAccessible(true);
                      System.out.println(resultSet.getObject(annotationsByType.name()));
                      declaredField.set(object, resultSet.getObject(annotationsByType.name()));
                  }
              }
          }
      }
      return object;
    }
}
