package edu.school21.application;


import edu.school21.entity.User;
import edu.school21.manager.ORMManager;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        DataSource dataSource = new edu.school21.datasource.DataSource().getDataSource();
        ORMManager ormManager = new ORMManager(dataSource);
        User newUser = new User("Mark", "Tven", 20, true, 15000L);
        System.out.println(newUser);
        ormManager.save(newUser);
        System.out.println("--------------FIND---------------");
        User findUser = ormManager.findByID(1L, User.class);
        System.out.println(findUser);
        Field field = newUser.getClass().getDeclaredField("firstName");
        field.setAccessible(true);
        field.set(findUser, "Marklin");
        ormManager.update(findUser);
        User findUserAfterUpdate = ormManager.findByID(1L, User.class);
        System.out.println("---------------AFTER UPDATE---------------");
        System.out.println(findUserAfterUpdate);

    }
}
