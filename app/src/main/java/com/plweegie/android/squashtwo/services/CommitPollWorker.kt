package com.plweegie.android.squashtwo.services

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.plweegie.android.squashtwo.App
import com.plweegie.android.squashtwo.R
import com.plweegie.android.squashtwo.data.Commit
import com.plweegie.android.squashtwo.data.RepoRepository
import com.plweegie.android.squashtwo.rest.GitHubService
import com.plweegie.android.squashtwo.ui.LastCommitDetailsActivity
import com.plweegie.android.squashtwo.utils.DateUtils
import com.plweegie.android.squashtwo.utils.QueryPreferences
import java.text.ParseException
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class CommitPollWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    @Inject
    lateinit var service: GitHubService

    @Inject
    lateinit var queryPrefs: QueryPreferences

    @Inject
    lateinit var dataRepository: RepoRepository

    private val commits: MutableList<Commit>

    companion object {
        private const val CHANNEL_ID = "com.plweegie.android.squashtwo"
        const val ACTION_SHOW_NOTIFICATION = "com.plweegie.android.squashtwo.SHOW_NOTIFICATION"
        const val REQUEST_CODE = "request_code"
        const val NOTIFICATION = "notification"
    }

    init {
        (applicationContext as App).netComponent.inject(this)
        commits = ArrayList()
        createNotificationChannel()
    }

    override suspend fun doWork(): Result {
        val repos = dataRepository.getFavoritesAsync()

        return try {
            repos.forEach { repo ->
                val foundCommit = service.getCommits(repo.owner.login, repo.name, 5)
                    .filterNot { it.commitBody.message.startsWith("Merge pull") }
                    .first()
                commits.add(foundCommit)
            }
            processCommits(commits)
            Result.success()
        } catch (e: Exception) {
            Log.e("WORKER", "Error fetching commits: $e")
            Result.failure()
        }
    }

    private fun processCommits(commits: List<Commit>) {
        val lastDate = queryPrefs.lastResultDate
        var newLastDate = 0L

        Collections.sort(commits, QueryPreferences.CommitCreatedComparator())

        try {
            newLastDate = if (commits.isEmpty()) lastDate else
                DateUtils.convertToTimestamp(commits[0].commitBody.committer.date)
        } catch (e: ParseException) {
            Log.e("CommitPollWorker", "Error parsing date: $e")
        }

        if (newLastDate > lastDate) {
            val requestCode = Random.nextInt()

            val updatedCommit = commits[0]
            val splitCommit = updatedCommit.htmlUrl.split("/")
            val commitRepo = splitCommit[4]
            val commitOwner = splitCommit[3]

            val commitDetailsIntent = LastCommitDetailsActivity.newIntent(context,
                    arrayOf(commitOwner, commitRepo))
            val commitPendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                commitDetailsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setTicker(context.getString(R.string.new_commit_headline))
                .setContentTitle(context.getString(R.string.new_commit_headline))
                .setContentText(commitRepo)
                .setContentIntent(commitPendingIntent)
                .setSmallIcon(R.drawable.ic_info_24dp)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            showBackgroundNotif(requestCode, notification)
            queryPrefs.lastResultDate = newLastDate
        }
    }

    private fun showBackgroundNotif(requestCode: Int, notif: Notification) {
        val intent = Intent(ACTION_SHOW_NOTIFICATION).apply {
            putExtra(REQUEST_CODE, requestCode)
            putExtra(NOTIFICATION, notif)
            setPackage(context.packageName)
        }

        context.sendOrderedBroadcast(intent, null, null, null, Activity.RESULT_OK, null, null)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "SquashTwo"
            val descriptionText = "Recent commit notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
