package com.example.prog;


import com.example.prog.Models.Information;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import static com.example.prog.App.stg;
import static com.example.prog.CreateAccount.balance;
import static com.example.prog.LogIn.userId;


public class MainWindow implements Initializable,Desk {

    private volatile boolean stop = false;


    protected static ObservableList<Information> tableData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {





        TimeNow();closingApp();

        User.setText(setUser());



        TimeColumn.setCellValueFactory(new PropertyValueFactory<Information,String>("Time"));
        OperationColumn.setCellValueFactory(new PropertyValueFactory<Information,String>("Operation"));
        NotesColumn.setCellValueFactory(new PropertyValueFactory<Information,String>("Notes"));
        table.setEditable(true);




        NotesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        OperationColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        NotesColumn.setOnEditCommit(event ->{
            String newOperation = event.getNewValue();
            System.out.println(newOperation);

            int rowIndex = event.getTablePosition().getRow();
            Information rowData = table.getItems().get(rowIndex);
            String timeValue = TimeColumn.getCellData(rowData);

            if(newOperation.length()<=60){
                editComment(timeValue,newOperation);
            }else{
                table.refresh();
            }

            table.getItems().clear();
            addingInfo();
        });


        OperationColumn.setOnEditCommit(event -> {
            float num,nbalance;
            char c;

            String value = event.getOldValue();
            value = value.replaceAll("[^0-9.-]", ""); // Удаление всех символов, кроме цифр, десятичной точки и знака минуса

            float previousValue = Float.parseFloat(value); // Предыдущее значение
            System.out.println(previousValue);


            String newOperation = event.getNewValue();
            int rowIndex = event.getTablePosition().getRow();

            Information rowData = table.getItems().get(rowIndex);
            String timeValue = TimeColumn.getCellData(rowData);

            if (newOperation.matches("^[-+]\\d+(\\.\\d{1,2})?$") && newOperation.length() <= 8) {

                System.out.println("edit success");
                c = newOperation.charAt(0);
                num = Float.parseFloat(newOperation);


                float newValue = num; // Новое значение


                nbalance = newValue - previousValue;
                System.out.println(newValue);
                System.out.println(balance+nbalance);

                if (num < 0) {
                    num = Math.abs(num);
                }
                System.out.println(timeValue);
                editMessage(timeValue,c,num,balance+nbalance);
                   table.getItems().clear();
                   addingInfo();

            } else {
                table.refresh();
            }
        });


        addingInfo();

        table.setItems(tableData);

        table.getItems().addListener((ListChangeListener.Change<? extends Information> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
                    refresh();
                }
            }
        });

    }
    private void editComment(String time,String comment){
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


            String query2 = String.format("UPDATE information SET note='%s' WHERE id ='%d'",comment,operation_id);
            statement = connection.createStatement();
            statement.executeUpdate(query2);
            System.out.println(query2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean editMessage = true;
    private void showMessage() {
        if(editMessage) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Сообщение");
            alert.setHeaderText(null);
            alert.setContentText("Чтобы отредактировать данные ,ввидите - или + , после число без знака $ (макс. длина числа - 8 и 2 цифры после запятой. При не правильном вводе ячейка просто обновится)");

            ButtonType showAgainButton = new ButtonType("Больше не показывать");
            ButtonType closeButton = new ButtonType("Закрыть");

            alert.getButtonTypes().setAll(showAgainButton, closeButton);


            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == showAgainButton) {
                    editMessage = false;
                    System.out.println("Сохранено: больше не показывать");
                }
            });
        }
    }


    @FXML
    void deleteData() {
        TableView.TableViewSelectionModel<Information> selectionModel = table.getSelectionModel();
        selectionModel.getSelectedIndex();
        System.out.println(selectionModel.getSelectedIndex());

        float value2;


        if(selectionModel.isEmpty()){
            System.out.println("select first");
        }else{
            Information rowData = table.getItems().get(selectionModel.getSelectedIndex());
            String timeValue = TimeColumn.getCellData(rowData);
            String value = OperationColumn.getCellData(rowData);
            value = value.replaceAll("[^0-9.-]", ""); // Удаление всех символов, кроме цифр, десятичной точки и знака минуса

            value2 =Float.parseFloat(value);

            if (value2 > 0) {
                value2 = -value2;
            }else if(value2 < 0){
                value2=Math.abs(value2);
            }


             deleteMessage(timeValue,balance+value2);
             table.getItems().clear();
             addingInfo();
        }
    }

    @FXML
    void refresh(){
            User.setText(setUser());
        User.requestLayout();
    }

    @FXML
    private Button AddNew;
    @FXML
    private SplitMenuButton ChangerSc;
    @FXML
    private Button MenuButt;
    @FXML
    private TableView<Information> table;
    @FXML
    private TableColumn<Information, String> NotesColumn;
    @FXML
    private TableColumn<Information, String> OperationColumn;
    @FXML
    private TableColumn<Information, String> TimeColumn;
    @FXML
    private Button Stat;
    @FXML
    private Label Time;
    @FXML
    private Label User;

    @FXML
    void EditMsg(){
        showMessage();
    }


    @FXML
    void AddNewNote() throws IOException {
        notesAdder();
    }

    @FXML
    void OpenMenu() throws IOException {
        App a = new App();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setPrefSize(270,1);
        alert.initOwner(stg);
        alert.setTitle("Exit");
        alert.setHeaderText(null);
        alert.setContentText("Выйти из аккаунта?");

        Optional<ButtonType> res = alert.showAndWait();
        if(res.get() == ButtonType.OK){
            stop = true;
            table.getItems().clear();
            a.changeScene("Window.fxml"/*,366,324*/);

        }
        else if(res.get() == ButtonType.CANCEL){
            System.out.println("Cancel");
        }
    }
    public static Stage stage2  = new Stage();


    @FXML
    void moveToGraph() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlanWindow.fxml"));
        Parent p  = fxmlLoader.load();
        stage2.setTitle("Plan");
        stage2.setScene(new Scene(p));
        stage2.setResizable(false);
        stage2.show();
    }

    public static Stage stage1  = new Stage();


    @FXML
    public void OpenStat() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StatisticWindow.fxml"));
        Parent p  = fxmlLoader.load();
        stage1.setTitle("Statistic");
        stage1.setScene(new Scene(p));
        stage1.setResizable(false);
        stage1.show();
    }


    public static Stage stage = new Stage();

    private void notesAdder() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddNote.fxml"));
        Parent root  = fxmlLoader.load();
        stage.setTitle("Adding information");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }




    private void TimeNow(){
        Thread thread = new Thread(() ->{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
            while (!stop){
              try {
                   Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String timeNow = sdf.format(new Date());
                Platform.runLater(() ->{
                    Time.setText(timeNow);
                });
            }
        });
        thread.start();
    }

    private void closingApp(){
        stg.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().setPrefSize(270,1);
            alert.initOwner(stg);
            alert.setTitle("Exit");
            alert.setHeaderText(null);
            alert.setContentText("Выйти из программы?");

            Optional<ButtonType> res = alert.showAndWait();
            if(res.get() == ButtonType.OK){
                stop = true;
                stg.close();
            }
            else if(res.get() == ButtonType.CANCEL){
                System.out.println("Cancel");
            }
            event.consume();
        });
    }
}





