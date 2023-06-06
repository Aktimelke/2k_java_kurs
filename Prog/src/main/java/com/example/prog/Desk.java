package com.example.prog;

import com.example.prog.Models.Information;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.prog.CreateAccount.balance;
import static com.example.prog.LogIn.login2;
import static com.example.prog.LogIn.userId;
import static com.example.prog.MainWindow.tableData;


public interface Desk {
    default void addingInfo()// For adding info to the tableView
    {
            Connection connection = null;
            Statement statement = null;
            Connect c = new Connect();
            connection = c.get_Connection();
            ResultSet res = null;
            try {
                String query = String.format("SELECT id_owner,time,operation,note FROM information WHERE id_owner='%d'", userId);
                statement = connection.createStatement();
                res = statement.executeQuery(query);
                while (res.next()) {
                    // System.out.println(res.getString("id_owner"));
                    Information information = new Information(res.getString("time"), res.getString("operation"), res.getString("note"));
                    tableData.add(information);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    default String setUser(){
        Connection connection=null;
        Statement statement=null;
        Connect c=new Connect();
        connection=c.get_Connection();
        ResultSet res =null;
        String login = null;
        try {
            String query = String.format("SELECT balance,username FROM users WHERE username='%s'",login2);
            statement=connection.createStatement();
            res = statement.executeQuery(query);
            while (res.next()){
                    balance= Float.parseFloat((res.getString("balance")));
            }
           // System.out.println(balance);
             login =String.format(login2 + " : %.2f $",balance);


        }catch (Exception e){
            e.printStackTrace();
        }
        return login;
    }

    default void editMessage(String time,char sign, float number,float newBalance){
        Connection connection = null;
        Statement statement = null;
        Connect c = new Connect();
        connection = c.get_Connection();
        ResultSet res = null;

        int operation_id=0;

        try {
            String query = String.format("Select id FROM information WHERE id_owner ='%d' AND time='%s'",userId,time);
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while (res.next()){
                operation_id=res.getInt("id");
            }
            System.out.println(query);


            String query2 = String.format("UPDATE information SET operation='%c%.2f $' WHERE id ='%d'",sign,number,operation_id);
            statement = connection.createStatement();
            statement.executeUpdate(query2);
            System.out.println(query2);

            String query3 = String.format("UPDATE users SET balance ='%.2f' WHERE id ='%d'",newBalance,userId);
            statement = connection.createStatement();
            statement.executeUpdate(query3);
            System.out.println(query3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void deleteMessage(String time,float newBalance){
        Connection connection = null;
        Statement statement = null;
        Connect c = new Connect();
        connection = c.get_Connection();
        ResultSet res = null;

        int operation_id=0;
        try {
            String query = String.format("Select id FROM information WHERE id_owner ='%d' AND time='%s'",userId,time);
            statement = connection.createStatement();
            res = statement.executeQuery(query);
            while (res.next()){
                operation_id=res.getInt("id");
            }
            System.out.println(query);


            String query2 = String.format("DELETE FROM information WHERE id ='%d'",operation_id);
            statement = connection.createStatement();
            statement.executeUpdate(query2);
            System.out.println(query2);

            String query3 = String.format("UPDATE users SET balance ='%.2f' WHERE id ='%d'",newBalance,userId);
            statement = connection.createStatement();
            statement.executeUpdate(query3);
            System.out.println(query3);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
