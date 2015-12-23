package com.anuj.awesomelist.views;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.anuj.awesomelist.MainActivity;
import com.anuj.awesomelist.R;
import com.anuj.awesomelist.utils.CommonMethods;
import com.cocosw.bottomsheet.BottomSheet;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anuj on 12/18/2015.
 */
public class DescriptionActivity extends AppCompatActivity {
    Context mContext;
    public final static int COLOR_ANIMATION_DURATION = 1000;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private int mDefaultBackgroundColor;
    private String imageURL = null;

    private Animation desc_open,desc_close;

    @Bind(R.id.detail_img)
    ImageView mImage;
    @Bind(R.id.detail_heading)
    TextView mHeading;
    @Bind(R.id.detail_subheading)
    TextView mSubHeading;
    @Bind(R.id.detail_layout)
    LinearLayout mLayout;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.detail_parentlayout)
    CoordinatorLayout mparentLayout;

    @OnClick(R.id.detail_parentlayout)
    void onClickOutside() {
//        CommonMethods.getInstance().DisplayToast(mContext, "clicked out side");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);
        ButterKnife.bind(this);
        mContext = DescriptionActivity.this;
        desc_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        desc_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        mDefaultBackgroundColor = mContext.getResources().getColor(R.color.image_without_palette);
        mLayout.setVisibility(View.INVISIBLE);

        onstartMethod();
        animateFab();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayout.startAnimation(desc_open);

            }
        }, 400);


        onFabClick();
    }

    void onFabClick(){
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(DescriptionActivity.this).title("Options").sheet(R.menu.menu_options).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.set_wallpaper:
                                WallpaperManager myWallpaperManager
                                        = WallpaperManager.getInstance(getApplicationContext());
                                try {
                                    /*if(imageURL!=null){
                                        Picasso.with(mContext).load(imageURL).into((Target) myWallpaperManager);
                                    }*/
                                    Bitmap bitmap = ((BitmapDrawable)mImage.getDrawable()).getBitmap();
                                    myWallpaperManager.setBitmap(bitmap);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                CommonMethods.getInstance().DisplayToast(mContext,"Setting Wallpaper..");
                                break;
                            case R.id.share:
                                CommonMethods.getInstance().DisplayToast(mContext,"Sharing..");
                                break;
                            case R.id.like:
//                                showHeartPopup();
                                CommonMethods.getInstance().DisplayToast(mContext,"Thumbs Up..");
                                break;
                            case R.id.help:
                                CommonMethods.getInstance().DisplayToast(mContext,"Happy to Help !");
                                break;
                        }
                    }
                }).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    void animateFab(){
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1); // start alpha, end alpha
        fadeInAnimation.setDuration(1000); // time for animation in milliseconds
        fadeInAnimation.setFillAfter(true); // make the transformation persist
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });

        mFab.setAnimation(fadeInAnimation);
    }

    void onstartMethod() {
        //get intent item
        try {
            // Recover items from the intent
            final int position = getIntent().getIntExtra("position", 0);
//			        mSelectedImage = (Image) getIntent().getSerializableExtra("selected_image");

            String imageurl = getIntent().getStringExtra("image");
            this.imageURL = imageurl;
            String title = getIntent().getStringExtra("title");
            String description = getIntent().getStringExtra("description");

            mHeading.setText(title);
            mSubHeading.setText(description);

            Bitmap imageCoverBitmap = MainActivity.photoCache.get(position);
            //safety check to prevent nullPointer in the palette if the detailActivity was in the background for too long
            if (imageCoverBitmap == null || imageCoverBitmap.isRecycled()) {
                this.finish();
                return;
            }
            mImage.setImageBitmap(imageCoverBitmap);
            if (Build.VERSION.SDK_INT >= 21) {
                mImage.setTransitionName("cover");
                // Add a listener to get noticed when the transition ends to animate the fab button
            } else {
                if (mImage == null)
                    CommonMethods.getInstance().e("", "before call is null ");
            }

            Palette palette = Palette.generate(imageCoverBitmap, 24);
            if (palette != null) {
                Palette.Swatch s = palette.getVibrantSwatch();
                if (s == null) {
                    s = palette.getDarkVibrantSwatch();
                }
                if (s == null) {
                    s = palette.getLightVibrantSwatch();
                }
                if (s == null) {
                    s = palette.getMutedSwatch();
                }

                if (s != null) {
                    mLayout.setBackgroundColor(s.getTitleTextColor());
                    mHeading.setTextColor(s.getTitleTextColor());
                    mSubHeading.setTextColor(s.getTitleTextColor());
                }
                CommonMethods.getInstance().animateViewColor(mLayout, mDefaultBackgroundColor, s.getRgb());
            }

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            CommonMethods.getInstance().DisplayToast(DescriptionActivity.this, "Unable to view Details");
        }

    }

    @Override
    public void onBackPressed() {
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1, 0); // start alpha, end alpha
        fadeOutAnimation.setDuration(100); // time for animation in milliseconds
        fadeOutAnimation.setFillAfter(true); // make the transformation persist
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });

        mFab.setAnimation(fadeOutAnimation);
        mLayout.startAnimation(desc_close);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleBackPress();

            }
        }, 400);
    }

    void handleBackPress() {
        finishActivity1();
    }
    /*
    Finish Activity
     */
    void finishActivity1(){
        super.onBackPressed();
    }


    /*
    Heart PopUP on Like Click
     */
    LinearLayout.LayoutParams params;
    LinearLayout mainLayout;
    PopupWindow popupWindow;
    LinearLayout layout;

    void showHeartPopup(){
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.heart_popup, null);

        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,
                true);

        popupWindow .setTouchable(true);
        popupWindow .setFocusable(true);

        popupWindow .showAtLocation(popupView, Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.style.heartAnimation_Window);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();

            }
        }, 2000);
    }

}
