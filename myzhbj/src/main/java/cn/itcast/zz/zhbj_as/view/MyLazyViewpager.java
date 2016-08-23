package cn.itcast.zz.zhbj_as.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 不能左右滑动
 *  不中断,不消费,就可以屏蔽
 * 不能预加载
 * @author wangdh
 *
 */
public class MyLazyViewpager extends LazyViewPager {

    public MyLazyViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLazyViewpager(Context context) {
        super(context);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
    
    
}
