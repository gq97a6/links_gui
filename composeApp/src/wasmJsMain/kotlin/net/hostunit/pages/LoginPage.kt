package net.hostunit.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import kotlinx.coroutines.launch
import links_gui.composeapp.generated.resources.Res
import links_gui.composeapp.generated.resources.gp_logo_gray
import links_gui.composeapp.generated.resources.gp_logo_orange
import net.hostunit.API
import net.hostunit.API.onFail
import net.hostunit.Notification
import net.hostunit.classes.User
import net.hostunit.onEnter
import org.jetbrains.compose.resources.painterResource

@Composable
fun BoxScope.LoginPage(param: String? = null) {

    var login by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var notiPayload by remember { mutableStateOf("") }
    var notiTrigger by remember { mutableStateOf(false) }


    fun onLogin(login: String, pass: String) {
        scope.launch {
            API.login(User(login, pass)) {
                window.location.href = if (param != null) "/edit/$param" else "/edit"
            } onFail {
                notiPayload = "Niepoprawne dane logowania"
                notiTrigger = !notiTrigger
            }
        }
    }

    Column {

        Box(Modifier.widthIn(max = 650.dp).fillMaxWidth(0.4f)) {
            Icon(
                painterResource(Res.drawable.gp_logo_gray),
                contentDescription = "",
                tint = colorScheme.onBackground
            )
            Icon(
                painterResource(Res.drawable.gp_logo_orange),
                contentDescription = "",
                tint = colorScheme.secondaryContainer
            )
        }

        OutlinedTextField(
            login,
            { login = it },
            label = { Text("Login") },
            singleLine = true,
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxWidth(0.7f)
                .padding(top = 10.dp)
                .onEnter { onLogin(login, pass) }
        )
        OutlinedTextField(
            pass,
            { pass = it },
            label = { Text("Hasło") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxWidth(0.8f)
                .padding(top = 10.dp)
                .onEnter { onLogin(login, pass) }
        )

        FilledTonalButton(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth(0.6f)
                .padding(top = 15.dp),
            onClick = {
                onLogin(login, pass)
            }
        ) {
            Text(text = "Zaloguj się")
        }
    }

    Notification(notiPayload, notiTrigger)
}