package cn.itcast.zz.zhbj_as.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.itcast.zz.zhbj_as.R;

/**
 * 可以左右滑动,并且自定义滚动的viewpager
 * @author wangdh
 *
 */
public class RollViewPager extends ViewPager {
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msg_auto_play:
                //切换下一张图片
                    int nextItem = (getCurrentItem() + 1)%topImageUrls.size();
                    setCurrentItem(nextItem);
                    //无限发送,再次发送
                    handler.sendEmptyMessageDelayed(msg_auto_play, 2000);
//                    Log.i("RollViewPager", "切换Item:"+nextItem);
                break;
                
                default:
                break;
            }
        }
    };
    private static final int msg_auto_play = 0;
    //数据源
    private List<String> topImageUrls;
    private BitmapUtils bitmapUtils;
    
    private int downX = 0;
    private MyRollPagerAdapter adapter;
    //条目点击监听
    private OnItemClickListener onItemClickListener;
    /**
     * 布局中使用,系统实例化
     * @param context
     * @param attrs
     */
    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapUtils = new BitmapUtils(getContext());
    }
    /**
     * 代码实例化
     * @param context
     */
    public RollViewPager(Context context) {
        super(context);
        bitmapUtils = new BitmapUtils(getContext());
    }
    public void initTopImageUrls(List<String> topImageUrls) {
        this.topImageUrls = topImageUrls;
        
    }
   
   
    /**
     * 开启轮播
     */
    public void startRoll() {
        Log.i("RollViewPager", "startRoll");
        //设置适配器
        if(adapter == null){
            adapter = new MyRollPagerAdapter();
            setAdapter(adapter);
        }
        //通过handler发送循环延迟消息 , 实现无限轮播
        handler.sendEmptyMessageDelayed(msg_auto_play, 2000);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i("RollViewPager", "onDetachedFromWindow");
        //移除消息
        handler.removeCallbacksAndMessages(null);//移除所有的回调和消息
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("RollViewPager", "onAttachedToWindow");
        handler.removeCallbacksAndMessages(null);//避免重复
        //通过handler发送循环延迟消息 , 实现无限轮播
        handler.sendEmptyMessageDelayed(msg_auto_play, 2000);
    }
    class MyRollPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topImageUrls.size();
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //ImageView
            ImageView imageView = (ImageView) View.inflate(getContext(), R.layout.viewpager_item, null);
            container.addView(imageView);
            //显示具体图片 
            /**
             * 三级缓存: **
             * 1.内存缓存:hashMap<key,value> key:图片网络url value:图片对象Bitmap
             *                v4.LruCache<key,value>  算法Lru:less recent use  (最少最近使用:less删除recent保存)
             * 2.本地缓存:sd: mnt/sdcard/工程名/cache/image/xxx.png   
             *                file: 图片文件:xxx.png  
             *                图片名称:图片url+md5加密       
             *                md5加密为了防止特殊字符(windows/linux) 
             * 3.网络缓存:
             *        什么是缓存? 不需要联网获取的数据
             *        
             * 图片加载逻辑步骤: (原则: 优先加载速度最快的缓存)
             *  1. 先从内存缓存中获取,如果获取到了,直接显示.如果没有获取到呢?在从本地获取
             *  2. 再从本地中获取,如果获取到了,先加载到内存缓存中,再展示.如果没有获取到,从网络获取.
             *  3. 从网络获取,一般都可以获取到.先缓存到内存中,再缓存到本地中,最后展示.
             *     
             */
            //通过BitmapUtils展示网络图片
            /**
             * 1. T : 展示的控件 (泛型:展示的控件不确定)
             * 2. uri: 图片的url
             */
            bitmapUtils.display(imageView, topImageUrls.get(position));
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        
    }
    /**
     * 一.滑动优化
     * 如果处于第一张图片,并且右滑,事件应该交由父亲处理
     * 如果处于最后一张图片,并且左滑,事件应该交由父亲处理
     * 其他情况,自己处理
     * 二.按下停止轮播
     *     按下停止/抬起继续/取消继续
     */
    private int downTime = 0;
    private int downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //内存的viewpager可以拿到down和一部分的move事件
//        Log.i("RollViewPager", "RollViewPager.onTouchEvent"+ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            downX = (int) ev.getX();
            downY = (int) ev.getY();
            downTime = (int) System.currentTimeMillis();
            //大喊一声:爹,把事件给我 (请求不允许中断事件)
            getParent().requestDisallowInterceptTouchEvent(true);
            //移除消息
            handler.removeCallbacksAndMessages(null);
            break;
            case MotionEvent.ACTION_MOVE:
            /**
             * 如果是水平滑动,原来逻辑
             * 如果是上下滑动,交由父亲(listview)
             */
            /**
             *  * 如果处于第一张图片,并且右滑,事件应该交由父亲处理
                 * 如果处于最后一张图片,并且左滑,事件应该交由父亲处理
                 * 其他情况,自己处理
             */
            int moveX = (int) ev.getX();
            int moveY = (int) ev.getY();
            int disX = Math.abs(moveX - downX);
            int disY = Math.abs(moveY-downY);
            if(disX >disY){//水平方向
                if(getCurrentItem()==0 && moveX > downX){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else if(getCurrentItem() == topImageUrls.size()-1 && downX>moveX){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else{
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }else{
                //竖直方向
                getParent().requestDisallowInterceptTouchEvent(false);
            }
            
            break;
            case MotionEvent.ACTION_UP:
            //继续轮播
            startRoll();
            /**
             *  3. 处罚条目点击监听的回调方法:onItemClick (****)
             * 单击事件: down/up
                                    单击事件/长按事件:down/up的时间间隔 (<=500ms)
                                    单击事件/拖动事件:down/up位移距离(防抖动效果 水平方向垂直方向 5px)
             */
            int upX = (int) ev.getX();
            int upY = (int) ev.getY();
            int upTime = (int) System.currentTimeMillis();
            disX = Math.abs(upX-downX);
            disY = Math.abs(upY-downY);
            if(upTime-downTime<=500 && disX<=5 && disY<=5){
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClick(getCurrentItem());//position就是当前显示的界面
            }
            break;
            case MotionEvent.ACTION_CANCEL:
            //继续轮播
            startRoll();
            break;
            
            default:
            break;
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 自定义监听:**

        1. 添加自定义 setOnItemClickListener (设置监听方法)
        2. 需要添加监听接口

             public interface OnItemClickListener
            
                监听回调方法 : onItemClick

        3. 处罚条目点击监听的回调方法:onItemClick (****)
                                    单击事件: down/up
                                    单击事件/长按事件:down/up的时间间隔
                                    单击事件/拖动事件:down/up位移距离
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
        
    }
    
    public interface OnItemClickListener{
        /**
         * 
         * @param position : 点击条目的索引,位置
         */
        public void onItemClick(int position);
    }
    
}
