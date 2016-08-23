package zz.zhanghao.newsclient.bean;

import java.util.List;

/**
 * Created by ${张昊} on 2016/8/21 0021.
 */
public class GroupIconBean
{
    public GroupIconData data;

    public class GroupIconData
    {
        public List<News> news;

        public class News
        {
            public String listimage;//图片的url
            public String title;//标题

        }
    }
}
