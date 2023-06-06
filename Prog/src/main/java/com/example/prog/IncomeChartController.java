package com.example.prog;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;


import java.net.URL;
import java.util.ResourceBundle;

import static com.example.prog.StatController.*;

public class IncomeChartController implements Initializable {
    @FXML
    PieChart incomeChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        incomeChart.setData(pieChartData);

        incomeChart.setTitle("Income Chart");
        percentage(incomeChart);


    }
}
