package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity
{

    private boolean isChuange = false;
    private TextView ww;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ww = (TextView) findViewById(R.id.tv);
    }

    public void click(View v)
    {
        isChuange = !isChuange;
        if (isChuange)
        {
            ww.setVisibility(View.VISIBLE);
        } else
        {
            ww.setVisibility(View.GONE);
        }
    }

}
