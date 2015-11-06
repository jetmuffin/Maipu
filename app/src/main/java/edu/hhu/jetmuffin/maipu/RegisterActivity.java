package edu.hhu.jetmuffin.maipu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.*;
import com.mobsandgeeks.saripaar.Validator;

import java.util.Calendar;
import java.util.List;


public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    @Email
    private EditText etEmail;

    @Password(min = 6, scheme = Password.Scheme.ANY)
    private EditText etPassword;

    @ConfirmPassword
    private EditText etRepeatPassword;

    @NotEmpty
    private EditText etCity;
    private EditText etMotto;
    @NotEmpty
    private EditText etNickname;


    private EditText etBirthday;
    private EditText etInterest;

    private Button registerButton;

    private String email;
    private String password;
    private String city;
    private String motto;
    private String birthday;
    private String interest;
    private String nickname;

    private final static int DATE_DIALOG = 0;
    private final static int SELECT_DIALOG = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        //validate
        final Validator validator = new Validator(this);
        validator.setValidationListener(this);

        //listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    /**
     * 初始化UI组件
     */
    protected void init(){
        etEmail = (EditText)findViewById(R.id.register_email);
        etPassword = (EditText)findViewById(R.id.register_password);
        etRepeatPassword = (EditText)findViewById(R.id.register_repeat_password);
        etCity = (EditText)findViewById(R.id.register_city);
        etNickname = (EditText)findViewById(R.id.register_nickname);
        etMotto = (EditText)findViewById(R.id.register_motto);
        etBirthday = (EditText)findViewById(R.id.register_birthday);
        etInterest = (EditText)findViewById(R.id.register_interest);
        registerButton = (Button)findViewById(R.id.register_button);

        etBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showDialog(DATE_DIALOG);
            }
        });

        etInterest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    onInterestDialog();
            }
        });
    }

    /**
     * 保存账号信息至SharedPreferences
     */
    protected void save(){
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        city = etCity.getText().toString();
        motto = etMotto.getText().toString();
        nickname = etNickname.getText().toString();
        birthday = etBirthday.getText().toString();
        interest = etInterest.getText().toString();

        SharedPreferences sp = getSharedPreferences("map", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(email, "1");
        editor.putString("password_"+email, password);
        editor.putString("city_"+email, city);
        editor.putString("nickname_"+email, nickname);
        editor.putString("motto_" + email, motto);
        editor.putString("birthday_" + email, birthday);
        editor.putString("interest_"+email, interest);
        editor.commit();
    }

    @Override
    public void onValidationSucceeded() {

        save();
        Toast.makeText(RegisterActivity.this, "Registered success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error:errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            //Display error messages
            if(view instanceof EditText){
                ((EditText) view).setError(message);
            }else{
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 创建日期及时间选择对话框
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DIALOG:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                dialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                etBirthday.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                            }
                        }, year, month, day);
                break;

        }
        return dialog;
    }

    /**
     * 兴趣选择框
     */
    public void onInterestDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Choose your hobbies");
        final String[] hobbies = {"Sports", "Books", "Musics", "Movies", "Games"};
        final StringBuffer sb = new StringBuffer(100);

        builder.setMultiChoiceItems(hobbies, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    sb.append(hobbies[which] + ", ");
                }
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etInterest.setText(sb.toString());
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
