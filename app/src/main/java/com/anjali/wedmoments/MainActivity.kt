package com.anjali.wedmoments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anjali.wedmoments.data.CreatorEntity
import com.anjali.wedmoments.data.PackageEntity
import com.anjali.wedmoments.ui.theme.WedMomentsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as WedMomentsApp
        val repository = app.repository
        val session = app.session

        setContent {
            WedMomentsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()

                    var selectedCreator by remember { mutableStateOf<CreatorEntity?>(null) }
                    var selectedPackage by remember { mutableStateOf<PackageEntity?>(null) }
                    var pendingBookingId by remember { mutableStateOf(0L) }
                    var resetEmail by remember { mutableStateOf("") }
                    var dbReady by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        repository.ensureSeeded()
                        dbReady = true
                    }

                    if (!dbReady) {
                        SplashScreen(onSplashFinished = {})
                        return@Surface
                    }

                    NavHost(navController = navController, startDestination = "splash") {

                        composable("splash") {
                            SplashScreen(onSplashFinished = {
                                val destination = if (session.isLoggedIn()) "home" else "login"
                                navController.navigate(destination) {
                                    popUpTo("splash") { inclusive = true }
                                }
                            })
                        }

                        composable("login") {
                            LoginScreen(
                                onLogin = { email, password ->
                                    repository.login(email, password).map { user ->
                                        session.login(user.id)
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                },
                                onGoogleLogin = {
                                    val user = repository.loginOrCreateGoogleUser()
                                    session.login(user.id)
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onCreateAccountClick = { navController.navigate("signup") },
                                onForgotPasswordClick = { navController.navigate("forgot") }
                            )
                        }

                        composable("signup") {
                            SignupScreen(
                                onBackClick = { navController.popBackStack() },
                                onSignup = { name, email, password ->
                                    repository.signup(name, email, password).map { user ->
                                        session.login(user.id)
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                },
                                onGoogleSignup = {
                                    val user = repository.loginOrCreateGoogleUser()
                                    session.login(user.id)
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("forgot") {
                            ForgotPasswordScreen(
                                onBackClick = { navController.popBackStack() },
                                onResetClick = { email ->
                                    if (repository.userExists(email)) {
                                        resetEmail = email
                                        navController.navigate("reset")
                                        Result.success(Unit)
                                    } else {
                                        Result.failure(IllegalArgumentException("No account found for this email"))
                                    }
                                }
                            )
                        }

                        composable("reset") {
                            ResetPasswordScreen(
                                onBackClick = {
                                    navController.navigate("login") {
                                        popUpTo("forgot") { inclusive = true }
                                    }
                                },
                                onReset = { newPassword ->
                                    repository.resetPassword(resetEmail, newPassword)
                                }
                            )
                        }

                        composable("home") {
                            HomeScreen(
                                repository = repository,
                                onCreatorClick = { creator ->
                                    selectedCreator = creator
                                    navController.navigate("portfolio")
                                },
                                onMyWeddingClick = { navController.navigate("myWedding") },
                                onProfileClick = { navController.navigate("profile") }
                            )
                        }

                        composable("portfolio") {
                            val creator = selectedCreator
                            if (creator == null) {
                                LaunchedEffect(Unit) { navController.popBackStack() }
                            } else {
                                CreatorPortfolioScreen(
                                    creator = creator,
                                    repository = repository,
                                    onBackClick = { navController.popBackStack() },
                                    onBookClick = { navController.navigate("packages") }
                                )
                            }
                        }

                        composable("packages") {
                            val creator = selectedCreator
                            if (creator == null) {
                                LaunchedEffect(Unit) { navController.popBackStack() }
                            } else {
                                PackagesScreen(
                                    creator = creator,
                                    repository = repository,
                                    onBackClick = { navController.popBackStack() },
                                    onPackageSelected = { pkg ->
                                        selectedPackage = pkg
                                        navController.navigate("booking")
                                    }
                                )
                            }
                        }

                        composable("booking") {
                            val creator = selectedCreator
                            if (creator == null) {
                                LaunchedEffect(Unit) { navController.popBackStack() }
                            } else {
                                BookingScreen(
                                    creator = creator,
                                    selectedPackage = selectedPackage,
                                    onBackClick = { navController.popBackStack() },
                                    onBookingConfirmed = { weddingDate, venue, events, requirements ->
                                        val userId = session.getUserId()
                                        val pkg = selectedPackage
                                        if (userId > 0 && pkg != null) {
                                            val id = repository.createBooking(
                                                userId = userId,
                                                creator = creator,
                                                selectedPackage = pkg,
                                                weddingDate = weddingDate,
                                                venue = venue,
                                                numberOfEvents = events,
                                                specialRequirements = requirements
                                            )
                                            pendingBookingId = id
                                            navController.navigate("payment")
                                        }
                                    }
                                )
                            }
                        }

                        composable("payment") {
                            PaymentScreen(
                                repository = repository,
                                bookingId = pendingBookingId,
                                creatorName = selectedCreator?.name.orEmpty(),
                                eventType = "Wedding",
                                onBackClick = { navController.popBackStack() },
                                onPaymentSuccess = { method, amount ->
                                    val userId = session.getUserId()
                                    if (pendingBookingId > 0 && userId > 0) {
                                        repository.recordPayment(pendingBookingId, userId, method, amount)
                                    }
                                    navController.navigate("success")
                                }
                            )
                        }

                        composable("success") {
                            BookingSuccessScreen(
                                repository = repository,
                                bookingId = pendingBookingId,
                                creatorName = selectedCreator?.name.orEmpty(),
                                onHomeClick = {
                                    navController.navigate("myBookings") { popUpTo("home") }
                                }
                            )
                        }

                        composable("myBookings") {
                            MyBookingsScreen(
                                repository = repository,
                                userId = session.getUserId(),
                                onBackClick = { navController.popBackStack() },
                                onPaymentClick = { booking ->
                                    selectedCreator = CreatorEntity(
                                        id = booking.creatorId,
                                        name = booking.creatorName,
                                        category = "",
                                        location = "",
                                        experience = "",
                                        rating = 0.0,
                                        reviewCount = 0,
                                        startingPrice = booking.packagePrice,
                                        instagram = "",
                                        speciality = ""
                                    )
                                    pendingBookingId = booking.id
                                    navController.navigate("payment")
                                }
                            )
                        }

                        composable("profile") {
                            ProfileScreen(
                                repository = repository,
                                userId = session.getUserId(),
                                onBackClick = { navController.popBackStack() },
                                onMyBookingsClick = { navController.navigate("myBookings") },
                                onMyWeddingClick = { navController.navigate("myWedding") },
                                onLogoutClick = {
                                    session.logout()
                                    selectedCreator = null
                                    selectedPackage = null
                                    pendingBookingId = 0L
                                    navController.navigate("login") { popUpTo(0) }
                                }
                            )
                        }

                        composable("myWedding") {
                            MyWeddingScreen(
                                repository = repository,
                                userId = session.getUserId(),
                                creatorName = selectedCreator?.name,
                                onBackClick = { navController.popBackStack() },
                                onToggleTask = { task ->
                                    scope.launch { repository.toggleTask(task) }
                                }
                            )
                        }

                        composable("gallery") {
                            val creator = selectedCreator
                            if (creator == null) {
                                LaunchedEffect(Unit) { navController.popBackStack() }
                            } else {
                                GalleryScreen(
                                    creator = creator,
                                    repository = repository,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }

                        composable("reviews") {
                            val creator = selectedCreator
                            if (creator == null) {
                                LaunchedEffect(Unit) { navController.popBackStack() }
                            } else {
                                ReviewsScreen(
                                    creator = creator,
                                    repository = repository,
                                    userId = session.getUserId(),
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
