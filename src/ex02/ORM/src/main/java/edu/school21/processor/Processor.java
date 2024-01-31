package edu.school21.processor;

import com.google.auto.service.AutoService;
import edu.school21.annotation.OrmColumn;
import edu.school21.annotation.OrmColumnId;
import edu.school21.annotation.OrmEntity;
import edu.school21.utils.Types;
import org.reflections.Reflections;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

@SupportedAnnotationTypes("edu.school21.annotation.OrmEntity")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class Processor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        StringBuilder SQLDropTable = new StringBuilder("DROP TABLE IF EXISTS ");
        DataSource dataSource = new edu.school21.datasource.DataSource().getDataSource();
        StringBuilder SQLQuery = new StringBuilder("CREATE TABLE ");
        Reflections reflections = new Reflections("edu.school21.entity");
        Set<Class<?>> elements = reflections.getTypesAnnotatedWith(OrmEntity.class);
        String tableName = null;
        for (Class<?> clasz : elements) {
            Annotation[] annotations1 = clasz.getAnnotations();
            for (Annotation annotation : annotations1) {
                if (annotation instanceof OrmEntity) {
                    tableName = ((OrmEntity) annotation).table();
                    SQLQuery.append(tableName).append(" (\n");
                    SQLDropTable.append(tableName);
                }
            }
            Field[] fields = clasz.getDeclaredFields();
            for (Field field : fields) {
                Annotation[] annotationsOnField = field.getAnnotations();
                for (Annotation annotation : annotationsOnField) {
                    if (annotation instanceof OrmColumn) {
                        SQLQuery.append(((OrmColumn) annotation).name()).append(" ").append(Types.returnType(field.getGenericType(), annotation)).append(" ,\n");
                    }
                    if (annotation instanceof OrmColumnId) {
                        SQLQuery.append("ID SERIAL PRIMARY KEY,\n");
                    }
                }
            }
        }
        SQLQuery.replace(SQLQuery.length() - 2, SQLQuery.length() - 1, ")");
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement1 = connection.prepareStatement(SQLDropTable.toString());
            System.out.println(SQLDropTable);
            System.out.println(SQLQuery);
            preparedStatement1.executeUpdate();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.toString());
            preparedStatement.executeUpdate();
            System.out.println(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


