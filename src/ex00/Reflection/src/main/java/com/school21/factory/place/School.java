package com.school21.factory.place;

import java.util.concurrent.ThreadLocalRandom;

public class School {
    private Integer number;

    private String name;

    private Double averagePoints;

    public School(java.lang.Integer number, java.lang.String name, java.lang.Double averagePoints) {
        this.number = number;
        this.name = name;
        this.averagePoints = averagePoints;
    }

    public School() {
        number = 0;
        name = "Default value";
        averagePoints = 0.0;
    }

//    public void growAveragePoints(Double delta) {
//        averagePoints += delta;
//    }


    public String changeNameRandom(String nameFirst, String nameSecond) {
        int random = ThreadLocalRandom.current().nextInt(0, 2);
        name = (random == 0 ? nameFirst : nameSecond);
        return name;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "School{" +
                "number=" + number +
                ", name=" + name +
                ", averagePoints=" + averagePoints +
                '}';
    }
}
