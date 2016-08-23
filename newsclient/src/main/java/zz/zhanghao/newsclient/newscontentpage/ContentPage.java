package zz.zhanghao.newsclient.newscontentpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import zz.zhanghao.newsclient.R;
import zz.zhanghao.newsclient.bean.NewsCenterBean.NewsCenterModeBean.NewsCenterAddressBean;
import zz.zhanghao.newsclient.bean.NewsCenterItemBean;
import zz.zhanghao.newsclient.bean.NewsCenterItemBean.NewsCenterItemData.News;
import zz.zhanghao.newsclient.myview.MyNewsItemView;
import zz.zhanghao.newsclient.myview.MyRefreshListView;
import zz.zhanghao.newsclient.page.BasePage;
import zz.zhanghao.newsclient.utils.NetUrl;
import zz.zhanghao.newsclient.view.NewsContentActivity;

;

/**
 * Created by ${张昊} on 2016/8/17 0017.
 */
public class ContentPage extends BasePage
{
    private  String title;
    private  String loadJson;
    private View view;
    @ViewInject(R.id.lv_news_item_pager)
    private MyRefreshListView listView;
    //头布局的空间
    //存放轮播图Viewpager的位置
    @ViewInject(R.id.ll_top_news_viewpager)
    private LinearLayout ll_top_news_viewpager;

    //头布局的标题
    @ViewInject(R.id.tv_top_news_title)
    private TextView tv_top_news_title;
    //存放指示点的位置
    @ViewInject(R.id.ll_dots)
    private LinearLayout ll_dots;

    private ArrayList<String> imgeNames=new ArrayList<String>();
    private ArrayList<String> imgeUris=new ArrayList<String>();
    private ArrayList<ImageView> imgeTop=new ArrayList<ImageView>();

    private NewsCenterAddressBean newsCenterAddressBean;
    private NewsCenterItemBean newsCenterItemBean;
    private BitmapUtils bitmapUtils;
    private String oneUri;
    private MyAdapter adapter;
    private ArrayList<News> news;
    private SharedPreferences sp;


    public ContentPage(Context context, NewsCenterAddressBean newsCenterAddressBean)
    {
        super(context);
        this.newsCenterAddressBean=newsCenterAddressBean;
    }

    @Override
    public View initView()
    {
        sp = context.getSharedPreferences("isItemClick",0);
        listView = (MyRefreshListView) View.inflate(context, R.layout.news_item_pager,null);
        ViewUtils.inject(this,listView);
        View rollView= View.inflate(context,R.layout.layout_roll_view,null);
        ViewUtils.inject(this,rollView);
        listView.addViewContent(rollView);
        bitmapUtils = new BitmapUtils(context);
        return listView;
    }

    @Override
    public void initData()
    {
        title = newsCenterAddressBean.title;
        oneUri=NetUrl.BASE_URL+newsCenterAddressBean.url;
        loadingJson(oneUri);
        //设置条目点击的监听
        listView.setOnItemClickListener(new MyItemClick());
        //设置上下拉的监听
        listView.setOnRefreshStateListener(new MyRefresgh());
    }

    public class MyAdapter extends BaseAdapter
    {


        @Override
        public int getCount()
        {
            return news.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view=null;
            MyNewsItemHolder holder =new MyNewsItemHolder();
            if(view == null){
                view = View.inflate(context,R.layout.listview_item_news,null);
                holder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
                holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
                holder.tv_pub_date = (TextView) view.findViewById(R.id.tv_pub_date);
                view.setTag(holder);
            }else{
                holder = (MyNewsItemHolder) view.getTag();
            }
            //填充数据
            News newss =news.get(position);
            holder.tv_title.setText(newss.title);
            holder.tv_pub_date.setText(newss.pubdate);
            //图片
            bitmapUtils.display(holder.iv_img,newss.listimage);
            String itemClick=sp.getString("isClick","");
            int id=news.get(position).id;
            if(itemClick.contains(String.valueOf(id)))
            {
                holder.tv_title.setTextColor(Color.RED);
            }
            else
            {
                holder.tv_title.setTextColor(Color.BLACK);
            }
            return view;
        }
    }
    class MyNewsItemHolder {
        ImageView iv_img;
        TextView tv_title,tv_pub_date;
    }

    /**
     * 设置显示头的图片名称和路径的集合
     */
    private void settingList()
    {
        imgeNames.clear();
        imgeUris.clear();
        for(int i=0;i<newsCenterItemBean.data.topnews.size();i++)
        {
            imgeNames.add(newsCenterItemBean.data.topnews.get(i).title);
            imgeUris.add(newsCenterItemBean.data.topnews.get(i).topimage);
        }
        //添加显示点
        addShowDots();
    }

    private void addShowDots()
    {
        imgeTop.clear();
        for(int i=0;i<imgeUris.size();i++)
        {
            ImageView iv=new ImageView(context);
            if(i==0){
                iv.setImageResource(R.drawable.dot_focus);//默认第0个是红色
            }else{
                iv.setImageResource(R.drawable.dot_normal);
            }
            //参数是宽高包裹内容
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin=10;
            ll_dots.addView(iv,params);
            imgeTop.add(iv);
        }
    }


    /**
     * 缓存加载Json
     */
    public void loadingJson(String oneUri)
    {
        //一获取缓存
        //loadJson=sp.getString("iconJson",null);
        //解析缓存的Json
       /* if(!TextUtils.isEmpty(loadJson))
        {
            parseJson(loadJson);
        }*/
        //获得json
        HttpUtils utils=new HttpUtils();

        //发送请求
        utils.send(HttpMethod.GET, oneUri, null, new MyRequestCallBack());

    }

    /**
     * 解析Json
     * @param loadJson
     */
    private void parseJson(String loadJson)
    {
        // Log.i("NewsCenterPager","请求成功:"+loadJson);
        Gson gson=new Gson();
        newsCenterItemBean = gson.fromJson(loadJson, NewsCenterItemBean.class);
        Log.i("NewsCenterPager","请求成功newsCenterItemBean:"+ newsCenterItemBean.data.topnews.get(0).title);

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
            //***显示头布局,必须设置适配器
            if(adapter==null)
            {
                news=  newsCenterItemBean.data.news;
                adapter = new MyAdapter();
                //设置显示头的图片名称和路径的集合
                settingList();
                addTopView();
                listView.setAdapter(adapter);
                tv_top_news_title.setText(imgeNames.get(0));
            }

            // 缓存数据
            // sp.edit().putString("iconJson",loadJson).commit();
            if(listView.isDownRefresh)
            {
                news=  newsCenterItemBean.data.news;
                //设置显示头的图片名称和路径的集合
               // settingList();
                adapter.notifyDataSetChanged();
                tv_top_news_title.setText(imgeNames.get(0));
            }
            else
            {
                news.addAll(newsCenterItemBean.data.news);
                adapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onFailure(HttpException error, String msg)
        {

        }
    }

    /**
     * 添加top的自定义ViewPage
     */
    private void addTopView()
    {
        MyNewsItemView newsViewTop=new MyNewsItemView(context);
        newsViewTop.setImageData(imgeUris);
        //设置top图片改变监听
        newsViewTop.addOnPageChangeListener(new MyPageChange());
        //设置top图片的点击监听
        newsViewTop.setOnItemClickListener(new MyNewsItemView.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                Toast.makeText(context,imgeNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        ll_top_news_viewpager.addView(newsViewTop);
    }

    public class MyPageChange implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            tv_top_news_title.setText(imgeNames.get(position                                                                         ));
            for(int i=0;i<imgeTop.size();i++)
            {
                ImageView iv=imgeTop.get(i);
                if(i==position){
                    iv.setImageResource(R.drawable.dot_focus);//默认第0个是红色
                }else{
                    iv.setImageResource(R.drawable.dot_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

    /**
     * 条目点击的回调
     */
    public class MyItemClick implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //需要得到减去头条目数的纯listview的下标
            String itemClick=sp.getString("isClick","");
            position=position-listView.getHeaderViewsCount();
            int _id=news.get(position).id;
            itemClick=itemClick+String.valueOf(_id)+"#";
            adapter.notifyDataSetChanged();
            sp.edit().putString("isClick",itemClick).commit();
            Intent intent=new Intent(context,NewsContentActivity.class);
            intent.putExtra("webUri",news.get(position).url);
            context.startActivity(intent);
        }
    }

    /**
     * 上下拉刷新的回调
     */
    public class MyRefresgh implements MyRefreshListView.OnRefreshStateListener
    {

        @Override
        public void onDownRefresh()
        {
            loadingJson(oneUri);
            listView.RefreshFinish();
            Log.i("下拉成功","下拉成功");
        }

        @Override
        public void onUpRefresh()
        {
            if(TextUtils.isEmpty(newsCenterItemBean.data.more))
            {
                Toast.makeText(context, "暂时不需要更新", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String moreUri=NetUrl.BASE_URL+newsCenterItemBean.data.more;
                loadingJson(moreUri);
                Log.i("上拉成功","上拉成功");
            }
            listView.RefreshFinish();

        }
    }
}
