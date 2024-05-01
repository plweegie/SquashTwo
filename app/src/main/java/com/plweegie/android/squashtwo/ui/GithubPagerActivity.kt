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
package com.plweegie.android.squashtwo.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.plweegie.android.squashtwo.R
import com.plweegie.android.squashtwo.adapters.GithubPagerAdapter
import com.plweegie.android.squashtwo.databinding.ActivityGithubPagerBinding
import com.plweegie.android.squashtwo.utils.AuthUtils
import com.plweegie.android.squashtwo.utils.WorkManagerUtil.Companion.enqueueWorkRequest

class GithubPagerActivity : VisibleActivity() {

    private val tabTitles = intArrayOf(R.string.list_repos, R.string.list_faves)

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            enqueueWorkRequest(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityGithubPagerBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController!!.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorToolbar)

        setSupportActionBar(binding.mainToolbar)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        if (!prefs.contains(AuthUtils.PREFERENCE_NAME)) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val tabLayout = findViewById<TabLayout>(R.id.list_tabs)
        val adapter = GithubPagerAdapter(this@GithubPagerActivity)

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1

        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int -> tab.setText(resources.getString(tabTitles[position])) }.attach()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            enqueueWorkRequest(this)
        }
    }
}
