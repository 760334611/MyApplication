package cn.itcast.zz.zhbj_as.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
/**
 * viewpager的PagerAdapter的自定义基类
 * @author wangdh
 * @param <T>
 *
 */
public abstract class DefaultPagerAdapter<T> extends PagerAdapter {
    //数据源
    private List<T> datas;

    public DefaultPagerAdapter(List<T> datas){
        this.datas = datas;
    }
    
    @Override
    public int getCount() {
        return datas.size();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    
    @Override
    public abstract Object instantiateItem(ViewGroup container, int position) ;
    
    
}
