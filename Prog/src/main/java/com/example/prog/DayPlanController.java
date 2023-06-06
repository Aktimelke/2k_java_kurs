package com.example.prog;

import com.example.prog.Models.Plan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import static com.example.prog.LogIn.userId;
import static com.example.prog.PlanWindowController.date;
import static com.example.prog.PlanWindowController.dayStage;


public class DayPlanController implements Initializable {
    @FXML
    private TableColumn<Plan, String> planColumn;

    @FXML
    private TableView<Plan> planTable;

    @FXML
    private TableColumn<Plan, String> timeColumn;

    private final ObservableList<Plan> PlanData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closingApp();
        timeColumn.setCellValueFactory(new PropertyValueFactory<Plan,String>("Hour"));
        planColumn.setCellValueFactory(new PropertyValueFactory<Plan,String>("Note"));
        planTable.setEditable(true);

        planColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        setTime();

        planColumn.setOnEditCommit(event ->{

            String newOperation = event.getNewValue();
            System.out.println(newOperation);

            int rowIndex = event.getTablePosition().getRow();
            Plan rowData = planTable.getItems().get(rowIndex);
            String timeValue = timeColumn.getCellData(rowData);

            if(newOperation.length()<=60){

                addInfo(timeValue,newOperation);

            }else{
                planTable.refresh();
            }

        });
        PlanInfo();

        planTable.setItems(PlanData);

    }


    private void PlanInfo()
    {
        Connection connection=null;
        Statement statement= null;
        Connect c=new Connect();
        connection=c.get_Connection();
        ResultSet res =null;

        try{
            String query = String.format("SELECT time,note FROM day_plan WHERE id_owner='%d' AND day='%s'",userId,date);
            statement=connection.createStatement();
            res = statement.executeQuery(query);
            while (res.next()){

                Plan plan = new Plan(res.getString("time"),res.getString("note"));
               // System.out.println(index(res.getString("time")));
                PlanData.set(index(res.getString("time")),plan);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int index(String timeString) {
        String hourString = timeString.substring(0, 2);
        return Integer.parseInt(hourString);
    }


    private void addInfo(String time,String note){
        boolean flag =true;
        int id=0;
        Connection connection=null;
        Statement statement= null;
        Connect c=new Connect();
        connection=c.get_Connection();
        ResultSet res =null;


        System.out.println(userId);
        System.out.println(date);
        System.out.println(time);

        try{
            String query = String.format("SELECT id FROM day_plan WHERE id_owner='%d' AND day='%s' AND time='%s'",userId,date,time);
            statement=connection.createStatement();
            res = statement.executeQuery(query);
            while (res.next()){
                flag = false;
                System.out.println(res.getInt("id"));
                id=res.getInt("id");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(flag){
            System.out.println("ins");
            try {
                String query = String.format("insert into day_plan(id_owner,day,time,note) values('%d','%s','%s','%s')", userId, date,time,note);
                statement = connection.createStatement();
                statement.executeUpdate(query);
                System.out.println(query);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("upd");
            System.out.println(id);
            try {
                String query = String.format("UPDATE day_plan SET note='%s' WHERE id ='%d'",note,id);
                statement = connection.createStatement();
                statement.executeUpdate(query);
                System.out.println(query);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    private void closingApp() {
        dayStage.setOnCloseRequest(event -> {
            PlanData.clear();

            dayStage.close();
            event.consume();

        });
    }
    private void setTime(){
        for(int i =0; i<24;i++){
            String  time = String.format("%02d:00", i);
            Plan plan = new Plan(time,"");

            PlanData.add(plan);
        }
    }
}
