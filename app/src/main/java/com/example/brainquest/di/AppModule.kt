package com.example.brainquest.di

import android.content.Context
import androidx.room.Room
import com.example.brainquest.data.local.AttemptDao
import com.example.brainquest.data.local.BrainQuestDatabase
import com.example.brainquest.data.remote.TriviaApi
import com.example.brainquest.data.repository.StatsRepository
import com.example.brainquest.data.repository.StatsRepositoryImpl
import com.example.brainquest.data.repository.TriviaRepository
import com.example.brainquest.data.repository.TriviaRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkAndDatabaseModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideTriviaApi(retrofit: Retrofit): TriviaApi =
        retrofit.create(TriviaApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BrainQuestDatabase = Room.databaseBuilder(
        context,
        BrainQuestDatabase::class.java,
        "brainquest.db"
    ).build()

    @Provides
    fun provideAttemptDao(database: BrainQuestDatabase): AttemptDao =
        database.attemptDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTriviaRepository(
        implementation: TriviaRepositoryImpl
    ): TriviaRepository

    @Binds
    abstract fun bindStatsRepository(
        implementation: StatsRepositoryImpl
    ): StatsRepository
}
