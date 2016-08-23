package zz.zhanghao.newsclient.myview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import zz.zhanghao.newsclient.R;

/**
 * Created by ${张昊} on 2016/8/19 0019.
 */
public class MyRefreshListView extends ListView
{
    private Context context;
    @ViewInject(R.id.ll_refresh_header_root)
    private LinearLayout ll_refresh_header_root;
    @ViewInject(R.id.ll_refresh_header_view)
    private LinearLayout ll_refresh_header_view;
    @ViewInject(R.id.pb_refresh_header)
    private ProgressBar pb_refresh_header;
    @ViewInject(R.id.iv_refresh_header_arrow)
    private ImageView iv_refresh_header_arrow;
    @ViewInject(R.id.tv_refresh_header_text)
    private TextView tv_refresh_header_text;
    @ViewInject(R.id.tv_refresh_header_time)
    private TextView tv_refresh_header_time;
    //脚
    @ViewInject(R.id.ll_refresh_footer_root)
    private LinearLayout ll_refresh_footer_root;
    private RotateAnimation rDownToUp;
    private RotateAnimation rUpToDown;
    /**
     * 下拉刷新
     */
    private final int refreshOfDown = 0;
    /**
     * 释放刷新
     */
    private final int refreshOfUp = 1;
    /**
     * 正在刷新
     */
    private final int refreshOfIng = 1;
    /**
     * 当前的刷新状态
     */
    private int refreshOfCurrent = refreshOfDown;
    /**
     * 是否是下拉刷新
     */
    public boolean isDownRefresh = false;

    private int downY;
    private int moveY;
    private int upY;
    private int refreshHeader;
    private OnRefreshStateListener refreshListener;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (refreshListener != null)
            {
                switch (msg.what)
                {
                    case 88:
                        refreshListener.onDownRefresh();
                        break;
                    case 99:
                        refreshListener.onUpRefresh();
                        handler.sendEmptyMessageDelayed(100,1000);
                        break;
                    case 100:
                        ll_refresh_footer_root.setPadding(0,0,0,0);
                        break;
                }
            }

        }
    };

    private View rollView;
    private int listY;
    private int roolY;
    private int refreshFoot;

    public MyRefreshListView(Context context)
    {
        super(context);
        init(context);
    }

    public MyRefreshListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public MyRefreshListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        this.context = context;
        //添加头
        View view = View.inflate(context, R.layout.refresh_header, null);
        addHeaderView(view);
        ViewUtils.inject(this, view);
        //添加脚
        View footView = View.inflate(context, R.layout.refresh_footer, null);
        addFooterView(footView);
        ViewUtils.inject(this, footView);
        //初始化下拉刷新的动画
        icoRotateAnimation();
        //得到下拉刷新控件的高度,0,0是随便测量
        ll_refresh_header_view.measure(0, 0);
        refreshHeader = ll_refresh_header_view.getMeasuredHeight();
        //得到上拉刷新控件的高度,0,0是随便测量
        ll_refresh_footer_root.measure(0, 0);
        refreshFoot = ll_refresh_footer_root.getMeasuredHeight();
        //隐藏下拉刷新控件
        ll_refresh_header_view.setPadding(0, -refreshHeader, 0, 0);
        //设置一个ListView滑动监听
        setOnScrollListener(new MyScroll());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (roolY >= listY)
                {
                    if (downY == 0)
                    {
                        //防止down事件没有触发
                        downY = (int) ev.getY();
                        break;
                    }
                    moveY = (int) ev.getY();
                    int distanceY = moveY - downY;
                    if (distanceY > 0)
                    {
                        int toTop = -refreshHeader + distanceY;
                        //移动下拉刷新控件
                        ll_refresh_header_view.setPadding(0, toTop, 0, 0);
                        if (refreshOfCurrent == refreshOfDown && toTop >= 0)
                        {
                            refreshOfCurrent = refreshOfUp;
                            //开始动画
                            iv_refresh_header_arrow.startAnimation(rDownToUp);
                            //设置文本
                            tv_refresh_header_text.setText("释放刷新");
                        } else if (refreshOfCurrent == refreshOfUp && toTop < 0)
                        {
                            refreshOfCurrent = refreshOfDown;
                            //开始动画
                            iv_refresh_header_arrow.startAnimation(rUpToDown);
                            //设置文本
                            tv_refresh_header_text.setText("下拉刷新");
                        }
                    }

                    return true;

                }

                break;
            case MotionEvent.ACTION_UP:
                downY = 0;
                if (refreshOfCurrent == refreshOfUp)
                {
                    //清空动画
                    iv_refresh_header_arrow.setVisibility(View.GONE);
                    iv_refresh_header_arrow.clearAnimation();
                    //加载控件显示
                    pb_refresh_header.setVisibility(View.VISIBLE);
                    //设置文本
                    tv_refresh_header_text.setText("正在刷新");
                    ll_refresh_header_view.setPadding(0, 0, 0, 0);
                    isDownRefresh = true;
                    handler.sendEmptyMessageDelayed(88, 2000);

                }
                if (refreshOfCurrent == refreshOfDown)
                {
                    ll_refresh_header_view.setPadding(0, -refreshHeader, 0, 0);
                }
                break;

        }

        return super.onTouchEvent(ev);
    }

    /**
     * 向自定义的控件里添加控件
     *
     * @param view
     */
    public void addViewContent(View view)
    {
        rollView = view;
        ll_refresh_header_root.addView(view);
    }

    //刷新完成
    public void RefreshFinish()
    {
        if (isDownRefresh)
        {
            refreshOfCurrent = refreshOfDown;
            ll_refresh_header_view.setPadding(0, -refreshHeader, 0, 0);
            iv_refresh_header_arrow.setVisibility(View.VISIBLE);
            //加载控件显示
            pb_refresh_header.setVisibility(View.INVISIBLE);
            //设置文本
            tv_refresh_header_text.setText("下拉刷新");
        } else
        {
            ll_refresh_footer_root.setPadding(0, 0, 0, -refreshFoot);
        }

    }

    /**
     * 下拉刷新的动画
     */
    public void icoRotateAnimation()
    {
        //下拉动画刷新，上拉动画刷新
        rDownToUp = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation
                .RELATIVE_TO_SELF, 0.5f);
        rDownToUp.setDuration(500);
        rDownToUp.setFillAfter(true);

        // 上拉动画刷新
        rUpToDown = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation
                .RELATIVE_TO_SELF, 0.5f);
        rUpToDown.setDuration(500);
        rUpToDown.setFillAfter(true);
    }

    //先执行这个事件方法，用来得到控件当前在屏幕上的位置
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        //设置查询的个数，宽，高
        int[] location = new int[2];
        getLocationInWindow(location);
        listY = location[1];

        //轮播图当前在屏幕上的位置
        rollView.getLocationInWindow(location);
        roolY = location[1];
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ListView滑动的回调
     */
    public class MyScroll implements OnScrollListener
    {
        //滑动状态的改变
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {
            //当滑动停止时
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
            {
                //getCount():包括头和脚，getadapter.getCount:不包括头和脚
                if (getLastVisiblePosition() == getCount() - 1)
                {
                    isDownRefresh = false;
                    handler.sendEmptyMessageDelayed(99, 2000);


                }
            }
        }

        //滑动中的调用
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
        {

        }
    }

    //设置一个下拉刷新和上拉加载的监听回调
    public void setOnRefreshStateListener(OnRefreshStateListener refreshListener)
    {
        this.refreshListener = refreshListener;
    }

    public interface OnRefreshStateListener
    {
        /**
         * 下拉刷新
         */
        void onDownRefresh();

        /**
         * 上拉加载
         */
        void onUpRefresh();
    }
}
