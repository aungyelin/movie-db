package dev.yelinaung.moviedb.presentation.movie_details

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import dev.yelinaung.moviedb.BuildConfig
import dev.yelinaung.moviedb.R
import dev.yelinaung.moviedb.utils.GradientBackground
import dev.yelinaung.moviedb.data.service.model.movie_details.BelongsToCollection
import dev.yelinaung.moviedb.data.service.model.movie_details.Genre
import dev.yelinaung.moviedb.data.service.model.movie_details.MovieDetailsResponse
import dev.yelinaung.moviedb.data.service.model.movie_details.ProductionCompany
import dev.yelinaung.moviedb.data.service.model.movie_details.ProductionCountry
import dev.yelinaung.moviedb.data.service.model.movie_details.SpokenLanguage
import dev.yelinaung.moviedb.data.service.model.player.Video
import dev.yelinaung.moviedb.ui.theme.MovieDbTheme
import dev.yelinaung.moviedb.utils.ObserveAsEvent
import dev.yelinaung.moviedb.utils.RatingBar

@Composable
fun MovieDetailsScreenRoot(
    navController: NavController,
    id: Int,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val movieDetails by viewModel.movieDetailsState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val videoState by viewModel.videoState.collectAsState()

    LaunchedEffect(id) {
        viewModel.fetchMovieDetails(id)
    }

    // Show a toast when there's an error
    ObserveAsEvent(viewModel.errorMessage) { error ->
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }


    movieDetails?.let {
        MovieDetailsScreen(
            state = it,
            isFavorite = isFavorite,
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onBack = { navController.popBackStack() },
            videoState = videoState
        )
    }
}

@Composable
private fun MovieDetailsScreen(
    state: MovieDetailsResponse,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit,
    videoState: Video?
) {

    var showYouTubeVideo by rememberSaveable { mutableStateOf(false) }

    GradientBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    Box {
                        // Backdrop Image
                        Image(
                            painter = rememberAsyncImagePainter(BuildConfig.IMAGE_BASE_URL + state.backdropPath),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )

                        // Back Button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onBack,
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Favorite Icon
                            IconButton(
                                onClick = onToggleFavorite,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(36.dp)

                                    .aspectRatio(1f)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_favorite),
                                    contentDescription = "Favorite",
                                    colorFilter = ColorFilter.tint(if (isFavorite) Color.Red else Color.Gray),
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Poster Image and Info
                    Row {
                        Image(
                            painter = rememberAsyncImagePainter(BuildConfig.IMAGE_BASE_URL + state.posterPath),
                            contentDescription = null,
                            modifier = Modifier
                                .width(120.dp)
                                .height(180.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {

                            //Title
                            state.title?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.White,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Rating
                            Row {
                                RatingBar(
                                    starsModifier = Modifier.size(18.dp),
                                    rating = state.voteAverage / 2
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = " (${state.voteCount} votes)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }


                            Spacer(modifier = Modifier.height(8.dp))

                            //Release Date
                            Text(
                                text = "Release Date: ${state.releaseDate}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Genres
                            if (state.genres?.isNotEmpty() == true) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .horizontalScroll(rememberScrollState()),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    state.genres.forEach { genre ->
                                        GenreCard(genre = genre.name)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    // Overview
                    state.overview?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // YouTube Video Section
                    if (videoState != null) {
                        if (!showYouTubeVideo) {
                            Button(
                                onClick = { showYouTubeVideo = true },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 16.dp)
                            ) {
                                Text(text = "Watch YouTube Video", color = Color.White)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(Color.Black)
                            ) {
                                YoutubePlayerScreen(videoState.key) // Use videoState key dynamically
                            }
                        }
                    } else {
                        Text(
                            text = "No video available",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GenreCard(genre: String) {
    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, Color.White),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = genre,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@PreviewScreenSizes
@Composable
private fun MovieDetailsScreenPreview() {
    MovieDbTheme {
        MovieDetailsScreen(
            isFavorite = false,
            onToggleFavorite = {},
            onBack = {},
            videoState = null,
            state = MovieDetailsResponse(
                adult = false,
                backdropPath = "/3V4kLQg0kSqPLctI5ziYWabAZYF.jpg",
                belongsToCollection = BelongsToCollection(
                    id = 558216,
                    name = "Venom Collection",
                    posterPath = "/4bXIKqdZIjR8wKgZaGDaLhLj4yF.jpg",
                    backdropPath = "/vq340s8DxA5Q209FT8PHA6CXYOx.jpg"
                ),
                budget = 120000000,
                genres = listOf(
                    Genre(id = 878, name = "Science Fiction"),
                    Genre(id = 28, name = "Action"),
                ),
                homepage = "https://venom.movie",
                id = 912649,
                imdbId = "tt16366836",
                originCountry = listOf("US"),
                originalLanguage = "en",
                originalTitle = "Venom: The Last Dance",
                overview = "Eddie and Venom are on the run. Hunted by both of their worlds and with the net closing in, the duo are forced into a devastating decision that will bring the curtains down on Venom and Eddie's last dance.",
                popularity = 3073.235,
                posterPath = "/aosm8NMQ3UyoBVpSxyimorCQykC.jpg",
                productionCompanies = listOf(
                    ProductionCompany(
                        id = 5,
                        logoPath = "/71BqEFAF4V3qjjMPCpLuyJFB9A.png",
                        name = "Columbia Pictures",
                        originCountry = "US"
                    ),
                    ProductionCompany(
                        id = 84041,
                        logoPath = "/nw4kyc29QRpNtFbdsBHkRSFavvt.png",
                        name = "Pascal Pictures",
                        originCountry = "US"
                    ),
                    ProductionCompany(
                        id = 53462,
                        logoPath = "/nx8B3Phlcse02w86RW4CJqzCnfL.png",
                        name = "Matt Tolmach Productions",
                        originCountry = "US"
                    ),
                    ProductionCompany(
                        id = 91797,
                        logoPath = "null",
                        name = "Hutch Parker Entertainment",
                        originCountry = "US"
                    ),
                    ProductionCompany(
                        id = 14439,
                        logoPath = "null",
                        name = "Arad Productions",
                        originCountry = "US"
                    )
                ),
                productionCountries = listOf(
                    ProductionCountry(
                        iso31661 = "US",
                        name = "United States of America"
                    )
                ),
                releaseDate = "2024-10-22",
                revenue = 456425476,
                runtime = 109,
                spokenLanguages = listOf(
                    SpokenLanguage(
                        englishName = "English",
                        iso6391 = "en",
                        name = "English"
                    )
                ),
                status = "Released",
                tagline = "'Til death do they part.",
                title = "Venom: The Last Dance",
                video = false,
                voteAverage = 6.503,
                voteCount = 865
            )
        )
    }
}

