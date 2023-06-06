package com.example.prog;

import com.example.prog.limitedFields.LimitedPasswordField;
import com.example.prog.limitedFields.LimitedTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LogIn implements Initializable {
    public LogIn(){}
    App a = new App();
    private boolean checkLogin ;
    private boolean checkPass ;
    public static String login2 = null;
    public static int userId;
    @FXML
    private Button logIn;
    @FXML
    private Button createAcc;
    @FXML
    private Label wrongLogIn;
    @FXML
    private LimitedPasswordField password;
    @FXML
    private LimitedTextField username;
    @FXML
    void userLogIn() throws IOException{
        checkLog();
    }
    @FXML
    void creatingAcc() throws IOException{
        a.changeScene("CreateAccWindow.fxml"/*,282,412*/);
    }

    private void checkLog() throws IOException{
        Connection connection=null;
        Statement statement=null;
        Connect c=new Connect();
        connection=c.get_Connection();
        ResultSet res =null;
        ResultSet res1 =null;
        String login = username.getText().toString();
        String pass = password.getText().toString();

        try{
            checkPass=false;
            checkLogin=false;
            String query = String.format("SELECT id,username FROM users WHERE username='%s'",login);
            statement=connection.createStatement();
            res = statement.executeQuery(query);
            while (res.next()){
               // System.out.println(res.getString("username"));
                userId= Integer.parseInt(res.getString("id"));
                checkLogin=true;
               // System.out.println(userId);
               // System.out.println("OK");
            }
            String query2 = String.format("SELECT password FROM users WHERE password='%s'",pass);
            statement=connection.createStatement();
            res1 = statement.executeQuery(query2);
            while (res1.next()){
               // System.out.println(res1.getString("password"));
                checkPass=true;
               // System.out.println("OK");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(checkLogin && checkPass){
                login2= username.getText().toString();
            a.changeScene("MainWindow.fxml"/*,705,470*/);
        }
        else if (username.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogIn.setText("Enter your data.");
        }
        else{
            wrongLogIn.setText("Wrong data!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setMaxLength(10);
        password.setMaxLength(15);
    }
}
