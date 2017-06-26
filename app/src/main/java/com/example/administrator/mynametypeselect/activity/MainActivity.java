package com.example.administrator.mynametypeselect.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.mynametypeselect.R;
import com.example.administrator.mynametypeselect.adapter.ViewPageAdapter;
import com.example.administrator.mynametypeselect.common.events.ChangeMediaTypeTabEvent;
import com.example.administrator.mynametypeselect.common.utils.UIUtils;
import com.example.administrator.mynametypeselect.common.views.MediaTypeNameView;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MediaTypeNameView> mediaTypeNameViews = new ArrayList<MediaTypeNameView>();
    private ViewPager vp_medic_type;
    private ViewPageAdapter mViewPagerAdapter;
    private List<View> ViewPagerViews = new ArrayList<View>();
    private int allTabWidth = 0;
    private int view_width = 300;
    private HorizontalScrollView hlv_medic_category;
    private LinearLayout ll_medic_category_views;
    public int dmw, dmh;
    public DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        findViewById();
        initData();
    }

    private void findViewById() {
        hlv_medic_category = (HorizontalScrollView) findViewById(R.id.hlv_medic_category);
        ll_medic_category_views = (LinearLayout) findViewById(R.id.ll_medic_category_views);
        vp_medic_type = (ViewPager) findViewById(R.id.vp_medic_type);
    }

    private void initData() {
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dmw = dm.widthPixels;
        dmh = dm.heightPixels;

        List<String> mDataList = new ArrayList<>();
        mDataList.add("手机视频");
        mDataList.add("手机照片");
        mDataList.add("微信照片");
        mDataList.add("QQ照片");
        mDataList.add("截图");

        getAllWidth(mDataList);
    }


    public void getAllWidth(final List<String> dataList) {
        /**提前预先得到总的宽度*/
        mediaTypeNameViews.clear();
        ll_medic_category_views.removeAllViews();
        //拿到ItemTab 的布局，获取布局里面的 TextView
        View convertView = LayoutInflater.from(this).inflate(R.layout.media_type_item_bucket, null);
        TextView itemTextView = (TextView) convertView.findViewById(R.id.tv_name);

        allTabWidth = 0;
        /**获取TeXtView,提前预先得到总的宽度,后面的值是布局里面Ui的  marginLeft + marginRight */
        for (String bean : dataList) {
            allTabWidth += (int) itemTextView.getPaint().measureText(bean) + 2 * UIUtils.dp2px(16);
        }
        LayoutInflater mInflater = LayoutInflater.from(this);
        View[] tab_medic_type_name = new View[dataList.size()];
        TextView[] lv_medic_name_type = new TextView[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            final int tab = i;
            MediaTypeNameView mediaTypeNameView = new MediaTypeNameView(this, dataList.get(i), i);
            mediaTypeNameViews.add(mediaTypeNameView);
            mediaTypeNameViews.get(0).setSelectedStatus(true);
            initItemWidth(dataList.get(i), mediaTypeNameView);
            ll_medic_category_views.addView(mediaTypeNameView.convertView);
            tab_medic_type_name[i] = mInflater.inflate(R.layout.tab_media_type_name, null);
            ViewPagerViews.add(tab_medic_type_name[i]);
            lv_medic_name_type[i] = (TextView) tab_medic_type_name[i].findViewById(R.id.lv_medic_name_type);
            lv_medic_name_type[i].setText(dataList.get(i));
        }
        mViewPagerAdapter = new ViewPageAdapter(ViewPagerViews);
        vp_medic_type.setAdapter(mViewPagerAdapter);
        vp_medic_type.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mediaTypeNameViews.size(); i++) {
                    if (position == i) {
                        mediaTypeNameViews.get(i).setSelectedStatus(true);
                    } else {
                        mediaTypeNameViews.get(i).setSelectedStatus(false);
                    }
                }

                if (position == 0) {
                    hlv_medic_category.scrollTo(0, 0);
                } else if (position >= 1) {
                    hlv_medic_category.smoothScrollTo(view_width * (position - 1), 0);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    private void initItemWidth(String mediaNameType, MediaTypeNameView mMediaTypeNameView) {
        /**得到这个textview 显示这个字符串需要的宽度*/
        int itemWidth = (int) mMediaTypeNameView.tv_name.getPaint().measureText(mediaNameType) + 2 * UIUtils.dp2px(16);
        if (allTabWidth < dmw) {
            /**不超过屏幕宽度的时候，进行权重比分配*/
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dmw * itemWidth / allTabWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mMediaTypeNameView.convertView.setLayoutParams(layoutParams);
        }
    }


    @Subscribe
    public void onEventMainThread(ChangeMediaTypeTabEvent event) {
        // 点击medicType
        if (event.position == 0) {
            hlv_medic_category.scrollTo(0, 0);
        } else if (event.position >= 1) {
            view_width = event.view_width;
            hlv_medic_category.smoothScrollTo(event.view_width * (event.position - 1), 0);
        }

        mediaTypeNameViews.get(event.position).setSelectedStatus(true);
        vp_medic_type.setCurrentItem(event.position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
