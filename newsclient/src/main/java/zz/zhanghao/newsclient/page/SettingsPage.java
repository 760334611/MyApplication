package zz.zhanghao.newsclient.page;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ${张昊} on 2016/8/15 0015.
 */
public class SettingsPage extends BasePage
{

    private TextView view;

    public SettingsPage(Context context)
    {
        super(context);
    }

    @Override
    public View initView()
    {
        view = new TextView(context);
        return view;
    }

    @Override
    public void initData()
    {
        view.setText("我是设置");
    }
}
