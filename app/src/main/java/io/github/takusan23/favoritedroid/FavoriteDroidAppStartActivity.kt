package io.github.takusan23.favoritedroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import io.github.takusan23.favoritedroid.db.AppBanditArmDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ウィジェットからアプリを起動する際に、まずこの画面を通る（データベースに起動カウントを足すため）
 */
class FavoriteDroidAppStartActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // アプリケーションIDから起動
        val launchApplicationId = intent?.getStringExtra(KEY_LAUNCH_APPLICATION_ID)!!
        println("launchApplicationId = $launchApplicationId")
        val launchIntent = packageManager.getLaunchIntentForPackage(launchApplicationId)
        if (launchIntent != null) {
            startActivity(launchIntent)

            // 起動カウントを足す
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val dao = AppBanditArmDb.getInstance(this@FavoriteDroidAppStartActivity).appBanditArmDao()
                    val entity = dao.getArmFromApplicationId(launchApplicationId)
                    dao.update(entity.copy(launchCount = entity.launchCount + 1))
                }
                // この Activity は不要になるので終了
                finishAndRemoveTask()
            }
        }
    }

    companion object {

        private const val KEY_LAUNCH_APPLICATION_ID = "launch_application_id"

        /**
         * [Intent]に[launchApplicationId]をセットする
         *
         * @param launchApplicationId 起動したいアプリの applicationId
         * @return [Intent]
         */
        fun setParameter(intent: Intent, launchApplicationId: String): Intent {
            return intent.apply {
                putExtra(KEY_LAUNCH_APPLICATION_ID, launchApplicationId)
            }
        }

        /**
         * [Intent]を返す
         *
         * @param context [Context]
         * @return [Intent]
         */
        fun createIntent(context: Context): Intent {
            return Intent(context, FavoriteDroidAppStartActivity::class.java)
        }

    }

}