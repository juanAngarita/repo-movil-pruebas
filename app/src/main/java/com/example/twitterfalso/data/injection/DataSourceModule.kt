package com.example.twitterfalso.data.injection

import com.example.twitterfalso.data.datasource.UserRemoteDataSource
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindUserRemoteDataSource(
        impl: UserFirestoreDataSourceImpl
    ): UserRemoteDataSource
}