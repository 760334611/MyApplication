package cn.itcast.zz.zhbj_as.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * 设置对应的自定义pager
 * 
 * @author wangdh
 * 
 */
public class SettingsPager extends BasePager {
    public SettingsPager(Context context) {
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
        textView.setText("我是设置");
    }
}
