package com.example.ui.pixel

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.ElementType
import com.example.model.PetSpecies
import java.util.Random

enum class SpriteFacing {
  FRONT,
  BACK
}

data class PixelPalette(
  val outline: Color,
  val primary: Color,
  val secondary: Color,
  val accent: Color,
  val eyeWhite: Color,
  val eyePupil: Color
)

object SpritePaletteGenerator {
  fun getPalette(primaryType: ElementType, secondaryType: ElementType?, seed: Long): PixelPalette {
    val rng = Random(seed)
    return when (primaryType) {
      ElementType.FIRE -> PixelPalette(
        outline = Color(0xFF270805),
        primary = Color(0xFFE65100),
        secondary = Color(0xFFFFB74D),
        accent = Color(0xFFD50000),
        eyeWhite = Color(0xFFFFFDE7),
        eyePupil = Color(0xFF00B0FF)
      )
      ElementType.WATER -> PixelPalette(
        outline = Color(0xFF0D1B2A),
        primary = Color(0xFF0288D1),
        secondary = Color(0xFF81D4FA),
        accent = Color(0xFF00E5FF),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF01579B)
      )
      ElementType.GRASS -> PixelPalette(
        outline = Color(0xFF1B3B18),
        primary = Color(0xFF43A047),
        secondary = Color(0xFFA5D6A7),
        accent = if (rng.nextBoolean()) Color(0xFFE91E63) else Color(0xFFFFD54F),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF2E7D32)
      )
      ElementType.ELECTRIC -> PixelPalette(
        outline = Color(0xFF261C02),
        primary = Color(0xFFFFD600),
        secondary = Color(0xFFFFF9C4),
        accent = Color(0xFF00E5FF),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF212121)
      )
      ElementType.ICE -> PixelPalette(
        outline = Color(0xFF102A43),
        primary = Color(0xFF4DD0E1),
        secondary = Color(0xFFE0F7FA),
        accent = Color(0xFF00B0FF),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF006064)
      )
      ElementType.EARTH -> PixelPalette(
        outline = Color(0xFF2D1B08),
        primary = Color(0xFF8D6E63),
        secondary = Color(0xFFD7CCC8),
        accent = Color(0xFFFFB300),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF3E2723)
      )
      ElementType.FLYING -> PixelPalette(
        outline = Color(0xFF1F1D36),
        primary = Color(0xFF9FA8DA),
        secondary = Color(0xFFE8EAF6),
        accent = Color(0xFF80DEEA),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF283593)
      )
      ElementType.PSYCHIC -> PixelPalette(
        outline = Color(0xFF310B2D),
        primary = Color(0xFFEC407A),
        secondary = Color(0xFFF8BBD0),
        accent = Color(0xFFBA68C8),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF4A148C)
      )
      ElementType.DARK -> PixelPalette(
        outline = Color(0xFF0F0F14),
        primary = Color(0xFF37474F),
        secondary = Color(0xFF78909C),
        accent = Color(0xFFFF1744),
        eyeWhite = Color(0xFFFF8A80),
        eyePupil = Color(0xFFD50000)
      )
      ElementType.DRAGON -> PixelPalette(
        outline = Color(0xFF140B34),
        primary = Color(0xFF5E35B1),
        secondary = Color(0xFFB39DDB),
        accent = Color(0xFFFFD600),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF00E676)
      )
      ElementType.STEEL -> PixelPalette(
        outline = Color(0xFF1E293B),
        primary = Color(0xFF90A4AE),
        secondary = Color(0xFFECEFF1),
        accent = Color(0xFFFFC107),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF263238)
      )
      ElementType.POISON -> PixelPalette(
        outline = Color(0xFF2A082C),
        primary = Color(0xFF8E24AA),
        secondary = Color(0xFFCE93D8),
        accent = Color(0xFF76FF03),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF4A148C)
      )
      ElementType.FAIRY -> PixelPalette(
        outline = Color(0xFF3E1F30),
        primary = Color(0xFFF48FB1),
        secondary = Color(0xFFFCE4EC),
        accent = Color(0xFFFFF59D),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF880E4F)
      )
      ElementType.NORMAL -> PixelPalette(
        outline = Color(0xFF26231B),
        primary = Color(0xFFA1887F),
        secondary = Color(0xFFD7CCC8),
        accent = Color(0xFF8D6E63),
        eyeWhite = Color(0xFFFFFFFF),
        eyePupil = Color(0xFF3E2723)
      )
    }
  }
}

/**
 * 16x16 Procedural Symmetric Pixel Sprite Generator
 * 0: Transparent
 * 1: Outline
 * 2: Primary Body
 * 3: Secondary Highlight / Shading
 * 4: Accent (horns, crest, wing, chest, tail)
 * 5: Eye White
 * 6: Eye Pupil
 */
class PetSpriteMatrix(val seed: Long, val facing: SpriteFacing) {
  val grid = Array(16) { IntArray(16) { 0 } }

  init {
    val rng = Random(seed xor (if (facing == SpriteFacing.BACK) 0x55AA55AA else 0L))
    val bodyWidth = 3 + (rng.nextInt(3)) // half-width from center (cols 7 and 8)
    val headHeight = 4 + rng.nextInt(3)
    val bodyHeight = 5 + rng.nextInt(4)
    val hasHorns = rng.nextBoolean()
    val hasWings = rng.nextBoolean()
    val hasTail = rng.nextBoolean()

    // Generate symmetrical half (cols 0..7) and mirror to (cols 8..15)
    val half = Array(16) { IntArray(8) { 0 } }

    val startY = 15 - bodyHeight - headHeight

    // 1. Build silhouette in half
    for (y in startY until 15) {
      val isHead = y < (startY + headHeight)
      val w = if (isHead) {
        (bodyWidth - 1 + (if (y == startY) -1 else 0)).coerceAtLeast(2)
      } else {
        bodyWidth + (if (y == 14) -1 else 0)
      }

      val minX = (7 - w).coerceIn(1, 7)
      for (x in minX..7) {
        val isEdge = x == minX || y == startY || y == 14
        half[y][x] = if (isEdge) 1 else 2
      }
    }

    // 2. Add Ears / Horns
    if (hasHorns) {
      val hornX = (7 - bodyWidth + 1).coerceIn(2, 6)
      val hornY = (startY - 2).coerceAtLeast(1)
      for (y in hornY until startY) {
        half[y][hornX] = 4
        if (hornX > 0) half[y][hornX - 1] = 1
      }
    }

    // 3. Add Wings / Fins
    if (hasWings) {
      val wingY = startY + headHeight
      for (x in 0..2) {
        if (wingY in 0..14) {
          half[wingY][x] = 1
          half[wingY + 1][x] = 4
        }
      }
    }

    // 4. Eyes (Front only)
    if (facing == SpriteFacing.FRONT) {
      val eyeY = startY + headHeight / 2
      val eyeX = (7 - bodyWidth / 2).coerceIn(3, 6)
      if (eyeY in 0..14 && eyeX in 0..7) {
        half[eyeY][eyeX] = 5
        half[eyeY][eyeX + 1] = 6
      }
    } else {
      // Back markings
      val backMarkY = startY + headHeight / 2
      val backX = 6
      if (backMarkY in 0..14) {
        half[backMarkY][backX] = 3
        half[backMarkY + 1][backX] = 3
      }
    }

    // 5. Belly / Accent patch
    for (y in (startY + headHeight) until 14) {
      for (x in 5..7) {
        if (half[y][x] == 2) {
          half[y][x] = if (rng.nextBoolean()) 4 else 3
        }
      }
    }

    // 6. Mirror to complete 16x16 matrix with outline wrapping
    for (y in 0..15) {
      for (x in 0..7) {
        val v = half[y][x]
        grid[y][x] = v
        grid[y][15 - x] = if (v == 6 && facing == SpriteFacing.FRONT) 6 else v
      }
    }

    // Ensure outer outline ring around non-zero pixels
    for (y in 0..15) {
      for (x in 0..15) {
        if (grid[y][x] > 1) {
          val neighbors = listOf(y - 1 to x, y + 1 to x, y to x - 1, y to x + 1)
          for ((ny, nx) in neighbors) {
            if (ny in 0..15 && nx in 0..15 && grid[ny][nx] == 0) {
              grid[ny][nx] = 1
            }
          }
        }
      }
    }
  }
}

@Composable
fun PixelPetSprite(
  species: PetSpecies,
  modifier: Modifier = Modifier,
  size: Dp = 120.dp,
  facing: SpriteFacing = SpriteFacing.FRONT,
  isAttacking: Boolean = false,
  isHit: Boolean = false,
  isFainted: Boolean = false,
  showIdleAnimation: Boolean = true
) {
  val palette = remember(species.id, species.primaryType, species.secondaryType) {
    SpritePaletteGenerator.getPalette(species.primaryType, species.secondaryType, species.spriteSeed)
  }

  val matrix = remember(species.spriteSeed, facing) {
    PetSpriteMatrix(species.spriteSeed, facing)
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pet_idle")
  val bounceY by if (showIdleAnimation && !isFainted) {
    infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 6f,
      animationSpec = infiniteRepeatable(
        animation = tween(650, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
      ),
      label = "bounce"
    )
  } else {
    remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
  }

  val breathScaleX by if (showIdleAnimation && !isFainted) {
    infiniteTransition.animateFloat(
      initialValue = 1.0f,
      targetValue = 1.03f,
      animationSpec = infiniteRepeatable(
        animation = tween(650, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
      ),
      label = "breath"
    )
  } else {
    remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
  }

  val attackOffset = if (isAttacking) {
    if (facing == SpriteFacing.BACK) -24f else 24f
  } else 0f

  val hitAlpha = if (isHit) 0.4f else if (isFainted) 0.2f else 1.0f
  val faintOffset = if (isFainted) 30f else 0f

  Box(
    modifier = modifier
      .size(size)
      .graphicsLayer {
        translationY = bounceY + faintOffset
        translationX = attackOffset
        scaleX = breathScaleX
        alpha = hitAlpha
      },
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(size)) {
      val pixelSize = this.size.width / 16f
      val grid = matrix.grid

      for (y in 0 until 16) {
        for (x in 0 until 16) {
          val pixelVal = grid[y][x]
          if (pixelVal == 0) continue

          val color = when (pixelVal) {
            1 -> palette.outline
            2 -> palette.primary
            3 -> palette.secondary
            4 -> palette.accent
            5 -> palette.eyeWhite
            6 -> palette.eyePupil
            else -> palette.primary
          }

          drawRect(
            color = color,
            topLeft = Offset(x * pixelSize, y * pixelSize),
            size = Size(pixelSize, pixelSize)
          )
        }
      }

      // Shadow underneath pet
      if (!isFainted) {
        drawOval(
          color = Color(0x33000000),
          topLeft = Offset(pixelSize * 2f, pixelSize * 14.5f),
          size = Size(pixelSize * 12f, pixelSize * 1.5f)
        )
      }
    }
  }
}
