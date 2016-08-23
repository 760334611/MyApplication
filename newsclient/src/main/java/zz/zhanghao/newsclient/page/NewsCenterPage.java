package zz.zhanghao.newsclient.page;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import zz.zhanghao.newsclient.R;
import zz.zhanghao.newsclient.bean.NewsCenterBean;
import zz.zhanghao.newsclient.subpage.SubGroupIconPage;
import zz.zhanghao.newsclient.subpage.SubInteractPage;
import zz.zhanghao.newsclient.subpage.SubNewsPage;
import zz.zhanghao.newsclient.subpage.SubSpecialPage;
import zz.zhanghao.newsclient.utils.NetUrl;
import zz.zhanghao.newsclient.view.MainActivity;

/**
 * Created by ${张昊} on 2016/8/15 0015.
 */
public class NewsCenterPage extends BasePage
{

    private View view;
    private SharedPreferences sp;
    private String loadJson;

    @ViewInject(R.id.btn_left)
    private Button btn_left;
    @ViewInject(R.id.imgbtn_left)
    private ImageButton imgbtn_left;
    @ViewInject(R.id.txt_title)
    private TextView txt_title;
    @ViewInject(R.id.imgbtn_title)
    private ImageButton imgbtn_title;
    @ViewInject(R.id.imgbtn_right)
    private ImageButton imgbtn_right;
    @ViewInject(R.id.imgbtn_right2)
    private ImageButton imgbtn_right2;
    @ViewInject(R.id.fl_news_center)
    private FrameLayout fl_news_center;
    private ArrayList<BasePage> subPages=new ArrayList<BasePage>();

    private int currentPosition=0;
    private NewsCenterBean newsCenterBean;

    public NewsCenterPage(Context context)
    {
        super(context);
    }

    @Override
    public View initView()
    {
        view =View.inflate(context, R.layout.pager_news_center,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData()
    {

        sp = context.getSharedPreferences("loadData",context.MODE_PRIVATE);
        //初始化页面
        initTitleView();

        //缓存加载Json
        loadingJson();

    }

    /**
     * 缓存加载Json
     */
    public void loadingJson()
    {
        //一获取缓存
        //loadJson=sp.getString("json",null);
        //解析缓存的Json
       /* if(!TextUtils.isEmpty(loadJson))
        {
            parseJson(loadJson);
        }*/
        //获得json
        HttpUtils utils=new HttpUtils();

        //发送请求
        utils.send(HttpMethod.GET, NetUrl.NEWS_CENTER_CATEGORIES, null, new MyRequestCallBack());

    }

    /**
     * 解析Json
     * @param loadJson
     */
    private void parseJson(String loadJson)
    {
        // Log.i("NewsCenterPager","请求成功:"+loadJson);
        Gson gson=new Gson();
        newsCenterBean = gson.fromJson(loadJson, NewsCenterBean.class);
        Log.i("NewsCenterPager","请求成功newsCenterBean:"+ newsCenterBean.data.get(0).children.get(0).title);
    }

    /**
     * 请求结果的回调
     */
    public class MyRequestCallBack extends RequestCallBack
    {

        @Override
        public void onSuccess(ResponseInfo responseInfo)
        {
            loadJson= (String) responseInfo.result;
            parseJson(loadJson);
            // 缓存数据
           // sp.edit().putString("json",loadJson).commit();
            //回调MainActivity的方法，传递数据
            MainActivity mainActivity= (MainActivity) context;
            mainActivity.setNewsContentData(newsCenterBean.data);

            subPages.add(new SubNewsPage(context,newsCenterBean.data.get(0)));
            subPages.add(new SubSpecialPage(context,newsCenterBean.data.get(1)));
            subPages.add(new SubGroupIconPage(context,newsCenterBean.data.get(2)));
            subPages.add(new SubInteractPage(context,newsCenterBean.data.get(3)));
            subPageChange(currentPosition);
        }

        @Override
        public void onFailure(HttpException error, String msg)
        {

        }
    }

    /**
     * 初始化标题控件
     */
    private void initTitleView() {
        //左侧按钮
        btn_left.setVisibility(View.GONE);
        imgbtn_left.setVisibility(View.VISIBLE);
        imgbtn_left.setImageResource(R.drawable.img_menu);
        imgbtn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击,关闭菜单  TODO
                MainActivity mainActivity= (MainActivity) context;
                mainActivity.chengeMenuPage();
            }
        });
        //中间
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText("新闻");
        imgbtn_title.setVisibility(View.GONE);
        //右侧
        imgbtn_right.setVisibility(View.GONE);
        imgbtn_right2.setVisibility(View.GONE);
    }

    public void subPageChange(int position)
    {
        //先移除当前的page，在添加
        fl_news_center.removeAllViews();

        BasePage bp=subPages.get(position);
        fl_news_center.addView(bp.initView());
        bp.initData();
        currentPosition=position;
        imgbtn_right.setVisibility(View.GONE);
        switch (position){
            case  0:
                txt_title.setText("新闻");
                break;
            case  1:
                txt_title.setText("专题");
                break;
            case  2:
                txt_title.setText("组图");
                //右侧按钮
                imgbtn_right.setVisibility(View.VISIBLE);
                imgbtn_right.setImageResource(R.drawable.icon_pic_grid_type);
                SubGroupIconPage icon= (SubGroupIconPage) subPages.get(2);
                //传递改变显示模式的按钮对象
                icon.setChangeButton(imgbtn_right);
                break;
            case  3:
                txt_title.setText("互动");
                break;
        }
    }

}
