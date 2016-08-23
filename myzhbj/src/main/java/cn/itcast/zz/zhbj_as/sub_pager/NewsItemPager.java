package cn.itcast.zz.zhbj_as.sub_pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.zz.zhbj_as.DetailActivity;
import cn.itcast.zz.zhbj_as.R;
import cn.itcast.zz.zhbj_as.adapter.DefaultBaseAdapter;
import cn.itcast.zz.zhbj_as.bean.NewsCenterBean.NewsCenterData.Children;
import cn.itcast.zz.zhbj_as.bean.NewsItemBean;
import cn.itcast.zz.zhbj_as.bean.NewsItemBean.NewsItemData.News;
import cn.itcast.zz.zhbj_as.bean.NewsItemBean.NewsItemData.TopNews;
import cn.itcast.zz.zhbj_as.pager.BasePager;
import cn.itcast.zz.zhbj_as.utils.NetUrl;
import cn.itcast.zz.zhbj_as.utils.SpUtils;
import cn.itcast.zz.zhbj_as.view.DownRefreshListView;
import cn.itcast.zz.zhbj_as.view.RollViewPager;
/**
 * 新闻子界面-不同新闻类型界面
 * @author wangdh
 *
 */
public class NewsItemPager extends BasePager {
    private final class OnMyLitViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            position = position - lv_news_item_pager.getHeaderViewsCount();
            //跳转到详情界面
            Intent intent = new Intent(context, DetailActivity.class);
            //传递详情界面的url
            String detailUrl = newsDatas.get(position).url;
            intent.putExtra("detailUrl", detailUrl);
            context.startActivity(intent);
            //记录当前点击新闻id
            SpUtils.saveReadNewsId(context, String.valueOf(newsDatas.get(position).id));
            adapter.notifyDataSetChanged();
        }
    }
    private final class OnMyRefreshListener implements DownRefreshListView.OnRefreshListener {
        /**
         * 下拉刷新  (获取最新数据)
         */
        @Override
        public void onRefresh() {
            requestData(NetUrl.BASE_URL+children.url);
        }
        /**
         * 上拉加载
         */
        @Override
        public void onLoadMore() {
            //发起请求,加载下一页
            String moreUrl = newsItemBean.data.more;// /10007/list_2.json
            if(!TextUtils.isEmpty(moreUrl)){
                moreUrl=NetUrl.BASE_URL+moreUrl;
                requestData(moreUrl);
            }else{
                Toast.makeText(context, 
                        "已经没有更多数据", Toast.LENGTH_SHORT).show();
                //执行刷新完成,隐藏加载更多脚布局
                lv_news_item_pager.refreshFinish();
            }
            
        }
    }
    private final class SimpleOnMyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            //标题切换
            tv_top_news_title.setText(topTitles.get(position));
            //指示点切换
            for (int i = 0; i < dotList.size(); i++) {
                //当前postition应该为红色
                if(i==position){
                    dotList.get(i).setImageResource(R.drawable.dot_focus);
                }else{
                    //非当前position应该为白色
                    dotList.get(i).setImageResource(R.drawable.dot_normal);
                }
            }
            
        }
    }
    //新闻子界面数据
    private Children children;
    @ViewInject(R.id.lv_news_item_pager)
    private DownRefreshListView lv_news_item_pager;
    
    //头布局控件
    @ViewInject(R.id.ll_top_news_viewpager)
    private LinearLayout ll_top_news_viewpager;//存放自定义viewpager(轮播图)
    @ViewInject(R.id.tv_top_news_title)
    private TextView tv_top_news_title;//轮播图标题
    @ViewInject(R.id.ll_dots)
    private LinearLayout ll_dots;//存放指示点
    
    //轮播图数据源(顶部图片集合)
    private List<String> topImageUrls = new ArrayList<String>();
    //顶部标题集合
    private List<String> topTitles = new ArrayList<String>();
  //指示点集合
    private List<ImageView> dotList = new ArrayList<ImageView>();
    private RollViewPager rollViewPager;
    
    private BitmapUtils bitmapUtils;
    private NewsItemBean newsItemBean;
    private MyNewsItemAdapter adapter;
    private List<News> newsDatas;
    
    public NewsItemPager(Context context, Children children) {
        super(context);
        this.children = children;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.news_item_pager, null);
        ViewUtils.inject(this, view);
        //listview添加头布局
        View rollView = View.inflate(context, R.layout.layout_roll_view, null);
        ViewUtils.inject(this, rollView);
//        lv_news_item_pager.addHeaderView(hearderView);
        lv_news_item_pager.addRollView(rollView);//挂载到刷新头布局下
        //只添加头布局,不会显示.必须设置适配器
        
//        String[] objects = {"aaa","bbb","ccc","eee","fff","asdfasldkfj"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
//                android.R.id.text1, objects);
//        lv_news_item_pager.setAdapter(adapter);
        
        //添加自定义的Viewpager(轮播图)
        rollViewPager = new RollViewPager(context);
        ll_top_news_viewpager.addView(rollViewPager);
        rollViewPager.setOnPageChangeListener(new SimpleOnMyPageChangeListener());
        //rollViewPager 添加条目点击监听
        rollViewPager.setOnItemClickListener(new RollViewPager.OnItemClickListener()
        {
            
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context, 
                        topTitles.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        //添加listview的自定义刷新监听
        lv_news_item_pager.setOnRefreshListener(new OnMyRefreshListener());
        //添加lv的条目点击事件
        lv_news_item_pager.setOnItemClickListener(new OnMyLitViewItemClickListener());
        return view;
    }
    
    @Override
    public void initData() {
        //1.获取缓存,解析缓存
        String cacheJson = SpUtils.getSp(context,NetUrl.BASE_URL+children.url);
        if(!TextUtils.isEmpty(cacheJson)){
            parseJson(cacheJson);
        }
        //2.请求网络数据
        requestData(NetUrl.BASE_URL+children.url);
        
    }
    /**
     * 请求数据
     */
    private void requestData(String url) {
        sendRequest(url, new RequestCallBack<String>()
        {
            
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //获取结果json
                String resultJson = responseInfo.result;
                Log.i("NewsItemPager", "请求成功:"+resultJson);
                //3. 将缓存存储起来
                SpUtils.setSp(context, NetUrl.BASE_URL+children.url, resultJson);
                parseJson(resultJson);
            }
            
            @Override
            public void onFailure(HttpException error, String msg) {
                  error.printStackTrace();
                  Log.i("NewsItemPager", "请求失败:"+msg);
            }
        });
    }
    /**
     * 解析json
     * @param resultJson
     */
    protected void parseJson(String resultJson) {
        //Gson
        //1. json -- Javabean
        //2. fromJson()
        Gson gson = new Gson();
        newsItemBean = gson.fromJson(resultJson, NewsItemBean.class);
        //打印轮播图标题
        Log.i("NewsItemPager", "解析成功:"+newsItemBean.data.topnews.get(0).title);
        //如果是获取最新数据,再处理轮播图
        if(!lv_news_item_pager.isLoadingMore){
            //封装轮播图需要的数据源
            topImageUrls.clear();
            topTitles.clear();
            List<TopNews> topnewsList = newsItemBean.data.topnews;
            for (int i = 0; i < topnewsList.size(); i++) {
                TopNews topNews = topnewsList.get(i);
                topImageUrls.add(topNews.topimage);
                topTitles.add(topNews.title);
            }
            //将数据传递给rollViewPager ,他自己设置适配器(将代码抽离到自定义控件中)
            rollViewPager.initTopImageUrls(topImageUrls);
            //开启轮播
            rollViewPager.startRoll();
            //处理默认标题,指示点
            tv_top_news_title.setText(topTitles.get(0));
            addPoint();
        }
        //如果是获取最新数据,实例化适配器,设置适配器
        if(!lv_news_item_pager.isLoadingMore){
            //给新闻列表设置适配器
            newsDatas = newsItemBean.data.news;
            adapter = new MyNewsItemAdapter(newsDatas);
            lv_news_item_pager.setAdapter(adapter);
        }else{
            //如果是加载下一页,数据源拼接,适配器刷新
            newsDatas.addAll(newsItemBean.data.news);
            adapter.notifyDataSetChanged();
        }
      //执行刷新完成
        lv_news_item_pager.refreshFinish();
        
    }
    
    /**
     * 添加指示点
     */
    private void addPoint() {
        ll_dots.removeAllViews();//添加指示点之前,先移除
        dotList.clear();
        for (int i = 0; i < topImageUrls.size(); i++) {
            //指示点--imageview
            ImageView imageView = new ImageView(context);
            if(i==0){
                //红色
                imageView.setImageResource(R.drawable.dot_focus);
            }else{
                //白色
                imageView.setImageResource(R.drawable.dot_normal);
            }
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;//px
            ll_dots.addView(imageView,params);
            dotList.add(imageView);
        }
        
    }
    class MyNewsItemAdapter extends DefaultBaseAdapter<News> {

        public MyNewsItemAdapter(List<News> datas) {
            super(datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyNewsHolder newsHolder = null;
            if(convertView==null){
                convertView = View.inflate(context, R.layout.listview_item_news, null);
                newsHolder = new MyNewsHolder();
                newsHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
                newsHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                newsHolder.tv_pub_date = (TextView) convertView.findViewById(R.id.tv_pub_date);
                convertView.setTag(newsHolder);
            }else{
                newsHolder = (MyNewsHolder) convertView.getTag();
            }
            //数据填充
            News news = datas.get(position);
            //标题,发布日期
            newsHolder.tv_title.setText(news.title);
            newsHolder.tv_pub_date.setText(news.pubdate);
            //图片
            bitmapUtils.display(newsHolder.iv_img, news.listimage);
            //改变已读新闻列表的标题为红色
            List<Integer> readNewsId = SpUtils.getReadNewsId(context);
            int newsID = news.id;
            if(readNewsId.contains(newsID)){
                newsHolder.tv_title.setTextColor(Color.RED);
                Log.i("NewsItemPager", "red:"+position);
            }else{
                newsHolder.tv_title.setTextColor(Color.BLACK);
                Log.i("NewsItemPager", "black:"+position);
            }
            return convertView;
        }
        
    }
    class MyNewsHolder {
        ImageView iv_img;
        TextView tv_title,tv_pub_date;
    }
    
}
