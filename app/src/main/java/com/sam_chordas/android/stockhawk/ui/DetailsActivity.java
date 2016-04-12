package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.service.StockDetailsTaskService;

import java.util.ArrayList;

public class DetailsActivity extends Activity {

    private LineChart lineChartView;
    String[] labels = {"A", "b", "c", "d"};
    float[] d = {2, 3, 4, 5};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);


        float[] floatValues = new float[MyStocksActivity.arrayList.size()];

        for (int i = 0; i < MyStocksActivity.arrayList.size(); i++) {
            floatValues[i] = Float.parseFloat(MyStocksActivity.arrayList.get(i));
        }
        lineChartView = (LineChart) findViewById(R.id.chart);


        XAxis xAxis = lineChartView.getXAxis();
        xAxis.setLabelsToSkip(QuoteCursorAdapter.bidPrice / 20);

        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < floatValues.length; i++) {

            entries.add(new Entry(floatValues[i], i));
        }

        LineDataSet dataset = new LineDataSet(entries, "Last One Year");

        LineData data = new LineData(StockDetailsTaskService.xAxis, dataset);
        lineChartView.setData(data);
    }

}
