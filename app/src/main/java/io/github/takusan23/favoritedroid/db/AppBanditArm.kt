package io.github.takusan23.favoritedroid.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * データベースに保存するエンティティ
 *
 * @param id 主キー
 * @param applicationId アプリケーションID
 * @param launchCount 起動回数
 */
@Entity(tableName = "app_bandit_arm")
data class AppBanditArm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "application_id") val applicationId: String,
    @ColumnInfo(name = "launch_count") val launchCount: Int
)