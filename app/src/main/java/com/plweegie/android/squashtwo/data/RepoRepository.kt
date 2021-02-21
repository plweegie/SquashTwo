package com.plweegie.android.squashtwo.data

import androidx.lifecycle.LiveData
import com.plweegie.android.squashtwo.rest.GitHubService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoRepository @Inject constructor(database: RepoDatabase,
                                         private val network: GitHubService) {

    private val repoDao: RepoDao = database.repoDao()

    val allFavorites: LiveData<List<RepoEntry>>
        get() = repoDao.favorites

    suspend fun fetchRepos(userName: String, page: Int) =
            network.getRepos(userName, page)

    suspend fun getLastCommit(userName: String, repoName: String): Commit =
            network.getCommits(userName, repoName, 5)
                    .first { !it.commitBody.message.startsWith("Merge pull") }

    suspend fun getFavoritesAsync() = repoDao.getFavoritesAsync()

    suspend fun addFavorite(repo: RepoEntry) {
       repoDao.insertFavorite(repo)
    }

    suspend fun deleteRepo(repoId: Long) {
       repoDao.deleteSelected(repoId)
    }

    suspend fun deleteAllRepos() {
        repoDao.deleteAll()
    }
}
