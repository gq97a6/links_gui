package net.hostunit.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import links_gui.composeapp.generated.resources.Res
import links_gui.composeapp.generated.resources.gp_logo_gray
import links_gui.composeapp.generated.resources.gp_logo_orange
import org.jetbrains.compose.resources.painterResource

@Composable
fun RowScope.LinkCard(i: Int) {
    Box(
        Modifier
            .weight(1f)
            .aspectRatio(1f)
            .border(1.dp, colorScheme.primaryContainer, shapes.large)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Text("$i", fontSize = 180.sp, fontWeight = FontWeight.Bold, color = colorScheme.primary)
    }
}

@Composable
fun CodePage(param: String? = null) = Column(horizontalAlignment = Alignment.CenterHorizontally) {

    var code by remember { mutableStateOf(param ?: "") }
    var isShown by remember { mutableStateOf(code == "Z3Q5") }

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
                    isShown = code == "Z3Q5"
                },
                label = { Text("Kod") },
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(0.7f)
                    .padding(top = 15.dp),
            )

            FilledTonalButton(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.6f)
                    .padding(top = 5.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Potwierdź")
            }
        }
    }

    AnimatedVisibility(isShown) {
        Row(Modifier.padding(top = 80.dp)) {
            Spacer(Modifier.weight(0.35f))
            LinkCard(1)
            Spacer(Modifier.weight(0.35f))
            LinkCard(2)
            Spacer(Modifier.weight(0.35f))
            LinkCard(3)
            Spacer(Modifier.weight(0.35f))
            LinkCard(4)
            Spacer(Modifier.weight(0.35f))
        }
    }
}