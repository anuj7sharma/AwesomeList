package com.anuj.awesomelist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anuj.awesomelist.adapters.MainActivityListAdapter;
import com.anuj.awesomelist.adapters.MainActivityListAdapter.MainScreenListener;
import com.anuj.awesomelist.customClasses.ConnectivityEvent;
import com.anuj.awesomelist.customClasses.ParallaxRecyclerView;
import com.anuj.awesomelist.models.MainScreenModel;
import com.anuj.awesomelist.utils.MobileConnectivity;
import com.anuj.awesomelist.views.DescriptionActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements MainScreenListener {

    Context mContext;
    private EventBus bus = EventBus.getDefault();

    @Bind(R.id.parentlayout)CoordinatorLayout mParent;
    @Bind(R.id.toolbar)Toolbar mToolbar;
    @Bind(R.id.mainscreen_recycler)ParallaxRecyclerView mRecyclerView;

    @Bind(R.id.connectivity_status)TextView mConnectivityStatus;



    MainScreenModel obj=null;
    LinearLayoutManager llm =null;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private List<MainScreenModel> mList = new ArrayList<MainScreenModel>();
    MainActivityListAdapter mRecyclerAdapter = null;

    //for palette class
    private int mDefaultBackgroundColor;
    public final static int COLOR_ANIMATION_DURATION = 1000;
    public static SparseArray<Bitmap> photoCache = new SparseArray<>(1);

    public String[] imagesTitle = new String[]{"Cool View","View without Rain","Mountains meet with Sea","Best Stoppie",
            "Beautiful Sky","Awesome Sun Rise","Lily Flowers","Ape age","Blue Waves","Stars revealing","Golden Fish",
            "Zannat view","Where Earth meet Sky",
            "Conceptualization","Beauty of Rocks",
            "Coast Beach",
            "Crossing Border",
            "Thirsty Earth",
            "Sea Horse",
            "Sky Scrappers",
            "God Gifts","Mountains"};
    public String[] gridViewImages = new String[] {
            "https://lh4.googleusercontent.com/-AaHAJPmcGYA/URqu3PIldHI/AAAAAAAAAbs/lcTqk1SIcRs/s1024/Monument%252520Valley%252520Overlook.jpg",
            "https://lh4.googleusercontent.com/-vKxfdQ83dQA/URqu31Yq_BI/AAAAAAAAAbs/OUoGk_2AyfM/s1024/Moving%252520Rock.jpg",
            "https://lh5.googleusercontent.com/-CG62QiPpWXg/URqu4ia4vRI/AAAAAAAAAbs/0YOdqLAlcAc/s1024/Napali%252520Coast.jpg",
            "https://lh6.googleusercontent.com/-wdGrP5PMmJQ/URqu5PZvn7I/AAAAAAAAAbs/m0abEcdPXe4/s1024/One%252520Wheel.jpg",
            "https://lh6.googleusercontent.com/-6WS5DoCGuOA/URqu5qx1UgI/AAAAAAAAAbs/giMw2ixPvrY/s1024/Open%252520Sky.jpg",
            "https://lh6.googleusercontent.com/-u8EHKj8G8GQ/URqu55sM6yI/AAAAAAAAAbs/lIXX_GlTdmI/s1024/Orange%252520Sunset.jpg",
            "https://lh6.googleusercontent.com/-74Z5qj4bTDE/URqu6LSrJrI/AAAAAAAAAbs/XzmVkw90szQ/s1024/Orchid.jpg",
            "https://lh6.googleusercontent.com/-lEQE4h6TePE/URqu6t_lSkI/AAAAAAAAAbs/zvGYKOea_qY/s1024/Over%252520there.jpg",
            "https://lh5.googleusercontent.com/-cauH-53JH2M/URqu66v_USI/AAAAAAAAAbs/EucwwqclfKQ/s1024/Plumes.jpg",
            "https://lh3.googleusercontent.com/-eDLT2jHDoy4/URqu7axzkAI/AAAAAAAAAbs/iVZE-xJ7lZs/s1024/Rainbokeh.jpg",
            "https://lh5.googleusercontent.com/-j1NLqEFIyco/URqu8L1CGcI/AAAAAAAAAbs/aqZkgX66zlI/s1024/Rainbow.jpg",
            "https://lh5.googleusercontent.com/-DRnqmK0t4VU/URqu8XYN9yI/AAAAAAAAAbs/LgvF_592WLU/s1024/Rice%252520Fields.jpg",
            "https://lh3.googleusercontent.com/-hwh1v3EOGcQ/URqu8qOaKwI/AAAAAAAAAbs/IljRJRnbJGw/s1024/Rockaway%252520Fire%252520Sky.jpg",
            "https://lh5.googleusercontent.com/-wjV6FQk7tlk/URqu9jCQ8sI/AAAAAAAAAbs/RyYUpdo-c9o/s1024/Rockaway%252520Flow.jpg",
            "https://lh6.googleusercontent.com/-6cAXNfo7D20/URqu-BdzgPI/AAAAAAAAAbs/OmsYllzJqwo/s1024/Rockaway%252520Sunset%252520Sky.jpg",
            "https://lh3.googleusercontent.com/-sl8fpGPS-RE/URqu_BOkfgI/AAAAAAAAAbs/Dg2Fv-JxOeg/s1024/Russian%252520Ridge%252520Sunset.jpg",
            "https://lh6.googleusercontent.com/-gVtY36mMBIg/URqu_q91lkI/AAAAAAAAAbs/3CiFMBcy5MA/s1024/Rust%252520Knot.jpg",
            "https://lh6.googleusercontent.com/-GHeImuHqJBE/URqu_FKfVLI/AAAAAAAAAbs/axuEJeqam7Q/s1024/Sailing%252520Stones.jpg",
            "https://lh3.googleusercontent.com/-hBbYZjTOwGc/URqu_ycpIrI/AAAAAAAAAbs/nAdJUXnGJYE/s1024/Seahorse.jpg",
            "https://lh3.googleusercontent.com/-Iwi6-i6IexY/URqvAYZHsVI/AAAAAAAAAbs/5ETWl4qXsFE/s1024/Shinjuku%252520Street.jpg",
            "https://lh6.googleusercontent.com/-amhnySTM_MY/URqvAlb5KoI/AAAAAAAAAbs/pFCFgzlKsn0/s1024/Sierra%252520Heavens.jpg",
            "https://lh5.googleusercontent.com/-dJgjepFrYSo/URqvBVJZrAI/AAAAAAAAAbs/v-F5QWpYO6s/s1024/Sierra%252520Sunset.jpg"
    };

    @Override
    protected void onStart() {
        super.onStart();
        mDefaultBackgroundColor = MainActivity.this.getResources().getColor(R.color.image_without_palette);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Register as a subscriber
        bus.register(this);

        mContext = MainActivity.this;
        if(!MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()){
            mConnectivityStatus.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
            mConnectivityStatus.setVisibility(View.VISIBLE);
            mConnectivityStatus.setText("Internet not Available :(");
        }
        initToolBar();
        initRecyclerView();

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        // Unregister
        bus.unregister(this);
        super.onDestroy();
    }

    public void onEvent(ConnectivityEvent event){
        if(event.getData().contains("Connected :)")){
            mConnectivityStatus.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mConnectivityStatus.setVisibility(View.GONE);
                }
            },2000);
        }
        else{
            mConnectivityStatus.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
            mConnectivityStatus.setVisibility(View.VISIBLE);
        }
        mConnectivityStatus.setText(event.getData());
    }
    /*
        Initialize Toolbar Here
         */
    void initToolBar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle("Awesome List");
    }

    /*
    Initialize Recycler View
     */
    void initRecyclerView(){
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);

        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);
        /*
        Add Scrolling Listener in RecyclerView
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();
                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
            /*
            Insert Dummy Data into List
             */
        Log.e("","Length of Array->>> " + gridViewImages.length);
        for(int i=0;i<gridViewImages.length;i++){
            obj = new MainScreenModel();
            obj.setId(""+i);
            obj.setHeading(imagesTitle[i]);
            obj.setSubHeading("Sub Heading");
            obj.setImage(gridViewImages[i]);
            obj.setIsSelected(false);
            mList.add(obj);
        }

        if (mRecyclerAdapter == null) {
            try {
                Log.e("","Length of List->>> " + mList.size());
                if (mList != null && mList.size() > 0) {
                    mRecyclerAdapter = new MainActivityListAdapter(mContext, mList);
                    mRecyclerView.setAdapter(mRecyclerAdapter);
                }
//                mRecyclerAdapter.notifyDataSetChanged();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(MainScreenModel obj, int position, View v) {
        if(obj!=null){
            /*if(!obj.getIsSelected()){
                //Make image blur
                obj.setIsSelected(true);
                mImage.setAlpha((float) 0.5);
            }
            else{
                //Make image normal
                obj.setIsSelected(false);
                mImage.setAlpha((float) 1);

            }*/
            /*
            Move to Transparent Activity
             */
            Intent intent = new Intent(mContext, DescriptionActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("image", obj.getImage());
            intent.putExtra("title", obj.getHeading());
            intent.putExtra("description", obj.getSubHeading());
            ImageView coverImage = (ImageView) v.findViewById(R.id.view_img);
            if (coverImage == null) {
                coverImage = (ImageView) ((View) v.getParent())
                        .findViewById(R.id.view_img);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (coverImage.getParent() != null) {
                    ((ViewGroup) coverImage.getParent())
                            .setTransitionGroup(false);
                }
            }
            if (coverImage != null && coverImage.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) coverImage.getDrawable())
                        .getBitmap(); // ew
                if (bitmap != null && !bitmap.isRecycled()) {
                    photoCache.put(position, bitmap);

                    // Setup the transition to the detail activity
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(MainActivity.this,
                                    coverImage, "cover");

                    startActivity(intent, options.toBundle());
                }
            }
//            startActivity(intent);
        }
    }
}
