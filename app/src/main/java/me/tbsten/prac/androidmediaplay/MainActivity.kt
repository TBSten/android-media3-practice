package me.tbsten.prac.androidmediaplay

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import me.tbsten.prac.androidmediaplay.ui.theme.AndroidMediaPlayPracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidMediaPlayPracticeTheme {
                PlayerScreen()
            }
        }
    }
}

@Composable
internal fun PlayerScreen() {
    val player: Player = rememberExoPlayer(R.raw.test_video)

    PlayerScreen(
        player = { player },
        onPlay = { player.play() },
        onPause = { player.pause() },
    )
}

@Composable
private fun PlayerScreen(
    player: () -> Player,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        this.player = player()
//                        this.useController = false // デフォルトのコントローラ(止めるボタン等) を表示しない
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
            Row {
                Button(onClick = onPlay, modifier = Modifier.weight(1f)) {
                    Text("play")
                }
                Button(onClick = onPause, modifier = Modifier.weight(1f)) {
                    Text("pause")
                }
            }
        }
    }
}

@Composable
internal fun rememberExoPlayer(videoUri: Uri): Player {
    val context = LocalContext.current
    val player: Player = remember {
        context.initPlayer()
            .apply {
                val mediaItem = MediaItem.fromUri(videoUri)
                setMediaItem(mediaItem)
            }
    }

    LifecycleStartEffect(player) {
        player.prepare()
        onStopOrDispose {
            player.release()
        }
    }

    return player
}

@Composable
internal fun rememberExoPlayer(@RawRes videoFile: Int): Player =
    rememberExoPlayer(LocalContext.current.resourceUri(videoFile))

private fun Context.initPlayer(): Player {
    val audioAttrs = AudioAttributes.Builder()
        .build()
    val player = ExoPlayer.Builder(this)
        .setAudioAttributes(audioAttrs, true)
        .setHandleAudioBecomingNoisy(true)
        .build()
    return player
}

private fun Context.resourceUri(@RawRes resourceId: Int): Uri = with(resources) {
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(getResourcePackageName(resourceId))
        .appendPath(getResourceTypeName(resourceId))
        .appendPath(getResourceEntryName(resourceId))
        .build()
}