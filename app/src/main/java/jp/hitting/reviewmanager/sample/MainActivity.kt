package jp.hitting.reviewmanager.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.hitting.reviewmanager.ReviewPromptManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ReviewPromptManager.launched(this)
    }

    override fun onStart() {
        super.onStart()
        ReviewPromptManager.showReviewPrompt(this)
    }
}
