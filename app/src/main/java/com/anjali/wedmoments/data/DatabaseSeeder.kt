package com.anjali.wedmoments.data

object DatabaseSeeder {
    suspend fun seedIfNeeded(db: AppDatabase) {
        if (db.creatorDao().count() > 0) return

        if (db.userDao().getByEmail("anjali@gmail.com") == null) {
            val userId = db.userDao().insert(
                UserEntity(
                    name = "Anjali Sharma",
                    email = "anjali@gmail.com",
                    password = "password123",
                    weddingDate = "25 Dec 2026"
                )
            )
            db.userDao().getById(userId)?.let { seedWeddingForUser(db, it) }
        }

        val creators = listOf(
            CreatorEntity(name = "Anjali Photography", category = "Photography", location = "Mumbai", experience = "5+ Years", rating = 4.9, reviewCount = 210, startingPrice = "₹15,000", instagram = "anjaliphotography", speciality = "Candid, Traditional, Pre-Wedding"),
            CreatorEntity(name = "Rohan Films", category = "Videography", location = "Mumbai", experience = "6+ Years", rating = 4.8, reviewCount = 180, startingPrice = "₹25,000", instagram = "rohanfilms", speciality = "Cinematic Films, Teasers"),
            CreatorEntity(name = "Meera Makeup Artist", category = "Makeup", location = "Pune", experience = "4+ Years", rating = 4.9, reviewCount = 150, startingPrice = "₹12,000", instagram = "meeramakeup", speciality = "Bridal Makeup, Hair Styling"),
            CreatorEntity(name = "Vikram Decorators", category = "Decor", location = "Mumbai", experience = "8+ Years", rating = 4.7, reviewCount = 95, startingPrice = "₹40,000", instagram = "vikramdecorators", speciality = "Floral, Mandap, Lighting"),
            CreatorEntity(name = "Sneha Mehendi Art", category = "Mehendi", location = "Delhi", experience = "7+ Years", rating = 4.8, reviewCount = 120, startingPrice = "₹8,000", instagram = "snehamehendi", speciality = "Bridal Mehendi, Arabic"),
            CreatorEntity(name = "Amit DJ Sound", category = "DJ", location = "Mumbai", experience = "5+ Years", rating = 4.6, reviewCount = 80, startingPrice = "₹18,000", instagram = "amitdjsound", speciality = "Sangeet, Reception")
        )
        val creatorIds = db.creatorDao().insertAll(creators)

        val packages = mutableListOf<PackageEntity>()
        val gallery = mutableListOf<GalleryItemEntity>()
        val reviews = mutableListOf<ReviewEntity>()

        creatorIds.forEach { creatorId ->
            packages += listOf(
                PackageEntity(creatorId = creatorId, name = "Basic", price = "₹15,000", photos = "100 Edited Photos", reels = "2 Reels", videos = "1 Traditional Video", hours = "6 Hours Coverage"),
                PackageEntity(creatorId = creatorId, name = "Standard", price = "₹30,000", photos = "300 Edited Photos", reels = "5 Reels", videos = "2 Cinematic Videos", hours = "12 Hours Coverage"),
                PackageEntity(creatorId = creatorId, name = "Premium", price = "₹50,000", photos = "600 Edited Photos", reels = "10 Reels", videos = "Full Wedding Film + Teaser", hours = "Full Day Coverage")
            )
            repeat(9) { i ->
                gallery += GalleryItemEntity(creatorId = creatorId, type = "Photos", title = "Photo ${i + 1}", emoji = "📸")
            }
            repeat(4) { i ->
                gallery += GalleryItemEntity(creatorId = creatorId, type = "Videos", title = "Video ${i + 1}", emoji = "🎥")
            }
            repeat(6) { i ->
                gallery += GalleryItemEntity(creatorId = creatorId, type = "Reels", title = "Reel ${i + 1}", emoji = "🎬")
            }
            reviews += listOf(
                ReviewEntity(creatorId = creatorId, userId = 0, authorName = "Priya & Rohan", comment = "Amazing work! Loved our wedding photos. Very professional.", rating = 5),
                ReviewEntity(creatorId = creatorId, userId = 0, authorName = "Sneha & Amit", comment = "Best creator in Mumbai! Candid shots were awesome.", rating = 5),
                ReviewEntity(creatorId = creatorId, userId = 0, authorName = "Ananya & Vikram", comment = "Pre-wedding shoot was so fun. Highly recommend!", rating = 4),
                ReviewEntity(creatorId = creatorId, userId = 0, authorName = "Neha & Karan", comment = "On time delivery and quality is top notch.", rating = 5)
            )
        }

        db.packageDao().insertAll(packages)
        db.galleryDao().insertAll(gallery)
        db.reviewDao().insertAll(reviews)
    }

    suspend fun seedWeddingForUser(db: AppDatabase, user: UserEntity) {
        if (db.weddingDao().getByUser(user.id) != null) return
        val weddingId = db.weddingDao().insert(
            WeddingEntity(
                userId = user.id,
                brideName = user.name,
                groomName = "Rahul Verma",
                date = user.weddingDate.ifBlank { "25 Dec 2026" },
                venue = "Hotel Taj, Mumbai",
                photographer = "Anjali Photography",
                packageName = "Premium"
            )
        )
        db.weddingTaskDao().insertAll(
            listOf(
                WeddingTaskEntity(weddingId = weddingId, title = "Venue Booked", isDone = true),
                WeddingTaskEntity(weddingId = weddingId, title = "Photographer Booked", isDone = true),
                WeddingTaskEntity(weddingId = weddingId, title = "Send Invitations", isDone = false),
                WeddingTaskEntity(weddingId = weddingId, title = "Buy Outfit", isDone = false)
            )
        )
    }
}
