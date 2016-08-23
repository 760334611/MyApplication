package cn.itcast.zz.zhbj_as.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
/**
 * 自定义的BaseAdapter的基类
 * 1.抽取共性
 *   getCount()/getItem()/getItemId();
 * 2.发现未知数据
 *    通过构造传递
 *    通过setXXX();
 * 3.发现未知数据类型
 *     直接泛型 T
 * 3.非共性方法 getView
 *     抽象: 限制子类必须重写
 *     不抽象: 不限制
 * @author wangdh
 * @param <T>
 *
 */
public abstract class DefaultBaseAdapter<T> extends BaseAdapter {
    //数据源
    public List<T> datas;

    public DefaultBaseAdapter(List<T> datas){
        this.datas = datas;
    }
    
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
    
}
