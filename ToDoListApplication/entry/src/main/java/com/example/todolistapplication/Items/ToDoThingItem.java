package com.example.todolistapplication.Items;

import java.util.Date;

public class ToDoThingItem {
    private int toDoThingId;  // 主键
    private String thingTittle;
    private String startTime;
    private String endTime;
    private String context;
    private boolean isActive;
    private boolean isComplete;


    public ToDoThingItem(int toDoThingId, String thingTittle, String startTime, String endTime, String context, boolean isActive, boolean isComplete) {
        this.toDoThingId = toDoThingId;
        this.thingTittle = thingTittle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.context = context;
        this.isActive = isActive;
        this.isComplete = isComplete;
    }


    public int getToDoThingId() {
        return toDoThingId;
    }

    public void setToDoThingId(int toDoThingId) {
        this.toDoThingId = toDoThingId;
    }

    public String getThingTittle() {
        return thingTittle;
    }

    public void setThingTittle(String thingTittle) {
        this.thingTittle = thingTittle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
