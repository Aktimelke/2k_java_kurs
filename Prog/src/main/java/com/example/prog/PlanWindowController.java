package com.example.prog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import static com.example.prog.MainWindow.stage2;

public class PlanWindowController implements Initializable {
    @FXML
    private GridPane calendarGridPane;
    @FXML
    private ComboBox<String> monthComboBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        monthComboBox.getItems().addAll(
                "Январь", "Февраль", "Март", "Апрель",
                "Май", "Июнь", "Июль", "Август",
                "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
        );


        Calendar calendar = new GregorianCalendar();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        monthComboBox.getSelectionModel().select(month);


        showCalendar(month, year);


        monthComboBox.setOnAction(event -> {
            int selectedMonth = monthComboBox.getSelectionModel().getSelectedIndex();
            showCalendar(selectedMonth, year);
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
    }

    private void showCalendar(int month, int year) {

        calendarGridPane.getChildren().clear();


        Calendar calendar = new GregorianCalendar(year, month, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int row = 0;
        int col = startDayOfWeek - 1;
        for (int day = 1; day <= daysInMonth; day++) {
            Button dayButton = new Button(String.valueOf(day));
            dayButton.getStyleClass().add("calendar-day-button");
            int finalDay = day;
            dayButton.setOnAction(event -> {
                try {
                    openNewWindow(finalDay, month, year);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            calendarGridPane.add(dayButton, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }
    public static Stage dayStage  = new Stage();
    public static LocalDate date;

    @FXML
    void close() {
        stage2.close();
    }

    private void openNewWindow(int day, int month, int year) throws IOException {
        String dateStr = String.format("%02d-%02d-%04d", day, month+1, year);
         date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DayPlan.fxml"));
        Parent p  = fxmlLoader.load();
        dayStage.setTitle("Plan");
        dayStage.setScene(new Scene(p));
        dayStage.setResizable(false);
        dayStage.show();
    }

}
