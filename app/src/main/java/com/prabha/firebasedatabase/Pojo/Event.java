package com.prabha.firebasedatabase.Pojo;

import androidx.annotation.Keep;

import java.util.List;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
@Keep
public class Event {
    private String eventName;
    private String startDate;
    private String area;
    private String eventId;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String distance;

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String venue;
    private String createdAt;
    private List<User> joinUsers;

    public Event() {}
    public Event(String eventName, String area, String startDate,String venue,String createdAt,String distance,String description,List<User> joinUsers,String eventId) {
        this.eventName = eventName;
        this.area = area;
        this.eventId = eventId;
        this.startDate = startDate;
        this.venue = venue;
        this.createdAt = createdAt;
        this.joinUsers = joinUsers;
        this.description = description;
        this.distance  =distance;
    }
    public List<User> getJoinUsers() {
        return joinUsers;
    }
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setJoinUsers(List<User> joinUsers) {
        this.joinUsers = joinUsers;
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


}
