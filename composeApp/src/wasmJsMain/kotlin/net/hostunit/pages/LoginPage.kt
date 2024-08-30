package net.hostunit.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import links_gui.composeapp.generated.resources.Res
import links_gui.composeapp.generated.resources.gp_logo_gray
import links_gui.composeapp.generated.resources.gp_logo_orange
import org.jetbrains.compose.resources.painterResource

@Composable
fun BoxScope.LoginPage(param: String? = null) = Column {

    var login by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

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
        modifier = Modifier
            .widthIn(max = 800.dp)
            .fillMaxWidth(0.7f)
            .padding(top = 10.dp)
    )
    OutlinedTextField(
        pass,
        { pass = it },
        label = { Text("Hasło") },
        modifier = Modifier
            .widthIn(max = 800.dp)
            .fillMaxWidth(0.8f)
            .padding(top = 10.dp)
    )

    FilledTonalButton(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth(0.6f)
            .padding(top = 15.dp),
        onClick = { /*TODO*/ }
    ) {
        Text(text = "Zaloguj się")
    }
}