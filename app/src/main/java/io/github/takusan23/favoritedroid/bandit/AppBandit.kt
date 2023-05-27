package io.github.takusan23.favoritedroid.bandit

import android.content.Context
import io.github.takusan23.favoritedroid.db.AppBanditArm
import io.github.takusan23.favoritedroid.db.AppBanditArmDb
import io.github.takusan23.favoritedroid.tool.InstallAppTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

object AppBandit {

    /** 捜索をする割合 */
    private const val EPSILON = 0.3f

    /**
     * 起動しそうなアプリ一覧を返す。
     * バンディットマシーンのアームを引いて出たもの。
     */
    suspend fun getAppBanditList(context: Context, size: Int) = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val dao = AppBanditArmDb.getInstance(context).appBanditArmDao()
        // 空っぽの場合はとりあえず全部いれる
        if (dao.getAllCount() == 0) {
            val appList = InstallAppTool.getAllInstallAppList(context).map { resolveInfo ->
                AppBanditArm(
                    applicationId = resolveInfo.activityInfo.packageName,
                    launchCount = 0
                )
            }
            dao.insertAll(appList)
        }
        // 取り出す
        val appBanditList = dao.getAll()
        // データベースからアプリケーションIDとその起動回数を取得できたので、
        // あとは起動する確率（パーセント。起動回数をトータル起動回数で割る）を出す
        val allLaunchCount = dao.getAllLaunchCount()
        val appBanditDataList = appBanditList.map {
            val info = InstallAppTool.getApplicationInfo(context, it.applicationId)
            AppBanditData(
                arm = it,
                label = info.loadLabel(packageManager).toString(),
                icon = info.loadIcon(packageManager),
                // 0 で割るな
                percent = if (allLaunchCount == 0) 0f else (it.launchCount / allLaunchCount.toFloat())
            )
        }
        // バンディット問題に向き合う
        // 捜索と活用を繰り返す
        // size の数だけアプリを返す
        val banditList = arrayListOf<AppBanditData>().also { buildList ->
            repeat(size) {
                // すでに追加済みのアプリを再度選ばないようにする
                val filterList = appBanditDataList.filter { origin -> !buildList.any { it.applicationId == origin.applicationId } }
                // 捜索と活用、どちらを行うか
                buildList += if (randomBoolean(EPSILON)) {
                    // 捜索を行う。適当に選ぶ
                    filterList.random()
                } else {
                    // 活用を行う。起動する可能性があるものを順に選ぶ
                    filterList.maxBy { it.percent }
                }
            }
        }.toList()
        return@withContext banditList
    }

    /**
     * 起動したことを記録する。
     * つまりバンディットマシーンの腕に報酬を与える。
     *
     * @param appBanditArm 起動したアプリ
     */
    suspend fun rewardAppLaunch(context: Context, appBanditArm: AppBanditArm) = withContext(Dispatchers.IO) {
        val updateData = appBanditArm.copy(launchCount = appBanditArm.launchCount + 1)
        AppBanditArmDb.getInstance(context).appBanditArmDao().update(updateData)
    }

    /**
     * 確率で true を返す
     *
     * @param percent この確率で true
     * @return true / false
     */
    private fun randomBoolean(percent: Float): Boolean {
        return percent > Random.nextFloat()
    }

}