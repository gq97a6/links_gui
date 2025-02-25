package net.hostunit.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Theme(isDark: Boolean = true, content: @Composable () -> Unit) {

    //isSystemInDarkTheme()
    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        content = content,
        colorScheme = colorScheme,
        typography = Typography,
        //shapes = Shapes(
        //    small = RoundedCornerShape(10.dp),
        //    medium = RoundedCornerShape(10.dp),
        //    large = RoundedCornerShape(10.dp)
        //)
    )
}