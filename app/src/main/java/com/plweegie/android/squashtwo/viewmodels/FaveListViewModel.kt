package com.plweegie.android.squashtwo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.android.squashtwo.data.RepoEntry
import com.plweegie.android.squashtwo.data.RepoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FaveListViewModel(
    private val repository: RepoRepository
) : ViewModel() {

    val faveList: StateFlow<List<RepoEntry>> = repository.allFavorites
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList())

    fun deleteRepo(repoId: Long) {
        viewModelScope.launch {
            repository.deleteRepo(repoId)
        }
    }

    fun deleteAllRepos() {
        viewModelScope.launch {
            repository.deleteAllRepos()
        }
    }
}
