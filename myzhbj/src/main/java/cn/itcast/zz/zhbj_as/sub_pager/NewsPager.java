package cn.itcast.zz.zhbj_as.sub_pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.zz.zhbj_as.R;
import cn.itcast.zz.zhbj_as.adapter.DefaultPagerAdapter;
import cn.itcast.zz.zhbj_as.indicator.TabPageIndicator;
import cn.itcast.zz.zhbj_as.pager.BasePager;
import  cn.itcast.zz.zhbj_as.bean.NewsCenterBean.NewsCenterData;

/**
 * 二级菜单-新闻界面
 * @author wangdh
 *
 */
public class NewsPager extends BasePager {
    
    @ViewInject(R.id.vp_sub_pager_news)
    private ViewPager vp_sub_pager_news;
    @ViewInject(R.id.indicator)
    private TabPageIndicator indicator;
    
    //新闻子界面数据源
    private List<NewsItemPager> newsItemPagers = new ArrayList<NewsItemPager>();
    //新闻界面数据
    private NewsCenterData newsCenterData;
    
    public NewsPager(Context context, NewsCenterData newsCenterData) {
        super(context);
        this.newsCenterData =newsCenterData;
    }
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.sub_pager_news, null);
        ViewUtils.inject(this, view);
        return view;
    }
    
    @Override
    public void initData() {
        //initData可能会被调用两次,newsItemPagers重复添加
        newsItemPagers.clear();
        for (int i = 0; i < newsCenterData.children.size(); i++) {
            newsItemPagers.add(new NewsItemPager(context,newsCenterData.children.get(i)));
        }
        //设置适配器
        MyNewsPagerAdapter adapter = new MyNewsPagerAdapter(newsItemPagers);
        vp_sub_pager_news.setAdapter(adapter);
        //绑定
        indicator.setViewPager(vp_sub_pager_news);
    }
    class MyNewsPagerAdapter extends DefaultPagerAdapter<NewsItemPager> {

        public MyNewsPagerAdapter(List<NewsItemPager> datas) {
            super(datas);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsItemPager currentItemPager = newsItemPagers.get(position);
            View currentItemView = currentItemPager.initView();//初始化view
            currentItemPager.initData();//初始化数据(一定不要忘记)
            container.addView(currentItemView);
            return currentItemView;
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
           return newsCenterData.children.get(position).title;
        }
    }
    
}
