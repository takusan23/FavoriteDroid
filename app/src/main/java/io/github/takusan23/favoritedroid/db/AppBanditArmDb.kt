package io.github.takusan23.favoritedroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** データベース */
@Database(entities = [AppBanditArm::class], version = 1)
abstract class AppBanditArmDb : RoomDatabase() {

    /** DAO を取得する */
    abstract fun appBanditArmDao(): AppBanditArmDao

    companion object {
        private var instance: AppBanditArmDb? = null

        /**
         * データベースはシングルトンにするべきなので
         *
         * @param context [Context]
         * @return [AppBanditArmDb]
         */
        fun getInstance(context: Context): AppBanditArmDb {
            instance = instance ?: Room.databaseBuilder(context, AppBanditArmDb::class.java, "favorite-droid.db").build()
            return instance!!
        }
    }
}