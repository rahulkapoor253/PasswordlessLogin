package com.example.rahulkapoor.passwordlesslogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class AndroidResideMenu extends AppCompatActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    //instead of making string we make residemenu items so that they can be handled;
    //private String[] titles = {"Home", "Profile", "Contact Us"};
    private ResideMenuItem home, profile, contactUs;
    private ImageView ivHamburger;
    //private ResideMenuItem resideMenuItem;

    //private int[] icons = {R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_reside_menu);

        //hamburger button setup;
        ivHamburger = (ImageView) findViewById(R.id.iv_hamburger);
        ivHamburger.setOnClickListener(this);

        //to open my reside menu from left onClick;
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.colorPrimary);
        resideMenu.attachToActivity(this);
        //to disable the right swipe of reside menu;
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
//        for (int i = 0; i < titles.length; i++) {
//            //to pass context, int, string;
//            resideMenuItem = new ResideMenuItem(this, icons[i], titles[i]);
//            resideMenuItem.setOnClickListener(this);
//            //reside menu opened from left direction;
//            resideMenu.addMenuItem(resideMenuItem, ResideMenu.DIRECTION_LEFT);
//        }

        //creating menu items;
        home = new ResideMenuItem(this, R.mipmap.ic_launcher_round, "Home");
        profile = new ResideMenuItem(this, R.mipmap.ic_launcher_round, "Profile");
        contactUs = new ResideMenuItem(this, R.mipmap.ic_launcher_round, "Contact Us");
        //adding menu items to reside menu;
        resideMenu.addMenuItem(home, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(profile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(contactUs, ResideMenu.DIRECTION_LEFT);
        //setting up on Click listeners;
        home.setOnClickListener(this);
        profile.setOnClickListener(this);
        contactUs.setOnClickListener(this);


    }


    @Override
    public void onClick(final View v) {

        //we can take view of reside menu items and open fragments followed by resideMenu.close;

    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
}
