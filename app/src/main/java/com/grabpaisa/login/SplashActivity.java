package com.grabpaisa.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grabpaisa.R;


public class SplashActivity extends Activity {
    ImageView imgLogo, imgTick;
    ScaleAnimation scale;
    public boolean flage;
    RelativeLayout rlayout;
    Animation anim;
    String acname, mobile_no, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        rlayout = (RelativeLayout) findViewById(R.id.rlayout);
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        //imgTick = (ImageView) findViewById(R.id.img_tick);
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

        };
        t.start();


    }

    @Override
    protected void onResume() {
        super.onResume();
    /*    scale = new ScaleAnimation((float) 1.0, (float) .35, (float) 1.0, (float) .35);
        scale.setFillAfter(true);
        scale.setDuration(3000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                imgTick.setVisibility(View.VISIBLE);
                imgTick.startAnimation(scale);
                imgLogo.setVisibility(View.GONE);

            }
        }, 2000);

        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //rlayout.setAlpha((float) 0.3);
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

*/
    }


}
