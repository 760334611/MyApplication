package cn.itcast.zz.zhbj_as.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SpUtils {
    /**
     * 根据key获取value
     * @param context
     * @param url: url
     * @return
     */
    public static String getSp(Context context, String url) {
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        return sp.getString(url, "");
    }
    /**
     * 保存sp
     * @param context
     * @param url
     */
    public static void setSp(Context context, String url,String value) {
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        sp.edit().putString(url, value).commit();
        
    }
    /**
     * 
     * @param context
     * @param id : newsId : id1#id2
     */
    public static void saveReadNewsId(Context context, String id) {
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        String value ;
        String cache = sp.getString("readNewsIds", "");
        if(TextUtils.isEmpty(cache)){
              value = id;
        }else{
            value = cache+"#"+id;
        }
        sp.edit().putString("readNewsIds", value).commit();
    }
    /**
     * 获取已读新闻列表id
     * @param context
     * @return
     */
    public static List<Integer> getReadNewsId(Context context) {
        List<Integer> readNewsIdList = new ArrayList<Integer>();
        SharedPreferences sp = context.getSharedPreferences("cache", context.MODE_PRIVATE);
        String readNewsIds = sp.getString("readNewsIds", "");//id1#id2
        if(TextUtils.isEmpty(readNewsIds)){
            return readNewsIdList;
        }
        String[] split = readNewsIds.split("#");
        for (int i = 0; i < split.length; i++) {
            readNewsIdList.add(Integer.parseInt(split[i]));
        }
        return readNewsIdList;
    }
    
}
