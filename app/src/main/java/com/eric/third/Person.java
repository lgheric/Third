package com.eric.third;

public class Person {
    private int id;
    private String name;
    private int age;

    public Person(){

    }

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }


    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Integer getAge(){
        return this.age;
    }
    public void setAge(Integer age){
        this.age = age;
    }

    @Override
    public String toString() {
        return "姓名：" + this.name + "，年龄：" + this.age;
    }

}
