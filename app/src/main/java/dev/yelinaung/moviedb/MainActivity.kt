package dev.yelinaung.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.yelinaung.moviedb.presentation.Navigation
import dev.yelinaung.moviedb.ui.theme.MovieDbTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieDbTheme {
                Navigation()
            }
        }
    }

}
