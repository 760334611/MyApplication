package zz.zhanghao.newsclient.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

import zz.zhanghao.newsclient.R;

/**
 * HorizontalScrollView，只能左右水平滑动
 *
 * @author Administrator
 */
public class SlidingMenu extends HorizontalScrollView
{


    private boolean isOnce;
    private LinearLayout mOutLayout;
    private int mScreenWidth;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mMenuWidth;
    private int mContentWidth;
    private int mRightPadding;
    private boolean isOpen;
    private int startX;
    private int endX;
    private int downX;
    private int menuPosition = 0;
    private boolean state;


    //得到自定义属性，需要三个参数的构造方法
    public SlidingMenu(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        //获得我们的自定义属性
        //TypedArray实例是个属性的容器，context.obtainStyledAttributes()方法返回得到
        //参数1.是values里的attrs文件，2.是自定义控件的属性名数组，3.默认是defStyle,4,优先级，style，Theme，而这些位置定义的值有一个优先级
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyle, 0);
        //得到属性的个数
        int att = ta.getIndexCount();
        for (int i = 0; i < att; i++)
        {
            int attr = ta.getIndex(i);
            switch (attr)
            {
                case R.styleable.SlidingMenu_rightPadding:
                    mRightPadding = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics()));//最后一个参数是pid转sp默认是80
                    break;
            }
        }
        //不要忘了释放
        ta.recycle();
        init(context);

    }

    public SlidingMenu(Context context, AttributeSet attrs)
    {
        //调用三个参数的构造
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context)
    {
        //调用两个个参数的构造
        this(context, null);

    }

    // 初始化数据
    private void init(Context context)
    {
        // TODO Auto-generated method stub
        // 得到屏幕的宽
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        // 设置子控件的宽高，因为这个方法可能调用多次
        if (!isOnce)
        {
            // 得到第一层子view
            mOutLayout = (LinearLayout) getChildAt(0);
            // 得到第二层view
            mMenu = (ViewGroup) mOutLayout.getChildAt(0);
            mContent = (ViewGroup) mOutLayout.getChildAt(1);
            // 给第二层的子view设置宽度
            //mMenuWidth = mMenu.getLayoutParams().width = 3 * mScreenWidth / 4;
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mRightPadding;
            mContentWidth = mContent.getLayoutParams().width = mScreenWidth;
            isOnce = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
        // 通过设置偏移量将menu隐藏
        scrollTo(mMenuWidth, 0);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (menuPosition != 1)
        {
            return false;
        } else
        {
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) ev.getX();
                    downX = (int) ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    endX = (int) ev.getX();
                    int insdance = endX - startX;
                    startX = endX;

                    if (downX<mScreenWidth/5)
                    {
                        return true;
                    }
                    else if (insdance < 0 && isOpen)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }

            }

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        // TODO Auto-generated method stub

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                // 根据偏移的距离来判断显示的页面
                //我们手指划过的距离
                int moveX = getScrollX();

                if (moveX > mMenuWidth / 2)
                {
                    smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else
                {
                    smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }

        return super.onTouchEvent(ev);
    }

    //切换菜单
    public void ChangeMenu()
    {
        if (isOpen)
        {
            smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        } else
        {
            smoothScrollTo(0, 0);
            isOpen = true;

        }
    }

    /**
     * onScrollChanged他可以回调手动或者是系统自动的拖动
     * l和t是滚动参数，开始是控件的宽高，oldl, oldt，减少或增加的梯度
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        if(l<mMenuWidth)
        {
            state=true;
        }
        else
        {
            state=false;
        }
        //得到偏移的百分比
        float per = l * 1.0f / mMenuWidth;
        //Content页面缩放
        float contentScale = 0.7f + 0.3f * per;
        //Menu页面缩放
        float menuScale = 1.0f - per * 0.3f;
        //Menu页面的透明度
        float menuAlpha = 0.6f + 0.4f * (1 - per);

        //Menu的动画
        ViewHelper.setTranslationX(mMenu, l * 0.6f);
        ViewHelper.setAlpha(mMenu, menuAlpha);
        ViewHelper.setScaleX(mMenu, menuScale);
        ViewHelper.setScaleY(mMenu, menuScale);

        //Content的动画，因为中心店的问题，页面会全部消失，所以要设置中心点在左边的中点
        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleX(mContent, contentScale);
        ViewHelper.setScaleY(mContent, contentScale);


    }

    public void getFirstMenu(int menuPosition)
    {
        this.menuPosition = menuPosition;
    }

    //得到我们滑动页面与menu是否打开的关系
    public boolean getMenuState()
    {

        return state;
    }

}
