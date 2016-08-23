package cn.itcast.zz.zhbj_as.bean;

import java.util.List;

/**
 * 新闻中心对应的Javabean
 * @author wangdh
 *
 */
public class NewsCenterBean {
    public int retcode;//状态码
    public List<Integer> extend;//额外数据
    public List<NewsCenterData> data;//新闻中心主要数据
    
    public class NewsCenterData{
        public int id;
        public String title;//二级菜单标题
        public int type;
        public String url;
        public String url1;
        public String dayurl;
        public String excurl;
        public String weekurl;
        public List<Children> children;
        public class Children{
            public int id;
            public String title;//新闻类型标题
            public int type;
            public String url;//数据url
        }
        
    }
}
