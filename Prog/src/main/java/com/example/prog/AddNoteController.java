package com.example.prog;

import com.example.prog.Models.Information;
import com.example.prog.limitedFields.LimitedTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.prog.MainWindow.*;
import static com.example.prog.LogIn.userId;
import static com.example.prog.CreateAccount.balance;



public class AddNoteController implements Initializable,Desk {
    @FXML
    private Label inValue;
    @FXML
    private Button Cancelb;
    @FXML
    private Button Infb;
    @FXML
    private LimitedTextField Numbers;
    @FXML
    private LimitedTextField Text;
    @FXML
    private Button Sign;
    @FXML
    private Label plusMinus;
    @FXML
    void Add() {
        Adding();
    }
    private boolean flag3= true;
    private boolean flag,flag2;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a dd-MM-yyyy");
    String timeNow = sdf.format(new Date());
    private void Adding(){
        Connection connection=null;
        Statement statement=null;
        Connect c=new Connect();
        connection=c.get_Connection();

        input();
        if(flag3 && !Numbers.getText().toString().isEmpty()) {
            float number = 0;
            String text = null;
            char p=' ';

            text = Text.getText().toString();
            number = Float.parseFloat(Numbers.getText());
            if (flag) {
                number = +number;
                p = '+';
            } else {
                number = -number;
            }
          //   System.out.println(number);
          //  System.out.println(text);
            // System.out.println(userId);

            closeWindow();
            if(flag2) {
                try {
                    String query = String.format("insert into information(id_owner,time,operation,note) values('%d','%s','%c%.2f $','%s')", userId, timeNow,p,number,text);
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                    System.out.println(query);

                    balance +=number;

                    String query2 = String.format("UPDATE users SET balance ='%.2f' WHERE id ='%d'",balance,userId);
                    statement = connection.createStatement();
                    statement.executeUpdate(query2);
                    System.out.println(query2);

                    addingInfo();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            inValue.setText("Invalid input");
        }
    }



    @Override
    public void addingInfo(){
            Connection connection=null;
            Statement statement= null;
            Connect c=new Connect();
            connection=c.get_Connection();
            ResultSet res =null;
            try {
                String query = String.format("SELECT id_owner,time,operation,note FROM information WHERE id_owner='%d'",userId);
                statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                res = statement.executeQuery(query);
                while (res.next()){
                    res.last();
                    {
                      //  System.out.println(res.getString("id_owner"));
                        Information information = new Information(res.getString("time"),res.getString("operation"),res.getString("note"));
                        tableData.add(information);
                    }
                }
            }  catch (Exception e){
                e.printStackTrace();
            }
    }

    @FXML
    void Cancel() {
        stage.close();
    }

    @FXML
    void ChooseSign() {
        if(plusMinus.getText().toString().equals("+")){
            plusMinus.setText("-");
            flag = false;
        }
        else if(plusMinus.getText().toString().equals("-")){
            plusMinus.setText("+");
            flag = true;
        }
    }
    private void input(){
        char i ='.';
        String input = Numbers.getText();
        if(countOccurrences(input,i)>1){
            flag3= false;
        }
        else if(countOccurrences(input,i)<=1){
            flag3 = true;
        }
    }
    public static int countOccurrences(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }
    @FXML
    void chekInput(KeyEvent event) {
            if (event.getCharacter().matches("[A-Za-z _-]")) {
                event.consume();
                Numbers.setStyle("-fx-border-color: red");
                inValue.setText("Only numbers");
            } else {
                Numbers.setStyle("-fx-border-color: blue");
                inValue.setText("");
            }

    }
    private void closeWindow(){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().setPrefSize(270,1);
            alert.initOwner(stage);
            alert.setTitle("Adding");
            alert.setHeaderText(null);
            alert.setContentText("Добавить запись?");

            Optional<ButtonType> res = alert.showAndWait();
            if(res.get() == ButtonType.OK){
                flag2 = true;
                stage.close();
            }
            else if(res.get() == ButtonType.CANCEL){
                System.out.println("Cancel");
                flag = false;
            }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Numbers.setFloatField();
        Numbers.setMaxLength(10);
        Text.setMaxLength(60);

    }

}


