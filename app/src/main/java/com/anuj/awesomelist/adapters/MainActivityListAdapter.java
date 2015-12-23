package com.anuj.awesomelist.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anuj.awesomelist.R;
import com.anuj.awesomelist.models.MainScreenModel;
import com.anuj.awesomelist.utils.CommonMethods;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by Raj on 12/15/2015.
 */
public class MainActivityListAdapter extends RecyclerView.Adapter<MainActivityListAdapter.ViewHolder> {
    public interface MainScreenListener {
        public void onItemClick(MainScreenModel obj, int position, View v);
    }
//    Dealer_HomeResponse mResponse;
    Context mContext;
    MainScreenListener mListener;
    List<MainScreenModel> mList;
    public final static int COLOR_ANIMATION_DURATION = 1000;
    private int mDefaultBackgroundColor;

    public MainActivityListAdapter(Context context, List<MainScreenModel> list) {
        this.mContext = context;
        this.mList = list;
        this.mListener = (MainScreenListener) mContext;
        mDefaultBackgroundColor = context.getResources().getColor(R.color.image_without_palette);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mainactivity, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mList != null && mList.size()>0) {
            final MainScreenModel obj = mList.get(position);
            holder.mHeading.setText(obj.getHeading());
            holder.mSubHeading.setText(obj.getSubHeading());
            /*
            Set Image
             */
            Picasso.with(mContext)
                    .load(obj.getImage())
                    .error(R.drawable.ic_material_img)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            /* Save the bitmap or do something with it here */
                            //Set it in the ImageView
                            holder.mImg.setImageBitmap(bitmap);
                            /*
                            Use Pallet For Getting Color
                             */
                            Palette palette = Palette.generate(bitmap, 24);
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
                                    holder.mLayout.setBackgroundColor(s.getTitleTextColor());
                                    holder.mImg.setBackgroundColor(s.getTitleTextColor());
                                    holder.mContent.setBackgroundColor(s.getTitleTextColor());
                                    holder.mHeading.setTextColor(s.getTitleTextColor());
                                    holder.mSubHeading.setTextColor(s.getTitleTextColor());
                                }
                                CommonMethods.getInstance().animateViewColor(holder.mContent, mDefaultBackgroundColor, s.getRgb());
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

            /*
            Set Parallax Effect on Images
             */
            Matrix matrix = holder.mImg.getImageMatrix();
            matrix.postTranslate(0, -100);
            holder.mImg.setImageMatrix(matrix);

            holder.itemView.setTag(holder);

            /*
            Set click listeners
             */
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // move to other screen with full detail
//                    CommonMethods.getInstance().displaySnackBar(mContext,"order clicked",v);
                    mListener.onItemClick(obj, position, v);
                }
            });
        }

        }

    @Override
    public int getItemCount() {
//        return mResponse.getValue().size();
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mLayout;
        public LinearLayout mContent;
        public ImageView mImg;
        public TextView mHeading;
        public TextView mSubHeading;

        public ViewHolder(View itemView) {
            super(itemView);

            mLayout = (RelativeLayout)itemView.findViewById(R.id.view_parent);
            mContent = (LinearLayout) itemView.findViewById(R.id.view_content);
            mImg = (ImageView)itemView.findViewById(R.id.view_img);
            mHeading = (TextView)itemView.findViewById(R.id.view_heading);
            mSubHeading = (TextView)itemView.findViewById(R.id.view_subheading);
        }
    }
}