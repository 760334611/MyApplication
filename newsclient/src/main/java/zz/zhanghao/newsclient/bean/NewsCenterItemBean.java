package zz.zhanghao.newsclient.bean;

import java.util.ArrayList;

/**
 * Created by ${张昊} on 2016/8/16 0016.
 */
public class NewsCenterItemBean
{
    public NewsCenterItemData data;
    public class NewsCenterItemData
    {
        public String more;//下一页的url
        public ArrayList<News> news;
        public ArrayList<Topnews> topnews;

        public class News
        {
            public int id;//新闻的唯一标识
            public String listimage;//新闻图片
            public String pubdate;//新闻发布日期
            public String title;//新闻标题
            public String url;//详情界面url
        }

        public class Topnews
        {
            public String title;//标题
            public String topimage;//图片
        }
    }

}
