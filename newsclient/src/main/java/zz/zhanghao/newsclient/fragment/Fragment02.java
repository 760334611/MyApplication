package zz.zhanghao.newsclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ${张昊} on 2016/8/14 0014.
 */
public class Fragment02 extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(getClass().getSimpleName());//Fragment1 / cn.itcast.zz.slidingmenutest17.Fragment1
        return textView;
    }
}
