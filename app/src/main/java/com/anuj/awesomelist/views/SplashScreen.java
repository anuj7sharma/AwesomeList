package com.anuj.awesomelist.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.anuj.awesomelist.MainActivity;
import com.anuj.awesomelist.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by anuj on 12/17/2015.
 */
public class SplashScreen extends Activity {
    Context mContext;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Bind(R.id.shimmer_view_container)ShimmerFrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        mContext = SplashScreen.this;
        container.startShimmerAnimation();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        container.stopShimmerAnimation();
        super.onPause();
    }
}
