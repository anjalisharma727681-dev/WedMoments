package com.anjali.wedmoments.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AppRepository(private val db: AppDatabase) {
    private val seedMutex = Mutex()
    @Volatile
    private var seeded = false

    suspend fun ensureSeeded() {
        if (seeded) return
        seedMutex.withLock {
            if (seeded) return
            DatabaseSeeder.seedIfNeeded(db)
            seeded = true
        }
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = db.userDao().getByEmail(email.trim().lowercase())
            ?: return Result.failure(IllegalArgumentException("No account found for this email"))
        if (user.password != password) {
            return Result.failure(IllegalArgumentException("Incorrect password"))
        }
        return Result.success(user)
    }

    suspend fun signup(name: String, email: String, password: String): Result<UserEntity> {
        val normalized = email.trim().lowercase()
        if (db.userDao().getByEmail(normalized) != null) {
            return Result.failure(IllegalArgumentException("An account with this email already exists"))
        }
        val id = db.userDao().insert(
            UserEntity(name = name.trim(), email = normalized, password = password, weddingDate = "25 Dec 2026")
        )
        val user = db.userDao().getById(id) ?: return Result.failure(IllegalStateException("Could not create account"))
        DatabaseSeeder.seedWeddingForUser(db, user)
        return Result.success(user)
    }

    suspend fun loginOrCreateGoogleUser(): UserEntity {
        val email = "google@wedmoments.app"
        val existing = db.userDao().getByEmail(email)
        if (existing != null) return existing
        val id = db.userDao().insert(
            UserEntity(name = "Google User", email = email, password = "google", weddingDate = "25 Dec 2026")
        )
        val user = db.userDao().getById(id)!!
        DatabaseSeeder.seedWeddingForUser(db, user)
        return user
    }

    suspend fun userExists(email: String): Boolean {
        return db.userDao().getByEmail(email.trim().lowercase()) != null
    }

    suspend fun resetPassword(email: String, newPassword: String): Result<Unit> {
        val updated = db.userDao().updatePassword(email.trim().lowercase(), newPassword)
        return if (updated > 0) Result.success(Unit)
        else Result.failure(IllegalArgumentException("No account found for this email"))
    }

    fun observeUser(userId: Long): Flow<UserEntity?> = db.userDao().observeById(userId)

    suspend fun getUser(userId: Long): UserEntity? = db.userDao().getById(userId)

    fun observeCreators(query: String): Flow<List<CreatorEntity>> {
        return if (query.isBlank()) db.creatorDao().observeAll() else db.creatorDao().search(query.trim())
    }

    suspend fun getCreator(id: Long): CreatorEntity? = db.creatorDao().getById(id)

    suspend fun getPackages(creatorId: Long): List<PackageEntity> = db.packageDao().getByCreator(creatorId)

    suspend fun getGallery(creatorId: Long, type: String): List<GalleryItemEntity> {
        return db.galleryDao().getByCreatorAndType(creatorId, type)
    }

    fun observeReviews(creatorId: Long): Flow<List<ReviewEntity>> = db.reviewDao().observeByCreator(creatorId)

    suspend fun addReview(creatorId: Long, userId: Long, authorName: String, comment: String, rating: Int) {
        db.reviewDao().insert(
            ReviewEntity(
                creatorId = creatorId,
                userId = userId,
                authorName = authorName,
                comment = comment,
                rating = rating
            )
        )
    }

    suspend fun createBooking(
        userId: Long,
        creator: CreatorEntity,
        selectedPackage: PackageEntity,
        weddingDate: String,
        venue: String,
        numberOfEvents: String,
        specialRequirements: String
    ): Long {
        val bookingId = db.bookingDao().insert(
            BookingEntity(
                userId = userId,
                creatorId = creator.id,
                creatorName = creator.name,
                packageName = selectedPackage.name,
                packagePrice = selectedPackage.price,
                weddingDate = weddingDate,
                venue = venue,
                numberOfEvents = numberOfEvents,
                specialRequirements = specialRequirements,
                status = "Pending"
            )
        )
        db.userDao().updateWeddingDate(userId, weddingDate)
        val wedding = db.weddingDao().getByUser(userId)
        if (wedding != null) {
            db.weddingDao().update(
                wedding.copy(
                    date = weddingDate,
                    venue = venue,
                    photographer = creator.name,
                    packageName = selectedPackage.name
                )
            )
        }
        return bookingId
    }

    fun observeBookings(userId: Long): Flow<List<BookingEntity>> = db.bookingDao().observeByUser(userId)

    fun observeBooking(bookingId: Long): Flow<BookingEntity?> {
        if (bookingId <= 0) return flowOf(null)
        return db.bookingDao().observeById(bookingId)
    }

    suspend fun getBooking(bookingId: Long): BookingEntity? = db.bookingDao().getById(bookingId)

    suspend fun recordPayment(bookingId: Long, userId: Long, method: String, amount: String) {
        db.paymentDao().insert(
            PaymentEntity(
                bookingId = bookingId,
                userId = userId,
                method = method,
                amount = amount,
                status = "Paid"
            )
        )
        val booking = db.bookingDao().getById(bookingId) ?: return
        db.bookingDao().update(booking.copy(status = "Confirmed"))
    }

    suspend fun paymentsForBooking(bookingId: Long): List<PaymentEntity> = db.paymentDao().getByBooking(bookingId)

    fun observeWedding(userId: Long): Flow<WeddingEntity?> = db.weddingDao().observeByUser(userId)

    fun observeTasks(weddingId: Long): Flow<List<WeddingTaskEntity>> {
        if (weddingId <= 0) return flowOf(emptyList())
        return db.weddingTaskDao().observeByWedding(weddingId)
    }

    suspend fun toggleTask(task: WeddingTaskEntity) {
        db.weddingTaskDao().update(task.copy(isDone = !task.isDone))
    }
}

fun parseRupees(price: String): Int = price.filter { it.isDigit() }.toIntOrNull() ?: 0
