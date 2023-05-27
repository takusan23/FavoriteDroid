package io.github.takusan23.favoritedroid.bandit

import android.graphics.drawable.Drawable
import io.github.takusan23.favoritedroid.db.AppBanditArm

/**
 * アプリの情報とバンディットマシーンの腕の情報
 *
 * @param arm DBの Entity
 * @param percent 起動する確率
 * @param label ラベル
 * @param icon アイコン
 */
data class AppBanditData(
    val arm: AppBanditArm,
    val percent: Float,
    val label: String,
    val icon: Drawable
) {

    /** アプリケーションIDを返す */
    val applicationId: String
        get() = arm.applicationId

}