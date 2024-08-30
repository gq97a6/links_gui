package net.hostunit.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import links_gui.composeapp.generated.resources.Res
import links_gui.composeapp.generated.resources.gp_logo_gray
import links_gui.composeapp.generated.resources.gp_logo_orange
import net.hostunit.Logo
import net.hostunit.adaptWidth
import net.hostunit.isMobile
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

@Composable
fun LinkSlot(i: Int) = Column {
    var selected by remember { mutableStateOf(0) }
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        value,
        { value = it },
        label = { Text("Slot$i") },
        singleLine = true,
        modifier = Modifier.adaptWidth(0.9f, 800.dp).padding(top = 15.dp),
    )

    Spacer(Modifier.height(3.dp))

    Row {
        listOf("Karta", "Link", "Schowek").forEachIndexed { i, text ->
            InputChip(
                selected == i,
                { selected = i },
                { Text(text = text, maxLines = 1, modifier = Modifier.padding(horizontal = 10.dp)) },
                modifier = Modifier.padding(end = 20.dp)
            )
        }
    }
}

@Composable
fun BoxScope.EditPage(param: String? = null) {
    var message by remember { mutableStateOf("") }

    Column(Modifier.onKeyEvent {
        if (it.key.keyCode == Key.Enter.keyCode) {
            message = Random.nextInt().toString()
            return@onKeyEvent true
        }
        return@onKeyEvent false
    }) {

        var code by remember { mutableStateOf(param ?: "") }
        var direct by remember { mutableStateOf(false) }
        var permament by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.adaptWidth(0.9f, 800.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isMobile) {
                Logo()
                Spacer(Modifier.width(15.dp))
            }

            Column {
                OutlinedTextField(
                    code,
                    { value ->
                        code = value.uppercase().filter { it.isDigit() || it.isLetter() }
                    },
                    label = { Text("Kod") },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                )

                Row(Modifier.fillMaxWidth().padding(top = 5.dp)) {
                    FilledTonalButton(
                        modifier = Modifier.weight(1f),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "Edytuj", maxLines = 1)
                    }

                    Spacer(Modifier.width(15.dp))

                    Button(
                        modifier = Modifier.weight(1.2f),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "Stwórz nowy", maxLines = 1)
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 25.dp).offset(x = (-10).dp)
        ) {
            Checkbox(direct, { direct = it })
            Text("Bezpośredni", color = colorScheme.primary)

            Spacer(Modifier.width(30.dp))

            Checkbox(permament, { permament = it })
            Text("Permanentny", color = colorScheme.primary)
        }

        repeat(if (direct) 1 else 4) { LinkSlot(it) }

        Row(Modifier.adaptWidth(0.8f, 500.dp).padding(top = 20.dp)) {
            FilledTonalButton(
                modifier = Modifier.weight(1f),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Potwierdź", maxLines = 1)
            }

            Spacer(Modifier.width(30.dp))

            Button(
                modifier = Modifier.weight(1.3f),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Usuń ten adres", maxLines = 1)
            }
        }
    }
}