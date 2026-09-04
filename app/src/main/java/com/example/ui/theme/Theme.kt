package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PixelGameColorScheme = darkColorScheme(
  primary = Color(0xFFE11D48),
  secondary = Color(0xFFF59E0B),
  tertiary = Color(0xFF38BDF8),
  background = Color(0xFF090D16),
  surface = Color(0xFF0F172A),
  onPrimary = Color.White,
  onSecondary = Color(0xFF0F172A),
  onBackground = Color(0xFFF8FAFC),
  onSurface = Color(0xFFF8FAFC)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = PixelGameColorScheme,
    typography = Typography,
    content = content
  )
}

