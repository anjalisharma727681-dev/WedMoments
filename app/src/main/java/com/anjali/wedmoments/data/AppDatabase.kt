package com.anjali.wedmoments.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        CreatorEntity::class,
        PackageEntity::class,
        GalleryItemEntity::class,
        ReviewEntity::class,
        BookingEntity::class,
        PaymentEntity::class,
        WeddingEntity::class,
        WeddingTaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun creatorDao(): CreatorDao
    abstract fun packageDao(): PackageDao
    abstract fun galleryDao(): GalleryDao
    abstract fun reviewDao(): ReviewDao
    abstract fun bookingDao(): BookingDao
    abstract fun paymentDao(): PaymentDao
    abstract fun weddingDao(): WeddingDao
    abstract fun weddingTaskDao(): WeddingTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wedmoments.db"
                ).fallbackToDestructiveMigration(true).build().also { INSTANCE = it }
            }
        }
    }
}
