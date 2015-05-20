package example.twl.helloworld;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import io.twlab.twlib.TWApplication;
import io.twlab.twlib.TWAutoLayout;

public class WatchService extends Service {
    public WatchService() {
    }

    public static WatchService currentInstance;
    WatchApplication watchApplication ;

    String TAG = "WatchService";

    public class WatchApplication extends TWApplication {
        TWAutoLayout layout = new TWAutoLayout();
        TWAutoLayout.Widget mainText;
        int ID_BTN_OK = 1;
        int ID_BTN_CANCEL = 2;


        @Override
        public void onButtonClick(int id) {
            super.onButtonClick(id);
            if (id == ID_BTN_OK) {
                mainText.text = "你点击了确定！";
            }
            if (id == ID_BTN_CANCEL) {
                mainText.text = "你点击了取消！";
            }
            /* 更新布局，并把焦点放置在之前所按下的按钮之上 */
            updateLayout(layout, id);

        }

        @Override
        public void onActivate() {
            mainText.text = "你好！\n<hr/>\n";
            /* 更新布局 */
            updateLayout(layout, 0);
            super.onActivate();
        }

        WatchApplication(Service s) {
            super(s);
            setDisplayName("HelloWorld");
            /* 创建文本 Widget */
            mainText = TWAutoLayout.Widget.makeText("");
            layout.addWidget(mainText);
            /* 创建确定和取消按钮 */
            layout.addWidget(TWAutoLayout.Widget.makeButton("确定", ID_BTN_OK));
            layout.addWidget(TWAutoLayout.Widget.makeButton("取消", ID_BTN_CANCEL));
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
        watchApplication = new WatchApplication(this);
        watchApplication.registerCurrentApplication();
        currentInstance = this;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        currentInstance = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if (intent != null) {
            int cmd = intent.getIntExtra("cmd", 0);
            if (cmd > 0) {
                watchApplication.handleIntent(intent);
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }
}
