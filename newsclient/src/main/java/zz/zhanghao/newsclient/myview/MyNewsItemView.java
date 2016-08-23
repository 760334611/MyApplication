package zz.zhanghao.newsclient.myview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import zz.zhanghao.newsclient.R;
import zz.zhanghao.newsclient.view.MainActivity;

/**
 * Created by ${张昊} on 2016/8/18 0018.
 */
public class MyNewsItemView extends ViewPager
{

    private ArrayList<String> image;
    private MainActivity mainActivity;
    private long downTime;
    private long upTime;
    private long moveTime;
    private int downX;
    private int downY;
    private int upX;
    private int upY;
    private int moveX;
    private int moveY;
    private OnItemClickListener listener;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 88:
                    if(!mainActivity.getMenuState())
                    {
                        int toPosition=(getCurrentItem()+1)%image.size();
                        setCurrentItem(toPosition);
                        handler.sendEmptyMessageDelayed(88,2000);
                    }
                    else
                    {
                        handler.sendEmptyMessageDelayed(99,2000);
                    }
                    break;
                case 99:
                    if(mainActivity.getMenuState())
                    {
                        handler.sendEmptyMessageDelayed(99,2000);
                    }
                    else
                    {
                        handler.sendEmptyMessageDelayed(88,2000);
                    }
                    break;
            }
        }
    };
    private BitmapUtils bitmapUtils;

    public MyNewsItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MyNewsItemView(Context context)
    {
        super(context);
        mainActivity= (MainActivity) context;
        init();
    }

    private void init()
    {
        bitmapUtils = new BitmapUtils(getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downX = (int) ev.getX();
                downTime=System.currentTimeMillis();
                //告诉父亲我要事件
                getParent().requestDisallowInterceptTouchEvent(true);
                //停止轮播
                handler.removeMessages(88);
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) ev.getY();
                moveX = (int) ev.getX();
                moveTime=System.currentTimeMillis();
                int disX = Math.abs(moveX - downX);
                int disY = Math.abs(moveY - downY);
                float time=moveTime-downTime;
                if(disX > disY){//水平 滑动
                    /**
                     * 如果是第一个界面,并且向右滑动 (moveX>downX)
                     * 如果是最后一个界面并且向左滑动(moveX<downX)
                     * 其他,自己处理
                     */
                    if(getCurrentItem()==0&&moveX>downX){
                        //如果是第一个界面,并且向右滑动
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if(getCurrentItem() == image.size()-1&&moveX<downX){
                        //如果是最后一个界面并且向左滑动(
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else{
                        //其他,自己处理
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else{
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
                upY = (int) ev.getY();
                upX = (int) ev.getX();
                disX = Math.abs(upX - downX);
                disY = Math.abs(upY - downY);
                downTime=System.currentTimeMillis();
                long disTime = upTime - downTime;
                if(disTime<500&&disX<5&&disY<5){
                    if(listener!=null){//防止用户不设置条目点击监听
                        listener.onItemClick(getCurrentItem());//点击的肯定是当前显示的界面
                    }
                }
                handler.sendEmptyMessageDelayed(88,2000);
                break;
            case MotionEvent.ACTION_CANCEL:
                handler.sendEmptyMessageDelayed(88,2000);
                break;

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 得到图片的数据
     */
    public void setImageData(ArrayList<String> image)
    {
        this.image=image;
        //设置适配
        setAdapter(new MyAdapter());
        handler.sendEmptyMessageDelayed(88,2000);
    }
    public class MyAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return image.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView imageView = (ImageView) View.inflate(getContext(), R.layout.viewpager_item ,null);
            //显示网络图片  TODO
            bitmapUtils.display(imageView,image.get(position));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }

    /**
     * 添加到屏幕上的时候回调
     */
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
    }

    /**
     * 从屏幕上移除时候回调
     */
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置viewpage的点击监听
     */
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener=listener;
    }
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

}
