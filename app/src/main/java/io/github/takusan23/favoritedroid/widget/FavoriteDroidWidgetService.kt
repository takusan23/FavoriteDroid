package io.github.takusan23.favoritedroid.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import io.github.takusan23.favoritedroid.FavoriteDroidAppStartActivity
import io.github.takusan23.favoritedroid.R
import io.github.takusan23.favoritedroid.bandit.AppBandit
import io.github.takusan23.favoritedroid.bandit.AppBanditData
import io.github.takusan23.favoritedroid.tool.AppIconTool
import kotlinx.coroutines.runBlocking

/** ウィジェットの GridView で使う Adapter */
class FavoriteDroidWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return FavoriteDroidWidgetViewFactory(this.applicationContext)
    }

    class FavoriteDroidWidgetViewFactory(private val context: Context) : RemoteViewsFactory {

        private val appBanditList = arrayListOf<AppBanditData>()

        companion object {
            /** 表示するアイテム数 */
            private const val GRIDVIEW_ITEM_COUNT = 10
        }

        override fun onCreate() {
            // do nothing
        }

        override fun onDataSetChanged() {
            // 多分同期処理してもいいはず
            runBlocking {
                appBanditList.clear()
                appBanditList += AppBandit.getAppBanditList(context, GRIDVIEW_ITEM_COUNT)
            }
        }

        override fun getCount(): Int {
            return appBanditList.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val data = appBanditList[position]
            return RemoteViews(context.packageName, R.layout.favorite_droid_widget_item).apply {
                setImageViewBitmap(R.id.favorite_droid_widget_item_image_view, AppIconTool.createIconBitmap(context, data.icon))
                setTextViewText(R.id.favorite_droid_widget_item_text_view, data.label)
                // 押したらアプリを起動
                // アプリを起動するんだけど、データベースに起動回数をインクリメントするため、一旦自前の画面を通ってから
                val intent = FavoriteDroidAppStartActivity.setParameter(Intent(), data.applicationId)
                setOnClickFillInIntent(R.id.favorite_droid_widget_item_parent, intent)
            }
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun onDestroy() {
            // do nothing
        }
    }
}
