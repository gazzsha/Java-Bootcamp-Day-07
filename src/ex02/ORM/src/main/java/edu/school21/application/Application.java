package edu.school21.application;


import edu.school21.entity.User;
import edu.school21.manager.ORMManager;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        DataSource dataSource = new edu.school21.datasource.DataSource().getDataSource();
        ORMManager ormManager = new ORMManager(dataSource);
        User user = ormManager.findByID(1L, User.class);
        System.out.println(user);
    }
}
