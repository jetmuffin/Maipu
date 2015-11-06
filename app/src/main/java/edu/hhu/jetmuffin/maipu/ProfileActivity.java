package edu.hhu.jetmuffin.maipu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import edu.hhu.jetmuffin.model.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvEmail;
    private TextView tvNickname;
    private TextView tvBirthday;
    private TextView tvCity;
    private TextView tvInterest;
    private TextView tvMotto;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        loadUserData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(user.getNickname() + "'s Profile");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is "+user.getNickname()+"'s Profile", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    public void init(){
        tvEmail = (TextView)findViewById(R.id.user_email);
        tvNickname = (TextView)findViewById(R.id.user_nickname);
        tvBirthday = (TextView)findViewById(R.id.user_birthday);
        tvCity = (TextView)findViewById(R.id.user_city);
        tvInterest = (TextView)findViewById(R.id.user_interest);
        tvMotto = (TextView)findViewById(R.id.user_motto);
    }

    public void loadUserData(){
        SharedPreferences sp = getSharedPreferences("map", Activity.MODE_PRIVATE);
        String email = sp.getString("email", "");
        user = new User(sp);

        tvEmail.setText(user.getEmail());
        tvNickname.setText(user.getNickname());
        tvBirthday.setText(user.getBirthday());
        tvMotto.setText(user.getMotto());
        tvInterest.setText(user.getInterest());
        tvCity.setText(user.getCity());
    }
}
