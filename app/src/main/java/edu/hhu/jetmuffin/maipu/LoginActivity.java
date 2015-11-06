package edu.hhu.jetmuffin.maipu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {


    // UI references.
    @NotEmpty
    @Email
    private AutoCompleteTextView mEmailView;
    @NotEmpty
    private EditText mPasswordView;
    private Button mRegisterButton;
    private Button mLoginButton;
    private CheckBox mRememberMe;


    private SharedPreferences sp;

    private String email;
    private String password;
    private Boolean rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRememberMe = (CheckBox) findViewById(R.id.remember_me);

        onRememberCheck();
        mRegisterButton = (Button) findViewById(R.id.email_sign_up_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        //validate
        final Validator validator = new Validator(this);
        validator.setValidationListener(this);

        mLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        if(accountAuth()){
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            loadProfile(email);
            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error:errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            //Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean accountAuth(){
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        rememberMe = mRememberMe.isChecked();

        sp = getSharedPreferences("map", Activity.MODE_PRIVATE);

        boolean exist = sp.getString(email, "0").equals("1");
        if(exist){
            String authPassword = sp.getString("password_"+email,"");
            if(authPassword.equals(password)){
                return true;
            }else{
                Toast.makeText(LoginActivity.this, "密码不正确,请确认后再输入!", Toast.LENGTH_LONG).show();
                return false;
            }

        }else{
            Toast.makeText(LoginActivity.this, "账号不存在,请确认后再输入!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void onRememberCheck(){
        sp = getSharedPreferences("map", Activity.MODE_PRIVATE);
        String rememberMe = sp.getString("remember","");
        if(!rememberMe.equals("")){
            String _email = sp.getString("remember", "");
            String _password = sp.getString("remember_password","");
            mEmailView.setText(_email);
            mPasswordView.setText(_password);
            mRememberMe.setChecked(true);
        }
    }
    public void loadProfile(String _email){
        sp = getSharedPreferences("map", Activity.MODE_PRIVATE);
        String nickname = sp.getString("nickname_" + _email,"");
        String city = sp.getString("city_" + _email, "");
        String motto = sp.getString("motto_" + _email, "");
        String birthday = sp.getString("birthday_"+_email, "");
        String interest = sp.getString("interest_"+_email, "");

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("nickname", nickname);
        editor.putString("city", city);
        editor.putString("motto", motto);
        editor.putString("birthday", birthday);
        editor.putString("interest", interest);

        if(rememberMe){
            editor.putString("remember",_email);
            editor.putString("remember_password", password);
        }else{
            editor.putString("remember", "");
            editor.putString("remember_password","");
        }

        editor.commit();
    }
}

