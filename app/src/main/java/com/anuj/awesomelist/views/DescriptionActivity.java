package com.anuj.awesomelist.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anuj.awesomelist.MainActivity;
import com.anuj.awesomelist.R;
import com.anuj.awesomelist.utils.CommonMethods;

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

    @Bind(R.id.detail_img)
    ImageView mImage;
    @Bind(R.id.detail_heading)
    TextView mHeading;
    @Bind(R.id.detail_subheading)
    TextView mSubHeading;
    @Bind(R.id.detail_layout)
    LinearLayout mLayout;
    @Bind(R.id.detail_parentlayout)
    LinearLayout mparentLayout;

    @OnClick(R.id.detail_parentlayout)
    void onClickOutside() {
        CommonMethods.getInstance().DisplayToast(mContext, "clicked out side");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);
        ButterKnife.bind(this);
        mContext = DescriptionActivity.this;
        mDefaultBackgroundColor = mContext.getResources().getColor(R.color.image_without_palette);
        mLayout.setVisibility(View.INVISIBLE);
        onstartMethod();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateDescLayout();
            }
        }, 300);

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    void onstartMethod() {
        //get intent item
        try {
            // Recover items from the intent
            final int position = getIntent().getIntExtra("position", 0);
//			        mSelectedImage = (Image) getIntent().getSerializableExtra("selected_image");

            String imageurl = getIntent().getStringExtra("image");
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
               /* getWindow().getSharedElementEnterTransition().addListener(new CustomTransitionListener() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        super.onTransitionEnd(transition);
                        animateActivityStart();
                    }
                });*/
            } else {
                if (mImage == null)
                    CommonMethods.getInstance().e("", "before call is null ");
//                Utils.showViewByScale(mDetailImage).setDuration(ANIMATION_DURATION_LONG).start();
//                animateActivityStart();
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

    /*
    Animation for Detail layout
     */
    private void animateDescLayout() {
        mLayout.setVisibility(View.VISIBLE);
        mLayout.setTranslationY(-mLayout.getHeight());    //layout
        mLayout.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
        mHeading.animate().alpha(1).setDuration(200).setStartDelay(400).setInterpolator(INTERPOLATOR).start();
    }

    @Override
    public void onBackPressed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                handleBackPress();
            }
        }, 300);
        mLayout.setVisibility(View.GONE);
        mLayout.setTranslationY(+mLayout.getHeight());    //layout
        mLayout.animate().translationY(0).setDuration(200).setInterpolator(INTERPOLATOR);
        mHeading.animate().alpha(1).setDuration(200).setStartDelay(200).setInterpolator(INTERPOLATOR).start();


    }

    void handleBackPress() {
        super.onBackPressed();
    }
}
