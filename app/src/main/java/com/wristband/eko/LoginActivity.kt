package com.wristband.eko

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wristband.eko.ui.theme.EkoTheme
import com.wristband.eko.vm.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EkoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    LoginScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreen() {

    val mContext = LocalContext.current
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val viewModel: AuthViewModel = viewModel()
    val result by viewModel.loggedIn.observeAsState(null)

    // Result
    if(result != null) {
        showSnack(scaffoldState, coroutineScope, result!!.message)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.LightGray) {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.Center) {

            Text(
                text = "Welcome Back",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp)
            )
            Text(
                text = "Login to continue",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.padding(16.dp))

            val username = remember {
                mutableStateOf(TextFieldValue())
            }
            OutlinedTextField(
                        value = username.value,
                placeholder = { Text(text = "Username") },
                onValueChange = {username.value = it},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(10.dp))

            val password = remember {
                mutableStateOf(TextFieldValue())
            }
            OutlinedTextField(
                value = password.value,
                placeholder = { Text(text = "Password") },
                onValueChange = {password.value = it},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(10.dp))
            val modifier = Modifier.fillMaxWidth();
            Button(
                onClick = {

                    if (username.value.text.isEmpty()) {
                        showSnack(scaffoldState, coroutineScope, "Enter your username")
                    }

                    if (password.value.text.isEmpty()) {
                        showSnack(scaffoldState, coroutineScope, "Enter your Password")
                    }

                    if (username.value.text.trim().isNotEmpty() && password.value.text.trim().isNotEmpty()) {
                        viewModel.login(username.value.text, password.value.text)
                    }

                    val intent = Intent(mContext, MainActivity::class.java)
                    mContext.startActivity(intent)
                },
                modifier = modifier,
                contentPadding = PaddingValues(14.dp)) {
                Text(text = "Login")
            }
        }
    }
}

fun showSnack(scaffoldState: ScaffoldState, coroutineScope: CoroutineScope, message: String) {
    coroutineScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(message)
    }
}