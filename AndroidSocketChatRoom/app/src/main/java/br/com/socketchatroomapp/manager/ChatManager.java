package br.com.socketchatroomapp.manager;

import android.app.Application;

import br.com.socketchatroomapp.utilities.AppSingleton;

/**
 * Created by brunogabriel on 8/8/16.
 */
public class ChatManager extends Application {

    public static final boolean DEVELOPER_MODE = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initSingletons();
    }

    protected void initSingletons() {
        AppSingleton.getInstance();
    }
}
