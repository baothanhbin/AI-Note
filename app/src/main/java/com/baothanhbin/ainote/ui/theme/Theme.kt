package com.baothanhbin.ainote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.ainote.core.designsystem.theme.AINoteTheme as CoreAINoteTheme

@Composable
fun AINoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    CoreAINoteTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content
    )
}