package com.example.teacherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphGenerator extends Fragment {

    GraphView graphView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_graph_generator, container, false);

        graphView = root.findViewById(R.id.idGraphView);

        int [] plots = passModel.plots;



        // on below line we are adding data to our graph view.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                // on below line we are adding
                // each point on our x and y axis.
                new DataPoint(0, 0),
                new DataPoint(0.5, plots[0]),
                new DataPoint(1, plots[1]),
                new DataPoint(1.5, plots[2]),
                new DataPoint(2, plots[3]),
                new DataPoint(2.5, plots[4]),
                new DataPoint(3, plots[5]),
                new DataPoint(3.5, plots[6]),
                new DataPoint(4, plots[7]),
                new DataPoint(4.5, plots[8]),
                new DataPoint(5, plots[9]),
                new DataPoint(5.5, plots[10]),
                new DataPoint(6, plots[11]),
                new DataPoint(6.5, plots[12]),
                new DataPoint(7, 0),
        });

        // title
        //graphView.setTitleTextSize(30f);
        //graphView.setTitle("My Graph View");
        // text color
        //graphView.setTitleColor(R.color.purple_200);

        //title text size.
        graphView.setTitleTextSize(18);

        // data series
        graphView.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        //staticLabelsFormatter.setVerticalLabels(new String[] {"0","2","4","6","8","10"});
        //graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        staticLabelsFormatter.setHorizontalLabels(new String[] {" ","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"," "});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.getGridLabelRenderer().setTextSize(32f);

        graphView.getViewport().setScrollable(true);


        // Inflate the layout for this fragment
        return root;

    }
}