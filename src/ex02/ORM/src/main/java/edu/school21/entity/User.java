package edu.school21.entity;


import edu.school21.annotation.OrmColumn;
import edu.school21.annotation.OrmColumnId;
import edu.school21.annotation.OrmEntity;

@OrmEntity(table = "simple_user")
public class User {
    @OrmColumnId
    private Integer id;

    @OrmColumn(name = "first_name",lenght = 10)
    private String firstName;
    @OrmColumn(name = "last_name",lenght = 10)
    private String lastName;

    @OrmColumn(name = "age")
    private Integer age;

    @OrmColumn(name = "status")
    private Boolean status;

    @OrmColumn(name = "money")
    private Long money;

    public User() {
    }

    public User(String firstName, String lastName, Integer age, Boolean status, Long money) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.status = status;
        this.money = money;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", status=" + status +
                ", money=" + money +
                '}';
    }
}
