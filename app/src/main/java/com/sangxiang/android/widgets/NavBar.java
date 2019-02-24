package com.sangxiang.android.widgets;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sangxiang.android.R;
import com.sangxiang.android.ui.home.MainActivity;
import com.sangxiang.android.utils.ResourcesUtils;

import java.util.HashMap;

public class NavBar extends RelativeLayout {
    private ImageView mTaoBaoIcon;
    private TextView mTaoBaoLabel;
    private ImageView mPingduoduoIcon;
    private TextView mPingduoduoLabel;
    private ImageView mProfileIcon;
    private TextView mProfileLabel;
    private ImageView mBaby;
    private View mBabyMenuBackdrop;
    private View mBabyMenuButtons;

    private int mLastActive;
    private TextView pregnancyCheckButton;
    private ImageView mJingdongIcon;
    private TextView mJingdongLabel;
    private View mTaoBao;
    private View mBgCenter;
    private FrameLayout mBabyLayout;
    private Context mContext;
    public NavBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTaoBaoIcon.setImageDrawable(null);
        mPingduoduoIcon.setImageDrawable(null);
        mProfileIcon.setImageDrawable(null);
        mBaby.setImageDrawable(null);
        mJingdongIcon.setImageDrawable(null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        mTaoBao = findViewById(R.id.nav_taobao);
        mTaoBaoIcon = (ImageView) findViewById(R.id.taobao_icon);
        mTaoBaoLabel = (TextView) findViewById(R.id.taobao_label);
        View mJingdong=findViewById(R.id.nav_jingdong);
        mJingdongIcon = (ImageView) findViewById(R.id.nav_jingdong_icon);
        mJingdongLabel = (TextView) findViewById(R.id.nav_jingdong_label);
        mBaby = (ImageView) findViewById(R.id.nav_baby);
        mBabyLayout=(FrameLayout)findViewById(R.id.nav_baby_layout);
        mBgCenter=(View)findViewById(R.id.mBgCenter);
        View mPingduoduo = findViewById(R.id.nav_pingduoduo);
        mPingduoduoIcon = (ImageView) findViewById(R.id.pingduoduo_icon);
        mPingduoduoLabel = (TextView) findViewById(R.id.pingduoduo_label);
        View mProfile = findViewById(R.id.nav_profile);
        mProfileIcon = (ImageView) findViewById(R.id.profile_icon);
        mProfileLabel = (TextView) findViewById(R.id.profile_label);
        // init views
        final Context context = getContext();
        mContext=context;
        if (context instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity) context;
            // init top buttons in baby button pop layout


            mTaoBao.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    setActive(1);
                }
            });



            mPingduoduo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActive(2);
                }
            });

            mJingdong.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActive(3);

                }
            });

            mProfile.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActive(4);
                }
            });
        }
    }


    public void setActive(int number) {
        int offColor = getResources().getColor(R.color.color_8A8A8A);
        mTaoBaoIcon.setImageResource(R.drawable.button_dashboard_normal);
        mPingduoduoIcon.setImageResource(R.drawable.button_community_normal);
        mProfileIcon.setImageResource(R.drawable.button_profile_normal);
        mJingdongIcon.setImageResource(R.drawable.icon_shangcheng0);
        mTaoBaoLabel.setTextColor(offColor);
        mPingduoduoLabel.setTextColor(offColor);
        mProfileLabel.setTextColor(offColor);
        mJingdongLabel.setTextColor(offColor);
        ((MainActivity)mContext).switchFragment(number);
        switch (number) {
            case 1:
                mTaoBaoIcon.setImageResource(R.drawable.button_dashboard_select);
                mTaoBaoLabel.setTextColor(getDefault(getContext()));
                mLastActive = number;
                break;
            case 2:
                mPingduoduoIcon.setImageResource(R.drawable.button_community_select);
                mPingduoduoLabel.setTextColor(getDefault(getContext()));
                mLastActive = number;
                break;
            case 3:
                mJingdongIcon.setImageResource(R.drawable.icon_shangcheng1);
                mJingdongLabel.setTextColor(getDefault(getContext()));
                mLastActive = number;
                break;
            case 4:
                mProfileIcon.setImageResource(R.drawable.button_profile_select);
                mProfileLabel.setTextColor(getDefault(getContext()));
                mLastActive = number;
                break;

            default:
                break;
        }
    }

    public int getActive() {
        return mLastActive;
    }

    public static int getDefault(Context context) {
        return ResourcesUtils.Companion.getColor(R.color.color_e66f7d);
    }
}
