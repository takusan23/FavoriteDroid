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

    /** 更新ボタンの受け取り */
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return
        when (intent?.getStringExtra(KEY_BROADCAST_TYPE)?.let { BroadcastType.find(it) }) {
            BroadcastType.UPDATE -> updateAppWidget(context)
            else -> {
                // do nothing
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateAppWidget(context)
    }

    override fun onEnabled(context: Context) {
        // do nothing
    }

    override fun onDisabled(context: Context) {
        // do nothing
    }

    /** ブロードキャストの操作。更新操作しか無いけど */
    private enum class BroadcastType(val action: String) {
        UPDATE("io.github.takusan23.favoritedroid.widget.FavoriteDroidWidget.UPDATE");

        companion object {
            fun find(action: String) = values().firstOrNull { it.action == action }
        }
    }

    companion object {

        private const val KEY_BROADCAST_TYPE = "type"

        /** ウィジェットを更新する */
        fun updateAppWidget(context: Context) {
            val views = RemoteViews(context.packageName, R.layout.favorite_droid_widget).apply {
                val remoteViewsFactoryIntent = Intent(context, FavoriteDroidWidgetService::class.java)
                setRemoteAdapter(R.id.widget_grid_view, remoteViewsFactoryIntent)
                // GridViewのセルを押した時
                val templatePendingIntent = FavoriteDroidAppStartActivity.createIntent(context).let {
                    PendingIntent.getActivity(context, 10, it, PendingIntent.FLAG_MUTABLE) // MUTABLE じゃないと Adapter 側のが反映されない
                }
                setPendingIntentTemplate(R.id.widget_grid_view, templatePendingIntent)
                // 更新ボタン。このクラスへブロードキャストを送る
                val updateBroadcastPendingIntent = Intent(context, FavoriteDroidWidget::class.java)
                    .apply { putExtra(KEY_BROADCAST_TYPE, BroadcastType.UPDATE.action) }
                    .let { PendingIntent.getBroadcast(context, 11, it, PendingIntent.FLAG_IMMUTABLE) }
                setOnClickPendingIntent(R.id.widget_update_button, updateBroadcastPendingIntent)
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
