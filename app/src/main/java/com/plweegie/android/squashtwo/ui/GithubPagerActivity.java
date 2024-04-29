/*
 * Copyright (c) 2017 Jan K Szymanski

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.plweegie.android.squashtwo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.plweegie.android.squashtwo.R;
import com.plweegie.android.squashtwo.adapters.GithubPagerAdapter;
import com.plweegie.android.squashtwo.databinding.ViewPagerBinding;
import com.plweegie.android.squashtwo.utils.AuthUtils;
import com.plweegie.android.squashtwo.utils.WorkManagerUtil;

public class GithubPagerActivity  extends VisibleActivity {
    
    private ViewPager mViewPager;
    private GithubPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private SharedPreferences mPrefs;
    private ViewPagerBinding mBinding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ViewPagerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getInsetsController().setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            );
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorToolbar));

        setSupportActionBar(mBinding.mainToolbar);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(!mPrefs.contains(AuthUtils.PREFERENCE_NAME)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        
        mViewPager = findViewById(R.id.pager);
        mTabLayout = findViewById(R.id.list_tabs);
        mAdapter = new GithubPagerAdapter(getSupportFragmentManager(),
                GithubPagerActivity.this);
        
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        
        mTabLayout.setupWithViewPager(mViewPager);

        WorkManagerUtil.enqueueWorkRequest(this);
    }
}
