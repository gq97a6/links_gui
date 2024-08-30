package net.hostunit.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
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
import net.hostunit.Logo
import net.hostunit.adaptWidth
import net.hostunit.isMobile

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
        Text(
            "$i",
            fontSize = if (isMobile) 60.sp else 140.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )
    }
}

@Composable
fun SearchBar(code: String, onChange: (String) -> Unit) {
    Column {
        OutlinedTextField(
            code,
            onValueChange = onChange,
            label = { Text("Kod") },
            modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth(0.7f).padding(top = 12.dp),
        )

        FilledTonalButton(
            modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth(0.6f).padding(top = 5.dp),
            onClick = { }
        ) {
            Text(text = "Potwierdź")
        }
    }
}

@Composable
fun BoxScope.CodePage(param: String? = null) = Column(horizontalAlignment = Alignment.CenterHorizontally) {

    var code by remember { mutableStateOf(param ?: "") }
    var isShown by remember { mutableStateOf(true) }

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
                onValueChange = { value ->
                    code = value.uppercase().filter { it.isDigit() || it.isLetter() }
                    isShown = code == "Z3Q5"
                },
                label = { Text("Kod") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            )

            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(.7f).padding(top = 5.dp),
                onClick = { }
            ) {
                Text(text = "Potwierdź")
            }
        }
    }

    AnimatedVisibility(isShown) {
        Row(Modifier.padding(top = 80.dp)) {
            Spacer(Modifier.weight(0.4f))
            LinkCard(1)
            Spacer(Modifier.weight(0.4f))
            LinkCard(2)
            Spacer(Modifier.weight(0.4f))
            LinkCard(3)
            Spacer(Modifier.weight(0.4f))
            LinkCard(4)
            Spacer(Modifier.weight(0.4f))
        }
    }
}