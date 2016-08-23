package zz.zhanghao.newsclient.subpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import zz.zhanghao.newsclient.R;
import zz.zhanghao.newsclient.adapter.MyBaseAdapter;
import zz.zhanghao.newsclient.bean.GroupIconBean;
import zz.zhanghao.newsclient.bean.NewsCenterBean;
import zz.zhanghao.newsclient.page.BasePage;
import zz.zhanghao.newsclient.utils.NetUrl;

/**
 * Created by ${张昊} on 2016/8/17 0017.
 */
public class SubGroupIconPage extends BasePage
{

    private View view;
    @ViewInject(R.id.lv_arr_pic)
    private ListView lv_arr_pic;
    @ViewInject(R.id.gv_arr_pic)
    private GridView gv_arr_pic;
    private String loadJson;
    private GroupIconBean groupIconBean;
    private List<GroupIconBean.GroupIconData.News> news;
    private BitmapUtils bit;
    private boolean isShowListView=true;

    public SubGroupIconPage(Context context, NewsCenterBean.NewsCenterModeBean newsCenterModeBean)
    {
        super(context);
    }

    @Override
    public View initView()
    {
        view =View.inflate(context, R.layout.sub_pager_arr_pic,null);
        ViewUtils.inject(this,view);
        bit = new BitmapUtils(context);
        return view;
    }

    @Override
    public void initData()
    {
        loadingJson();

    }

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
        utils.send(HttpMethod.GET, NetUrl.PHOTOS_URL, null, new MyRequestCallBack());

    }

    /**
     * 解析Json
     * @param loadJson
     */
    private void parseJson(String loadJson)
    {
        // Log.i("NewsCenterPager","请求成功:"+loadJson);
        Gson gson=new Gson();
        groupIconBean = gson.fromJson(loadJson, GroupIconBean.class);
        news = groupIconBean.data.news;
        //Log.i("NewsCenterPager","请求成功newsCenterBean:"+ newsCenterBean.data.get(0).children.get(0).title);
    }

    /**
     * 获得改变显示模式的按钮对象
     * @param imgbtn_right
     */
    public void setChangeButton(final ImageButton imgbtn_right)
    {
        imgbtn_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isShowListView)
                {
                    lv_arr_pic.setVisibility(View.GONE);
                    gv_arr_pic.setVisibility(View.VISIBLE);
                    imgbtn_right.setImageResource(R.drawable.icon_pic_list_type);
                    isShowListView=false;
                }
                else
                {
                    lv_arr_pic.setVisibility(View.VISIBLE);
                    gv_arr_pic.setVisibility(View.GONE);
                    imgbtn_right.setImageResource(R.drawable.icon_pic_grid_type);
                    isShowListView=true;
                }
            }
        });
    }

    /**
     * 请求结果的回调
     */
    public class MyRequestCallBack extends RequestCallBack
    {

        @Override
        public void onSuccess(ResponseInfo responseInfo)
        {
            loadJson=  (String) responseInfo.result;
            parseJson(loadJson);
            MyAdapter adapter= new MyAdapter(news);
            lv_arr_pic.setAdapter(adapter);
            gv_arr_pic.setAdapter(adapter);


        }

        @Override
        public void onFailure(HttpException error, String msg)
        {

        }

    }
    public class MyAdapter extends MyBaseAdapter
    {

        public MyAdapter(List list)
        {
            super(list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            MyGroupIconHolder holder=null;
            if(convertView==null)
            {
                holder=new MyGroupIconHolder();
                convertView=View.inflate(context,R.layout.listview_item_arr_pic,null);
                holder.iv_arr_pic_item= (ImageView) convertView.findViewById(R.id.iv_arr_pic_item);
                holder.tv_arr_pic_item= (TextView) convertView.findViewById(R.id.tv_arr_pic_item);
                convertView.setTag(holder);
            }
            else
            {
                 holder= (MyGroupIconHolder) convertView.getTag();
            }
            bit.display( holder.iv_arr_pic_item,news.get(position).listimage);
            holder.tv_arr_pic_item.setText(news.get(position).title);
            return convertView;
        }
    }

    class MyGroupIconHolder {
        ImageView iv_arr_pic_item;
        TextView tv_arr_pic_item;
    }
}
