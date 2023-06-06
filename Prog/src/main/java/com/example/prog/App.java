package com.example.prog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Objects;

import static com.example.prog.MainWindow.*;
import static com.example.prog.PlanWindowController.dayStage;
import static com.example.prog.StatController.incom;
import static com.example.prog.StatController.spend;


public class App extends Application {
    protected static Stage stg;

    @Override
        public void start (Stage stage) throws IOException{
        stg =stage;
        stage.setResizable(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Window.fxml")));
        stage.setTitle("Program");
        stage.setScene(new Scene(root/*, 366, 324*/));
        stage.show();
        checkConnect();
    }

    public void changeScene(String fxml/*,int v,int v1*/) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stg.setScene(new Scene(pane/*, v,v1*/));
    }
    private void checkConnect(){
        Connection connection=null;
        Statement statement=null;
        Connect c=new Connect();
        connection=c.get_Connection();
        try{
            String query="create table if not exists users(id SERIAL primary key ,name varchar(15), username varchar(10), password varchar(15), balance float(10))";
            statement=connection.createStatement();
            statement.executeUpdate(query);
           // System.out.println("Table users has been created");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String query="create table if not exists information ( id SERIAL PRIMARY KEY , id_owner integer default 1, time varchar(30), operation varchar(20), note varchar(60),foreign key (id_owner) references users (id))";
            statement=connection.createStatement();
            statement.executeUpdate(query);
          //  System.out.println("Table information has been created");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String query="create table if not exists Day_plan ( id SERIAL PRIMARY KEY , id_owner integer default 1, day varchar(15), time varchar(15), note varchar(60),foreign key (id_owner) references users (id))";
            statement=connection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage1.initModality(Modality.APPLICATION_MODAL);
        stage2.initModality(Modality.APPLICATION_MODAL);
        dayStage.initModality(Modality.APPLICATION_MODAL);
        incom.initModality(Modality.APPLICATION_MODAL);
        spend.initModality(Modality.APPLICATION_MODAL);
    }

    public static void main(String[] args) {
        launch();
    }
}