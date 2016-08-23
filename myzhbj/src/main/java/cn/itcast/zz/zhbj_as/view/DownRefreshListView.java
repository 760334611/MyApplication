package cn.itcast.zz.zhbj_as.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
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

import cn.itcast.zz.zhbj_as.R;

/**
 * 下拉刷新lisview
 *  1. 添加刷新头
    2. 隐藏刷新头
    3. 跟着手指下滑,一起出现
 * @author wangdh
 *
 */
public class DownRefreshListView extends ListView {

    private final class OnMyScrollListener implements OnScrollListener {
        /**
         * 滑动状态改变
         * 2.看到脚布局之后,加载下一页数据
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            /** 
             *  public static int SCROLL_STATE_IDLE = 0;  //空闲状态
                public static int SCROLL_STATE_TOUCH_SCROLL = 1; //触摸滑动
                public static int SCROLL_STATE_FLING = 2;//飞翔
             */
            /**
             * 1. 状态: 空闲状态
             * 2. 最后显示的条目=listview的最后一个条目(脚布局)
             */
            //最后显示的条目
            int lastVisiblePosition = getLastVisiblePosition();
            if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisiblePosition == getCount()-1){
                //添加开关,避免重复请求
                if(!isLoadingMore){//如果当前没有正在加载更多
                    Log.i("DownRefreshListView", "加载下一页");
                    isLoadingMore = true;
                    //如果正在加载更多,应该请求网络(通过handler发送延迟消息模拟网络请求)
//                    handler.sendEmptyMessageDelayed(refresh_finish, 2000);
                    if(onRefreshListener!=null){
                        onRefreshListener.onLoadMore();
                    }
                }
            }
            
        }
        /**
         * 滑动
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            
        }
    }
    private Context context;
    
    /**
     * 下拉刷新 (默认)
                释放刷新
                正在刷新
     */
    public static final int state_down_refresh  = 0;
    public static final int state_release_refresh  = 1;
    public static final int state_refresh_ing  = 2;

    private static final int refresh_finish = 0;

    private static final int msg_show_footer = 1;
    public int currentState = state_down_refresh;//当前状态:默认下拉刷新
    //加载下一页开关
    public boolean isLoadingMore = false;//是否正在加载更多:默认没有
    public Handler handler = new Handler(){
      public void handleMessage(android.os.Message msg) {
          switch (msg.what) {
            case refresh_finish:
            refreshFinish();
            break;
            case msg_show_footer:
               //再次显示加载更多脚布局
               footerView.setPadding(0, 0, 0, 0);
               //将加载更多标志,变为false
               isLoadingMore = false;
            break;
            
            default:
            break;
        }
      }

    };
    
    public DownRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context =context; 
        initView();
    }

    public DownRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
        initView();
    }

    public DownRefreshListView(Context context) {
        super(context);
        this.context =context;
        initView();
    }
    //刷新头跟布局(添加轮播图)
    @ViewInject(R.id.ll_refresh_header_root)
    private LinearLayout ll_refresh_header_root;

    //刷新头布局
    @ViewInject(R.id.ll_refresh_header_view)
    private LinearLayout ll_refresh_header_view;
    
    //进度条
    @ViewInject(R.id.pb_refresh_header)
    private ProgressBar pb_refresh_header;
    //箭头
    @ViewInject(R.id.iv_refresh_header_arrow)
    private ImageView iv_refresh_header_arrow;
    //刷新头标题
    @ViewInject(R.id.tv_refresh_header_text)
    private TextView tv_refresh_header_text;

    private RotateAnimation down2up;

    private RotateAnimation up2down;
    private int downY = 0;

    private int refreshHeaderHeight;

    private int listViewY;

    private View rollView;

    private int rollViewY;

    private int footerViewHeight;

    private View footerView;

    private OnRefreshListener onRefreshListener;
    
    private void initView() {
        //1. 添加刷新头
        View refreshView = View.inflate(context, R.layout.refresh_header,null);
        ViewUtils.inject(this,refreshView);
        addHeaderView(refreshView);
        //初始化箭头旋转动画
        initAnimation();
        // 2. 隐藏刷新头
//        ll_refresh_header_view.setPadding(0, 50, 0, 0);//正的paddingTop:向下偏移
//        ll_refresh_header_view.setPadding(0, -60, 0, 0);//负的paddingTop:向上偏移(隐藏)
        //完全隐藏 (paddingTop=-刷新头高度)
//        ll_refresh_header_view.getHeight();//控件显示到屏幕上之后的高度
        //手动测量
        ll_refresh_header_view.measure(0, 0);//00代表随意测量,以实际宽高为准
        refreshHeaderHeight = ll_refresh_header_view.getMeasuredHeight();
        ll_refresh_header_view.setPadding(0, -refreshHeaderHeight, 0, 0);
        
        /**
         * 1. 添加脚布局
            2. 看到脚布局之后,加载下一页数据
         */
        footerView = View.inflate(context, R.layout.refresh_footer, null);
        addFooterView(footerView);
        setOnScrollListener(new OnMyScrollListener());
        //脚布局测量
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        
    }

    /**
     * 添加轮播图
     */
    public void addRollView(View rollView){
        this.rollView = rollView;
        ll_refresh_header_root.addView(rollView);
    }
    /**
     * //初始化箭头旋转动画
     */
    private void initAnimation() {
        //下-上
        /**
         * 1.fromDegrees 开始角度
         * 2.toDegrees 结束角度
         */
        down2up = new RotateAnimation(0, -180, 
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        
        down2up.setFillAfter(true);
        down2up.setDuration(500);
        //上--下
        up2down = new RotateAnimation(-180, -360, 
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        
        up2down.setFillAfter(true);
        up2down.setDuration(500);
    }
    
    
    /**
     * 状态切换:
     * move:
     *  1. 下拉刷新 ,paddingTop >=0 ---- 释放刷新
        2. 释放刷新 ,paddingTop <0 ----下拉刷新
    
       up:
        3. 释放刷新,松手---正在刷新
    
                           下拉刷新,松手--状态没改变,隐藏
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //刚开始可能拿不到down事件,第一个move事件当成down事件
//        Log.i("DownRefreshListView", "DownRefreshListView.onTouchEvent:"+ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//            downY = (int) ev.getY();//可能会跳
            break;
            case MotionEvent.ACTION_MOVE:
            //如果viewpager的Y坐标大于等于listview的y坐标,再去改变paddingTop
            if(rollViewY>=listViewY){
                if(downY==0){//第一个move事件当成down事件
                    downY = (int) ev.getY();
                    break;
                }
                int moveY = (int) ev.getY();
                int disY = moveY - downY;//位移距离
                //让刷新头跟着手指一起出现 (下滑时,改变paddingTop值,让刷新头显示.上滑是利用listview的上滑隐藏即可)
                if(disY>0){
                    int paddingTop = -refreshHeaderHeight+disY;
                    ll_refresh_header_view.setPadding(0, paddingTop, 0, 0);
                    /**
                     *  1. 下拉刷新 ,paddingTop >=0 ---- 释放刷新
                    2. 释放刷新 ,paddingTop <0 ----下拉刷新
                     */
                    if(currentState == state_down_refresh && paddingTop>=0){
                        currentState = state_release_refresh;
                        refreshState();
                    }
                    if(currentState == state_release_refresh && paddingTop<0){
                        currentState = state_down_refresh;
                        refreshState();
                    }
                    return true;//不让listview上下滑动
                }
            }
            
            break;
            case MotionEvent.ACTION_UP:
            downY = 0;//抬起之后,重新变为0
            /**
             * 3. 释放刷新,松手---正在刷新
    
                                           下拉刷新,松手--状态没改变,隐藏
             */
            if(currentState == state_release_refresh){
                currentState = state_refresh_ing;
                refreshState();
            }
            if(currentState == state_down_refresh){
                ll_refresh_header_view.setPadding(0, -refreshHeaderHeight, 0, 0);
            }
            
            break;
            
            default:
            break;
        }
        return super.onTouchEvent(ev);//处理listview上下滑动
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //获取listview的位置
        int[] location  = new int [2];//xy
        getLocationInWindow(location);
        listViewY = location[1];
        
        //获取viewpager的位置
        rollView.getLocationInWindow(location);
        rollViewY = location[1];
        
//        Log.i("DownRefreshListView", "listViewY:"+listViewY);
//        Log.i("DownRefreshListView", "rollViewY:"+rollViewY);
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 状态改变之后,刷新界面
     */
    private void refreshState() {
        switch (currentState) {
            case state_release_refresh:
            //下拉刷新-释放刷新 (箭头向上旋转,标题变为:释放刷新)
            iv_refresh_header_arrow.startAnimation(down2up);
            tv_refresh_header_text.setText("释放刷新");
            
            break;
            case state_down_refresh:
            //释放刷新-下拉刷新 (箭头向下旋转,标题变为:下拉刷新)
            iv_refresh_header_arrow.startAnimation(up2down);
            tv_refresh_header_text.setText("下拉刷新");
            
            break;
            case state_refresh_ing:
            //箭头隐藏,进度条显示,标题:正在刷新
            iv_refresh_header_arrow.setVisibility(View.GONE);
            //箭头隐藏之后,动画还在
            iv_refresh_header_arrow.clearAnimation();
            pb_refresh_header.setVisibility(View.VISIBLE);
            tv_refresh_header_text.setText("正在刷新");
            //让刷新头完全显示
            ll_refresh_header_view.setPadding(0, 0, 0, 0);
            //发送handler,模拟网络操作.2s之后隐藏正在刷新
//            handler.sendEmptyMessageDelayed(refresh_finish, 2000);
            if(onRefreshListener!=null){
                onRefreshListener.onRefresh();
            }
            break;
            
            default:
            break;
        }
        
    }
    /**
     * 刷新完成
     */
    public void refreshFinish() {
        if(!isLoadingMore){
            //下拉刷新完成 (完全隐藏刷新头,复原状态)
            ll_refresh_header_view.setPadding(0, -refreshHeaderHeight, 0, 0);
            currentState = state_down_refresh;
            pb_refresh_header.setVisibility(View.INVISIBLE);
            iv_refresh_header_arrow.setVisibility(View.VISIBLE);
            tv_refresh_header_text.setText("下拉刷新");
        }else{
            //加载更多刷新完成.让加载更多脚布局隐藏
            footerView.setPadding(0, 0, 0, -footerViewHeight);//paddingTop负数隐藏,正数是偏移 (控件隐藏:指定负的paddingTop/Bottom)
            //隐藏之后,再次显示
            handler.sendEmptyMessageDelayed(msg_show_footer, 500);
        }
        
    };  
    /**
     * 自定义刷新监听回调:
     * 1. 定义监听接口
     * 2. 对外暴露设置监听接口方法 setxxx
     * 3. 回调接口方法
     */
    public interface OnRefreshListener{
        /**
         * 下拉刷新
         */
        public void onRefresh();
        /**
         * 上拉加载
         */
        public void onLoadMore();
    }
    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
    }
    
}
