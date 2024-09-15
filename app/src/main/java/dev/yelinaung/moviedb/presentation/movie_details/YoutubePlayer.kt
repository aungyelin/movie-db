package dev.yelinaung.moviedb.presentation.movie_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dev.yelinaung.moviedb.utils.IsLoading


@Composable
fun YoutubePlayerScreen(youtubeCode: String) {
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val context = LocalContext.current

    val loading = remember { mutableStateOf(true) }

    // Initialize YouTube Player View
    val youtubePlayer = remember {
        YouTubePlayerView(context).apply {
            lifecycleOwner.value.lifecycle.addObserver(this) // Add lifecycle observer
            enableAutomaticInitialization = false
            initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    // Load the YouTube video directly
                    youTubePlayer.apply {
                        //loadVideo(youtubeCode, 0f)
                        cueVideo(youtubeCode, 0f)
                        loading.value = false
                    }
                }
            })
        }
    }

    // Display the YouTube Player
    AndroidView(
        factory = { youtubePlayer },
        modifier = Modifier.fillMaxSize()
    )

    IsLoading(isLoading = loading.value)
}

