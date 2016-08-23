package zz.zhanghao.newsclient.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by ${张昊} on 2016/8/19 0019.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter
{
    public List<T> list;

    public MyBaseAdapter(List<T> list)
    {
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

}
