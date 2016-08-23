package cn.itcast.zz.zhbj_as.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * 政务指南对应的自定义pager
 * 
 * @author wangdh
 * 
 */
public class GovPager extends BasePager {
    public GovPager(Context context) {
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
        textView.setText("我是政务指南");
    }
}
