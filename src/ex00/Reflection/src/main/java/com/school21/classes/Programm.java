package com.school21.classes;

import com.school21.employee.User;
import com.school21.factory.place.School;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;

public class Programm {


    static final String CLASS_NAME_EMPLOYEE = "com.school21.employee.User";
    static final String CLASS_NAME_FACTORY = "com.school21.factory.place.School";

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        try (Scanner scanner = new Scanner(System.in)) {
            menu(scanner);
        }

    }

    static void printNameClasses(String name1, String name2) {
        System.out.println("Classes:");
        System.out.println(name1);
        System.out.println(name2);
        System.out.println("----------------------------");
    }


    static void menus(Scanner scanner, Class clazz, String className, Object user, Object school) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        System.out.println("2. Show all fields");
        System.out.println("3. Create object");
        System.out.println("4. Call function");
        System.out.println("5. Change field");
        int use = scanner.nextInt();
        switch (use) {
            case 2: {
                System.out.println("Fields:");
                Arrays.stream(clazz.getDeclaredFields()).map(field -> field.getGenericType() + " " + field.getName()).forEach(System.out::println);
                System.out.println("Methods:");
                Arrays.stream(clazz.getDeclaredMethods()).map(method -> method.getGenericReturnType() + " " + method.getName()).forEach(System.out::println);
                System.out.println("----------------------------");
                break;
            }
            case 3: {
                if ("User".equals(className)) {
                    Class classUser = Class.forName(CLASS_NAME_EMPLOYEE);
                    Class[] userParams = {String.class, String.class, int.class};
                    System.out.println("fistName:");
                    String firstName = scanner.next();
                    System.out.println("secondName:");
                    String secondName = scanner.next();
                    System.out.println("height:");
                    int height = scanner.nextInt();
                    user = classUser.getConstructor(userParams).newInstance(firstName, secondName, height);
                    System.out.println("Object created: " + user);
                } else if ("School".equals(className)) {
                    Class classSchool = Class.forName(CLASS_NAME_FACTORY);
                    Class[] schoolParams = {Integer.class, String.class, Double.class};
                    System.out.println("ID:");
                    Integer id = scanner.nextInt();
                    System.out.println("Name:");
                    String name = scanner.next();
                    System.out.println("Average points:");
                    Double avrgPoints = scanner.nextDouble();
                    school = classSchool.getConstructor(schoolParams).newInstance(id, name, avrgPoints);
                    System.out.println("Object created: " + school);
                } else menu(scanner);
                System.out.println("----------------------------");
                break;
            }
            case 4: {
                Arrays.stream(clazz.getDeclaredMethods()).map((Method::getName)).forEach(System.out::println);
                System.out.println("Enter name of the method for call");
                String nameMethod = scanner.next();
                Class paramType = "User".equals(className) ? int.class : String.class;
                Method method = "User".equals(className) ? clazz.getDeclaredMethod(nameMethod, paramType) :
                        clazz.getDeclaredMethod(nameMethod, paramType, paramType);
                if ("User".equals(className)) {
                    System.out.println("Enter int value");
                    int value = scanner.nextInt();
                    Object result = method.invoke(user, value);
                    System.out.println("Method returned:");
                    System.out.println((int) result);
                } else {
                    System.out.println("Enter string first value");
                    String s1 = scanner.next();
                    System.out.println("Enter string second value");
                    String s2 = scanner.next();
                    Object result = method.invoke(school, s1, s2);
                    System.out.println("Method returned:");
                    System.out.println((String) result);
                }
                System.out.println("----------------------------");
                break;
            }
            case 5: {
                System.out.println("Enter name of the field for changing:");
                String field = scanner.next();
                Field fieldOfClass = clazz.getDeclaredField(field);
                fieldOfClass.setAccessible(true);
                Class<?> typeofField = fieldOfClass.getGenericType().getClass();
                Object param;
                if (fieldOfClass.getGenericType() == Integer.class) {
                    System.out.println("Enter Integer value:");
                    param = scanner.nextInt();
                } else if (fieldOfClass.getGenericType() == String.class) {
                    System.out.println("Enter String value:");
                    param = scanner.next();
                } else {
                    System.out.println("Enter Double value:");
                    param = scanner.nextDouble();
                }
                System.out.println("Object updated:");
                if ("User".equals(className)) {
                    fieldOfClass.set(user, param);
                    System.out.println(user);
                } else if ("School".equals(className)) {
                    fieldOfClass.set(school,param);
                    System.out.println(school);
                }
                break;
            }
        }
        menus(scanner, clazz, className, user, school);
    }

    static void menu(Scanner scanner) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        printNameClasses(CLASS_NAME_EMPLOYEE, CLASS_NAME_FACTORY);
        System.out.println("1. Enter class name, which will be created");
        String className = scanner.next();
        Class<?> userClass = Class.forName(CLASS_NAME_EMPLOYEE);
        Class<?> factory = Class.forName(CLASS_NAME_FACTORY);
        Class<?> clazz = "User".equals(className) ? Class.forName(CLASS_NAME_EMPLOYEE) : Class.forName(CLASS_NAME_FACTORY);
        Object user = null;
        Object school = null;
        menus(scanner, clazz, className, user, school);
    }
}
