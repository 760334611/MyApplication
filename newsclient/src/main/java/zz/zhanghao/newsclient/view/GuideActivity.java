package zz.zhanghao.newsclient.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import zz.zhanghao.newsclient.R;


/**
 * Created by ${张昊} on 2016/8/14 0014.
 */
public class GuideActivity extends Activity
{

    private ViewPager view_pager;
    private Button button;
    private List<Integer> imageResIds = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        imageResIds.add(R.drawable.guide_1);
        imageResIds.add(R.drawable.guide_2);
        imageResIds.add(R.drawable.guide_3);

        view_pager = (ViewPager) findViewById(R.id.view_pager);

        button = (Button) findViewById(R.id.button);

        view_pager.setAdapter(new MyAdapter());

        view_pager.addOnPageChangeListener(new MyPageChange());
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
            if (position == imageResIds.size() - 1)
            {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else
            {
                button.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

    public class MyAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return imageResIds.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView view = new ImageView(GuideActivity.this);
            view.setBackgroundResource(imageResIds.get(position));
            container.addView(view);
            return view;
        }
    }
}
