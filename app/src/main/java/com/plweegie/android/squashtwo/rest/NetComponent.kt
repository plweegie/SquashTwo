package com.plweegie.android.squashtwo.rest


import com.plweegie.android.squashtwo.AppModule
import com.plweegie.android.squashtwo.data.RoomModule
import com.plweegie.android.squashtwo.services.CommitPollWorker
import com.plweegie.android.squashtwo.ui.FaveListFragment
import com.plweegie.android.squashtwo.ui.LastCommitDetailsActivity
import com.plweegie.android.squashtwo.ui.RepoListFragment
import com.plweegie.android.squashtwo.ui.RepoReadmeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class, SharedPrefModule::class, RoomModule::class])
interface NetComponent {
    fun inject(lastCommitDetailsActivity: LastCommitDetailsActivity)
    fun inject(activity: RepoReadmeActivity)
    fun inject(fragment: RepoListFragment)
    fun inject(fragment: FaveListFragment)
    fun inject(worker: CommitPollWorker)
}
