package com.jacpy.busline.widget.model;

import android.graphics.RectF;

/**
 * Created by jacpy.may@gmail.com on 2016/8/24 10:56.
 */
public class BusLineItem {

    public static final int BUS_STATION_LENGTH = 100;

    public static final int BUS_NONE = -1;

    /** 公交站名称 */
    public String name;

    /** 用户是否是在当前站的位置 */
    public boolean isCurrentPosition;

    /** 相对于当前让的位置，从-1 ~ 100（不包含100），-1表示当前站点之内的位置没有公交车，0表示公交车正在当前站点位置 */
    public int busPosition = BusLineItem.BUS_NONE;

    /** 是否是地铁站附近 */
    public boolean isRailwayStation;

    /** 在View中的位置 */
    public RectF location;

    /** 交通路况 */
    public RoadState[] trafficState;

    /**
     * 路况
     */
    public static class RoadState {

        /** 畅通 */
        public static final int ROAD_NORMAL = 0;

        /** 忙路 */
        public static final int ROAD_BUSY = 1;

        /** 拥堵 */
        public static final int ROAD_BLOCK = 2;

        /** 位置下标 */
        public int idx;

        /** 起始位置占比 */
        public float start;

        /** 占比 */
        public float ratio;

        /** 状态 */
        public int state;
    }

}
