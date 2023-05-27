package io.github.takusan23.favoritedroid.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import io.github.takusan23.favoritedroid.FavoriteDroidAppStartActivity
import io.github.takusan23.favoritedroid.R

/** バンディット問題を使ってアプリアイコンをウィジェットに表示する */
class FavoriteDroidWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateAppWidget(context)
    }

    override fun onEnabled(context: Context) {
        // do nothing
    }

    override fun onDisabled(context: Context) {
        // do nothing
    }

    companion object {

        /** ウィジェットを更新する */
        fun updateAppWidget(context: Context) {
            val views = RemoteViews(context.packageName, R.layout.favorite_droid_widget).apply {
                val remoteViewsFactoryIntent = Intent(context, FavoriteDroidWidgetService::class.java)
                setRemoteAdapter(R.id.widget_grid_view, remoteViewsFactoryIntent)
                // アプリを押した時
                val pendingIntent = FavoriteDroidAppStartActivity.createIntent(context).let {
                    PendingIntent.getActivity(context, 10, it, PendingIntent.FLAG_MUTABLE) // MUTABLE じゃないと Adapter 側のが反映されない
                }
                setPendingIntentTemplate(R.id.widget_grid_view, pendingIntent)
            }

            // 更新
            val componentName = ComponentName(context, FavoriteDroidWidget::class.java)
            val manager = AppWidgetManager.getInstance(context)
            manager.getAppWidgetIds(componentName).forEach { id ->
                manager.updateAppWidget(id, views)
                // ListView更新
                manager.notifyAppWidgetViewDataChanged(id, R.id.widget_grid_view)
            }
        }

    }
}
