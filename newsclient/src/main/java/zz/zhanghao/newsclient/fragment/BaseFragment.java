package zz.zhanghao.newsclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ${张昊} on 2016/8/15 0015.
 */
public abstract class BaseFragment extends Fragment
{

    public Context cxt;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle
            savedInstanceState)
    {
        cxt=getActivity();
        View view=initView();
        initData();
        return view;
    }

    /**
     * 初始化控件
     * @return
     */
    public abstract View initView();

    /**
     * 初始化控件的数据
     */
    public abstract void initData();
}
