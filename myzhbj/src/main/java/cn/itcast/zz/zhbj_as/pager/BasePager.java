package cn.itcast.zz.zhbj_as.pager;

import android.content.Context;
import android.view.View;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 自定义pager的基类
 * 
 * @author wangdh
 * 
 */
public abstract class BasePager {
    public Context context;
    
    public BasePager(Context context) {
        this.context = context;
    }
    /**
     * 初始化view
     * @return
     */
    public abstract View initView();
    /**
     * 初始化数据
     */
    public abstract void initData();
    
    /**
     * 自定义的请求
     * @param url:接口地址
     * @param requestCallBack:请求回调
     */
    public void sendRequest(String url,RequestCallBack<String> requestCallBack) {
        /**
         * 网络框架:
         * 1. HttpUrlConnection :  Socket
         * 2. HttpClient
         * 3. AsyncHttpClient
         * 4. xUtils.HttpUtils
         */
        HttpUtils httpUtils = new HttpUtils();
        //异步(主线程中调用,开启子线程)
//      httpUtils.send(method, url, params, callBack)
      //同步 (子线程中调用)
//      httpUtils.sendSync(method, url, params)
      /**
       * 1.HttpMethod : 请求方式:post/get
       *      post:对url长度无限制,安全 (一般用于提交数据)
       *      get:一般用于获取数据
       * 2. String url : 接口的地址 
       * 3. RequestParams :请求参数  (post采用)
       *      http://ip:8080/zhbj/login?username=xx&pwd=xxx
       * 4. RequestcallBack : 请求回调,请求之后会回调
       */
//      RequestParams params = new RequestParams();
//      params.addBodyParameter("username", "xx");
//      params.addBodyParameter("pwd", "xx");
//      NameValuePair nameValuePair = new BasicNameValuePair("username", "xx");
//      params.addBodyParameter(nameValuePair);
        httpUtils.send(HttpMethod.GET, url, null, requestCallBack);
    }
}
