package com.plweegie.android.squashtwo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.plweegie.android.squashtwo.data.RepoRepository
import javax.inject.Inject
import javax.inject.Provider


class LastCommitDetailsViewModelFactory @Inject constructor(
        private val repository: Provider<RepoRepository>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LastCommitDetailsViewModel(repository.get()) as T
}