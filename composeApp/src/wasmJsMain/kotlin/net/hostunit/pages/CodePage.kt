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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.browser.window
import kotlinx.coroutines.launch
import net.hostunit.*
import net.hostunit.API.onFail
import net.hostunit.classes.Address
import net.hostunit.classes.Link

@Composable
fun RowScope.LinkCard(i: Int, address: Address, notify: (String) -> Unit) {
    Box(
        Modifier
            .weight(1f)
            .aspectRatio(1f)
            .alpha(if (address.links.size > i) 1f else 0.3f)
            .border(1.dp, colorScheme.primaryContainer, shapes.large)
            .clickable(enabled = address.links.size > i) {
                when (address.links[i].action) {
                    Link.Action.COPY -> {
                        // Paste payload into clipboard
                        window.navigator.clipboard.writeText(address.links[i].payload)
                        notify("Przeniesiono do schowka")
                    }

                    Link.Action.LINK -> {
                        // Redirect current tab to payload
                        window.location.href = address.links[i].payload
                    }

                    Link.Action.TAB -> {
                        // Open new tab with payload
                        window.open(address.links[i].payload, "_blank")
                    }
                }
            },
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
fun BoxScope.CodePage(param: String = "") {

    var code by remember { mutableStateOf(param.uppercase().filter { it.isDigit() || it.isLetter() }) }
    var address by remember { mutableStateOf(null as Address?) }
    var cardsShown by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var notiPayload by remember { mutableStateOf("") }
    var notiTrigger by remember { mutableStateOf(false) }

    fun onSearch(code: String) {
        scope.launch {
            if (code.isNotEmpty()) API.getAddress(code) {
                address = it
                cardsShown = true
                window.history.replaceState(null, "", "/$code")
            } onFail {
                window.history.replaceState(null, "", "/")
                cardsShown = false
                notiPayload = "Ten adres nie istnieje"
                notiTrigger = !notiTrigger
            }
        }
    }

    LaunchedEffect("key") {
        if (code.isNotEmpty()) onSearch(code)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

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
                        if (code != value) {
                            window.history.replaceState(null, "", "/")
                            cardsShown = false
                        }
                        code = value.uppercase().filter { it.isDigit() || it.isLetter() }
                    },
                    label = { Text("Kod") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .onEnter { onSearch(code) },
                )

                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(.7f).padding(top = 5.dp),
                    onClick = { onSearch(code) }
                ) {
                    Text(text = "Potwierdź")
                }
            }
        }

        AnimatedVisibility(cardsShown) {
            address?.let { address ->
                Row(Modifier.padding(top = 80.dp)) {
                    repeat(4) {
                        Spacer(Modifier.weight(0.4f))
                        LinkCard(it, address) {
                            notiPayload = it
                            notiTrigger = !notiTrigger
                        }
                    }
                    Spacer(Modifier.weight(0.4f))
                }
            }
        }
    }

    Notification(notiPayload, notiTrigger)
}