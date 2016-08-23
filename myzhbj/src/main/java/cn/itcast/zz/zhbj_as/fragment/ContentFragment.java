package cn.itcast.zz.zhbj_as.fragment;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.zz.zhbj_as.R;
import cn.itcast.zz.zhbj_as.pager.BasePager;
import cn.itcast.zz.zhbj_as.pager.GovPager;
import cn.itcast.zz.zhbj_as.pager.HomePager;
import cn.itcast.zz.zhbj_as.pager.NewsCenterPager;
import cn.itcast.zz.zhbj_as.pager.SettingsPager;
import cn.itcast.zz.zhbj_as.pager.SmartServicePager;
import cn.itcast.zz.zhbj_as.view.MyLazyViewpager;

/**
 * 内容Fragment
 * @author wangdh
 *
 */
public class ContentFragment extends BaseFragment {
    
    private final class OnMyCheckedChangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 切换显示内容 (切换Viewpager)
            int item = 0;
            switch (checkedId) {
                case R.id.rb_home:
                    item = 0;
                break;
                case R.id.rb_news_center:
                    item = 1;
                break;
                case R.id.rb_smart_service:
                    item = 2;
                break;
                case R.id.rb_gov_affairs:
                    item = 3;
                break;
                case R.id.rb_setting:
                    item = 4;
                break;
                
                default:
                break;
            }
            vp_home.setCurrentItem(item);
        }
    }

    @ViewInject(R.id.vp_home)
    private MyLazyViewpager vp_home;
    @ViewInject(R.id.rg_home)
    private RadioGroup rg_home;
    
    //数据源
    private List<BasePager> pagers = new ArrayList<BasePager>();
    
    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        //注册
        ViewUtils.inject(this, view);
        //RadioGroup 添加切换监听
        rg_home.setOnCheckedChangeListener(new OnMyCheckedChangeListener());
        //RadioGroup 默认选中 : 首页
        rg_home.check(R.id.rb_home);
        return view;
    }
    
    @Override
    public void initData() {
        pagers.add(new HomePager(mContext));
        pagers.add(new NewsCenterPager(mContext));
        pagers.add(new SmartServicePager(mContext));
        pagers.add(new GovPager(mContext));
        pagers.add(new SettingsPager(mContext));
        
        MyPagerAdapter adapter =new MyPagerAdapter();
        vp_home.setAdapter(adapter);
    }
    /**
     * 已经菜单数据源暴露出去
     * @author wangdh
     *
     */
    public List<BasePager> getPagers(){
        return pagers;
    }
    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("ContentFragment", "instantiateItem:"+position);
            BasePager currentPager = pagers.get(position);
            View currentView = currentPager.initView();//初始化View
            //(***必须调用initData())初始化数据
            currentPager.initData();
            container.addView(currentView);
            return currentView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("ContentFragment", "destroyItem:"+position);
            container.removeView((View) object);
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        
    }
    
}
