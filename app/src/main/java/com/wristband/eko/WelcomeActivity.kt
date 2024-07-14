package com.wristband.eko

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Space
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wristband.eko.data.Role
import com.wristband.eko.data.SessionManager
import com.wristband.eko.entities.Agent
import com.wristband.eko.ui.theme.EkoTheme
import com.wristband.eko.vm.AgentViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EkoTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: AgentViewModel = viewModel()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { Welcome(navController)}
        composable("login") { LoginScreen(viewModel)}
    }
}

@Composable
fun Welcome(navController: NavController) {
    Scaffold(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.wow),
                contentDescription = "",
                modifier = Modifier.size(200.dp))

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = "Wristband NG",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Text(text = stringResource(
                id = R.string.about),
                style = TextStyle(
                    color = Color.Gray,
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Button(
                onClick = {
                    navController.navigate("login")
                },
                contentPadding = PaddingValues(14.dp),
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Proceed to Login",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: AgentViewModel) {

    val places = listOf("Tropical Land", "African Junction", "Theme Park")
    val mContext = LocalContext.current
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val result by viewModel.loggedIn.observeAsState()
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    var place by remember { mutableStateOf("") }

    // Result
    if(result != null) {
        result?.let {
            if(it.status) {
                showToast(mContext, it.message)
            }
            else {
                showSnack(scaffoldState, coroutineScope, it.message)
            }

            //
            homePage(it.body, mContext, place)
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
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

            // Username
            Spacer(modifier = Modifier.padding(16.dp))
            val username = remember {
                mutableStateOf(TextFieldValue())
            }
            OutlinedTextField(
                value = username.value,
                placeholder = { Text(text = "Username") },
                onValueChange = {username.value = it},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Username") },
            )

            // Password
            Spacer(modifier = Modifier.padding(10.dp))
            val password = remember {
                mutableStateOf(TextFieldValue())
            }
            OutlinedTextField(
                value = password.value,
                label = {
                    Text("Password")
                },
                placeholder = { Text(text = "Password") },
                onValueChange = {password.value = it},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.padding(10.dp))

            // Place
            val icon = if (expanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown

            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expanded = !expanded
                        },
                    value = place,
                    readOnly = true,
                    onValueChange = {
                        expanded = true
                    },
                    label = {
                        Text("Place")
                    },
                    trailingIcon = {
                        Icon(icon, "Toggle", Modifier.clickable {
                            expanded = !expanded
                        })
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = { expanded = false }) {
                    places.forEach {
                        DropdownMenuItem(
                            onClick = {
                                place = it
                                expanded = false
                            }) { Text(it) }
                    }
                }
            }

            // Login
            Spacer(Modifier.height(20.dp))
            val modifier = Modifier.fillMaxWidth()
            Button(
                onClick = {

                    if (username.value.text.isEmpty()) {
                        showSnack(scaffoldState, coroutineScope, "Enter your username")
                        return@Button
                    }

                    if (password.value.text.isEmpty()) {
                        showSnack(scaffoldState, coroutineScope, "Enter your Password")
                        return@Button
                    }

                    if (place.isEmpty()) {
                        showSnack(scaffoldState, coroutineScope, "Select a Place")
                        return@Button
                    }

                    if (username.value.text.trim().isNotEmpty() && password.value.text.trim().isNotEmpty()) {
                        viewModel.login(username.value.text.trim(), password.value.text.trim())
                    }
                },
                modifier = modifier,
                contentPadding = PaddingValues(14.dp)
            ) {
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

fun showToast(context: Context, message: String) {
    Toasty.success(context, message).show()
}

fun homePage(agent: Agent?, context: Context, place: String) {

    if(agent != null) {
        val intent: Intent = if (agent.role == Role.ADMIN.name) {
            Intent(context, DashboardActivity::class.java)
        } else {
            Intent(context, MainActivity::class.java)
        }

        //
        val sessionManager = SessionManager(context)
        sessionManager.login(agent, place)

        //
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }
}
