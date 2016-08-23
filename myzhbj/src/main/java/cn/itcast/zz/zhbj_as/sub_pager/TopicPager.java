package cn.itcast.zz.zhbj_as.sub_pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.itcast.zz.zhbj_as.bean.NewsCenterBean.NewsCenterData;
import cn.itcast.zz.zhbj_as.pager.BasePager;

/**
 * 二级菜单-专题界面
 * @author wangdh
 *
 */
public class TopicPager extends BasePager {
    
    public TopicPager(Context context, NewsCenterData newsCenterData) {
        super(context);
    }
    private TextView textView;
    @Override
    public View initView() {
        textView = new TextView(context);
        return textView;
    }
    
    @Override
    public void initData() {
        textView.setText("我是二级菜单-专题界面");
    }
    
}
