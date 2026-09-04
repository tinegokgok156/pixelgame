package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PetRepository
import com.example.model.PetSpecies
import com.example.ui.pixel.PixelContainer
import com.example.ui.pixel.PixelPetSprite
import com.example.ui.pixel.PixelTypeBadge

@Composable
fun StarterSelectScreen(
  onStarterSelected: (PetSpecies) -> Unit
) {
  val starters = remember { PetRepository.getStarters() }
  var selectedPet by remember { mutableStateOf(starters.first()) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF090D16))
      .padding(16.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = "PIXEL PETS DUEL",
      color = Color(0xFFFBBF24),
      fontSize = 26.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace,
      letterSpacing = 2.sp,
      textAlign = TextAlign.Center
    )

    Text(
      text = "CHOOSE YOUR STARTER PET",
      color = Color(0xFF94A3B8),
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace,
      modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
    )

    // Starters selection row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      starters.forEach { pet ->
        val isSelected = pet.id == selectedPet.id
        val borderColor = if (isSelected) Color(0xFFF59E0B) else Color(0xFF334155)

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { selectedPet = pet }
            .background(
              if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A),
              RoundedCornerShape(8.dp)
            )
            .border(if (isSelected) 3.dp else 1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .testTag("starter_choice_${pet.id}")
        ) {
          PixelPetSprite(
            species = pet,
            size = 80.dp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = pet.name,
            color = if (isSelected) Color(0xFFF8FAFC) else Color(0xFF94A3B8),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
          Spacer(modifier = Modifier.height(2.dp))
          PixelTypeBadge(type = pet.primaryType, fontSize = 9)
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Detail card for selected starter
    PixelContainer(
      modifier = Modifier.fillMaxWidth(),
      borderColor = selectedPet.primaryType.color
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          PixelPetSprite(
            species = selectedPet,
            size = 110.dp
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "${selectedPet.formattedId} ${selectedPet.name}",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
              PixelTypeBadge(type = selectedPet.primaryType)
              selectedPet.secondaryType?.let { sType ->
                Spacer(modifier = Modifier.width(4.dp))
                PixelTypeBadge(type = sType)
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "HP: ${selectedPet.baseHp}  ATK: ${selectedPet.baseAttack}",
              color = Color(0xFFCBD5E1),
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "DEF: ${selectedPet.baseDefense}  SPD: ${selectedPet.baseSpeed}",
              color = Color(0xFFCBD5E1),
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = selectedPet.description,
          color = Color(0xFFE2E8F0),
          fontSize = 13.sp,
          fontFamily = FontFamily.Monospace,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Starting Moves:",
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          selectedPet.baseMoves.forEach { move ->
            Box(
              modifier = Modifier
                .background(Color(0xFF0F172A), RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF334155), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
              Text(
                text = move.name,
                color = Color(0xFFF1F5F9),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    Button(
      onClick = { onStarterSelected(selectedPet) },
      colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFFE11D48),
        contentColor = Color.White
      ),
      shape = RoundedCornerShape(6.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("confirm_starter_button")
    ) {
      Text(
        text = "BEGIN ADVENTURE WITH ${selectedPet.name.uppercase()}",
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp
      )
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}
