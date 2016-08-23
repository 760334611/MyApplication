package zz.zhanghao.newsclient.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import zz.zhanghao.newsclient.R;

/**
 * Created by ${张昊} on 2016/8/14 0014.
 */
public class SplashActivity extends Activity
{
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            boolean isFirst= sp.getBoolean("isFirst",true);
            if(isFirst)
            {
                Intent intent=new Intent(SplashActivity.this,GuideActivity.class);
                startActivity(intent);

            }
            else
            {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            }
            SplashActivity.this.finish();
        }
    };
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences("spName",0);
        handler.sendEmptyMessageDelayed(88,2000);
    }
}
