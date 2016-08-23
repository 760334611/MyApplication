package zz.zhanghao.newsclient.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import zz.zhanghao.newsclient.R;
import zz.zhanghao.newsclient.adapter.MyPageAdapter;
import zz.zhanghao.newsclient.myview.MyLazyViewPager;
import zz.zhanghao.newsclient.page.BasePage;
import zz.zhanghao.newsclient.page.GovPage;
import zz.zhanghao.newsclient.page.HomePage;
import zz.zhanghao.newsclient.page.NewsCenterPage;
import zz.zhanghao.newsclient.page.SettingsPage;
import zz.zhanghao.newsclient.page.SmartServicePage;
import zz.zhanghao.newsclient.view.MainActivity;

/**
 * Created by ${张昊} on 2016/8/14 0014.
 */
public class ContentFragment extends BaseFragment
{

    private View fragmentView;
    @ViewInject(R.id.layout_content)
    protected MyLazyViewPager layout_content;
    @ViewInject(R.id.main_radio)
    private RadioGroup main_radio;
    private ArrayList<BasePage> pages=new ArrayList<BasePage>();
    /**
     * 页面所在的位置
     */
    private int pagePosition=0;


    @Override
    public View initView()
    {
        fragmentView = View.inflate(cxt, R.layout.fragment_home,null);
        ViewUtils.inject(this,fragmentView);
        return fragmentView;
    }

    @Override
    public void initData()
    {
        pages.add(new HomePage(cxt));
        pages.add(new NewsCenterPage(cxt));
        pages.add(new SmartServicePage(cxt));
        pages.add(new GovPage(cxt));
        pages.add(new SettingsPage(cxt));

        //初始化RadioButton的位置
        main_radio.check(R.id.rb_function);
        MyAdapter myAdapter = new MyAdapter(pages);
        layout_content.setAdapter(myAdapter);
        //设置RadioButton的点击事件
        main_radio.setOnCheckedChangeListener(new MyButtonClick());
    }

    public class MyButtonClick implements RadioGroup.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            switch (checkedId)
            {
                //首页
                case R.id.rb_function:
                    pagePosition=0;
                    break;
                //新闻中心
                case R.id.rb_news_center:
                    pagePosition=1;
                    break;
                //智慧服务
                case R.id.rb_smart_service:
                    pagePosition=2;
                    break;
                //政务服务
                case R.id.rb_gov_affairs:
                    pagePosition=3;
                    break;
                //设置
                case R.id.rb_setting:
                    pagePosition=4;
                    break;
            }
            //跳到指定的页面
            layout_content.setCurrentItem(pagePosition);
            MainActivity mainActivity= (MainActivity) getActivity();
            mainActivity.setFirstMenu(pagePosition);

        }
    }

    public class  MyAdapter extends MyPageAdapter
    {
        public MyAdapter(List list)
        {
            super(list);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            BasePage basePage=pages.get(position);
            View view=basePage.initView();
            basePage.initData();
            container.addView(view);
            return view;
        }

    }

    public void changeSubPage(int position)
    {
        NewsCenterPage ncp=(NewsCenterPage)pages.get(1);
        ncp.subPageChange(position);
    }



}
