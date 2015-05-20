package example.twl.helloworld;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class WatchService extends Service {
    public WatchService() {
    }

    public static RMCService currentInstance;


    String TAG = "RMCService";

    public class RMCApplication extends TWApplication {
        TWAutoLayout layout = new TWAutoLayout();

        public void updateCmdList(ArrayList<String> btns) {
            layout.widgetList.clear();
            for (int i = 0; i < btns.size(); i++) {
                layout.addWidget(TWAutoLayout.Widget.makeButton(btns.get(i), i));
            }
            updateLayout(layout, 0);
        }

        @Override
        public void onButtonClick(int id) {
            super.onButtonClick(id);
            try {

                if (Global.client.currentPadActivity != null) {
                    Global.client.currentPadActivity.handleWatchButtonClicked(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onActivate() {
            super.onActivate();
        }

        RMCApplication(Service s) {
            super(s);
            setDisplayName("RMC");
        }

    }

    ;

    RMCApplication app;

    public RMCService() {
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
        app = new RMCApplication(this);
        app.registerCurrentApplication();
        currentInstance = this;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        currentInstance = null;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if (intent != null) {
            int cmd = intent.getIntExtra("cmd", 0);
            if (cmd > 0) {
                app.handleIntent(intent);
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }
}
