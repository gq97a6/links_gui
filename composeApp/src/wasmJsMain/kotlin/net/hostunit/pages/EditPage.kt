package net.hostunit.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import links_gui.composeapp.generated.resources.Res
import links_gui.composeapp.generated.resources.gp_logo_gray
import links_gui.composeapp.generated.resources.gp_logo_orange
import org.jetbrains.compose.resources.painterResource

@Composable
fun LinkSlot(i: Int) = Column {
    var selected by remember { mutableStateOf(0) }
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        value,
        { value = it },
        label = { Text("Slot$i") },
        modifier = Modifier.widthIn(max = 800.dp).fillMaxWidth(0.9f).padding(top = 15.dp),
    )

    Spacer(Modifier.height(3.dp))

    Row {
        InputChip(
            selected == 0,
            { selected = 0 },
            { Text(text = "Karta", maxLines = 1, modifier = Modifier.padding(horizontal = 10.dp)) },
            modifier = Modifier.padding(end = 20.dp)
        )
        InputChip(
            selected == 1,
            { selected = 1 },
            { Text(text = "Link", maxLines = 1, modifier = Modifier.padding(horizontal = 10.dp)) },
            modifier = Modifier.padding(end = 20.dp)
        )
        InputChip(
            selected == 2,
            { selected = 2 },
            { Text(text = "Schowek", maxLines = 1, modifier = Modifier.padding(horizontal = 10.dp)) },
            modifier = Modifier.padding(end = 20.dp)
        )
    }
}

@Composable
fun EditPage(param: String? = null) = Column {

    var code by remember { mutableStateOf(param ?: "") }
    var direct by remember { mutableStateOf(false) }
    var permament by remember { mutableStateOf(false) }

    //var slot1 by remember { mutableStateOf("") }
    //var slot2 by remember { mutableStateOf("") }
    //var slot3 by remember { mutableStateOf("") }
    //var slot4 by remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically) {
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

        Spacer(Modifier.width(15.dp))

        Column {
            OutlinedTextField(
                code,
                { value ->
                    code = value.uppercase().filter { it.isDigit() || it.isLetter() }
                },
                label = { Text("Kod") },
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(0.7f)
                    .padding(top = 15.dp),
            )

            Row(
                Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(0.7f)
                    .padding(top = 5.dp)
            ) {
                FilledTonalButton(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Edytuj", maxLines = 1)
                }

                Spacer(Modifier.width(15.dp))

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Stwórz nowy", maxLines = 1)
                }
            }
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 25.dp).offset(x = (-10).dp)) {
        Checkbox(direct, { direct = it })
        Text("Bezpośredni", color = colorScheme.primary)

        Spacer(Modifier.width(30.dp))

        Checkbox(permament, { permament = it })
        Text("Permanentny", color = colorScheme.primary)
    }

    repeat(if (direct) 1 else 4) { LinkSlot(it) }

    Row(
        Modifier
            .widthIn(max = 700.dp)
            .fillMaxWidth(0.9f)
            .padding(top = 20.dp)
    ) {
        FilledTonalButton(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Potwierdź", maxLines = 1)
        }

        Spacer(Modifier.width(30.dp))

        Button(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Usuń ten adres", maxLines = 1)
        }
    }
}