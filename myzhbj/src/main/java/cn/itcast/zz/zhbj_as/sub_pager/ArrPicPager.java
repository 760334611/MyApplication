package cn.itcast.zz.zhbj_as.sub_pager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.itcast.zz.zhbj_as.R;
import cn.itcast.zz.zhbj_as.adapter.DefaultBaseAdapter;
import cn.itcast.zz.zhbj_as.bean.ArrPicBean;
import cn.itcast.zz.zhbj_as.bean.NewsCenterBean.NewsCenterData;
import cn.itcast.zz.zhbj_as.pager.BasePager;
import cn.itcast.zz.zhbj_as.utils.MyBitmapUtils;
import cn.itcast.zz.zhbj_as.utils.NetUrl;
import cn.itcast.zz.zhbj_as.utils.SpUtils;
import  cn.itcast.zz.zhbj_as.bean.ArrPicBean.ArrPicData.News;
/**
 * 二级菜单-组图界面
 * @author wangdh
 *
 */
public class ArrPicPager extends BasePager {
    @ViewInject(R.id.lv_arr_pic)
    private ListView lv_arr_pic;
    
    @ViewInject(R.id.gv_arr_pic)
    private GridView gv_arr_pic;
    
    private BitmapUtils bitmapUtils;
    private MyBitmapUtils myBitmapUtils;
    
    public ArrPicPager(Context context, NewsCenterData newsCenterData) {
        super(context);
        bitmapUtils = new BitmapUtils(context);
        myBitmapUtils = new MyBitmapUtils(context);
    }
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.sub_pager_arr_pic, null);
        ViewUtils.inject(this,view);
        return view;
    }
    
    @Override
    public void initData() {
        //请求数据
        //1.获取缓存
        String cache = SpUtils.getSp(context, NetUrl.PHOTOS_URL);
        if(!TextUtils.isEmpty(cache)){
//            parseJson(cache);
        }
        //2.请求网络数据
        sendRequest(NetUrl.PHOTOS_URL, new RequestCallBack<String>()
        {
            
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String resultJson = responseInfo.result;
                //3. 缓存数据
//                SpUtils.setSp(context, NetUrl.PHOTOS_URL, resultJson);
                parseJson(resultJson);
            }
            
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Log.i("ArrPicPager", "请求失败:"+msg);
            }
        });

    }
    protected void parseJson(String resultJson) {
        //Gson
        Gson gson = new Gson();
        ArrPicBean arrPicBean = gson.fromJson(resultJson, ArrPicBean.class);
        //打印解析结果
        Log.i("ArrPicPager", "组图界面,解析成功:"+arrPicBean.data.news.get(0).title);
        //设置适配器
        MyArrPicAdapter adapter = new MyArrPicAdapter(arrPicBean.data.news);
        lv_arr_pic.setAdapter(adapter);
        //lv 驴 
        gv_arr_pic.setAdapter(adapter);
    }
    class MyArrPicAdapter extends DefaultBaseAdapter<News> {

        public MyArrPicAdapter(List<News> datas) {
            super(datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyArrPicHolder holder = null;
            if(convertView==null){
                convertView = View.inflate(context, R.layout.listview_item_arr_pic, null);
                holder = new MyArrPicHolder();
                holder.iv_arr_pic_item = (ImageView) convertView.findViewById(R.id.iv_arr_pic_item);
                holder.tv_arr_pic_item = (TextView) convertView.findViewById(R.id.tv_arr_pic_item);
                convertView.setTag(holder);
            }else{
                holder = (MyArrPicHolder) convertView.getTag();
            }
            //填充数据
            News news = datas.get(position);
            //标题
            holder.tv_arr_pic_item.setText(news.title);
            //图片
//            bitmapUtils.display(holder.iv_arr_pic_item, news.listimage);
            holder.iv_arr_pic_item.setTag(position);
            myBitmapUtils.display(holder.iv_arr_pic_item, news.listimage);
            return convertView;
        }
        
    }
    class MyArrPicHolder {
        ImageView iv_arr_pic_item;
        TextView tv_arr_pic_item;
    }
    private boolean isListType = true;//是否是listview类型:默认是
    /**
     * 传递顶部右侧按钮
     * @param imgbtn_right
     */
    public void passTitleBtn(final ImageButton imgbtn_right) {
        imgbtn_right.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v) {
                if(isListType){
                    //listview--gridView
                    imgbtn_right.setImageResource(R.drawable.icon_pic_grid_type);
                    gv_arr_pic.setVisibility(View.VISIBLE);
                    lv_arr_pic.setVisibility(View.GONE);
                }else{
                    imgbtn_right.setImageResource(R.drawable.icon_pic_list_type);
                    gv_arr_pic.setVisibility(View.GONE);
                    lv_arr_pic.setVisibility(View.VISIBLE);
                }
                isListType = !isListType;
            }
        });
        
    }
    
}
