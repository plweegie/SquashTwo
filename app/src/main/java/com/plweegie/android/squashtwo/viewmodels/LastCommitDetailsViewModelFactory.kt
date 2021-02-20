package com.plweegie.android.squashtwo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.plweegie.android.squashtwo.rest.GitHubService
import javax.inject.Inject
import javax.inject.Provider


class LastCommitDetailsViewModelFactory @Inject constructor(
        private val service: Provider<GitHubService>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LastCommitDetailsViewModel(service.get()) as T
}