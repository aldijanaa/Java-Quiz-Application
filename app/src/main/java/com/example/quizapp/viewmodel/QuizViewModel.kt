import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.QuizDataSource
import com.example.quizapp.data.QuizQuestion
import com.example.quizapp.data.User
import com.example.quizapp.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch




//This viewModel acts as a central point for managing quiz related questions and operations
class QuizViewModel(private val userDao: UserDao) : ViewModel() {
    //variables to hold current username and score, they are of mutable cuz they can be changed
    var username: String? = null
    var score: Int = 0

    //defining variables for scores also of mutable type
    private val _easyScore = MutableStateFlow(0)
    val easyScore: StateFlow<Int> = _easyScore

    private val _mediumScore = MutableStateFlow(0)
    val mediumScore: StateFlow<Int> = _mediumScore

    private val _hardScore = MutableStateFlow(0)
    val hardScore: StateFlow<Int> = _hardScore


    //function to retrieve user's score from userDao with the 'username'
    fun getUserScores(username: String) {
        viewModelScope.launch {
            val user = userDao.getUser(username)
            _easyScore.value = user?.easyScore ?: 0
            _mediumScore.value = user?.mediumScore ?: 0
            _hardScore.value = user?.hardScore ?: 0
        }
    }


    //function to return list of quiz questions based on the difficulty (thats why it its parameter is difficulty)
    fun getQuizQuestions(difficulty: String): List<QuizQuestion> {
        return when (difficulty) {
            "easy" -> QuizDataSource.easyQuestions
            "medium" -> QuizDataSource.mediumQuestions
            "hard" -> QuizDataSource.hardQuestions
            else -> throw IllegalArgumentException("Invalid difficulty level: $difficulty")
        }
    }


    //function for handling user registration
    suspend fun register(username: String, password: String): String? {
        if (!username.all { it.isLetterOrDigit() }) {
            return "Username can only contain alphanumeric characters"
        }

        if (password.length < 8) {
            return "Password must be at least 8 characters long"
        }

        val user = User(username, password)
        userDao.register(user)

        //caused the crash when registering
        this.username = user.username
        return null
    }



    //function for handling login
    suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUser(username)
        return if (user != null && user.password == password) {
            this.username = user.username
            this.score = 0
            true
        } else {
            false
        }
    }

    //function for handling logout
    fun logout() {
        username = null
        score = 0
    }


    //function to update the user's score for easy, medium , hard
    suspend fun updateEasyScore(username: String, score: Int) {
        val user = userDao.getUser(username)
        if (user != null) {
            userDao.updateUser(user.copy(easyScore = score))
            _easyScore.value = score
            println("Updated medium score for user $username to $score")
        } else {
            println("Failed to update medium score for user $username")
        }
    }



    suspend fun updateMediumScore(username: String, score: Int) {
        val user = userDao.getUser(username)
        if (user != null) {
            userDao.updateUser(user.copy(mediumScore = score))
            _mediumScore.value = score
            println("Updated medium score for user $username to $score")
        } else {
            println("Failed to update medium score for user $username")
        }
    }

    suspend fun updateHardScore(username: String, score: Int) {
        val user = userDao.getUser(username)
        if (user != null) {
            userDao.updateUser(user.copy(hardScore = score))
            _hardScore.value = score
            println("Updated hard score for user $username to $score")
        } else {
            println("Failed to update hard score for user $username")
        }
    }
}
