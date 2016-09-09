package com.jacpy.sb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jacpy.busline.widget.BusLineView;
import com.jacpy.busline.widget.model.BusLineItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BusLineView.OnBusStationClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BusLineView view = (BusLineView) findViewById(R.id.bus_line);
        view.setBusLineData(initData()); // 设置数据
        view.setOnBusStationClickListener(this); // 设置公交站点的点击事件
    }

    @Override
    public void onBusStationClick(View view, BusLineItem item) {
        if (BuildConfig.DEBUG) {
            Log.d("may", "name: " + item.name);
        }
    }

    private List<BusLineItem> initData() {
        ArrayList<BusLineItem> list = new ArrayList<>();
        BusLineItem item = new BusLineItem();
        item.name = "紫薇阁";
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "景蜜村";
        item.busPosition = 0;
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "福田外语学校东";
        item.trafficState = initRoadState();
        item.isRailwayStation = true;
        item.isCurrentPosition = true;
        list.add(item);

        item = new BusLineItem();
        item.name = "景田沃尔玛";
        item.trafficState = initRoadState();
        item.isRailwayStation = true;
        item.busPosition = 30;
        list.add(item);

        item = new BusLineItem();
        item.name = "特发小区";
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "枫丹雅苑";
        item.isRailwayStation = false;
        list.add(item);

        item = new BusLineItem();
        item.name = "市委党校";
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "香蜜红荔立交";
        item.busPosition = 50;
        list.add(item);

        item = new BusLineItem();
        item.name = "景蜜村";
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "天安数码城";
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "上沙村";
        list.add(item);

        item = new BusLineItem();
        item.name = "沙嘴路口";
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "新洲中学";
        item.busPosition = 80;
        list.add(item);

        item = new BusLineItem();
        item.name = "新洲三街";
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "新洲村";
        list.add(item);

        item = new BusLineItem();
        item.name = "众孚小学";
        item.trafficState = initRoadState();
        list.add(item);

        item = new BusLineItem();
        item.name = "石厦学校";
        list.add(item);

        item = new BusLineItem();
        item.name = "福田区委";
        item.trafficState = initRoadState();
        item.isRailwayStation = true;
        list.add(item);

        item = new BusLineItem();
        item.name = "皇岗村";
        item.busPosition = 50;
        item.isRailwayStation = true;
        list.add(item);

        item = new BusLineItem();
        item.name = "福田保健院";
        item.trafficState = initRoadState();
        item.isRailwayStation = true;
        list.add(item);

        item = new BusLineItem();
        item.name = "福民地铁站";
        item.isRailwayStation = true;
        list.add(item);

        item = new BusLineItem();
        item.name = "福强路口";
        item.trafficState = initRoadState();
        item.busPosition = 80;
        list.add(item);

        item = new BusLineItem();
        item.name = "福田口岸总站";
        item.isRailwayStation = true;
        list.add(item);

        return list;
    }

    private BusLineItem.RoadState[] initRoadState() {
        BusLineItem.RoadState[] rss = new BusLineItem.RoadState[5];
        BusLineItem.RoadState rs = new BusLineItem.RoadState();
        rs.idx = 0;
        rs.start = 0;
        rs.ratio = 0.1f;
        rs.state = BusLineItem.RoadState.ROAD_BUSY;
        rss[0] = rs;

        rs = new BusLineItem.RoadState();
        rs.idx = 1;
        rs.start = 0.1f;
        rs.ratio = 0.2f;
        rs.state = BusLineItem.RoadState.ROAD_NORMAL;
        rss[1] = rs;

        rs = new BusLineItem.RoadState();
        rs.idx = 2;
        rs.start = 0.3f;
        rs.ratio = 0.2f;
        rs.state = BusLineItem.RoadState.ROAD_BLOCK;
        rss[2] = rs;

        rs = new BusLineItem.RoadState();
        rs.idx = 3;
        rs.start = 0.5f;
        rs.ratio = 0.3f;
        rs.state = BusLineItem.RoadState.ROAD_BUSY;
        rss[3] = rs;

        rs = new BusLineItem.RoadState();
        rs.idx = 4;
        rs.start = 0.8f;
        rs.ratio = 0.2f;
        rs.state = BusLineItem.RoadState.ROAD_BLOCK;
        rss[4] = rs;
        return rss;
    }
}
