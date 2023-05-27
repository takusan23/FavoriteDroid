package io.github.takusan23.favoritedroid.tool

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** インストール済みアプリに対しての関数 */
object InstallAppTool {

    /**
     * インストール済みアプリで、ランチャーから起動できるアプリを返す。
     *
     * @param context [Context]
     * @return インストール済みアプリ
     */
    suspend fun getAllInstallAppList(context: Context): List<ResolveInfo> = withContext(Dispatchers.Default) {
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return@withContext if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.queryIntentActivities(mainIntent, PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong()))
        } else {
            context.packageManager.queryIntentActivities(mainIntent, 0)
        }.toList()
    }

    /**
     * アプリの情報を取得する
     *
     * @param context [Context]
     * @param applicationId パッケージ名
     * @return アプリ情報
     */
    suspend fun getApplicationInfo(context: Context, applicationId: String) = withContext(Dispatchers.IO) {
        return@withContext if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getApplicationInfo(applicationId, PackageManager.ApplicationInfoFlags.of(0))
        } else {
            context.packageManager.getApplicationInfo(applicationId, 0)
        }
    }

}