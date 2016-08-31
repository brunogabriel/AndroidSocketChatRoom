package br.com.socketchatroomapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.net.URISyntaxException;

import br.com.socketchatroomapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by brunogabriel on 8/30/16.
 */
public class ChatActivity extends BaseActivity {

    private static final String TAG = "CHAT_ACTIVITY";
    private static final String SERVER_URL = "http://192.168.1.33:3000";

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    protected String username;

    private Socket socketClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        try {
            username = getIntent().getExtras().getString("username");
        } catch (Exception e) {
            Log.e(TAG, "Fail getting username: " + e.getMessage());
            Intent mIntent = new Intent();
            mIntent.setClass(this, EnterActivity.class);
            startActivity(mIntent);
            this.finish();
        }

        ButterKnife.bind(this);
        initUI();
        setupSocketClient();
    }


    public void setupSocketClient() {
        IO.Options options = new IO.Options();
        options.query = "username=" +username;
        try {
            socketClient = IO.socket(SERVER_URL, options);
            socketClient.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, "Fail to create socket: " + e.getMessage());
        }
    }

    @Override
    public void initUI() {
        setupToolBar(mToolbar, "Hello " + username, null, false, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketClient != null) {
            socketClient.disconnect();
        }
    }
}
