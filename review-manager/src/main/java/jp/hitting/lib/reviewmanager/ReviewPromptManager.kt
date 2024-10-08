package jp.hitting.lib.reviewmanager

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * ReviewPromptManager supports to show review prompt (use Google Play In-App Review API).
 *
 * Show review prompt when the conditions of {@link #daysUntilPrompt} and {@link #usesUntilPrompt} are satisfied.
 */
object ReviewPromptManager {
    interface PromptListener {
        fun onComplete()
        fun onSuccess()
        fun onFailure(e: Exception)
    }

    private enum class PrefKey {
        launchedDate, launchedCount, isReviewed
    }

    /**
     * Use in development.
     *
     * If {@code true}, {@link FakeReviewManager} is used.
     */
    var IS_DEV = false

    /**
     * Use for debug.
     *
     * If {@code true}, {@link #shouldShowReviewPrompt(android.content.Context)} always returns {@code true}.
     */
    var DEBUG = false

    /**
     * Days until showing review prompt
     */
    var daysUntilPrompt = 1

    /**
     * Uses until showing review prompt
     */
    var usesUntilPrompt = 10

    private val prefName = "jp.hitting.ReviewManager"

    /**
     * Count launched app.
     *
     * Call this method from first activity.
     *
     * @param context {@link android.content.Context}
     */
    fun launched(context: Context) {
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        val count = pref.getInt(PrefKey.launchedCount.name, 0) + 1
        editor.putInt(PrefKey.launchedCount.name, count)
        if (pref.getLong(PrefKey.launchedDate.name, -1L) == -1L) {
            val now = Date()
            editor.putLong(PrefKey.launchedDate.name, now.time)
        }
        editor.apply()
    }

    /**
     * Determine whether review prompt should be showed or not.
     *
     * You can use this method directly to show your original review dialog.
     *
     * @param context {@link android.content.Context}
     *
     * @return {@code true} if {@link DEBUG} is {@code true} or,
     *              the number of launched app >= {@link usesUntilPrompt} and the number of days elapsed >= {@link daysUntilPrompt},
     *         {@code false} otherwise
     */
    fun shouldShowReviewPrompt(context: Context): Boolean {
        if (DEBUG) {
            return true
        }

        if (isReviewed(context)) {
            return false
        }
        if (getLaunchedCount(context) < usesUntilPrompt) {
            return false
        }
        val launchedDate = getLaunchedDate(context)
        if (launchedDate == -1L) {
            return false
        }
        val today = Date().time
        val diffMillis = today - launchedDate
        return TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS) >= daysUntilPrompt
    }

    /**
     * Show review prompt (use Google Play In-App Review API) if {@link #shouldShowReviewPrompt(android.content.Context)} is {@link true}.
     *
     * @param activity {@link android.app.Activity}
     * @param listener {@link #PromptListener} (optional)
     */
    fun showReviewPrompt(activity: Activity, listener: PromptListener? = null) {
        if (!shouldShowReviewPrompt(activity)) {
            return
        }

        val manager = getPlayReviewManager(activity)
        manager.requestReviewFlow().addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val reviewInfo = it.result
            manager.launchReviewFlow(activity, reviewInfo)
                .addOnCompleteListener {
                    setIsReviewed(activity, true)
                    listener?.onComplete()
                }
                .addOnSuccessListener { listener?.onSuccess() }
                .addOnFailureListener { listener?.onFailure(it) }
        }
    }

    private fun setIsReviewed(context: Context, isReviewed: Boolean) {
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        pref.edit().putBoolean(PrefKey.isReviewed.name, isReviewed).apply()
    }

    private fun isReviewed(context: Context): Boolean {
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return pref.getBoolean(PrefKey.isReviewed.name, false)
    }

    private fun getLaunchedCount(context: Context): Int {
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return pref.getInt(PrefKey.launchedCount.name, 0)
    }

    private fun getLaunchedDate(context: Context): Long {
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return pref.getLong(PrefKey.launchedDate.name, -1L)
    }

    private fun getPlayReviewManager(context: Context): ReviewManager {
        return if (IS_DEV) {
            FakeReviewManager(context)
        } else {
            ReviewManagerFactory.create(context)
        }
    }
}
