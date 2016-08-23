package zz.zhanghao.newsclient.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ${张昊} on 2016/8/19 0019.
 */
public abstract class MyPageAdapter<T> extends PagerAdapter
{
    private List<T> list;
    public MyPageAdapter(List<T> list)
    {
        this.list=list;
    }
    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public abstract Object instantiateItem(ViewGroup container, int position);

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }
}
