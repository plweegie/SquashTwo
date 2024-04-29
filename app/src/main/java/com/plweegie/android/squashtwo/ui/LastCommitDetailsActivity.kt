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

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.plweegie.android.squashtwo.App
import com.plweegie.android.squashtwo.R
import com.plweegie.android.squashtwo.data.Commit
import com.plweegie.android.squashtwo.databinding.ActivityLastCommitDetailsBinding
import com.plweegie.android.squashtwo.rest.GitHubService
import com.plweegie.android.squashtwo.utils.DateUtils
import com.plweegie.android.squashtwo.viewmodels.LastCommitDetailsViewModel
import com.plweegie.android.squashtwo.viewmodels.LastCommitDetailsViewModelFactory
import kotlinx.coroutines.launch
import java.text.ParseException
import javax.inject.Inject

class LastCommitDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var service: GitHubService

    @Inject
    lateinit var viewModelFactory: LastCommitDetailsViewModelFactory

    private val viewModel by viewModels<LastCommitDetailsViewModel> { viewModelFactory }
    private var repoProps: Array<String> = arrayOf()

    private lateinit var binding: ActivityLastCommitDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).netComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityLastCommitDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorToolbar)

        setSupportActionBar(binding.mainToolbar)

        repoProps = intent.getStringArrayExtra(EXTRA_REPO_PROPS) ?: arrayOf()

        if (savedInstanceState != null && savedInstanceState.containsKey(TEXT_VIEW_CONTENTS)) {
            binding.commitMessageTv.text = savedInstanceState.getCharSequenceArray(TEXT_VIEW_CONTENTS)!![0]
            binding.commitInfoTv.text = savedInstanceState.getCharSequenceArray(TEXT_VIEW_CONTENTS)!![1]
            binding.commitDateTv.text = savedInstanceState.getCharSequenceArray(TEXT_VIEW_CONTENTS)!![2]
            return
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequenceArray(TEXT_VIEW_CONTENTS, arrayOf(
                binding.commitMessageTv.text,
                binding.commitInfoTv.text,
                binding.commitDateTv.text))
    }

    private fun updateUI() {
        viewModel.getLastCommit(repoProps[0], repoProps[1])

        lifecycleScope.launch {
            viewModel.loadingState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is LastCommitDetailsViewModel.LoadingState.Succeeded -> {
                            binding.commitMessageTv.text = it.result.commitBody.message
                                .split("\n").toTypedArray()[0]
                            binding.commitInfoTv.text = buildCommitInfo(it.result)
                            binding.commitDateTv.text = buildCommitDate(it.result)
                        }
                        is LastCommitDetailsViewModel.LoadingState.Failed -> {
                            Log.e("LastCommitDetailsActivity", "Error loading commit: ${it.exception}")
                        }
                        else -> {
                            Log.d("LastCommitDetailsActivity", "Loading commit...")
                        }
                    }
                }
        }
    }

    private fun buildCommitInfo(commit: Commit): CharSequence {
        val authorId = commit.commitBody.commitBodyAuthor.name
        val info = this.resources.getString(R.string.commit_info, authorId)

        return Html.fromHtml(info, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun buildCommitDate(commit: Commit): CharSequence {
        val date = commit.commitBody.committer.date
        var formattedDate: String? = ""

        try {
            formattedDate = DateUtils.changeDateFormats(date)
        } catch (e: ParseException) {
            Log.e("LastCommitDetailsActivity", "Date parser error")
        }
        return resources.getString(R.string.commit_date, formattedDate)
    }

    companion object {
        private const val TEXT_VIEW_CONTENTS = "textViewContents"
        private const val EXTRA_REPO_PROPS = "repoPropsExtra"

        fun newIntent(packageContext: Context, repoProps: Array<String>?): Intent =
                Intent(packageContext, LastCommitDetailsActivity::class.java).apply {
                    putExtra(EXTRA_REPO_PROPS, repoProps)
                }
    }
}