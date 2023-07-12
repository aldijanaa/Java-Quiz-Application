package com.example.quizapp.ui

import LoginScreen
import MainMenuScreen
import QuizScreen
import QuizViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizapp.ui.screens.*

//Using sealed class to represent each screen and their corresponding routes
sealed class Screen(val route: String) {
    object MainMenu : Screen("MainMenuScreen")
    object Login : Screen("LoginScreen")
    object Register : Screen("RegisterScreen")
    object User : Screen("UserScreen")
    class Quiz(val difficulty: String) : Screen("quiz/$difficulty")
    class Results(val score: Int, val totalQuestions: Int) : Screen("results/${score}/${totalQuestions}")
}


//Setting up navigation host
//Navigation is responsible for navigating between different screens based on the sealed class
@Composable
fun AppNavHost(navController: NavHostController, viewModel: QuizViewModel) {
    val viewModel = viewModel

    //NavHost composable is responsible for definining routes and associated them with composable functions
    NavHost(navController = navController, startDestination = Screen.Login.route) { //startDestination takes initial screen
        composable(Screen.Login.route) { LoginScreen(navController, viewModel) }
        composable(Screen.MainMenu.route) { MainMenuScreen(navController, viewModel) }
        composable(Screen.Register.route) { RegisterScreen(navController, viewModel) }

        //backStackEntry.arguments is used to retrieve the values of the path parameters.
        //this is a dynamic route to handle user-specific info
        composable("user/{username}/{score}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            UserScreen(navController, viewModel)
        }

        composable(Screen.Quiz("easy").route) { QuizScreen(navController, "easy", viewModel) }
        composable(Screen.Quiz("medium").route) { QuizScreen(navController, "medium", viewModel) }
        composable(Screen.Quiz("hard").route) { QuizScreen(navController, "hard", viewModel) }

        composable(Screen.Results(0, 0).route) { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            ResultsScreen(navController, score, totalQuestions)
        }

        composable("results/{score}/{totalQuestions}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            ResultsScreen(navController, score, totalQuestions)
        }
    }
}

