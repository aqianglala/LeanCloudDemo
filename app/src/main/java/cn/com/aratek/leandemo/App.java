package cn.com.aratek.leandemo;

import android.app.Application;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        AVOSCloud.initialize(this, "IoqG2mxkwdsYmUBswCLYKNuO-gzGzoHsz", "0cYufX8owWo9lWNQJ4unFS5P", "https://ioqg2mxk.lc-cn-n1-shared.com");
        //测试
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("words", "Hello world!");
//        testObject.saveInBackground().blockingSubscribe();
    }
}
