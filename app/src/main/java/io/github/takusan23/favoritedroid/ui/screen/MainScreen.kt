package io.github.takusan23.favoritedroid.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import io.github.takusan23.favoritedroid.bandit.AppBandit
import io.github.takusan23.favoritedroid.bandit.AppBanditData
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val list = remember { mutableStateOf(emptyList<AppBanditData>()) }

    LaunchedEffect(key1 = Unit) {
        list.value = AppBandit.getAppBanditList(context, 8)
    }

    LazyColumn {
        item {
            Button(onClick = {
                scope.launch {
                    list.value = AppBandit.getAppBanditList(context, 8)
                }
            }) { Text(text = "再取得") }
        }
        items(list.value) { item ->
            Surface(onClick = {
                scope.launch {
                    AppBandit.rewardAppLaunch(context, item.arm)
                    list.value = AppBandit.getAppBanditList(context, 8)
                }
            }) {
                Column {
                    Text(text = item.label)
                    Text(text = item.percent.toString())
                    Divider()
                }
            }
        }
    }

}