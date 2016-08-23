package zz.zhanghao.newsclient.subpage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import zz.zhanghao.newsclient.R;
import zz.zhanghao.newsclient.bean.NewsCenterBean.NewsCenterModeBean;
import zz.zhanghao.newsclient.indicator.TabPageIndicator;
import zz.zhanghao.newsclient.newscontentpage.ContentPage;
import zz.zhanghao.newsclient.page.BasePage;

/**
 * Created by ${张昊} on 2016/8/17 0017.
 */
public class SubNewsPage extends BasePage
{

    private List<NewsCenterModeBean.NewsCenterAddressBean> children;
    private ArrayList<BasePage> Pages = new ArrayList<BasePage>();
    private View view;
    @ViewInject(R.id.indicator)
    private TabPageIndicator indicator;
    @ViewInject(R.id.vp_sub_pager_news)
    private ViewPager vp_sub_pager_news;
    private MyOnPageChange listener;


    public SubNewsPage(Context context, NewsCenterModeBean newsCenterModeBean)
    {
        super(context);
        this.children = newsCenterModeBean.children;
    }

    @Override
    public View initView()
    {
        view = View.inflate(context, R.layout.sub_pager_news, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData()
    {
        Pages.clear();
        for (int i = 0; i < children.size(); i++)
        {
            Pages.add(new ContentPage(context, children.get(i)));
        }
        vp_sub_pager_news.setAdapter(new MyAdapter());
        indicator.setViewPager(vp_sub_pager_news);
        listener = new MyOnPageChange();
        indicator.setOnMyClickListener(new MyIndicator());
    }

    public class MyAdapter extends PagerAdapter
    {
        @Override
        public CharSequence getPageTitle(int position)
        {
            return children.get(position).title;
        }

        @Override
        public int getCount()
        {
            return children.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            BasePage bp = Pages.get(position);
            View view = bp.initView();
            bp.initData();
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }
    public class MyIndicator implements TabPageIndicator.OnMyClickListener
    {
        @Override
        public void indicatorClick()
        {

            vp_sub_pager_news.setOnPageChangeListener(listener);
        }

        @Override
        public void indicatorMove()
        {
            indicator.setOnPageChangeListener(listener);
        }
    }

    public class MyOnPageChange implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {

        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

}
