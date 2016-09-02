package br.com.socketchatroomapp.activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import br.com.socketchatroomapp.R;
import br.com.socketchatroomapp.adapters.MessageAdapter;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by brunogabriel on 8/30/16.
 */
public class ChatActivity extends BaseActivity implements Validator.ValidationListener {

    private static final String TAG = "CHAT_ACTIVITY";
    private static final String SEND_MESSAGE = "sendMessage";
    private static final String IS_OFFLINE = "isOffline";
    private static final String IS_ONLINE = "isOnline";

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @NotEmpty(message = "This field should be filled")
    @BindView(R.id.editTextMessage)
    protected AppCompatEditText editTextMessage;

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @BindString(R.string.server_url)
    protected String serverURL;

    protected String username;
    protected String userIdentifier;
    protected Validator mValidator;

    private Socket socketClient;
    private MessageAdapter mMessageAdapter;
    private Uri soundUri;
    private Ringtone notificationRing;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        try {
            username = getIntent().getExtras().getString("username");
            userIdentifier = getIdentifier();
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
        options.query = "username=" +username + "&userid=" + userIdentifier;
        try {
            socketClient = IO.socket(serverURL, options);
            socketClient.connect();
            createSocketCommands();
        } catch (URISyntaxException e) {
            Log.e(TAG, "Fail to create socket: " + e.getMessage());
        }
    }

    @Override
    public void initUI() {
        setupToolBar(mToolbar, "Hello " + username, null, false, false);

        editTextMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    mValidator.validate();
                }
                return false;
            }
        });
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mMessageAdapter = new MessageAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMessageAdapter);
    }

    @OnClick(R.id.sendImage)
    protected void onClickSendText() {
        mValidator.validate();
    }

    private void createSocketCommands() {
        socketClient.on(SEND_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null && args.length>0) {
                    try {
                        final JSONObject jsonMessage = (JSONObject) args[0];

                        if (jsonMessage.getString("userid").compareToIgnoreCase(userIdentifier) == 0) {
                            jsonMessage.put("appType", MessageAdapter.VIEW_TYPE_SEND_MESSAGE);
                        } else {
                            jsonMessage.put("appType", MessageAdapter.VIEW_TYPE_RECEIVE_MESSAGE);
                            emitSound();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMessageAdapter.addItem(jsonMessage);
                            }
                        });

                    }catch (Exception e) {
                        Log.d(TAG, "Fail on execute SEND_MESSAGE: " + e.getMessage());
                    }
                }
            }
        });

        socketClient.on(IS_ONLINE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                int x = 1;
            }
        });

        socketClient.on(IS_OFFLINE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                int x = 1;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketClient != null) {
            socketClient.off();
            socketClient.disconnect();
        }
    }

    @Override
    public void onValidationSucceeded() {
        String mUsername = editTextMessage.getText().toString().trim();
        if (socketClient.connected()) {
            try {
                JSONObject jsonMessage = new JSONObject().put("message", mUsername);
                editTextMessage.setText("");
                hideKeyboard();
                socketClient.emit(SEND_MESSAGE, jsonMessage);
            } catch (JSONException e) {
                Log.e(TAG, "Fail on send message, cause: " + e.getMessage());
            }

        } else {
            showSimpleDialog(null, getString(R.string.app_network_error));
        }
    }

    public void emitSound() {
        try {

            if (soundUri == null) {
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationRing = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
            }

            notificationRing.play();

        } catch (Exception e) {
            Log.e("MessageAdapter", "Fail during alert: " + e.getMessage());
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError mError : errors) {
            String mErrorMessage = mError.getCollatedErrorMessage(this);
            View mView = mError.getView();
            if (mView instanceof AppCompatEditText)
                ((AppCompatEditText) mView).setError(mErrorMessage);

        }
    }
}
