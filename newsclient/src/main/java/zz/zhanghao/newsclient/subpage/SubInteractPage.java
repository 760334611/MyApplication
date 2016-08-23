package zz.zhanghao.newsclient.subpage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import zz.zhanghao.newsclient.bean.NewsCenterBean;
import zz.zhanghao.newsclient.page.BasePage;

/**
 * Created by ${张昊} on 2016/8/17 0017.
 */
public class SubInteractPage extends BasePage
{

    private TextView textView;

    public SubInteractPage(Context context, NewsCenterBean.NewsCenterModeBean newsCenterModeBean)
    {
        super(context);
    }

    @Override
    public View initView()
    {
        textView = new TextView(context);
        return textView;
    }

    @Override
    public void initData()
    {
        textView.setText("我是二级互动页面");
    }
}
