package zz.zhanghao.newsclient.bean;

import java.util.List;

/**
 * Created by ${张昊} on 2016/8/16 0016.
 */
public class NewsCenterBean
{
    public Integer retcode;
    public List<Integer> extend;
    public List< NewsCenterModeBean> data;

    public class NewsCenterModeBean
    {
        public String title;
        public Integer id;
        public Integer type;
        public String url;
        public String url1;
        public String dayurl;
        public String excurl;
        public String weekurl;
        public List<NewsCenterAddressBean> children;

        public class NewsCenterAddressBean
        {
            public String title;
            public Integer id;
            public Integer type;
            public String url;
        }
    }
}
