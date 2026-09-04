package com.example.ui.pixel

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ElementType

@Composable
fun PixelContainer(
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color(0xFF1E293B),
  borderColor: Color = Color(0xFF64748B),
  content: @Composable () -> Unit
) {
  Box(
    modifier = modifier
      .background(borderColor, RoundedCornerShape(4.dp))
      .padding(3.dp)
      .background(Color(0xFF0F172A), RoundedCornerShape(2.dp))
      .padding(2.dp)
      .background(backgroundColor, RoundedCornerShape(2.dp))
      .padding(8.dp)
  ) {
    content()
  }
}

@Composable
fun PixelDialogueBox(
  text: String,
  modifier: Modifier = Modifier,
  promptNext: Boolean = false,
  onClick: (() -> Unit)? = null
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
      .border(3.dp, Color(0xFF94A3B8), RoundedCornerShape(6.dp))
      .padding(4.dp)
      .background(Color(0xFFF8FAFC), RoundedCornerShape(4.dp))
      .border(2.dp, Color(0xFF334155), RoundedCornerShape(4.dp))
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
      .padding(horizontal = 14.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = text,
        color = Color(0xFF0F172A),
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        lineHeight = 22.sp,
        modifier = Modifier.weight(1f)
      )
      if (promptNext) {
        Text(
          text = "▼",
          color = Color(0xFFE11D48),
          fontSize = 14.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace,
          modifier = Modifier.padding(start = 6.dp)
        )
      }
    }
  }
}

@Composable
fun PixelHpBar(
  currentHp: Int,
  maxHp: Int,
  modifier: Modifier = Modifier,
  showNumbers: Boolean = true,
  height: Dp = 10.dp
) {
  val ratio = if (maxHp > 0) (currentHp.toFloat() / maxHp.toFloat()).coerceIn(0f, 1f) else 0f
  val animatedRatio by animateFloatAsState(
    targetValue = ratio,
    animationSpec = tween(durationMillis = 400),
    label = "hp_ratio"
  )

  val barColor = when {
    ratio > 0.5f -> Color(0xFF22C55E) // Vibrant Green
    ratio > 0.2f -> Color(0xFFF59E0B) // Warning Yellow
    else -> Color(0xFFEF4444) // Critical Red
  }

  Column(modifier = modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .background(Color(0xFF0F172A), RoundedCornerShape(2.dp))
          .padding(horizontal = 4.dp, vertical = 1.dp)
      ) {
        Text(
          text = "HP",
          color = Color(0xFFFACC15),
          fontSize = 10.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace
        )
      }
      Spacer(modifier = Modifier.width(4.dp))
      Box(
        modifier = Modifier
          .weight(1f)
          .height(height)
          .background(Color(0xFF334155), RoundedCornerShape(3.dp))
          .border(1.dp, Color(0xFF0F172A), RoundedCornerShape(3.dp))
          .padding(1.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(animatedRatio)
            .height(height)
            .clip(RoundedCornerShape(2.dp))
            .background(barColor)
        )
      }
    }
    if (showNumbers) {
      Text(
        text = "$currentHp / $maxHp",
        color = Color(0xFFE2E8F0),
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .align(Alignment.End)
          .padding(top = 2.dp)
      )
    }
  }
}

@Composable
fun PixelExpBar(
  currentExp: Int,
  maxExp: Int,
  modifier: Modifier = Modifier
) {
  val ratio = if (maxExp > 0) (currentExp.toFloat() / maxExp.toFloat()).coerceIn(0f, 1f) else 0f
  val animatedRatio by animateFloatAsState(
    targetValue = ratio,
    animationSpec = tween(durationMillis = 350),
    label = "exp_ratio"
  )

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "EXP",
      color = Color(0xFF38BDF8),
      fontSize = 9.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace,
      modifier = Modifier.padding(end = 4.dp)
    )
    Box(
      modifier = Modifier
        .weight(1f)
        .height(6.dp)
        .background(Color(0xFF1E293B), RoundedCornerShape(2.dp))
        .border(1.dp, Color(0xFF0F172A), RoundedCornerShape(2.dp))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(animatedRatio)
          .height(6.dp)
          .background(
            Brush.horizontalGradient(
              listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
            )
          )
      )
    }
  }
}

@Composable
fun PixelTypeBadge(
  type: ElementType,
  modifier: Modifier = Modifier,
  fontSize: Int = 11
) {
  Box(
    modifier = modifier
      .background(type.badgeColor, RoundedCornerShape(3.dp))
      .border(1.dp, Color(0xFF0F172A), RoundedCornerShape(3.dp))
      .padding(horizontal = 6.dp, vertical = 2.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = type.displayName.uppercase(),
      color = type.textColor,
      fontSize = fontSize.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun PixelBattlePlatform(
  modifier: Modifier = Modifier,
  isOpponent: Boolean = false,
  tintColor: Color = Color(0xFF2E7D32)
) {
  Box(
    modifier = modifier
      .height(34.dp)
      .fillMaxWidth()
      .background(
        Brush.radialGradient(
          colors = listOf(
            tintColor.copy(alpha = 0.85f),
            tintColor.copy(alpha = 0.45f),
            Color.Transparent
          )
        ),
        shape = RoundedCornerShape(50)
      )
      .border(
        width = 2.dp,
        color = tintColor.copy(alpha = 0.6f),
        shape = RoundedCornerShape(50)
      )
  )
}
