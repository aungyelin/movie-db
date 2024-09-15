package dev.yelinaung.moviedb.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import dev.yelinaung.moviedb.ui.theme.MovieDbBlack
import dev.yelinaung.moviedb.ui.theme.MovieDbGray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun IsLoading(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MovieDbBlack.copy(alpha = 0.5f))
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp)
                    .align(androidx.compose.ui.Alignment.Center),
                color = MovieDbBlack,
                strokeWidth = 4.dp
            )
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    starsModifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
) {

    val filledStars = kotlin.math.floor(rating).toInt()
    val unfilledStars = (stars - kotlin.math.ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))

    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(
                modifier = starsModifier,
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = starsColor
            )
        }
        if (halfStar) {
            Icon(
                modifier = starsModifier,
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                modifier = starsModifier,
                imageVector = Icons.Filled.StarOutline,
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}

/*
LocalLifecycleOwner provides access to the lifecycle of a Composable. The lifecycle owner is typically an Activity or Fragment

LocalLifecycleOwner.current.lifecycle is used with repeatOnLifecycle,
which pauses the Flow collection when the app is in the background and resumes when it returns to the foreground.

STARTED: The component is now visible to the user. For example, an Activity has been started, but it may still be partially covered by other UI elements.
*/
@Composable
fun <T> ObserveAsEvent(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var lastEvent by remember { mutableStateOf<T?>(null) } // Track the last event

    LaunchedEffect(flow, lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect { event ->
                    // Check if the event is different from the last
                    if (event != lastEvent) {
                        lastEvent = event
                        onEvent(event)
                    }
                }
            }
        }
    }
}

fun mapPopularityToCategory(popularity: Double?): String {
    return when {
        popularity == null -> "Unknown"
        popularity >= 3000 -> "Very Popular"
        popularity >= 1500 -> "Popular"
        popularity >= 200 -> "Fairly Popular"
        popularity >= 0 -> "Currently Not Very Popular"
        else -> "---"
    }
}

fun getPopularityColor(category: String): Color {
    return when (category) {
        "Very Popular" -> Color(0xFF4CAF50)
        "Popular" -> Color(0xFF8BC34A)
        "Fairly Popular" -> Color(0xFFFF9800)
        "Currently Not Very Popular" -> Color(0xFFF44336)
        "Unknown" -> MovieDbGray
        else -> Color.White
    }
}