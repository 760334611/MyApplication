package zz.zhanghao.newsclient.page;

import android.content.Context;
import android.view.View;

/**
 * Created by ${张昊} on 2016/8/15 0015.
 */
public abstract class BasePage
{
    public Context context;
    public BasePage(Context context)
    {
        this.context=context;
    }

    public abstract View initView();

    public abstract void initData();

}
