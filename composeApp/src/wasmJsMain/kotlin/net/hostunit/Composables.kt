package net.hostunit

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.browser.window
import kotlinx.coroutines.delay
import links_gui.composeapp.generated.resources.Res
import links_gui.composeapp.generated.resources.gp_logo_gray
import links_gui.composeapp.generated.resources.gp_logo_orange
import org.jetbrains.compose.resources.painterResource

val isMobile = window.innerHeight.toDouble() / window.innerWidth.toDouble() > 1f

fun Modifier.adaptWidth(mobile: Float, desktop: Dp): Modifier {
    return if (isMobile) this.fillMaxWidth(mobile)
    else this.width(desktop)
}

fun Modifier.adaptHeight(mobile: Float, desktop: Dp): Modifier {
    return if (isMobile) this.fillMaxHeight(mobile)
    else this.height(desktop)
}

fun Modifier.onEnter(action: () -> Unit) = this.onKeyEvent {
    if (it.key.keyCode == Key.Enter.keyCode) {
        action()
        return@onKeyEvent true
    }
    return@onKeyEvent false
}

@Composable
fun BoxScope.Notification(text: String, trigger: Boolean) {
    var isShown by remember { mutableStateOf(false) }

    if (text.isNotEmpty()) LaunchedEffect(trigger) {
        isShown = true
        delay(3000)
        isShown = false
    }

    AnimatedVisibility(
        isShown && text.isNotEmpty(),
        modifier = Modifier.align(Alignment.TopCenter),
        enter = fadeIn() + slideInVertically(),
        exit = slideOutVertically() + fadeOut(),
    ) {
        ElevatedCard(Modifier.padding(top = 20.dp).height(50.dp)) {
            Box(Modifier.fillMaxHeight().padding(horizontal = 50.dp), contentAlignment = Alignment.Center) {
                Text(text, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = colorScheme.primary)
            }
        }
    }
}

@Composable
fun Logo(modifier: Modifier = Modifier.height(130.dp)) {
    Box(modifier) {
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
}