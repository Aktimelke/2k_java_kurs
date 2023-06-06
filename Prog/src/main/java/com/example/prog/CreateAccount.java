package com.example.prog;

import com.example.prog.limitedFields.LimitedPasswordField;
import com.example.prog.limitedFields.LimitedTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;

import static com.example.prog.App.stg;
import static com.example.prog.LogIn.login2;
import static com.example.prog.LogIn.userId;
public class CreateAccount implements Initializable {

    public CreateAccount(){
    }
    App a = new App();
    protected static float balance = 0;
    @FXML
    private LimitedTextField name;

    @FXML
    private LimitedTextField username;
    @FXML
    private LimitedPasswordField password;
    @FXML
    private LimitedPasswordField conPass;
    @FXML
    private Label noName;
    @FXML
    private Label noUsername;
    @FXML
    private Label noPass;
    @FXML
    private Label wrongPass;
    @FXML
    private Button BackToLogIn;
    @FXML
    private Button reg;
    private boolean flag;
    @FXML
    void BackLogIn() throws IOException {
        a.changeScene("Window.fxml"/*,366,324*/);
    }
    @FXML
    void RegWindow() throws IOException {
        if (checkInfo()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setPrefSize(270,1);
            alert.initOwner(stg);
            alert.setTitle("notice");
            alert.setHeaderText(null);
            alert.setContentText("Your account created successfully");
            alert.showAndWait();
            a.changeScene("MainWindow.fxml"/*,705,470*/);
        }//alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE)); Может пригодится.
    }
    private boolean checkInfo() {
        Connection connection=null;
        Statement statement=null;

        Connect c=new Connect();
        connection=c.get_Connection();

        boolean checkName = false;
        boolean checkLogin = false;
        boolean checkPass = false;

        String name1,username1,password1,conPassword;
        name1=name.getText().toString();
        username1=username.getText().toString();
        password1=password.getText().toString();
        ResultSet res =null;
        ResultSet res2 =null;

        if(enterName()){
            checkName =true;
        }

        if(enterUsername()) {
            try {
                String query = String.format("SELECT username FROM users WHERE username='%s'", username1);
                statement = connection.createStatement();
                res = statement.executeQuery(query);
                if (res.next()) {
                    System.out.println(res.getString("username"));
                    noUsername.setText("This Username already exists");
                    System.out.println("error");
                } else {
                    checkLogin = true;
                    login2 = username1;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(enterPassword()){
            checkPass =true;
        }

        if(enterConPass()) {
            if (conPass.getText().toString().equals(password.getText().toString())) {
                wrongPass.setText("");
            } else {
                wrongPass.setText("Wrong password");
                checkPass =false;
            }
        }
        boolean flag2;
        if(checkName && checkLogin && checkPass &&enterConPass()){

            try{
                String query= String.format("insert into users(name,username,password,balance) values('%s','%s','%s','%.2f')",name1,username1,password1,balance);

                statement=connection.createStatement();
                statement.executeUpdate(query);
                System.out.println(query);
            }catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("New account has been created");
            try {
                String query2 = String.format("SELECT id,username FROM users WHERE username='%s'", username1);
                statement = connection.createStatement();
                res2 = statement.executeQuery(query2);
                while (res2.next()) {
                    System.out.println(res2.getString("username"));
                    userId = Integer.parseInt(res2.getString("id"));
                }
            }catch (Exception e){
                System.out.println(e);
            }
            flag2 = true;
        }
        else{
            flag2 = false;
        }

        return flag2;
    }
    private boolean enterName(){
        if(name.getText().isEmpty()){
            noName.setText("Please enter your name");
            flag =false;
        }
        else{
            noName.setText("");
            flag = true;
        }
        return flag;
    }
    private boolean enterUsername(){
        if(username.getText().isEmpty()){
            noUsername.setText("Please enter your username");
            flag = false;
        }
        else{
            noUsername.setText("");
            flag=true;
        }
        return flag;
    }
    private boolean enterPassword(){
        if(password.getText().isEmpty()){
            noPass.setText("Please enter password");
            flag = false;
        }
        else{
            noPass.setText("");
            flag=true;
        }
        return flag;
    }
    private boolean enterConPass(){
        if (conPass.getText().isEmpty() && enterPassword()) {
            wrongPass.setText("Confirm your password please");
            flag = false;
        }
        else {
            flag = true;
        }
        return flag;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setMaxLength(15);
        username.setMaxLength(10);
        password.setMaxLength(15);
        conPass.setMaxLength(15);
    }
}
