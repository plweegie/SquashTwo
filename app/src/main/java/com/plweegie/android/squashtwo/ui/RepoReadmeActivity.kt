package com.plweegie.android.squashtwo.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.plweegie.android.squashtwo.App
import com.plweegie.android.squashtwo.R
import com.plweegie.android.squashtwo.databinding.ActivityRepoReadmeBinding
import com.plweegie.android.squashtwo.rest.GitHubService
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject


class RepoReadmeActivity : AppCompatActivity() {

    companion object {

        private const val README_OWNER_EXTRA = "readme_owner"
        private const val README_NAME_EXTRA = "readme_name"

        @JvmStatic
        fun newIntent(context: Context, owner: String, name: String): Intent =
                Intent(context, RepoReadmeActivity::class.java).apply {
                    putExtra(README_OWNER_EXTRA, owner)
                    putExtra(README_NAME_EXTRA, name)
                }
    }

    @Inject
    lateinit var apiService: GitHubService

    private lateinit var markwon: Markwon
    private var readmeOwner: String? = null
    private var readmeName: String? = null

    private lateinit var binding: ActivityRepoReadmeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).netComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityRepoReadmeBinding.inflate(layoutInflater)
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

        markwon = Markwon.create(this)

        readmeOwner = intent.getStringExtra(README_OWNER_EXTRA)
        readmeName = intent.getStringExtra(README_NAME_EXTRA)
        updateUI()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                readmeOwner?.let {
                    try {
                        val readme = apiService.getReadme(it, readmeName!!)
                        val data = Base64.decode(readme.content, Base64.DEFAULT)

                        withContext(Dispatchers.Main) {
                            markwon.setMarkdown(binding.readmeContentTv, String(data, Charsets.UTF_8))
                        }
                    } catch(e: HttpException) {
                        markwon.setMarkdown(binding.readmeContentTv, getString(R.string.no_readme_found))
                    }
                }
            }
        }
    }
}