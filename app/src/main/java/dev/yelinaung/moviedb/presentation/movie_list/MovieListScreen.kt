package dev.yelinaung.moviedb.presentation.movie_list

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dev.yelinaung.moviedb.BuildConfig
import dev.yelinaung.moviedb.data.service.model.movie_list.Movie
import dev.yelinaung.moviedb.ui.theme.MovieDbBlack
import dev.yelinaung.moviedb.ui.theme.MovieDbDarkGray
import dev.yelinaung.moviedb.ui.theme.MovieDbGold
import dev.yelinaung.moviedb.ui.theme.MovieDbGray
import dev.yelinaung.moviedb.ui.theme.MovieDbTheme
import dev.yelinaung.moviedb.utils.GradientBackground
import dev.yelinaung.moviedb.utils.ObserveAsEvent
import dev.yelinaung.moviedb.utils.getPopularityColor
import dev.yelinaung.moviedb.utils.mapPopularityToCategory
import kotlinx.coroutines.launch

@Composable
fun MovieListScreenRoot(
    navController: NavController,
    viewModel: MovieViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    ObserveAsEvent(viewModel.errorMessage) { error ->
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    MovieListScreen(
        state = viewModel.movieListState.collectAsState().value,
        onAction = viewModel::onAction,
        onMovieClick = { movieId ->
            navController.navigate("movieDetails/$movieId")
        })
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MovieListScreen(
    state: MovieListState,
    onAction: (MovieListAction) -> Unit,
    onMovieClick: (Int) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    GradientBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(), containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                FilterTabs(
                    selectedCategory = when (pagerState.currentPage) {
                        0 -> "Popular"
                        1 -> "Now Playing"
                        else -> "Favorites"
                    },
                    onCategorySelected = { category ->
                        val pageIndex = when (category) {
                            "Popular" -> 0
                            "Now Playing" -> 1
                            else -> 2
                        }
                        // Navigate to the selected page
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pageIndex)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TitleSection(
                    title = when (pagerState.currentPage) {
                        0 -> "Popular"
                        1 -> "Now Playing"
                        else -> "Favorites"
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalPager(
                    count = 3, // Three sections: Popular, Now Playing, Favorites
                    state = pagerState, modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> MovieList(
                            movies = state.popularMovieList,
                            lazyListState = state.popularLazyListState,
                            onMovieClick = onMovieClick,
                            onPaginate = {
                                onAction(MovieListAction.Paginate("Popular"))
                            })

                        1 -> MovieList(
                            movies = state.nowPlayingList,
                            lazyListState = state.nowPlayingLazyListState,
                            onMovieClick = onMovieClick,
                            onPaginate = { onAction(MovieListAction.Paginate("Now Playing")) })

                        2 -> {
                            MovieList(
                                movies = state.favoriteMovies,
                                lazyListState = state.favoritesLazyListState,
                                onMovieClick = onMovieClick,
                                onPaginate = {})

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
    )
}

@Composable
private fun FilterTabs(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
) {
    val categories = listOf("Popular", "Now Playing", "Favorites")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .background(
                color = MovieDbDarkGray.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                color = if (category == selectedCategory) MovieDbGold else MovieDbGray,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (category == selectedCategory) MovieDbGold.copy(alpha = 0.2f) else Color.Transparent,
                        shape = MaterialTheme.shapes.small
                    )
                    .clip(MaterialTheme.shapes.small)
                    .clickable { onCategorySelected(category) }
                    .padding(8.dp),
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun MovieList(
    movies: List<Movie>,
    lazyListState: LazyListState,
    onMovieClick: (Int) -> Unit,
    onPaginate: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), state = lazyListState
    ) {
        items(movies) { movie ->
            MovieRow(movie = movie, onMovieClick = onMovieClick)
        }
        item {
            onPaginate()
        }
    }
}

@Composable
private fun MovieRow(movie: Movie, onMovieClick: (Int) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
        color = MovieDbBlack.copy(alpha = 0.7f),
        shape = RoundedCornerShape(10.dp),
        onClick = { movie.id?.let { onMovieClick(it) } }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = rememberAsyncImagePainter(BuildConfig.IMAGE_BASE_URL + movie.backdropPath),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                movie.title?.let {
                    Text(
                        text = it, style = MaterialTheme.typography.bodyLarge, color = MovieDbGold
                    )
                }

                movie.releaseDate?.let {
                    Text(
                        text = it, color = MovieDbGray, style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                movie.overview?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val popularityCategory = mapPopularityToCategory(movie.popularity)
                val popularityColor = getPopularityColor(popularityCategory)

                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.bodySmall.toSpanStyle()
                            .copy(color = popularityColor)
                    ) {
                        append(popularityCategory)
                    }
                    withStyle(
                        style = MaterialTheme.typography.bodySmall.toSpanStyle()
                            .copy(color = MovieDbGold)
                    ) {
                        append(" (${movie.popularity?.toInt()}%)")
                    }
                }

                Text(text = annotatedString)
            }
        }
    }
}

@Preview
@Composable
private fun MovieListScreenPreview() {
    MovieDbTheme {
        MovieListScreen(state = MovieListState(), onAction = {}, onMovieClick = {})
    }
}
