package cn.itcast.zz.zhbj_as.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * 智慧服务对应的自定义pager
 * 
 * @author wangdh
 * 
 */
public class SmartServicePager extends BasePager {
    public SmartServicePager(Context context) {
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
        textView.setText("我是智慧服务");
    }
}
