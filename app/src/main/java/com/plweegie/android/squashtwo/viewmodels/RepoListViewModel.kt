package com.plweegie.android.squashtwo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.android.squashtwo.data.RepoEntry
import com.plweegie.android.squashtwo.data.RepoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class RepoListViewModel(private val repository: RepoRepository) : ViewModel() {

    sealed class LoadingState {
        object Idle : LoadingState()
        object Loading : LoadingState()
        class Failed(val exception: Exception) : LoadingState()
        class Succeeded(val repos: List<RepoEntry>?) : LoadingState()
    }

    val loadingState: StateFlow<LoadingState>
        get() = _loadingState

    private val _loadingState: MutableStateFlow<LoadingState> =
        MutableStateFlow(LoadingState.Idle)

    fun fetchData(userName: String, page: Int) {
        _loadingState.value = LoadingState.Loading

        launchInScope {
            try {
                val result = repository.fetchRepos(userName, page)
                _loadingState.value = LoadingState.Succeeded(result)
            } catch (e: Exception) {
                _loadingState.value = LoadingState.Failed(e)
            }
        }
    }

    fun addFavorite(repo: RepoEntry) {
        launchInScope { repository.addFavorite(repo) }
    }

    private fun launchInScope(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }
}