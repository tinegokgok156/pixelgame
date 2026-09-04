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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BattlePet
import com.example.model.Item
import com.example.model.ItemCategory
import com.example.model.StandardItems
import com.example.ui.pixel.PixelContainer
import com.example.ui.pixel.PixelExpBar
import com.example.ui.pixel.PixelHpBar
import com.example.ui.pixel.PixelPetSprite
import com.example.ui.pixel.PixelTypeBadge

@Composable
fun PartyScreen(
  party: List<BattlePet>,
  inventory: Map<String, Int>,
  onHealAll: () -> Unit,
  onUseItemOnPet: (Item, BattlePet) -> Unit
) {
  var selectedPet by remember { mutableStateOf<BattlePet?>(null) }
  var showItemDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF090D16))
      .padding(12.dp)
  ) {
    // Header & Heal Station
    PixelContainer(
      modifier = Modifier.fillMaxWidth(),
      backgroundColor = Color(0xFF1E293B),
      borderColor = Color(0xFF10B981)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "ACTIVE PARTY",
            color = Color(0xFFFBBF24),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "${party.size} / 6 PETS IN TEAM",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Button(
          onClick = { onHealAll() },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.testTag("heal_party_button")
        ) {
          Text(
            text = "❤ HEAL ALL",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Party List
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      itemsIndexed(party) { index, pet ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
            .border(
              1.dp,
              if (index == 0) Color(0xFFF59E0B) else Color(0xFF334155),
              RoundedCornerShape(6.dp)
            )
            .clickable { selectedPet = pet }
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Sprite
          Box(contentAlignment = Alignment.TopStart) {
            PixelPetSprite(species = pet.species, size = 64.dp)
            if (index == 0) {
              Box(
                modifier = Modifier
                  .background(Color(0xFFF59E0B), RoundedCornerShape(2.dp))
                  .padding(horizontal = 4.dp, vertical = 1.dp)
              ) {
                Text(
                  text = "LEAD",
                  color = Color(0xFF0F172A),
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Black,
                  fontFamily = FontFamily.Monospace
                )
              }
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          // Info
          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = pet.nickname,
                color = if (pet.isFainted) Color(0xFFEF4444) else Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = "Lv.${pet.level}",
                color = Color(0xFFFBBF24),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              PixelTypeBadge(type = pet.species.primaryType, fontSize = 8)
              pet.species.secondaryType?.let {
                PixelTypeBadge(type = it, fontSize = 8)
              }
            }

            Spacer(modifier = Modifier.height(6.dp))
            PixelHpBar(
              currentHp = pet.currentHp,
              maxHp = pet.maxHp,
              height = 6.dp
            )

            Spacer(modifier = Modifier.height(4.dp))
            PixelExpBar(
              currentExp = pet.currentExp,
              maxExp = pet.expForNextLevel
            )
          }
        }
      }
    }
  }

  // PET DETAIL / USE ITEM DIALOG
  selectedPet?.let { pet ->
    Dialog(onDismissRequest = {
      selectedPet = null
      showItemDialog = false
    }) {
      Surface(
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, pet.species.primaryType.color),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${pet.species.formattedId} ${pet.nickname} (Lv.${pet.level})",
              color = Color(0xFFFBBF24),
              fontSize = 15.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "✕",
              color = Color(0xFF94A3B8),
              fontSize = 16.sp,
              modifier = Modifier.clickable { selectedPet = null }
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          PixelPetSprite(species = pet.species, size = 100.dp)

          Spacer(modifier = Modifier.height(6.dp))
          PixelHpBar(currentHp = pet.currentHp, maxHp = pet.maxHp, height = 8.dp)

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "MOVESET:",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )

          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            pet.moves.forEach { move ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color(0xFF1E293B), RoundedCornerShape(4.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = move.name,
                  color = Color.White,
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  PixelTypeBadge(type = move.type, fontSize = 8)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "${move.currentPp}/${move.maxPp}",
                    color = Color(0xFFFBBF24),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Bag Items for Healing/Candy
          Text(
            text = "USE BAG ITEM:",
            color = Color(0xFFFBBF24),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )

          val usableItems = StandardItems.ALL_SHOP_ITEMS.filter {
            it.category in listOf(ItemCategory.HEALING, ItemCategory.REVIVE, ItemCategory.ENHANCER) &&
                (inventory[it.id] ?: 0) > 0
          }

          if (usableItems.isEmpty()) {
            Text(
              text = "No healing potions or candies in bag.",
              color = Color(0xFF64748B),
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(vertical = 6.dp)
            )
          } else {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              usableItems.forEach { item ->
                val count = inventory[item.id] ?: 0
                Button(
                  onClick = {
                    onUseItemOnPet(item, pet)
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                  shape = RoundedCornerShape(4.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Text(
                    text = "${item.name}\n(x$count)",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFF1F5F9)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
