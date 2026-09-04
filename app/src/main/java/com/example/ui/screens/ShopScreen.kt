package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Item
import com.example.model.StandardItems
import com.example.ui.pixel.PixelContainer

@Composable
fun ShopScreen(
  coins: Int,
  inventory: Map<String, Int>,
  onBuyItem: (Item, Int) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF090D16))
      .padding(12.dp)
  ) {
    // Header
    PixelContainer(
      modifier = Modifier.fillMaxWidth(),
      backgroundColor = Color(0xFF1E293B),
      borderColor = Color(0xFFF59E0B)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "PET-MART",
            color = Color(0xFFFBBF24),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "SUPPLIES FOR PET DUELS",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Box(
          modifier = Modifier
            .background(Color(0xFF0F172A), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFFFACC15), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "$$coins COINS",
            color = Color(0xFFFACC15),
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(StandardItems.ALL_SHOP_ITEMS) { item ->
        val currentCount = inventory[item.id] ?: 0
        val canAfford = coins >= item.price

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(6.dp))
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = item.name,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = " (Owned: $currentCount)",
                color = Color(0xFF94A3B8),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
              )
            }
            Text(
              text = item.description,
              color = Color(0xFFCBD5E1),
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(top = 2.dp)
            )
            Text(
              text = "$${item.price}",
              color = Color(0xFFFACC15),
              fontSize = 12.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(top = 2.dp)
            )
          }

          Button(
            onClick = { onBuyItem(item, 1) },
            enabled = canAfford,
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFF59E0B),
              disabledContainerColor = Color(0xFF334155)
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
              .padding(start = 8.dp)
              .testTag("buy_${item.id}")
          ) {
            Text(
              text = "BUY",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = if (canAfford) Color(0xFF0F172A) else Color(0xFF64748B)
            )
          }
        }
      }
    }
  }
}
