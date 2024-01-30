//package edu.school21.manager;
//
//import edu.school21.annotation.OrmColumn;
//import edu.school21.annotation.OrmColumnId;
//import edu.school21.annotation.OrmEntity;
//import edu.school21.entity.User;
//import edu.school21.utils.Types;
//import org.checkerframework.checker.units.qual.A;
//import org.reflections.Reflections;
//
//import javax.lang.model.element.Element;
//import javax.sql.DataSource;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Type;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.*;
//
//public class ORMManager {
//    private final DataSource dataSource;
//
//    public ORMManager(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public void init() throws SQLException {
//       StringBuilder SQLQuery = new StringBuilder("CREATE TABLE ");
//        Reflections reflections = new Reflections("edu.school21.entity");
//        Set<Class<?>> elements = reflections.getTypesAnnotatedWith(OrmEntity.class);
//        String tableName = null;
//        for (Class<?> clasz : elements) {
//            Annotation[] annotations = clasz.getAnnotations();
//            for (Annotation annotation : annotations) {
//                if (annotation instanceof OrmEntity) {
//                    tableName = ((OrmEntity) annotation).table();
//                    SQLQuery.append(tableName).append(" (\n");
//                }
//            }
//            Field[] fields = clasz.getDeclaredFields();
//            for (Field field : fields) {
//                Annotation[] annotationsOnField = field.getAnnotations();
//                for (Annotation annotation : annotationsOnField) {
//                    if (annotation instanceof OrmColumn) {
//                        SQLQuery.append(((OrmColumn) annotation).name()).append(" ").append(Types.returnType(field.getGenericType(), annotation)).append(" ,\n");
//                    }
//                    if (annotation instanceof OrmColumnId) {
//                       SQLQuery.append("ID SERIAL PRIMARY KEY,\n");
//                    }
//                }
//            }
//        }
//        SQLQuery.replace(SQLQuery.length() - 2,SQLQuery.length() - 1,")");
//        try (Connection connection = dataSource.getConnection()) {
//            PreparedStatement preparedStatement =
//            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.toString());
//            preparedStatement.executeUpdate();
//            System.out.println(SQLQuery);
//        }
//    }
//}
