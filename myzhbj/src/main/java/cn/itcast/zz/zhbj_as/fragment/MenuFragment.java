package cn.itcast.zz.zhbj_as.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.itcast.zz.zhbj_as.MainActivity;
import cn.itcast.zz.zhbj_as.R;
import cn.itcast.zz.zhbj_as.adapter.DefaultBaseAdapter;
import cn.itcast.zz.zhbj_as.bean.NewsCenterBean.NewsCenterData;
import cn.itcast.zz.zhbj_as.pager.NewsCenterPager;

/**
 * 菜单Fragment
 * @author wangdh
 *
 */
public class MenuFragment extends BaseFragment {
    
    private final class OnMyItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //一.
//            1. 添加listview条目点击监听
//            2. 记录当前点击位置
//            3. 刷新adapter (getView)
//            4. getView()处理当前点击位置子条目的样式
              clickPosition = position;
              adapter.notifyDataSetChanged();
              
              //二. 关闭菜单
              MainActivity mainActivity = (MainActivity) mContext;
              mainActivity.toggle();
              
              //三. 切换新闻中心内容区域FrameLayout
              /**
               * MenuFragment如何调用NewsCenterPager?

                    1. MenuFragment找到MainActivity?
                    
                            context
                    2. MainActivity找到ContentFragment?
                    
                            tag
                    3. ContentFragment找到NewsCenterPager?
                    
                                                        一级菜单集合,索引为1的就是Newscenterpager
               */
              ContentFragment contentFragment = mainActivity.getContentFragment();
              NewsCenterPager newsCenterPager= (NewsCenterPager) contentFragment.getPagers().get(1);
              newsCenterPager.switchSubMenu(position);
            
        }
    }
    
    //二级菜单数据
    private List<NewsCenterData> data ;
    
    @ViewInject(R.id.lv_menu_news_center)
    private ListView lv_menu_news_center;
    
    private int clickPosition;//点击的位置,默认0

    private MyBaseAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_menu, null);
        ViewUtils.inject(this,view);
        return view;
    }
    
    @Override
    public void initData() {
       
    }
    /**
     * 初始化二级菜单数据
     * @param data
     */
    public void initSubMenu(List<NewsCenterData> data) {
        this.data = data;
        Log.i("MenuFragment", "获取传递的数据:"+data.toString());
        //在这设置适配器
        adapter = new MyBaseAdapter(data);
        lv_menu_news_center.setAdapter(adapter);
        lv_menu_news_center.setOnItemClickListener(new OnMyItemClickListener());
    }
   
    class MyBaseAdapter extends DefaultBaseAdapter<NewsCenterData> {

        public MyBaseAdapter(List<NewsCenterData> datas) {
            super(datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder myHolder = null; 
            if(convertView==null){
                convertView = View.inflate(mContext, R.layout.listview_item_menu, null);
                myHolder = new MyHolder();
                myHolder.iv_menu_item = (ImageView) convertView.findViewById(R.id.iv_menu_item);
                myHolder.tv_menu_item = (TextView) convertView.findViewById(R.id.tv_menu_item);
                convertView.setTag(myHolder);
            }else{
                myHolder = (MyHolder) convertView.getTag();
            }
            //填充内容
            NewsCenterData newsCenterData = data.get(position);
            myHolder.tv_menu_item.setText(newsCenterData.title);
            //处理当前点击位置子条目的样式
            if(position == clickPosition){
                myHolder.iv_menu_item.setBackgroundResource(R.drawable.menu_arr_select);
                myHolder.tv_menu_item.setTextColor(Color.RED);
                //convertView 就是背景
                convertView.setBackgroundResource(R.drawable.menu_item_bg_select);
            }else{
                myHolder.iv_menu_item.setBackgroundResource(R.drawable.menu_arr_normal);
                myHolder.tv_menu_item.setTextColor(Color.WHITE);
                //convertView 就是背景
                convertView.setBackgroundDrawable(null);
//                convertView.setBackground(null);
            }
            return convertView;
        }
    }
    class MyHolder {
        ImageView iv_menu_item; //三角图片
        TextView tv_menu_item; //二级菜单内容
    }
    
}
