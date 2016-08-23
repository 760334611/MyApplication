package zz.zhanghao.newsclient.view;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import zz.zhanghao.newsclient.R;
import zz.zhanghao.newsclient.bean.NewsCenterBean;
import zz.zhanghao.newsclient.fragment.ContentFragment;

public class MainActivity extends FragmentActivity
{

    private FragmentManager fragmentManager;
    private SharedPreferences sp;
    @ViewInject(R.id.lv_menu_news_center)
    private ListView lv_menu_news_center;
    private ContentFragment fragment;
    private List<NewsCenterBean.NewsCenterModeBean> data;
    private int currentPoistion;
    @ViewInject(R.id.id_menu)
    private SlidingMenu slidingMenu;
    private MyContentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("spName",0);
        //第一次进入已经完成
        sp.edit().putBoolean("isFirst",false).commit();
        //添加Fragment
        fragmentManager = getSupportFragmentManager();
        fragment = new ContentFragment();
        fragmentManager.beginTransaction().replace(R.id.fl_content, fragment,"CONTENT").commit();
        lv_menu_news_center = (ListView) findViewById(R.id.lv_menu_news_center);
        ViewUtils.inject(this);
    }

    public void setNewsContentData(List<NewsCenterBean.NewsCenterModeBean> data)
    {
        this.data=data;
        adapter = new MyContentAdapter();
        lv_menu_news_center.setAdapter(adapter);
        //添加条目点击监听
        lv_menu_news_center.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                fragment.changeSubPage(position);
                currentPoistion=position;
                adapter.notifyDataSetChanged();
                chengeMenuPage();

            }
        });
    }

    /**
     *    用于其它类的回调，改变菜单的开和闭
     */
    public void chengeMenuPage()
    {
        slidingMenu.ChangeMenu();
    }
    /**
     *    用于其它类的回调，传递一级菜单的位置
     */
    public void setFirstMenu(int position)
    {
        slidingMenu.getFirstMenu(position);
    }
    /**
     *    用于其它类的回调，传递手势状态
     */
    public boolean getMenuState()
    {
        return slidingMenu.getMenuState();
    }

    /**
     * 配置左边菜单的列表
     */
    public class MyContentAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return data.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view=null;
            if(convertView==null)
            {
                view=View.inflate(MainActivity.this,R.layout.listview_item_menu,null);
            }
            else
            {
               view=convertView;
            }
            ImageView iv_menu_item= (ImageView) view.findViewById(R.id.iv_menu_item);

            TextView tv_menu_item= (TextView) view.findViewById(R.id.tv_menu_item);
            tv_menu_item.setText(data.get(position).title);
            if(position==currentPoistion)
            {
                iv_menu_item.setImageResource(R.drawable.menu_arr_select);
                tv_menu_item.setTextColor(Color.RED);
                view.setBackgroundResource(R.drawable.progress_dialog_bg);
            }
            else
            {
                iv_menu_item.setImageResource(R.drawable.menu_arr_normal);
                tv_menu_item.setTextColor(Color.WHITE);
                view.setBackground(null);

            }
            return view;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }


    }
}
