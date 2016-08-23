package cn.itcast.zz.zhbj_as.bean;

import java.util.List;

/**
 * 组图对应的Javabean
 * @author wangdh
 *
 */
public class ArrPicBean {
    public ArrPicData data;
    public class ArrPicData{
        //组图的数据
        public List<News> news;
        public class News{
            //图片url
            public String listimage;
            //标题
            public String title;
        }
    }
}
