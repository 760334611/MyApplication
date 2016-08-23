package cn.itcast.zz.zhbj_as.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.itcast.zz.zhbj_as.MainActivity;

/**
 * 自定义的三级缓存的图片工具
 * @author wangdh
 *
 */
public class MyBitmapUtils {

    private Context context;
    private LruCache<String, Bitmap> mLruCache;
    private File rootFile;
    private ExecutorService executorService;

    public MyBitmapUtils(Context context) {
        this.context = context;
        //1. 内存缓存:实例化LruCache
        /**
         * (1).maxSize:LruCache最大内存空间 , app运行时内存的8/1
         */
        int maxSize= (int) (Runtime.getRuntime().maxMemory()/8);
        mLruCache = new LruCache<String, Bitmap>(maxSize){
            /**
             * (2).mLruCache存储对象的内存大小如何计算
             */
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
        //2. 本地缓存: 图片的路径  (cache)
        rootFile = context.getCacheDir();
        
        //3. 网络缓存  : 开启子线程(线程池)
        executorService = Executors.newFixedThreadPool(3);
        
    }
    /**
     * 显示网络图片
     * @param imageView: 显示的控件
     * @param url:网络图片url
     */
    public void display(ImageView imageView, String url) {
        /**
         * 三级缓存: **
         * 1.内存缓存:hashMap<key,value> key:图片网络url value:图片对象Bitmap
         *                v4.LruCache<key,value>  算法Lru:less recent use  (最少最近使用:less删除recent保存)
         * 2.本地缓存:sd: mnt/sdcard/工程名/cache/image/xxx.png   
         *                file: 图片文件:xxx.png  
         *                图片名称:图片url+md5加密       
         *                md5加密为了防止特殊字符(windows/linux) 
         * 3.网络缓存:
         *        什么是缓存? 不需要联网获取的数据
         *        
         * 图片加载逻辑步骤: (原则: 优先加载速度最快的缓存)
         *  1. 先从内存缓存中获取,如果获取到了,直接显示.如果没有获取到呢?在从本地获取
         *  2. 再从本地中获取,如果获取到了,先加载到内存缓存中,再展示.如果没有获取到,从网络获取.
         *  3. 从网络获取,一般都可以获取到.先缓存到内存中,再缓存到本地中,最后展示.
         *     
         */
        //1. 先从内存缓存中获取
        Bitmap cacheBitmap = mLruCache.get(url);
        if(cacheBitmap!=null){
            Log.i("MyBitmapUtils", "从内存中获取");
            imageView.setImageBitmap(cacheBitmap);
            return;
        }
        //2.再从本地中获取
        String imageName = MD5Encoder.encode(url);//url+md5
        File imageFile = new File(rootFile, imageName);
        //本地缓存是否存在,文件大小
        if(imageFile.exists()&&imageFile.length()!=0){
            //file -- bitmap
            Bitmap decodeFileBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            //缓存到内存
            mLruCache.put(url, decodeFileBitmap);
            //展示
            imageView.setImageBitmap(decodeFileBitmap);
            Log.i("MyBitmapUtils", "从本地缓存中获取");
            return;
        }
        //3.从网络获取 httUrlConnection  线程池
//        new Thread()
//        {
//            public void run() {
//                //请求网络图片
//            }
//        }.start();
//        Handler handler;
//        handler.post(new Runnable()
//        {
//            
//            @Override
//            public void run() {
//                
//            }
//        })
        //获取imageview:tag
        int requestPosition = (Integer) imageView.getTag();
        executorService.execute(new DownImageRunnable(imageView, url,requestPosition));
        Log.i("MyBitmapUtils", "从网络获取");
    }
    class DownImageRunnable implements Runnable{
        private ImageView imageView;
        private String imageUrl;
        //请求位置
        private int requestPosition;

        public DownImageRunnable(ImageView imageView,String imageUrl, int requestPosition){
            this.imageView = imageView;
            this.imageUrl = imageUrl;
            this.requestPosition = requestPosition;
        }

        @Override
        public void run() {
            SystemClock.sleep(2*1000);
            //请求网络 httpUrlConnection  : 图片url,展示图片控件
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //post/get
                httpURLConnection.setRequestMethod("GET");
                //设置连接超时时间
                httpURLConnection.setConnectTimeout(5*1000);
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode==200){
                    //请求成功
                    //先缓存到内存中,再缓存到本地中,最后展示.
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //inputStream -- bitmap 
                    final Bitmap decodeStreamBitmap = BitmapFactory.decodeStream(inputStream);
                    //1.缓存到内存中
                    mLruCache.put(imageUrl, decodeStreamBitmap);
                    //2.缓存到本地 
                    /**
                     * compress压缩:
                     * format: 压缩格式,PNG,jpeg
                     * int quality: 压缩率,30代表压缩百分之70. 100-0
                     * outputStream:输出流,写本地文件的流
                     */
                    String imageName = MD5Encoder.encode(imageUrl);//url+md5
                    File imageFile = new File(rootFile, imageName);
                    OutputStream stream = new FileOutputStream(imageFile); 
                    decodeStreamBitmap.compress(CompressFormat.PNG, 100, stream);
                    //3.展示  (子线程不能更新ui)
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.runOnUiThread(new Runnable()
                    {
                        
                        @Override
                        public void run() {
                            //如果当前显示在屏幕上的imageview的tag与请求的tag一致,再展示
                            int screenPosition = (Integer) imageView.getTag();
                            if(screenPosition == requestPosition){
                                imageView.setImageBitmap(decodeStreamBitmap);
                            }
                        }
                    });
                   
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
