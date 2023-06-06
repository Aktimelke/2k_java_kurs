package com.example.prog;


import com.example.prog.Models.Information;
import com.example.prog.Models.Plan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


import static com.example.prog.LogIn.userId;
import static com.example.prog.MainWindow.stage1;
import static com.example.prog.MainWindow.tableData;

public class StatController implements Initializable {
    private double incomeValue = 0;
    private double spendingValue = 0;
    private double profitValue = 0;
    protected static ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    protected static ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList();

    @FXML
    private Label income;

    @FXML
    private Label profit;
    @FXML
    private Label StatisticLabel;

    @FXML
    private Label spending;
    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private SplitMenuButton Split;




    private static String formattedDate;

    @FXML
    void SwitchToDay() {
        LocalDate selectedDate = DayPicker.getValue();
        if (selectedDate != null) {
            formattedDate = selectedDate.format(formatter);
            LocalDate chosenDate = DayPicker.getValue();
            String cChosenDate = chosenDate.format(formatter2);
            PlanData.clear();
            Info(cChosenDate);
        //    System.out.println("Выбранная дата: " + formattedDate);
            dayInfo();
        } else {
           System.out.println("Дата не выбрана.");
        }

    }

    @FXML
    private Button ChooseDay;

    @FXML
    private DatePicker DayPicker;


    @FXML
    private TableView<Plan> table;

    @FXML
    private TableColumn<Plan, String> timeColumn;
    @FXML
    private TableColumn<Plan, String> PlanColumn;


    private String getIncome(double value){
        return String.format("%.2f $",value);
    }
    private String getSpending(double value){
        return String.format("%.2f $",value);
    }
    private String getProfit(double value){
        return String.format("%.2f $",value);
    }
    private boolean checkDate(String unChangedDate){

        int splitIndex = 12;
        String secondPart = unChangedDate.substring(splitIndex);


        LocalDate date = LocalDate.parse(secondPart, formatter);
        LocalDate today = LocalDate.now();
        boolean belongsToToday = date.isEqual(today);


        return belongsToToday;
    }
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean checkDate(String date,String date3){


        int splitIndex = 12;
        String secondPart = date3.substring(splitIndex);
      //  System.out.println(secondPart);

        LocalDate date1 = LocalDate.parse(date,formatter);
       // System.out.println(date1);
        LocalDate date2 = LocalDate.parse(secondPart,formatter);
       // System.out.println(date2);

        boolean belongsToDate = date1.isEqual(date2);

       // System.out.println("Date belongs to entered Date: " + belongsToDate);

        return belongsToDate;
    }

    private String removeCharFromString(String input) {
        char charToRemove = '$';
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != charToRemove) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void closingApp() {
        stage1.setOnCloseRequest(event -> {
            pieChartData.clear();
            pieChartData2.clear();
            PlanData.clear();

            stage1.close();
            event.consume();

        });
    }

    @FXML
    void BackToMainWindow() {
        pieChartData.clear();
        pieChartData2.clear();
        PlanData.clear();


        stage1.close();


    }

    @FXML
    void OpenIncomeChart() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SpendingChart.fxml"));
        Parent root  = fxmlLoader.load();
        spend.setTitle("Spend Chart");
        spend.setScene(new Scene(root));
        spend.setResizable(false);
        spend.show();

    }


    public static Stage incom = new Stage();

    public static Stage spend = new Stage();

    @FXML
    void OpenSpendingChart() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("IncomeChart.fxml"));
        Parent root  = fxmlLoader.load();
        incom.setTitle("Income Chart");
        incom.setScene(new Scene(root));
        incom.setResizable(false);
        incom.show();
    }

    private void dayInfo(){

        LocalDate today = LocalDate.now();
        LocalDate date = LocalDate.parse(formattedDate,formatter);
        if(today.isEqual(date)){
            StatisticLabel.setText("Todays Statistic");
            pieChartData.clear();
            pieChartData2.clear();
            incomeValue=spendingValue=profitValue=0;
            todayInfo();
        }else{
            pieChartData.clear();
            pieChartData2.clear();
            incomeValue=spendingValue=profitValue=0;
            income.setText(getIncome(incomeValue));
            spending.setText(getSpending(spendingValue));
            profit.setText(getProfit(profitValue));

            StatisticLabel.setText(formattedDate+" Statistic");
            info();

        }

    }
    private void info(){

        Map<String, Double> notesMap = new HashMap<>();

        for (Information i : tableData) {
            if (checkDate(formattedDate,i.getTime())) {
                values(notesMap, i);
            }
        }

        Map(notesMap);
    }

    private void values(Map<String, Double> notesMap, Information i) {
        if (i.getNotes().isEmpty()) {
            String noData = "No data";
            i.setNotes(noData);
        }
        String op = removeCharFromString(i.getOperation());
        double number = Double.parseDouble(op);

        String notes = i.getNotes();
        double currentValue = notesMap.getOrDefault(notes, 0.0);
        double newValue = currentValue + number;
        notesMap.put(notes, newValue);

        if (number > 0) {
            incomeValue += number;
        } else if (number < 0) {
            spendingValue += number;
        }
        profitValue += number;
    }

    private void Map(Map<String, Double> notesMap) {
        for (Map.Entry<String, Double> entry : notesMap.entrySet()) {
            String notes = entry.getKey();
            double value = entry.getValue();

            if (value > 0) {
                pieChartData.add(new PieChart.Data(notes, value));
            } else if (value < 0) {
                pieChartData2.add(new PieChart.Data(notes, Math.abs(value)));
            }
        }


        income.setText(getIncome(incomeValue));
        spending.setText(getSpending(spendingValue));
        profit.setText(getProfit(profitValue));

    }

    private void todayInfo(){

        Map<String, Double> notesMap = new HashMap<>();

        for (Information i : tableData) {
            if (checkDate(i.getTime())) {
                values(notesMap, i);
            }
        }

        Map(notesMap);
    }



    protected static void percentage(PieChart chart){
        double total = 0;
        for (PieChart.Data data : chart.getData()) {
            total += data.getPieValue();
        }
        for (PieChart.Data data : chart.getData()) {
            double percentage = (data.getPieValue() / total) * 100;
            String perc = String.format("%.2f%%", percentage);
            Tooltip tooltip = new Tooltip(perc);
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void Info(String today){
        Connection connection=null;
        Statement statement= null;
        Connect c=new Connect();
        connection=c.get_Connection();
        ResultSet res =null;

        System.out.println(today);

            try{
                String query = String.format("SELECT time,note FROM day_plan WHERE id_owner='%d' AND day='%s'",userId,today);
                statement=connection.createStatement();
                res = statement.executeQuery(query);
                while (res.next()){

                    Plan plan = new Plan(res.getString("time"),res.getString("note"));
                    // System.out.println(index(res.getString("time")));
                    PlanData.add(plan);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            table.refresh();

    }




    private final ObservableList<Plan> PlanData = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChooseDay.setTooltip(new Tooltip("Выберите дату и нажмите на кнопку чтобы посмотреть статистику за эту дату"));
        closingApp();
        todayInfo();

        timeColumn.setCellValueFactory(new PropertyValueFactory<Plan,String>("Hour"));
        PlanColumn.setCellValueFactory(new PropertyValueFactory<Plan,String>("Note"));

        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter2);
        Info(date);
        table.setItems(PlanData);


    }


}
