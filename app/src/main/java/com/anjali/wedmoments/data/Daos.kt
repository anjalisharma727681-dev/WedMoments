package com.anjali.wedmoments.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<UserEntity?>

    @Query("UPDATE users SET password = :password WHERE email = :email")
    suspend fun updatePassword(email: String, password: String): Int

    @Query("UPDATE users SET weddingDate = :weddingDate WHERE id = :userId")
    suspend fun updateWeddingDate(userId: Long, weddingDate: String)
}

@Dao
interface CreatorDao {
    @Insert
    suspend fun insertAll(creators: List<CreatorEntity>): List<Long>

    @Query("SELECT COUNT(*) FROM creators")
    suspend fun count(): Int

    @Query("SELECT * FROM creators ORDER BY rating DESC")
    fun observeAll(): Flow<List<CreatorEntity>>

    @Query("SELECT * FROM creators WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CreatorEntity?

    @Query(
        """
        SELECT * FROM creators
        WHERE name LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
           OR location LIKE '%' || :query || '%'
        ORDER BY rating DESC
        """
    )
    fun search(query: String): Flow<List<CreatorEntity>>
}

@Dao
interface PackageDao {
    @Insert
    suspend fun insertAll(packages: List<PackageEntity>)

    @Query("SELECT * FROM packages WHERE creatorId = :creatorId")
    suspend fun getByCreator(creatorId: Long): List<PackageEntity>
}

@Dao
interface GalleryDao {
    @Insert
    suspend fun insertAll(items: List<GalleryItemEntity>)

    @Query("SELECT * FROM gallery_items WHERE creatorId = :creatorId AND type = :type")
    suspend fun getByCreatorAndType(creatorId: Long, type: String): List<GalleryItemEntity>
}

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: ReviewEntity): Long

    @Insert
    suspend fun insertAll(reviews: List<ReviewEntity>)

    @Query("SELECT * FROM reviews WHERE creatorId = :creatorId ORDER BY id DESC")
    fun observeByCreator(creatorId: Long): Flow<List<ReviewEntity>>
}

@Dao
interface BookingDao {
    @Insert
    suspend fun insert(booking: BookingEntity): Long

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeByUser(userId: Long): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): BookingEntity?

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<BookingEntity?>

    @Update
    suspend fun update(booking: BookingEntity)
}

@Dao
interface PaymentDao {
    @Insert
    suspend fun insert(payment: PaymentEntity): Long

    @Query("SELECT * FROM payments WHERE bookingId = :bookingId ORDER BY createdAt DESC")
    suspend fun getByBooking(bookingId: Long): List<PaymentEntity>
}

@Dao
interface WeddingDao {
    @Insert
    suspend fun insert(wedding: WeddingEntity): Long

    @Query("SELECT * FROM weddings WHERE userId = :userId LIMIT 1")
    suspend fun getByUser(userId: Long): WeddingEntity?

    @Query("SELECT * FROM weddings WHERE userId = :userId LIMIT 1")
    fun observeByUser(userId: Long): Flow<WeddingEntity?>

    @Update
    suspend fun update(wedding: WeddingEntity)
}

@Dao
interface WeddingTaskDao {
    @Insert
    suspend fun insertAll(tasks: List<WeddingTaskEntity>)

    @Query("SELECT * FROM wedding_tasks WHERE weddingId = :weddingId")
    fun observeByWedding(weddingId: Long): Flow<List<WeddingTaskEntity>>

    @Update
    suspend fun update(task: WeddingTaskEntity)
}
