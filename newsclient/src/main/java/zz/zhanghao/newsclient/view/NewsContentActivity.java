package zz.zhanghao.newsclient.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import zz.zhanghao.newsclient.R;

/**
 * Created by ${张昊} on 2016/8/21 0021.
 */
public class NewsContentActivity extends Activity
{
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

    @ViewInject(R.id.wv_news_detail)
    private WebView wv_news_detail;

    @ViewInject(R.id.loading_view)
    private View loading_view;

    private boolean isTextSizeBig=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initView();
        initData();
    }

    private void initView()
    {
        ViewUtils.inject(this);
        //左侧按钮
        btn_left.setVisibility(View.GONE);
        imgbtn_left.setVisibility(View.VISIBLE);
        imgbtn_left.setImageResource(R.drawable.back);
        imgbtn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //中间
        txt_title.setVisibility(View.GONE);
        imgbtn_title.setVisibility(View.GONE);
        //右侧
        imgbtn_right.setVisibility(View.VISIBLE);
        imgbtn_right.setImageResource(R.drawable.icon_textsize);
        imgbtn_right2.setVisibility(View.VISIBLE);
        imgbtn_right2.setImageResource(R.drawable.icon_share);
        //设置字体大小
        imgbtn_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //可以设置字体大小
                WebSettings webs=wv_news_detail.getSettings();
                if(!isTextSizeBig)
                {
                    webs.setTextSize(WebSettings.TextSize.LARGEST);
                    isTextSizeBig=true;
                }
                else
                {
                    webs.setTextSize(WebSettings.TextSize.NORMAL);
                }
            }
        });

    }
    private void initData()
    {
        String webUri=getIntent().getStringExtra("webUri");
        wv_news_detail.loadUrl(webUri);
        //加一个完成的监听
        wv_news_detail.setWebViewClient(new WebViewClient(){
            //获取网页开始监听
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                loading_view.setVisibility(View.VISIBLE);
            }

            //获取网页完成监听
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                loading_view.setVisibility(View.GONE);
            }
        });
    }

}
