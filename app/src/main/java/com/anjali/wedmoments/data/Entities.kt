package com.anjali.wedmoments.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val password: String,
    val weddingDate: String = ""
)

@Entity(tableName = "creators")
data class CreatorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    val location: String,
    val experience: String,
    val rating: Double,
    val reviewCount: Int,
    val startingPrice: String,
    val instagram: String,
    val speciality: String
)

@Entity(tableName = "packages")
data class PackageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creatorId: Long,
    val name: String,
    val price: String,
    val photos: String,
    val reels: String,
    val videos: String,
    val hours: String
)

@Entity(tableName = "gallery_items")
data class GalleryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creatorId: Long,
    val type: String,
    val title: String,
    val emoji: String
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creatorId: Long,
    val userId: Long,
    val authorName: String,
    val comment: String,
    val rating: Int
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val creatorId: Long,
    val creatorName: String,
    val packageName: String,
    val packagePrice: String,
    val weddingDate: String,
    val venue: String,
    val numberOfEvents: String,
    val specialRequirements: String,
    val status: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookingId: Long,
    val userId: Long,
    val method: String,
    val amount: String,
    val status: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "weddings")
data class WeddingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val brideName: String,
    val groomName: String,
    val date: String,
    val venue: String,
    val photographer: String,
    val packageName: String
)

@Entity(tableName = "wedding_tasks")
data class WeddingTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weddingId: Long,
    val title: String,
    val isDone: Boolean
)
