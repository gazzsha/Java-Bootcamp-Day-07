package edu.school21.utils;

import edu.school21.annotation.OrmColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class Types {
 public static String returnType(Type type, Annotation annotation) {
     if (type == Integer.class) return "INTEGER";
     if (type == Double.class) return "real";
     if (type == String.class)  {
         int length = ((OrmColumn)annotation).lenght();
         return "varchar(" + length + ")";
     }
     if (type == Long.class) return "numeric";
     return null;
 }
}
