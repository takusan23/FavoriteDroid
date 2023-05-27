package io.github.takusan23.favoritedroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.takusan23.favoritedroid.ui.screen.MainScreen
import io.github.takusan23.favoritedroid.ui.theme.FavoriteDroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FavoriteDroidTheme {
                MainScreen()
            }
        }
    }
}
