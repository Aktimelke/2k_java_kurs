package com.example.prog.Models;

public class Plan {
    String hour;
    String note;
    public Plan(){}

    public Plan(String hour,String note){
        this.hour=hour;
        this.note=note;
    }

    public String getHour(){return hour;}
    public String getNote(){return note;}

    public void setHour(String hour){
        this.hour=hour;
    }
    public void setNote(String note){
        this.note=note;
    }

}
