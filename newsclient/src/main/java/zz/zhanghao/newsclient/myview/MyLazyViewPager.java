package zz.zhanghao.newsclient.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**处理左右滑动的事件，不拦截，不消耗
 * Created by ${张昊} on 2016/8/15 0015.
 */
public class MyLazyViewPager extends LazyViewPager
{
    public MyLazyViewPager(Context context)
    {
        super(context);
    }

    public MyLazyViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return false;
    }
}
