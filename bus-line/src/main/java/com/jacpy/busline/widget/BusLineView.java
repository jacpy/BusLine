package com.jacpy.busline.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.jacpy.busline.BuildConfig;
import com.jacpy.busline.R;
import com.jacpy.busline.widget.model.BusLineItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacpy.may@gmail.com on 2016/8/24 10:52.
 */
public class BusLineView extends View {

    private List<BusLineItem> mBusLines = new ArrayList<>();
    private int mBusStationWidth;
    private Drawable mBus, mBusSmall, mCurrentPosition, mCurrentPositionLocation, mRailway;
    private int mMargin1X, mMargin2P;
    private float mStrokeWidth;
    private int mColorPass, mColorUnreach, mSelectedColor, mColorBlock, mColorBusy, mColorNormal;

    private Rect mTxtRect;
    private RectF mBusRect, mLocationRectF;

    private Paint mStationCirclePaint, mTxtPaint;

    private float mNumTxtSize, mStationNameTxtSize, mSelectedTxtSize;
    private int mHeight;

    private OnBusStationClickListener mOnBusStationClickListener;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private boolean mIsScroll;
    private int mTouchSlop, mMinimumFlingVelocity;

    public BusLineView(Context context) {
        super(context);
        init(context);
    }

    public BusLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BusLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new OverScroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity() * 5;

        mBus = getResources().getDrawable(R.drawable.ic_bus);
        mBusSmall = getResources().getDrawable(R.drawable.ic_bus_small);
        mCurrentPosition = getResources().getDrawable(R.drawable.ic_current_bus_station);
        mCurrentPositionLocation = getResources().getDrawable(R.drawable.ic_current_bus_station_location);
        mRailway = getResources().getDrawable(R.drawable.ic_railway);

        mBusStationWidth = mBus.getIntrinsicWidth() << 1;
        float density = getResources().getDisplayMetrics().density;
        mMargin1X = (int) (10 * density);
        mMargin2P = (int) (2 * density);

        mStationCirclePaint = new Paint();
        mStrokeWidth = 3 * density;
        mStationCirclePaint.setStrokeWidth(mStrokeWidth);
        mStationCirclePaint.setStyle(Paint.Style.STROKE);
        mStationCirclePaint.setAntiAlias(true);
        mTxtPaint = new Paint();
        mTxtPaint.setAntiAlias(true);

        mColorPass = Color.parseColor("#1FB562");
        mColorUnreach = Color.parseColor("#C0C0C0");
        mSelectedColor = Color.parseColor("#FE771D");

        mColorBlock = Color.parseColor("#F41C0D");
        mColorBusy = Color.parseColor("#FFAC00");
        mColorNormal = Color.parseColor("#00BD00");

        mTxtRect = new Rect();
        mBusRect = new RectF();
        mLocationRectF = new RectF();
        float scaleDensity = getResources().getDisplayMetrics().scaledDensity;
        mNumTxtSize = 14 * scaleDensity;
        mStationNameTxtSize = 18 * scaleDensity;
        mSelectedTxtSize = 20 * scaleDensity;
    }

    /**
     * 设置公交车数据
     * @param list 公交车列表数据
     */
    public void setBusLineData(List<BusLineItem> list) {
        mBusLines.clear();
        if (list == null) {
            return;
        }

        mBusLines.addAll(list);
        mHeight = computeHeight();
    }

    private int computeHeight() {
        int num = 0;
        BusLineItem bli = null;
        for (BusLineItem item : mBusLines) {
            int length = item.name.length();
            if (length > num) {
                num = length;
                bli = item;
            }
        }

        int height = (int) (250 * getResources().getDisplayMetrics().density);
        if (bli != null) {
            Rect r = new Rect();
            int y = 0;
            Paint p = new Paint();
            p.setTextSize(mSelectedTxtSize);
            for (int k = 0, length = bli.name.length(); k < length; ++k) {
                String s = String.valueOf(bli.name.charAt(k));
                measureText(s, r, p);
                y += r.height() + mMargin2P;
            }

            p.setTextSize(mNumTxtSize);
            measureText("1", r, p);
            y += mMargin1X + mBus.getIntrinsicHeight() + mMargin1X + mCurrentPosition.getIntrinsicHeight() + mMargin1X + r.height() + mMargin2P + mRailway.getIntrinsicHeight() + mMargin1X + mMargin1X;
            height = y;
        }

        return height;
    }

    /**
     * 设置公交站显示的宽度，至少是公交车图标的两倍宽度。
     * @param width 宽度值
     */
    public void setBusStationWidth(int width) {
        if (width > mBusStationWidth) {
            mBusStationWidth = width;
        }
    }

    /**
     * 设置公交图标
     * @param d Drawable
     */
    public void setBusDrawable(Drawable d) {
        mBus = d;
        mBusStationWidth = d.getIntrinsicWidth() << 1;
    }

    /**
     * 设置小公交图标
     * @param d Drawable
     */
    public void setBusSmallDrawable(Drawable d) {
        mBusSmall = d;
    }

    /**
     * 设置当前位置显示的图标
     * @param d Drawable
     */
    public void setCurrentPositionDrawable(Drawable d) {
        mCurrentPosition = d;
    }

    /**
     * 设置定位的图标
     * @param d Drawable
     */
    public void setCurrentLocationDrawable(Drawable d) {
        mCurrentPositionLocation = d;
    }

    /**
     * 设置有地铁标识的公交站图标
     * @param d Drawable
     */
    public void setMetroStationDrawable(Drawable d) {
        mRailway = d;
    }

    /**
     * 设置不交通状况的颜色
     * @param normal 正常畅通的颜色
     * @param busy 交通繁忙的颜色
     * @param block 交通拥堵的颜色
     */
    public void setTrafficColor(int normal, int busy, int block) {
        mColorNormal = normal;
        mColorBusy = busy;
        mColorBlock = block;
    }

    /**
     * 设置路线的颜色
     * @param pass 到当前站点的颜色
     * @param unreach 当前站点以后和颜色
     * @param selected 选中公交站时的颜色
     */
    public void setBusLineColor(int pass, int unreach, int selected) {
        mColorPass = pass;
        mColorUnreach = unreach;
        mSelectedColor = selected;
    }

    /**
     * 设置公交站被点击的监听器
     * @param l listener
     */
    public void setOnBusStationClickListener(OnBusStationClickListener l) {
        mOnBusStationClickListener = l;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private float mDownX, mDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    if (BuildConfig.DEBUG) {
                        Log.d("may", "is not finished.");
                    }
                    return super.dispatchTouchEvent(event);
                }

                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float dx = x - mDownX;
                float dy = y - mDownY;
                if (BuildConfig.DEBUG) {
                    Log.d("may", "dx: " + dx + ", dy: " + dy + ", slop: " + mTouchSlop);
                }

                if (!mIsScroll && Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > mTouchSlop) {
                    mIsScroll = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (BuildConfig.DEBUG) {
            Log.d("may", "onTouchEvent, action: " + event.getAction() + ", scroll: " + mIsScroll);
        }

        addVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mIsScroll && !mScroller.isFinished()) {
                    // fling后点击屏幕停止
                    mScroller.abortAnimation();
                }

                mDownX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!mIsScroll) {
                    return super.onTouchEvent(event);
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                float x = event.getX();
                scrollBy((int) (mDownX - x), 0);
                mDownX = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float velocityX = getScrollVelocity();
                if (BuildConfig.DEBUG) {
                    Log.d("may", "vx: " + velocityX + ", minVx: " + mMinimumFlingVelocity);
                }

                Rect r = new Rect();
                getLocalVisibleRect(r);
                if (Math.abs(velocityX) <= mMinimumFlingVelocity) {
                    int sx = getScrollX();
                    int dx = sx + r.width() - getWidth();
                    if (sx < 0) {
                        mScroller.startScroll(sx, 0, -sx, 0, -sx * 2);
                        invalidate();
                    } else if (dx > 0) {
                        mScroller.startScroll(sx, 0, -dx, 0, dx * 2);
                        invalidate();
                    } else if (!mIsScroll) {
                        onTapUp(event, r.width());
                    }

                    mIsScroll = false;
                    break;
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mScroller.fling(getScrollX(), 0, (int) -velocityX, 0,
                        0, getWidth() - r.width(), 0, 0, r.width() / 2, 0);
                invalidate();

                mIsScroll = false;
                recycleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void onTapUp(MotionEvent event, int visibleWidth) {
        float x = event.getX();
        float y = event.getY();
        BusLineItem bli = null;
        if (BuildConfig.DEBUG) {
            Log.d("may", "x: " + x + ", y: " + y + ", sx: " + getScrollX() + ", cx: " + mScroller.getCurrX() + ", fx: " + mScroller.getFinalX() + ", startx: " + mScroller.getStartX());
        }

        for (BusLineItem item : mBusLines) {
            if (item.location != null && item.location.contains(x + getScrollX(), y + getScrollY()) && !item.isCurrentPosition) {
                bli = item;
                break;
            }
        }

        if (bli != null) {
            for (BusLineItem item : mBusLines) {
                item.isCurrentPosition = false;
            }

            bli.isCurrentPosition = true;
            BusLineItem firstItem = mBusLines.get(0);
            int pos = visibleWidth - mBusStationWidth;
            int dx = (int) (pos - bli.location.centerX() + getScrollX());
            if (BuildConfig.DEBUG) {
                Log.d("may", "dx: " + dx + ", compare: " + (getScrollX() - firstItem.location.left > 0));
            }

            if (getScrollX() - dx < 0) {
                // 第一个边界处理
                dx = getScrollX();
                if (BuildConfig.DEBUG) {
                    Log.d("may", "intercept first");
                }
            } else if (getScrollX() + visibleWidth - dx > getWidth()) {
                // 最后一个边界处理
                dx = getScrollX() + visibleWidth - getWidth();
                if (BuildConfig.DEBUG) {
                    Log.d("may", "intercept last");
                }
            }

            if (BuildConfig.DEBUG) {
                Log.d("may", "scroll dx: " + dx + ", cx: " + bli.location.centerX() + ", sx: " + mScroller.getCurrX() + ", sx2: " + getScrollX());
            }

            mScroller.startScroll(getScrollX(), 0, -dx, 0, Math.abs(dx) * 2);
            invalidate();

            if (mOnBusStationClickListener != null) {
                mOnBusStationClickListener.onBusStationClick(BusLineView.this, bli);
            }
        }
    }

    private void addVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private float getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return mVelocityTracker.getXVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mBusStationWidth * (mBusLines.size()) + getPaddingLeft() + getPaddingRight(),
                mHeight + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBusLines.size() == 0) {
            return;
        }

        long start = 0;
        if (BuildConfig.DEBUG) {
            start = System.currentTimeMillis();
        }

        int margin1X = mMargin1X;
        float startX = getPaddingLeft();
        float startY = getPaddingTop();

        int busStationWidth = mBusStationWidth;

        int margin2X = margin1X << 1;
        int busHeight = mBus.getIntrinsicHeight(); // 公交图片的高度

        int currentPosWidth = mCurrentPosition.getIntrinsicWidth();
        int halfCurrentPosWidth = currentPosWidth >> 1;
        int locationHeight = mCurrentPosition.getIntrinsicHeight();
        int halfLocationHeight = locationHeight >> 1;

        float lineStartY = startY + busHeight + margin2X + (locationHeight >> 1);

        int halfBusStationWidth = busStationWidth >> 1;
        startX += halfBusStationWidth;

        int currentPositionIdx = 0;
        for (int i = 0, size = mBusLines.size(); i < size; ++i) {
            if (mBusLines.get(i).isCurrentPosition) {
                currentPositionIdx = i;
                break;
            }
        }

        // 整个线路
        mStationCirclePaint.setColor(mColorPass);
        canvas.drawLine(startX, lineStartY, busStationWidth * mBusLines.size() - halfBusStationWidth, lineStartY, mStationCirclePaint);

        // 未到达的线路
        mStationCirclePaint.setColor(mColorUnreach);
        canvas.drawLine(startX + busStationWidth * currentPositionIdx, lineStartY, busStationWidth * mBusLines.size() - halfBusStationWidth, lineStartY, mStationCirclePaint);

        int halfBusWidth = mBus.getIntrinsicWidth() >> 1;

        BusLineItem hasBusNearCurrentPos = null;
        // 找最近的一个公交车
        for (int i = 0, size = mBusLines.size(); i < size; ++i) {
            BusLineItem item = mBusLines.get(i);
            if (item.isCurrentPosition) {
                if (item.busPosition == 0) {
                    hasBusNearCurrentPos = item;
                }

                break;
            }

            if (item.busPosition >= 0) {
                hasBusNearCurrentPos = item;
            }
        }

        for (int i = 0, size = mBusLines.size(); i < size; ++i) {
            BusLineItem item = mBusLines.get(i);
            if (item.location == null) {
                item.location = new RectF();
            }

            float pos = startX + i * busStationWidth; // 公交站点的起始位置
            // 路况
            if (item.trafficState != null) {
                for (int k = 0, length = item.trafficState.length; k < length; ++k) {
                    BusLineItem.RoadState rs = item.trafficState[k];
                    float sx = busStationWidth * rs.start;
                    switch (rs.state) {
                        case BusLineItem.RoadState.ROAD_BLOCK:
                            mStationCirclePaint.setColor(mColorBlock);
                            break;
                        case BusLineItem.RoadState.ROAD_BUSY:
                            mStationCirclePaint.setColor(mColorBusy);
                            break;
                        case BusLineItem.RoadState.ROAD_NORMAL:
                            mStationCirclePaint.setColor(mColorNormal);
                            break;
                        default:
                    }

                    canvas.drawLine(pos + sx, lineStartY, pos + sx + busStationWidth * rs.ratio, lineStartY, mStationCirclePaint);
                }
            }

            int offsetY = margin1X;
            if (item.busPosition >= 0) {
                // 公交车位置
                float busPos = (float) item.busPosition / BusLineItem.BUS_STATION_LENGTH * busStationWidth;
                mBusRect.top = offsetY;
                mBusRect.left = pos + busPos;
                mBusRect.bottom = offsetY + busHeight;
                mBusRect.right = mBusRect.left + mBus.getIntrinsicWidth();
                if (hasBusNearCurrentPos == item) {
                    // 当前最近的一个公交车
                    canvas.drawBitmap(((BitmapDrawable) mBus).getBitmap(), pos - halfBusWidth + busPos, offsetY, null);
                } else {
                    canvas.drawBitmap(((BitmapDrawable) mBusSmall).getBitmap(), pos - (mBusSmall.getIntrinsicWidth() >> 1) + busPos, offsetY + mBus.getIntrinsicHeight() - mBusSmall.getIntrinsicHeight(), null);
                }
            }

            offsetY += margin1X + busHeight;
            item.location.top = offsetY;
            if (item.isCurrentPosition) {
                // 当前位置
                mLocationRectF.top = offsetY - mCurrentPositionLocation.getIntrinsicHeight() - mMargin2P;
                mLocationRectF.left = pos - (mCurrentPositionLocation.getIntrinsicWidth() >> 1);
                mLocationRectF.bottom = offsetY - mMargin2P;
                mLocationRectF.right = pos + (mCurrentPositionLocation.getIntrinsicWidth() >> 1);
                if (item.busPosition > 0 && !hit(mBusRect, mLocationRectF)) {
                    // 公交车和显示当前位置的图标相冲突
                    canvas.drawBitmap(((BitmapDrawable) mCurrentPositionLocation).getBitmap(), mLocationRectF.left, mLocationRectF.top, null);
                }

                canvas.drawBitmap(((BitmapDrawable) mCurrentPosition).getBitmap(), pos - halfCurrentPosWidth, offsetY, null);
            } else {
                mStationCirclePaint.setColor(mColorPass);
                canvas.drawCircle(pos, offsetY + halfLocationHeight, halfCurrentPosWidth - mStrokeWidth, mStationCirclePaint);
                mStationCirclePaint.setStyle(Paint.Style.FILL);
                if (getBackground() instanceof ColorDrawable) {
                    mStationCirclePaint.setColor(((ColorDrawable) getBackground()).getColor());
                } else {
                    mStationCirclePaint.setColor(Color.WHITE);
                }

                canvas.drawCircle(pos, offsetY + halfLocationHeight, halfCurrentPosWidth - mStrokeWidth - (mMargin2P >> 1), mStationCirclePaint);
                mStationCirclePaint.setStyle(Paint.Style.STROKE);
            }

            offsetY += mCurrentPosition.getIntrinsicHeight() + margin2X;

            // 文字
            String s = String.valueOf(i + 1);
            mTxtPaint.setColor(Color.GRAY);
            mTxtPaint.setTextSize(mNumTxtSize);
            measureText(s, mTxtRect, mTxtPaint);
            canvas.drawText(s, pos - (mTxtRect.width() >> 1), offsetY, mTxtPaint);

            // 名称
            mTxtPaint.setColor(item.isCurrentPosition ? mSelectedColor : Color.BLACK);
            mTxtPaint.setTextSize(item.isCurrentPosition ? mSelectedTxtSize : mStationNameTxtSize);
            offsetY += mTxtRect.height() + margin1X;
            int oy = offsetY;
            for (int k = 0, length = item.name.length(); k < length; ++k) {
                s = String.valueOf(item.name.charAt(k));
                measureText(s, mTxtRect, mTxtPaint);
                canvas.drawText(s, pos - (mTxtRect.width() >> 1), oy, mTxtPaint);
                oy += mTxtRect.height() + mMargin2P;
            }

//            int max = Math.max(mTxtRect.width(), currentPosWidth);
            item.location.left = pos + halfBusStationWidth - busStationWidth;
            item.location.right = item.location.left + busStationWidth;

            if (item.isRailwayStation) {
                // 地铁站
                canvas.drawBitmap(((BitmapDrawable) mRailway).getBitmap(), pos - (mRailway.getIntrinsicWidth() >> 1), oy - mTxtRect.height() + mMargin2P, null);
            }

            item.location.bottom = oy + mMargin2P;
//            mTxtPaint.setStyle(Paint.Style.STROKE);
//            mTxtPaint.setStrokeWidth(1);
//            canvas.drawRect(item.location, mTxtPaint);
        }

        if (BuildConfig.DEBUG) {
            Log.d("may", ("draw used time: " + (System.currentTimeMillis() - start)) + "ms");
        }
    }

    private void measureText(String s, Rect r, Paint p) {
        p.getTextBounds(s, 0, s.length(), r);
    }

    private boolean hit(RectF src, RectF dst) {
        return src.contains(dst)
                || src.contains(dst.left, dst.top)
                || src.contains(dst.left, dst.bottom)
                || src.contains(dst.right, dst.bottom)
                || src.contains(dst.right, dst.top)
                || dst.contains(src);
    }

    public interface OnBusStationClickListener {
        void onBusStationClick(View view, BusLineItem item);
    }
}
