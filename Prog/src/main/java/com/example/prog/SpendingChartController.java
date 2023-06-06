package com.example.prog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;



import java.net.URL;
import java.util.ResourceBundle;

import static com.example.prog.StatController.*;

public class SpendingChartController implements Initializable {
    @FXML
    PieChart SpendingChart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SpendingChart.setData(pieChartData2);

        SpendingChart.setTitle("Spending Chart");
          percentage(SpendingChart);


    }

}

