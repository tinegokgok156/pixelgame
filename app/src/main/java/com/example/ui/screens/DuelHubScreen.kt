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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GymLeader
import com.example.data.PetRepository
import com.example.model.BattlePet
import com.example.ui.pixel.PixelContainer
import com.example.ui.pixel.PixelPetSprite
import com.example.ui.pixel.PixelTypeBadge

@Composable
fun DuelHubScreen(
  party: List<BattlePet>,
  coins: Int,
  duelsWon: Int,
  onStartWildDuel: (String) -> Unit,
  onStartGymDuel: (GymLeader) -> Unit,
  onStartRandomCustomDuel: () -> Unit,
  onOpenPetDex: () -> Unit
) {
  val gymLeaders = remember { PetRepository.getGymLeaders() }
  val leadPet = party.firstOrNull()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF090D16))
      .padding(12.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // Top Trainer Bar
    PixelContainer(
      modifier = Modifier.fillMaxWidth(),
      backgroundColor = Color(0xFF1E293B),
      borderColor = Color(0xFF38BDF8)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (leadPet != null) {
            PixelPetSprite(species = leadPet.species, size = 48.dp)
            Spacer(modifier = Modifier.width(8.dp))
          }
          Column {
            Text(
              text = "TRAINER PROFILE",
              color = Color(0xFFFBBF24),
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "Duels Won: $duelsWon",
              color = Color(0xFF38BDF8),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }

        Box(
          modifier = Modifier
            .background(Color(0xFF0F172A), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFFFBBF24), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "$$coins COINS",
            color = Color(0xFFFACC15),
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // SECTION 1: WILD PET BIOME EXPLORATION
    Text(
      text = "EXPLORE & DUEL WILD PETS",
      color = Color(0xFFFBBF24),
      fontSize = 14.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace,
      letterSpacing = 1.sp
    )
    Text(
      text = "Battle wild creatures and throw Pet Orbs to catch them!",
      color = Color(0xFF94A3B8),
      fontSize = 11.sp,
      fontFamily = FontFamily.Monospace,
      modifier = Modifier.padding(bottom = 8.dp)
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      BiomeCard("Verdant Forest", "Grass, Bug & Normal Pets (Lv. 3-8)", Color(0xFF10B981)) {
        onStartWildDuel("Forest")
      }
      BiomeCard("Volcanic Peaks", "Fire & Earth Magma Pets (Lv. 10-18)", Color(0xFFF97316)) {
        onStartWildDuel("Volcano")
      }
      BiomeCard("Sunken Ocean", "Water & Ice Deep Sea Pets (Lv. 15-24)", Color(0xFF0284C7)) {
        onStartWildDuel("Ocean")
      }
      BiomeCard("Thunder Plains", "Electric & Flying Storm Pets (Lv. 22-30)", Color(0xFFEAB308)) {
        onStartWildDuel("Thunder")
      }
      BiomeCard("Shadow Crypt", "Dark & Psychic Phantom Pets (Lv. 28-38)", Color(0xFFA855F7)) {
        onStartWildDuel("Shadow")
      }
      BiomeCard("Celestial Spire", "Dragon, Fairy & Apex Pets (Lv. 35-50)", Color(0xFFE11D48)) {
        onStartWildDuel("Spire")
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // SECTION 2: 500 PETS QUICK DUEL
    PixelContainer(
      modifier = Modifier.fillMaxWidth(),
      backgroundColor = Color(0xFF1E1B4B),
      borderColor = Color(0xFFA855F7)
    ) {
      Column {
        Text(
          text = "500 PETS DUEL ARENA",
          color = Color(0xFFE0E7FF),
          fontSize = 14.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace
        )
        Text(
          text = "Challenge any of the 500 species directly or test your team against a random opponent!",
          color = Color(0xFFC7D2FE),
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          modifier = Modifier.padding(vertical = 4.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = onStartRandomCustomDuel,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7)),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("random_duel_button")
          ) {
            Text("⚔ RANDOM DUEL", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
          }
          Button(
            onClick = onOpenPetDex,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4338CA)),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text("BROWSE 500", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // SECTION 3: GYM LEADER DUELS
    Text(
      text = "GYM DUELIST CHAMPIONSHIP",
      color = Color(0xFFFBBF24),
      fontSize = 14.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace,
      letterSpacing = 1.sp
    )
    Text(
      text = "Defeat region masters to win badges and big prize bounties!",
      color = Color(0xFF94A3B8),
      fontSize = 11.sp,
      fontFamily = FontFamily.Monospace,
      modifier = Modifier.padding(bottom = 8.dp)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      gymLeaders.forEach { leader ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
            .border(1.dp, leader.specialtyType.color, RoundedCornerShape(6.dp))
            .clickable { onStartGymDuel(leader) }
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = leader.name,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              )
              Spacer(modifier = Modifier.width(6.dp))
              PixelTypeBadge(type = leader.specialtyType, fontSize = 8)
            }
            Text(
              text = "${leader.title} • ${leader.badgeName}",
              color = Color(0xFFFBBF24),
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "\"${leader.dialog}\"",
              color = Color(0xFF94A3B8),
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              maxLines = 1,
              modifier = Modifier.padding(top = 2.dp)
            )
          }

          Button(
            onClick = { onStartGymDuel(leader) },
            colors = ButtonDefaults.buttonColors(containerColor = leader.specialtyType.badgeColor),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(start = 8.dp)
          ) {
            Text("DUEL", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun BiomeCard(
  title: String,
  subtitle: String,
  borderColor: Color,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
      .border(1.dp, borderColor, RoundedCornerShape(6.dp))
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = title,
        color = Color.White,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
      )
      Text(
        text = subtitle,
        color = Color(0xFF94A3B8),
        fontSize = 10.sp,
        fontFamily = FontFamily.Monospace
      )
    }

    Text(
      text = "ENTER ⚔",
      color = borderColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace
    )
  }
}
