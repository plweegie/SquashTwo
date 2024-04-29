package com.plweegie.android.squashtwo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.android.squashtwo.data.Commit
import com.plweegie.android.squashtwo.data.RepoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LastCommitDetailsViewModel(private val repository: RepoRepository) : ViewModel() {

    sealed class LoadingState {
        object Idle : LoadingState()
        object Loading : LoadingState()
        class Failed(val exception: Exception) : LoadingState()
        class Succeeded(val result: Commit) : LoadingState()
    }

    val loadingState: StateFlow<LoadingState>
        get() = _loadingState

    private val _loadingState: MutableStateFlow<LoadingState> =
        MutableStateFlow(LoadingState.Idle)

    fun getLastCommit(userName: String, repoName: String) {
        viewModelScope.launch {
            val result = repository.getLastCommit(userName, repoName)
            _loadingState.value = LoadingState.Succeeded(result)
        }
    }
}