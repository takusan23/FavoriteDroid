package io.github.takusan23.favoritedroid.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/** データベースにアクセスする関数 */
@Dao
interface AppBanditArmDao {

    /** 全件取得 */
    @Query("SELECT * FROM app_bandit_arm")
    suspend fun getAll(): List<AppBanditArm>

    /** 追加する */
    @Insert
    suspend fun insertAll(arm: List<AppBanditArm>)

    /** 削除する */
    @Delete
    suspend fun delete(arm: AppBanditArm)

    /** 更新する */
    @Update
    suspend fun update(arm: AppBanditArm)

    /** 合計数を出す */
    @Query("SELECT COUNT(*) FROM app_bandit_arm")
    suspend fun getAllCount(): Int

    /** アプリケーションIDから [AppBanditArm] を取得する */
    @Query("SELECT * FROM app_bandit_arm WHERE application_id = :applicationId")
    suspend fun getArmFromApplicationId(applicationId: String): AppBanditArm

    /** すべてのアプリの起動回数を合計する */
    @Query("SELECT SUM(launch_count) FROM app_bandit_arm")
    suspend fun getAllLaunchCount(): Int
}
