package io.github.takusan23.favoritedroid

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import io.github.takusan23.favoritedroid.widget.FavoriteDroidWidgetService

/** バンディット問題を使ってアプリアイコンをウィジェットに表示する */
class FavoriteDroidWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // do nothing
    }

    override fun onDisabled(context: Context) {
        // do nothing
    }

    companion object {

        /** ウィジェットを更新する */
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.favorite_droid_widget).apply {
                val remoteViewsFactoryIntent = Intent(context, FavoriteDroidWidgetService::class.java)
                setRemoteAdapter(R.id.widget_grid_view, remoteViewsFactoryIntent)
                val pendingIntent = FavoriteDroidAppStartActivity.createIntent(context).let {
                    PendingIntent.getActivity(context, 10, it, PendingIntent.FLAG_MUTABLE) // MUTABLE じゃないと Adapter 側のが反映されない
                }
                setPendingIntentTemplate(R.id.widget_grid_view, pendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

    }
}
