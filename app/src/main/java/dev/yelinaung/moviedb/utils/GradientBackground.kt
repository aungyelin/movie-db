package dev.yelinaung.moviedb.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.yelinaung.moviedb.ui.theme.MovieDbDarkBackgroundColor1
import dev.yelinaung.moviedb.ui.theme.MovieDbDarkBackgroundColor2

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    hasToolbar: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) {
        configuration.screenWidthDp.dp.roundToPx()
    }
    val screenHeightPx = with(density) {
        configuration.screenHeightDp.dp.roundToPx()
    }

    // Determine the smaller dimension of the screen
    val smallDimension = minOf(
        configuration.screenWidthDp.dp, configuration.screenHeightDp.dp
    )

    // Convert the smaller dimension to pixels
    val smallDimensionInPx = with(density) {
        smallDimension.roundToPx()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MovieDbDarkBackgroundColor1, // Soft light white center
                        MovieDbDarkBackgroundColor2 // Dark grey at the edges
                    ), center = Offset(
                        x = screenWidthPx / 2f, // Center horizontally
                        y = screenHeightPx / 3f // Slightly higher than the center vertically
                    ), radius = smallDimensionInPx * 1.5f // Expands to cover the screen
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (hasToolbar) {
                        Modifier
                    } else {
                        Modifier.systemBarsPadding()
                    }
                )
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun GradientBackgroundPreview() {
    GradientBackground(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}
