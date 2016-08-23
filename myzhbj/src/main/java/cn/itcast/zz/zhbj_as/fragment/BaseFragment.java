package cn.itcast.zz.zhbj_as.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 菜单和内容fragment的基类
 * @author wangdh
 *
 */
public abstract class BaseFragment extends Fragment {
    public Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = initView();
//        TextView textView = new TextView(getActivity());//初始化view  initView
//        textView.setText("xxx");//初始化数据 initData();
        initData();
        return view;
    }
  
    /**
     * 初始化View
     * @return
     */
    public abstract View initView() ;
    /**
     * 初始化数据
     */
    public abstract void initData() ;
    
}
