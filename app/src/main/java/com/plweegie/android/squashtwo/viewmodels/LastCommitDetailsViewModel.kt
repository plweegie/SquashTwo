package com.plweegie.android.squashtwo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.android.squashtwo.data.Commit
import com.plweegie.android.squashtwo.data.RepoRepository
import kotlinx.coroutines.launch


class LastCommitDetailsViewModel(private val repository: RepoRepository) : ViewModel() {

    val lastCommit: LiveData<Commit>
        get() = _lastCommit

    private val _lastCommit: MutableLiveData<Commit> = MutableLiveData()

    fun getLastCommit(userName: String, repoName: String) {
        viewModelScope.launch {
            val result = repository.getLastCommit(userName, repoName)
            _lastCommit.postValue(result)
        }
    }
}