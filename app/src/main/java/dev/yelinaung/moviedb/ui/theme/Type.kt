package dev.yelinaung.moviedb.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.yelinaung.moviedb.R

val Poppins = FontFamily(
    Font(
        resId = R.font.poppins_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.poppins_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.poppins_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.poppins_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.poppins_bold,
        weight = FontWeight.Bold
    ),
)

// Simplified Dynamic Typography
@Composable
fun movieDbTypography(): Typography {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val scaleFactor = if (screenWidthDp >= 600) 1.5f else 1f // Scale for larger screens (tablets)

    return Typography(
        bodySmall = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = (12.sp * scaleFactor),
            lineHeight = (20.sp * scaleFactor),
            color = MovieDbGray
        ),
        bodyMedium = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = (14.sp * scaleFactor),
            lineHeight = (22.sp * scaleFactor),
        ),
        bodyLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = (16.sp * scaleFactor),
            lineHeight = (24.sp * scaleFactor),
            letterSpacing = (0.5.sp * scaleFactor),
        ),
        labelLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = (14.sp * scaleFactor),
            lineHeight = (24.sp * scaleFactor),
        ),
        headlineMedium = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = (24.sp * scaleFactor),
            color = MovieDbGold
        ),
        headlineLarge = TextStyle(
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = (34.sp * scaleFactor),
            color = MovieDbWhite
        ),
    )
}
