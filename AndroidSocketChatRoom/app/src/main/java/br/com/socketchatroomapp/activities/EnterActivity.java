package br.com.socketchatroomapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import br.com.socketchatroomapp.R;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brunogabriel on 8/8/16.
 */
public class EnterActivity extends BaseActivity implements Validator.ValidationListener{

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @NotEmpty(message = "This field should be filled")
    @BindView(R.id.usernameEditText)
    protected AppCompatEditText mUsernameEditText;

    @BindString(R.string.app_name)
    protected String mScreenTitle;

    protected Validator mValidator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        ButterKnife.bind(this);
        initUI();
    }

    @Override
    public void initUI() {
        setupToolBar(mToolbar, mScreenTitle, null, false, false);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String mResult = editable.toString().replace(" ", "");
                if (!editable.toString().equals(mResult)) {
                    mUsernameEditText.setText(mResult);
                    mUsernameEditText.setSelection(mResult.length());
                }
            }
        });
    }

    @OnClick(R.id.enterButton)
    protected void onClickEnterButton() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        String mUsername = mUsernameEditText.getText().toString().trim();

        if (isNetworkAvailable()) {
            Intent mIntent = new Intent();
            mIntent.setClass(this, ChatActivity.class);
            mIntent.putExtra("username", mUsername);
            startActivity(mIntent);
            this.finish();
        } else {
            showSimpleDialog(null, getString(R.string.app_network_error));
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
