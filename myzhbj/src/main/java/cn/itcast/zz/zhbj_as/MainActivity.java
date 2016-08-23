package cn.itcast.zz.zhbj_as;

import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import cn.itcast.zz.zhbj_as.fragment.ContentFragment;
import cn.itcast.zz.zhbj_as.fragment.MenuFragment;

public class MainActivity extends SlidingFragmentActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        //设置内容和菜单
        setContentView(R.layout.slidingmenu_content);
        setBehindContentView(R.layout.slidingmenu_menu);
        //设置
        SlidingMenu slidingMenu = getSlidingMenu();
        //打开方式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //位置
        slidingMenu.setMode(SlidingMenu.LEFT);
        //分割线
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//        slidingMenu.setShadowWidth(R.dimen.shadow_width);
        //宽度 (偏移宽度)
        slidingMenu.setBehindWidthRes(R.dimen.slidingmenu_width);

        //将Fragment替换进去 
        //第三个参数:String tag ,给ContentFragment添加标记
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,new ContentFragment(),"CONTENT").commit();
//        getSupportFragmentManager().findFragmentByTag("CONTENT");//替换时打标记,可以通过findFragmentByTag获取对应的Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_menu,new MenuFragment(),"MENU").commit();
        
    }
    /**
     * 根据tag查找对应的Fragment
     * @return
     */
    public MenuFragment getMenuFragment(){
       return (MenuFragment) getSupportFragmentManager().findFragmentByTag("MENU");
    }
    public ContentFragment getContentFragment(){
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag("CONTENT");
    }
    
}
