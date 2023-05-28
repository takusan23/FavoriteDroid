package io.github.takusan23.favoritedroid.tool

import android.R
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

/** アイコンの関数たち */
object AppIconTool {

    /**
     * アイコンの Bitmap を作成する
     *
     * @param context [Context]
     * @param drawable アイコンの[Drawable]。
     * @see [android.content.pm.ActivityInfo.loadIcon]
     * @return アイコン画像。テーマアイコンに対応していればテーマアイコンを返す
     */
    fun createIconBitmap(context: Context, drawable: Drawable): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && drawable is AdaptiveIconDrawable && drawable.monochrome != null) {
            val (backgroundColor, foregroundColor) = getThemeIconColorPair(context)
            val monochromeIcon = drawable.monochrome!!.apply {
                mutate()
                setTint(foregroundColor)
            }
            AdaptiveIconDrawable(ColorDrawable(backgroundColor), monochromeIcon).toBitmap()
        } else {
            drawable.toBitmap()
        }
    }

    /**
     * テーマアイコンの時のバックグラウンド・フォアグラウンドの色を取得する
     * https://cs.android.com/android/platform/superproject/+/refs/heads/master:frameworks/libs/systemui/iconloaderlib/src/com/android/launcher3/icons/ThemedIconDrawable.java
     *
     * @param context [Context]
     * @return バックグラウンド・フォアグラウンドのPair
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun getThemeIconColorPair(context: Context): Pair<Int, Int> {
        return if (context.resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(context, R.color.system_neutral1_800) to ContextCompat.getColor(context, R.color.system_accent1_100)
        } else {
            ContextCompat.getColor(context, R.color.system_accent1_100) to ContextCompat.getColor(context, R.color.system_neutral2_700)
        }
    }

}