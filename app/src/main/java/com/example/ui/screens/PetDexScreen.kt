package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.PetRepository
import com.example.model.ElementType
import com.example.model.PetSpecies
import com.example.ui.pixel.PixelContainer
import com.example.ui.pixel.PixelPetSprite
import com.example.ui.pixel.PixelTypeBadge
import kotlin.math.roundToInt

@Composable
fun PetDexScreen(
  seenPetIds: Set<Int>,
  caughtPetIds: Set<Int>,
  onStartDuelWithPet: (speciesId: Int, level: Int) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedTypeFilter by remember { mutableStateOf<ElementType?>(null) }
  var inspectingPet by remember { mutableStateOf<PetSpecies?>(null) }

  val filteredPets = remember(searchQuery, selectedTypeFilter) {
    PetRepository.searchPets(searchQuery, selectedTypeFilter)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF090D16))
      .padding(12.dp)
  ) {
    // Top Dex Header
    PixelContainer(
      modifier = Modifier.fillMaxWidth(),
      backgroundColor = Color(0xFF1E293B),
      borderColor = Color(0xFFE11D48)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "PETDEX DATABASE",
            color = Color(0xFFFBBF24),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "500 TOTAL SPECIES",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "CAUGHT: ${caughtPetIds.size}",
              color = Color(0xFF22C55E),
              fontSize = 12.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "SEEN: ${seenPetIds.size}",
              color = Color(0xFF38BDF8),
              fontSize = 12.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Search input
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = {
        Text(
          "Search name or #ID (1-500)...",
          color = Color(0xFF64748B),
          fontSize = 13.sp,
          fontFamily = FontFamily.Monospace
        )
      },
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFFBBF24),
        unfocusedBorderColor = Color(0xFF334155),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = Color(0xFF0F172A),
        unfocusedContainerColor = Color(0xFF0F172A)
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("petdex_search_input")
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Element Type Filters
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      contentPadding = PaddingValues(vertical = 4.dp)
    ) {
      item {
        val isAll = selectedTypeFilter == null
        Box(
          modifier = Modifier
            .background(
              if (isAll) Color(0xFFFBBF24) else Color(0xFF1E293B),
              RoundedCornerShape(4.dp)
            )
            .border(1.dp, Color(0xFF475569), RoundedCornerShape(4.dp))
            .clickable { selectedTypeFilter = null }
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "ALL (500)",
            color = if (isAll) Color(0xFF0F172A) else Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
      }
      items(ElementType.values()) { type ->
        val isSelected = selectedTypeFilter == type
        Box(
          modifier = Modifier
            .background(
              if (isSelected) type.color else Color(0xFF1E293B),
              RoundedCornerShape(4.dp)
            )
            .border(1.dp, type.badgeColor, RoundedCornerShape(4.dp))
            .clickable { selectedTypeFilter = if (isSelected) null else type }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = type.displayName.uppercase(),
            color = if (isSelected) Color(0xFF0F172A) else Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // 500 Pets Grid
    LazyVerticalGrid(
      columns = GridCells.Fixed(3),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp),
      modifier = Modifier
        .weight(1f)
        .testTag("petdex_grid")
    ) {
      items(filteredPets) { pet ->
        val isCaught = caughtPetIds.contains(pet.id)
        val isSeen = seenPetIds.contains(pet.id)

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
            .border(
              1.dp,
              if (isCaught) Color(0xFF22C55E) else if (isSeen) Color(0xFF38BDF8) else Color(0xFF1E293B),
              RoundedCornerShape(6.dp)
            )
            .clickable { inspectingPet = pet }
            .padding(6.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = pet.formattedId,
              color = Color(0xFF64748B),
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            if (isCaught) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(Color(0xFF22C55E), CircleShape)
              )
            } else if (isSeen) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(Color(0xFF38BDF8), CircleShape)
              )
            }
          }

          PixelPetSprite(
            species = pet,
            size = 64.dp,
            showIdleAnimation = false
          )

          Text(
            text = pet.name,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            maxLines = 1
          )

          Spacer(modifier = Modifier.height(2.dp))
          PixelTypeBadge(type = pet.primaryType, fontSize = 7)
        }
      }
    }
  }

  // PET INSPECTOR MODAL
  inspectingPet?.let { pet ->
    var duelLevel by remember { mutableFloatStateOf(15f) }

    Dialog(onDismissRequest = { inspectingPet = null }) {
      Surface(
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, pet.primaryType.color),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${pet.formattedId} ${pet.name}",
              color = Color(0xFFFBBF24),
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "✕",
              color = Color(0xFF94A3B8),
              fontSize = 16.sp,
              modifier = Modifier
                .clickable { inspectingPet = null }
                .padding(4.dp)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          PixelPetSprite(
            species = pet,
            size = 130.dp
          )

          Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(vertical = 6.dp)
          ) {
            PixelTypeBadge(type = pet.primaryType)
            pet.secondaryType?.let { sType ->
              PixelTypeBadge(type = sType)
            }
          }

          Text(
            text = "Tier: ${pet.rarityTier.label}   Total Stats: ${pet.totalBaseStats}",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = pet.description,
            color = Color(0xFFE2E8F0),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Base Stats Bars
          Column(modifier = Modifier.fillMaxWidth()) {
            StatRow("HP", pet.baseHp, Color(0xFFEF4444))
            StatRow("ATK", pet.baseAttack, Color(0xFFF97316))
            StatRow("DEF", pet.baseDefense, Color(0xFF3B82F6))
            StatRow("SPD", pet.baseSpeed, Color(0xFF10B981))
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Duel setup
          Text(
            text = "PRACTICE DUEL AT LEVEL: ${duelLevel.roundToInt()}",
            color = Color(0xFFFBBF24),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Slider(
            value = duelLevel,
            onValueChange = { duelLevel = it },
            valueRange = 5f..80f,
            steps = 15,
            colors = SliderDefaults.colors(
              thumbColor = Color(0xFFFBBF24),
              activeTrackColor = Color(0xFFE11D48)
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Button(
            onClick = {
              val level = duelLevel.roundToInt()
              inspectingPet = null
              onStartDuelWithPet(pet.id, level)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("duel_selected_pet_button")
          ) {
            Text(
              text = "⚔ DUEL WITH ${pet.name.uppercase()}",
              fontSize = 13.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 1.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun StatRow(label: String, value: Int, color: Color) {
  val barFraction = (value.toFloat() / 160f).coerceIn(0.1f, 1f)
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 2.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label.padEnd(3),
      color = Color(0xFF94A3B8),
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace,
      modifier = Modifier.width(36.dp)
    )
    Text(
      text = value.toString().padStart(3),
      color = Color.White,
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace,
      modifier = Modifier.width(30.dp)
    )
    Box(
      modifier = Modifier
        .weight(1f)
        .height(8.dp)
        .background(Color(0xFF1E293B), RoundedCornerShape(2.dp))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(barFraction)
          .height(8.dp)
          .background(color, RoundedCornerShape(2.dp))
      )
    }
  }
}
