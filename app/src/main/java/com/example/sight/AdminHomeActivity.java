package com.example.sight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    String[] update={"NEW","IN-PROGRESS","COMPLETED","ON HOLD"};
    int[] num={7,5,9,4};

    private AnyChartView anyChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

         anyChartView=findViewById(R.id.any_chart_view);
         setupPieChart();
    }
    public void setupPieChart() {
        Pie pie= AnyChart.pie();

        List<DataEntry> dataEntries=new ArrayList<>();

        for(int i=0;i<update.length;i++){
            dataEntries.add(new ValueDataEntry(update[i],num[i]));
        }
        pie.data(dataEntries);
        anyChartView.setChart(pie);

    }


}
