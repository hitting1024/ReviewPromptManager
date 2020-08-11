package jp.hitting.reviewmanager.sample

import android.app.Application
import jp.hitting.reviewmanager.ReviewPromptManager

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        ReviewPromptManager.daysUntilPrompt = 1
        ReviewPromptManager.usesUntilPrompt = 10
//        ReviewPromptManager.IS_DEV = true
//        ReviewPromptManager.DEBUG = true
    }
}
