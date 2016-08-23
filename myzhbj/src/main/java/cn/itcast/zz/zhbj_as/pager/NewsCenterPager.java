package cn.itcast.zz.zhbj_as.pager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.zz.zhbj_as.MainActivity;
import cn.itcast.zz.zhbj_as.R;
import cn.itcast.zz.zhbj_as.bean.NewsCenterBean;
import cn.itcast.zz.zhbj_as.fragment.MenuFragment;
import cn.itcast.zz.zhbj_as.sub_pager.ArrPicPager;
import cn.itcast.zz.zhbj_as.sub_pager.InterActionPager;
import cn.itcast.zz.zhbj_as.sub_pager.NewsPager;
import cn.itcast.zz.zhbj_as.sub_pager.TopicPager;
import cn.itcast.zz.zhbj_as.utils.NetUrl;
import cn.itcast.zz.zhbj_as.utils.SpUtils;

/**
 * 新闻中心对应的自定义pager
 * 
 * @author wangdh
 * 
 */
public class NewsCenterPager extends BasePager {
    //顶部标题栏初始化
    //左侧控件
    @ViewInject(R.id.btn_left)
    private Button btn_left;
    @ViewInject(R.id.imgbtn_left)
    private ImageButton imgbtn_left;
    //中间控件
    @ViewInject(R.id.txt_title)
    private TextView txt_title;
    @ViewInject(R.id.imgbtn_title)
    private ImageButton imgbtn_title;
    //右侧控件
    @ViewInject(R.id.imgbtn_right)
    private ImageButton imgbtn_right;
    @ViewInject(R.id.imgbtn_right2)
    private ImageButton imgbtn_right2;
    
    //二级菜单数据源
    private List<BasePager> subPagers = new ArrayList<BasePager>();
    
    //新闻中心界面-内容区域
    @ViewInject(R.id.fl_news_center)
    private FrameLayout fl_news_center;
    
    /**
     * context就是MainActivity,ContentFragment中实例化时传递的
     * @param context
     */
    public NewsCenterPager(Context context) {
        super(context);
    }
    
    
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.pager_news_center, null);
        ViewUtils.inject(this, view);
        initTitleView();
        return view;
    }
    /**
     * 初始化标题栏
     */
    private void initTitleView() {
        //左侧按钮,中间文字显示
        imgbtn_left.setImageResource(R.drawable.img_menu);
        //左侧按钮,点击,关闭侧滑菜单
        imgbtn_left.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.toggle();
            }
        });
        txt_title.setText("新闻");
        //其他隐藏
        btn_left.setVisibility(View.GONE);
        imgbtn_title.setVisibility(View.GONE);
        imgbtn_right.setVisibility(View.GONE);
        imgbtn_right2.setVisibility(View.GONE);
    }


    @Override
    public void initData() {
        //1.获取缓存,解析缓存
        String cacheJson = SpUtils.getSp(context,NetUrl.NEWS_CENTER_CATEGORIES);
        if(!TextUtils.isEmpty(cacheJson)){
            parseJson(cacheJson);
        }
        //2.请求网络数据
        requestData();
    }

    private void requestData() {
        sendRequest(NetUrl.NEWS_CENTER_CATEGORIES,new RequestCallBack<String>()
        {
            /**
             * 成功  404(找不到资源,请求到服务器之后,才知道找不到资源)
             */
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String resultJson = responseInfo.result;
                Log.i("NewsCenterPager", "请求成功:"+resultJson);
                //3.将数据缓存起来 (json结果存储到sp)
                SpUtils.setSp(context,NetUrl.NEWS_CENTER_CATEGORIES,resultJson);
                parseJson(resultJson);
            }
            /**
             * 失败:没有网络权限,没有网络,服务器异常 ,
             */
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Log.i("NewsCenterPager", "请求失败:"+msg);
            }
        });
    }
    
    /**
     * 解析json
     * @param json
     */
    protected void parseJson(String json) {
        Gson gson = new Gson();
        //1.要解析的json字符串;2.解析类型Javabean
        NewsCenterBean newsCenterBean = gson.fromJson(json, NewsCenterBean.class);
        //打印北京
        Log.i("NewsCenterPager", "解析成功:"+newsCenterBean.data.get(0).children.get(0).title);
        
        /**
         * 2.  NewsCenterPager如何调用MenuFragment对象?
                1. NewsCenterPager如何获取MainActivity?
        
                    context就是MainActivity,ContentFragment中实例化时传递的
                    
                2. MainActivity如何获取MenuFragment?
        
                                                根据tag查找MenuFragment
         */
        MainActivity mainActivity = (MainActivity) context;
        MenuFragment menuFragment = mainActivity.getMenuFragment();
        menuFragment.initSubMenu(newsCenterBean.data);
        //初始化二级菜单集合
        subPagers.clear();
        subPagers.add(new NewsPager(context,newsCenterBean.data.get(0)));
        subPagers.add(new TopicPager(context,newsCenterBean.data.get(1)));
        subPagers.add(new ArrPicPager(context,newsCenterBean.data.get(2)));
        subPagers.add(new InterActionPager(context,newsCenterBean.data.get(3)));
        switchSubMenu(0);//默认切换新闻
    }

    /**
     * 切换二级菜单
     * 替换FrameLayout的子view?
     *  addView,removeView
     * @param position
     */
    public void switchSubMenu(int position) {
        //获取当前点击的view
        BasePager currentPager = subPagers.get(position);
        View currentView = currentPager.initView();//初始化view
        //初始化数据
        currentPager.initData();
        fl_news_center.removeAllViews();
        fl_news_center.addView(currentView);
        imgbtn_right.setVisibility(View.GONE);
        switch (position) {
            case 0:
            Log.i("NewsCenterPager", "点击了新闻");
            txt_title.setText("新闻");
            break;
            case 1:
            Log.i("NewsCenterPager", "点击了专题");
            txt_title.setText("专题");
            break;
            case 2:
            Log.i("NewsCenterPager", "点击了组图");
            txt_title.setText("组图");
            //显示右侧按钮
            imgbtn_right.setVisibility(View.VISIBLE);
            imgbtn_right.setImageResource(R.drawable.icon_pic_list_type);
            //获取组图界面对象
            ArrPicPager arrPicPager= (ArrPicPager) subPagers.get(2);
            arrPicPager.passTitleBtn(imgbtn_right);
            
            break;
            case 3:
            Log.i("NewsCenterPager", "点击了互动");
            txt_title.setText("互动");
            
            break;
            
            default:
            break;
        }
        
    }
}
