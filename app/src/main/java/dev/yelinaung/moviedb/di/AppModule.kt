package dev.yelinaung.moviedb.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.yelinaung.moviedb.BuildConfig
import dev.yelinaung.moviedb.data.database.MovieDao
import dev.yelinaung.moviedb.data.database.MovieDatabase
import dev.yelinaung.moviedb.domain.MovieRepository
import dev.yelinaung.moviedb.data.MovieRepositoryImpl
import dev.yelinaung.moviedb.data.service.MovieApiService
import dev.yelinaung.moviedb.data.service.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY
private const val BASE_URL = BuildConfig.BASE_URL
private const val DB_NAME = "movie_database"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * API
     */
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Singleton
    @Provides
    fun provideTokenInterceptor(): TokenInterceptor = TokenInterceptor(TMDB_API_KEY)

    @Singleton
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    /**
     * Database
     */
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            DB_NAME
        )
            .fallbackToDestructiveMigration() // So i wont need to change between DB_Version
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: MovieDatabase) : MovieDao = database.movieDao()

    /**
     * Repository
     */
    @Singleton
    @Provides
    fun provideMovieRepository(apiService: MovieApiService, movieDao: MovieDao): MovieRepository {
        return MovieRepositoryImpl(apiService, movieDao) // Provide the implementation of MovieRepository
    }

}