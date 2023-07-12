package com.example.quizapp
import QuizViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.data.AppDatabase
import com.example.quizapp.ui.AppNavHost
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodel.QuizViewModelFactory
class MainActivity : ComponentActivity() {
    //MAIN ENTRY POINT IN ANDROID STUDIO IS: onCreate() function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //This line sets up the theme for the application
        // theme is defined in the Theme.kt file in QuizAppTheme() function
        setTheme(R.style.Theme_QuizApp)
        setContent {
            QuizAppTheme {
                //Creating instance of NavController
                val navController = rememberNavController()

                //Getting 'UserDao' interface from the AppDatabase
                //WHY?? UserDao provides methods for inserting/deleting/updating user data in the database
                val userDao = AppDatabase.getDatabase(this).userDao()

                //Creating instance of QuizViewModelFactory, it requires instance of 'UserDao' to interact with database
                val viewModelFactory = QuizViewModelFactory(userDao)

                //Creating instance of QuizViewModel through ViewModelProvider class
                //WHAT IS ViewModelProvider class?? It is responsible for managing lifecycles of ViewModels and providing access to it
                val viewModel = ViewModelProvider(this, viewModelFactory).get(QuizViewModel::class.java)

                //Setting up navigation host for the app
                AppNavHost(navController, viewModel)
            }
        }
    }
}


