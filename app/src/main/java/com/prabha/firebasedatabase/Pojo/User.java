package com.prabha.firebasedatabase.Pojo;

import java.util.List;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class User {

    private String email;
    private String name;
    private String mobile;
    private String age;
    private String city;
    private String area;
    private String createdAt;
    private String userType;
    private String gender;



    public String shoes;
    private List<Event> joinEvents;

    public User(){

    }

    public User(String email, String name, String mobile, String age, String city, String area,String shoes,String gender, String longestDistanceRan,String createdAt,String userType,List<Event> joinEvents) {
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.age = age;
        this.city = city;
        this.area = area;
        this.shoes = shoes;
        this.gender = gender;
        this.createdAt = createdAt;
        this.userType = userType;
        this.joinEvents = joinEvents;
        this.longestDistanceRan = longestDistanceRan;
    }
    public String getShoes() {
        return shoes;
    }

    public void setShoes(String shoes) {
        this.shoes = shoes;
    }
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

       public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLongestDistanceRan() {
        return longestDistanceRan;
    }

    public void setLongestDistanceRan(String longestDistanceRan) {
        this.longestDistanceRan = longestDistanceRan;
    }
    public List<Event> getJoinEvents() {
        return joinEvents;
    }

    public void setJoinEvents(List<Event> joinEvents) {
        this.joinEvents = joinEvents;
    }
    private String longestDistanceRan;
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
