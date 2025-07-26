package com.example.productcrud.di

import android.app.Application
import androidx.room.Room
import com.example.productcrud.data.IProductRepository
import com.example.productcrud.data.ProductCategoryDao
import com.example.productcrud.data.ProductDao
import com.example.productcrud.data.ProductDatabase
import com.example.productcrud.data.ProductRepository
import com.example.productcrud.data.VatRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductDatabase(app: Application): ProductDatabase {
        return Room.databaseBuilder(
            app,
            ProductDatabase::class.java,
            "product_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductDao(db: ProductDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: ProductDatabase): ProductCategoryDao {
        return db.productCategoryDao()
    }

    @Provides
    @Singleton
    fun provideVatRateDao(db: ProductDatabase): VatRateDao {
        return db.vatRateDao()
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        productDao: ProductDao,
        categoryDao: ProductCategoryDao,
        vatRateDao: VatRateDao
    ): IProductRepository {
        return ProductRepository(productDao, categoryDao, vatRateDao)
    }
}