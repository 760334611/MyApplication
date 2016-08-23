package cn.itcast.zz.zhbj_as.bean;

import java.util.List;

/**
 * 新闻子界面对应的Javabean
 * @author wangdh
 *
 */
public class NewsItemBean {
    public int retcode;
    public NewsItemData data;
    public class NewsItemData{
        public String countcommenturl;
        public String more;//**第二页数据url
        public String title;//标题
        //**新闻列表数据
        public List<News> news;
        public class News{
            public String comment;//是否有评论
            public String commentlist;//评论列表
            public String commenturl;
            public String listimage;//** 列表新闻图片url
            public String pubdate;//** 发布日期 
            public String title;//** 新闻标题
            public String type;
            public String url;//** 详情url
            public int id;//** id  (唯一标识)
        }
        //专题数据
        public List<Topic> topic;
        public class Topic{
           public String description; //描述
           public String listimage; //列表图片url
           public String title; //标题
           public String url; //数据url
           public int id;//id
           public int sort;//排序
        }
        //** 顶部轮播图数据
        public List<TopNews> topnews;
        public class TopNews{
            public String comment;//是否有评论
            public String commentlist;//评论列表
            public String commenturl;
            public int id;//** id
            public String pubdate;//** 发布日期 
            public String title;//** 轮播图标题
            public String topimage;//** 顶部轮播图图url
            public String type;
            public String url;//** 详情url
            
        }
        
        
    }
}
