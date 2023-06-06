package com.example.prog.Models;

public class Information {
    String time;
    String Operation;
    String Notes;
    public Information(){}


    public Information(String time,String Operation,String Notes){
        this.time=time;
        this.Operation=Operation;
        this.Notes=Notes;
    }

    public String getTime(){
        return time;
    }
    public String getOperation(){
        return Operation;
    }
    public String getNotes(){
        return Notes;
    }
    public void setOperation(String Operation){
        this.Operation=Operation;
    }
    public void setNotes(String Notes) {
        this.Notes = Notes;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
