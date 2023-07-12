import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.quizapp.R
import com.example.quizapp.ui.Screen
import kotlinx.coroutines.launch



//experimental Material3 API features --> still under development, not fully stable
@OptIn(ExperimentalMaterial3Api::class) //indicating that this function is using experimental Material3 API features
@Composable
fun LoginScreen(navController: NavController, viewModel: QuizViewModel) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }  //for showing errors


    //content of login screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(  //adding logo and setting it's properties
                painterResource(R.drawable.icon),
                contentDescription = "Quiz Logo",
                modifier = Modifier
                    .width(190.dp)
                    .height(150.dp)
            )
            OutlinedTextField(  //input fields for password and username
                value = username.value,
                onValueChange = { username.value = it.trim().replace("\n", "") },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it.replace("\n", "") },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))  //adding vertical space of 16dp between input fields and button

            Button(  //button for login
                onClick = {

                    //if all input fields empty, through the snackbarHostState display error message
                    if (username.value.isBlank() || password.value.isBlank()) {
                        viewModel.viewModelScope.launch {
                            snackbarHostState.showSnackbar("Username or password cannot be empty")
                        }
                    } else {  //if its not empty, initiate login by calling viewModeL
                        viewModel.viewModelScope.launch {
                            val loginSuccessful = viewModel.login(username.value, password.value)
                            if (loginSuccessful) { //if login successful, navigate to main menu screen
                                navController.navigate(Screen.MainMenu.route)
                            } else { //if login fails, with snackbar display error
                                snackbarHostState.showSnackbar("Invalid username or password")
                            }
                        }
                    }
                },  //properties of the button
                colors = ButtonDefaults.buttonColors(colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(300.dp)

            ) {
                Text("Login")  //text of the button
            }

            val annotatedString = buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(
                    style = SpanStyle(textDecoration = TextDecoration.Underline)
                ) {
                    append("Register")
                }
            }

            Text(  //it triggers register screen
                text = annotatedString,
                color = colorScheme.scrim,
                modifier = Modifier
                    .clickable { navController.navigate(Screen.Register.route) }
                    .padding(top = 10.dp)
            )
        }
    }
}
