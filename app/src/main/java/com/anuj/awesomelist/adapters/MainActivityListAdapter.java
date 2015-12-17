package com.anuj.awesomelist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anuj.awesomelist.R;
import com.anuj.awesomelist.models.MainScreenModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Raj on 12/15/2015.
 */
public class MainActivityListAdapter extends RecyclerView.Adapter<MainActivityListAdapter.ViewHolder> {
    public interface MainScreenListener {
        public void onItemClick(MainScreenModel obj, int position,ImageView mImage);
    }
//    Dealer_HomeResponse mResponse;
    Context mContext;
    MainScreenListener mListener;
    List<MainScreenModel> mList;

    public MainActivityListAdapter(Context context, List<MainScreenModel> list) {
        this.mContext = context;
        this.mList = list;
        this.mListener = (MainScreenListener) mContext;
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
                    .placeholder(R.drawable.ic_material_img)
                    .error(R.drawable.ic_material_img)
                    .into(holder.mImg);
            /*
            Set click listeners
             */
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // move to other screen with full detail
//                    CommonMethods.getInstance().displaySnackBar(mContext,"order clicked",v);
                    mListener.onItemClick(obj,position,holder.mImg);
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
        public LinearLayout mLayout;
        public ImageView mImg;
        public TextView mHeading;
        public TextView mSubHeading;

        public ViewHolder(View itemView) {
            super(itemView);

            mLayout = (LinearLayout)itemView.findViewById(R.id.view_parent);
            mImg = (ImageView)itemView.findViewById(R.id.view_img);
            mHeading = (TextView)itemView.findViewById(R.id.view_heading);
            mSubHeading = (TextView)itemView.findViewById(R.id.view_subheading);
        }
    }
}