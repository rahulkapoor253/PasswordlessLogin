package com.example.rahulkapoor.passwordlesslogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class AndroidResideMenu extends AppCompatActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private String[] titles = {"Home", "Profile", "Contact Us"};
    private ResideMenuItem resideMenuItem;

    private int[] icons = {R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_reside_menu);

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.colorPrimary);
        resideMenu.attachToActivity(this);
        //to disable the right swipe of reside menu;
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        for (int i = 0; i < titles.length; i++) {
            //to pass context, int, string;
            resideMenuItem = new ResideMenuItem(this, icons[i], titles[i]);
            resideMenuItem.setOnClickListener(this);
            //reside menu opened from left direction;
            resideMenu.addMenuItem(resideMenuItem, ResideMenu.DIRECTION_LEFT);
        }

    }

    @Override
    public void onClick(final View v) {


    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
}
