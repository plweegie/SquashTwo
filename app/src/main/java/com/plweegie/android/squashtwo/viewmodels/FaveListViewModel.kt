package com.plweegie.android.squashtwo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.plweegie.android.squashtwo.data.RepoEntry
import com.plweegie.android.squashtwo.data.RepoRepository
import kotlinx.coroutines.launch

class FaveListViewModel(private val repository: RepoRepository) : ViewModel() {

    val faveList: LiveData<List<RepoEntry>> = repository.allFavorites

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
