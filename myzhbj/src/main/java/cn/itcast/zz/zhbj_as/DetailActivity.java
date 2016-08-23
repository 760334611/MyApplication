package cn.itcast.zz.zhbj_as;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
/**
 * 详情界面
 * @author wangdh
 *
 */
public class DetailActivity extends Activity {
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
    
    //webview  进度条
    @ViewInject(R.id.wv_news_detail)
    WebView wv_news_detail;
    @ViewInject(R.id.loading_view)
    View loading_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);
        initView();
        initTitleView();
        initData();
    }
    private void initTitleView() {
        //显示左边一个,右边两个
        imgbtn_left.setVisibility(View.VISIBLE);
        imgbtn_left.setImageResource(R.drawable.back);//返回
        imgbtn_left.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v) {
                finish();
                
            }
        });
        imgbtn_right.setVisibility(View.VISIBLE);
        imgbtn_right.setImageResource(R.drawable.icon_textsize);//字体大小
        imgbtn_right.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), 
                        "字体大小调整", Toast.LENGTH_SHORT).show();
                //通过webview调整字体大小
                WebSettings settings = wv_news_detail.getSettings();//webview的设置类
                settings.setTextSize(TextSize.LARGEST);
                
            }
        });
        imgbtn_right2.setVisibility(View.VISIBLE);
        imgbtn_right2.setImageResource(R.drawable.icon_share);//分享
        imgbtn_right2.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), 
                        "分享", Toast.LENGTH_SHORT).show();
                
            }
        });
        //不显示
        btn_left.setVisibility(View.GONE);
        txt_title.setVisibility(View.GONE);
        imgbtn_title.setVisibility(View.GONE);
        
    }
    public void initView(){
        
    }
    public void initData(){
        String url = getIntent().getStringExtra("detailUrl");
        //展示具体界面
        wv_news_detail.loadUrl(url);
        //隐藏进度条(界面展示完之后隐藏)(WebViewClient : webview的监听)
        wv_news_detail.setWebViewClient(new WebViewClient(){
            /**
                                    界面开始打开
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            /**
             * 界面打开完毕
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loading_view.setVisibility(View.GONE);
            }
            
        });
        
    }
    
}
