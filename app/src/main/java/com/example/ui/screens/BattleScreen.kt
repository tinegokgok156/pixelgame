package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BattleMode
import com.example.model.BattlePet
import com.example.model.DuelPhase
import com.example.model.DuelState
import com.example.model.Item
import com.example.model.ItemCategory
import com.example.model.PetMove
import com.example.model.StandardItems
import com.example.ui.pixel.PixelBattlePlatform
import com.example.ui.pixel.PixelContainer
import com.example.ui.pixel.PixelDialogueBox
import com.example.ui.pixel.PixelExpBar
import com.example.ui.pixel.PixelHpBar
import com.example.ui.pixel.PixelPetSprite
import com.example.ui.pixel.PixelTypeBadge
import com.example.ui.pixel.SpriteFacing

@Composable
fun BattleScreen(
  duelState: DuelState,
  inventory: Map<String, Int>,
  onMoveSelected: (PetMove) -> Unit,
  onCatchPet: (Item) -> Unit,
  onSwitchPet: (Int) -> Unit,
  onUseItem: (Item, BattlePet) -> Unit,
  onRunAway: () -> Unit,
  onExitDuel: () -> Unit,
  onSetPhase: (DuelPhase) -> Unit
) {
  val playerPet = duelState.activePlayerPet
  val opponentPet = duelState.activeOpponentPet
  val latestLog = duelState.battleLog.lastOrNull() ?: "Battle in progress..."

  val isPlayerAttacking = duelState.phase == DuelPhase.ANIMATING_PLAYER_ATTACK &&
      duelState.currentAnimation?.attackerIsPlayer == true
  val isOpponentAttacking = duelState.phase == DuelPhase.ANIMATING_OPPONENT_ATTACK &&
      duelState.currentAnimation?.attackerIsPlayer == false

  val isPlayerHit = isOpponentAttacking
  val isOpponentHit = isPlayerAttacking

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF0F172A))
      .padding(8.dp)
  ) {
    // 1. OPPONENT STATUS PANEL (Top-Left)
    if (opponentPet != null) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        PixelContainer(
          modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp),
          backgroundColor = Color(0xFF1E293B),
          borderColor = Color(0xFF475569)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = opponentPet.species.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = ":L${opponentPet.level}",
                color = Color(0xFFFBBF24),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            PixelTypeBadge(type = opponentPet.species.primaryType, fontSize = 9)
            Spacer(modifier = Modifier.height(6.dp))
            PixelHpBar(
              currentHp = opponentPet.currentHp,
              maxHp = opponentPet.maxHp,
              showNumbers = true,
              height = 8.dp
            )
          }
        }

        // OPPONENT SPRITE (Top-Right)
        Box(
          modifier = Modifier
            .size(130.dp),
          contentAlignment = Alignment.Center
        ) {
          PixelBattlePlatform(
            modifier = Modifier
              .width(120.dp)
              .align(Alignment.BottomCenter)
              .padding(bottom = 6.dp),
            isOpponent = true,
            tintColor = opponentPet.species.primaryType.color
          )
          PixelPetSprite(
            species = opponentPet.species,
            size = 110.dp,
            facing = SpriteFacing.FRONT,
            isAttacking = isOpponentAttacking,
            isHit = isOpponentHit,
            isFainted = opponentPet.isFainted
          )

          // Catch animation overlay
          if (duelState.phase == DuelPhase.ANIMATING_CAPTURE) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFE11D48), CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .align(Alignment.Center)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.weight(1f))

    // 2. PLAYER SPRITE & STATUS (Bottom)
    if (playerPet != null) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        // PLAYER SPRITE (Bottom-Left - Back View)
        Box(
          modifier = Modifier
            .size(140.dp),
          contentAlignment = Alignment.Center
        ) {
          PixelBattlePlatform(
            modifier = Modifier
              .width(130.dp)
              .align(Alignment.BottomCenter)
              .padding(bottom = 4.dp),
            isOpponent = false,
            tintColor = playerPet.species.primaryType.color
          )
          PixelPetSprite(
            species = playerPet.species,
            size = 120.dp,
            facing = SpriteFacing.BACK,
            isAttacking = isPlayerAttacking,
            isHit = isPlayerHit,
            isFainted = playerPet.isFainted
          )
        }

        // PLAYER STATUS PANEL (Bottom-Right)
        PixelContainer(
          modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp),
          backgroundColor = Color(0xFF1E293B),
          borderColor = Color(0xFF475569)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = playerPet.nickname,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = ":L${playerPet.level}",
                color = Color(0xFFFBBF24),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            PixelTypeBadge(type = playerPet.species.primaryType, fontSize = 9)
            Spacer(modifier = Modifier.height(6.dp))
            PixelHpBar(
              currentHp = playerPet.currentHp,
              maxHp = playerPet.maxHp,
              showNumbers = true,
              height = 8.dp
            )
            Spacer(modifier = Modifier.height(4.dp))
            PixelExpBar(
              currentExp = playerPet.currentExp,
              maxExp = playerPet.expForNextLevel
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // 3. RETRO DIALOGUE TICKER
    PixelDialogueBox(
      text = latestLog,
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // 4. ACTION INTERFACE
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(148.dp)
        .background(Color(0xFF020617), RoundedCornerShape(6.dp))
        .border(2.dp, Color(0xFF334155), RoundedCornerShape(6.dp))
        .padding(6.dp)
    ) {
      when (duelState.phase) {
        DuelPhase.PLAYER_INPUT -> {
          // 4 Big Command Buttons: FIGHT, BAG, PETS, RUN
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              BattleCommandButton(
                text = "FIGHT",
                color = Color(0xFFE11D48),
                modifier = Modifier
                  .weight(1f)
                  .testTag("battle_fight_button"),
                onClick = { onSetPhase(DuelPhase.SELECT_MOVE) }
              )
              BattleCommandButton(
                text = "BAG",
                color = Color(0xFFF59E0B),
                modifier = Modifier
                  .weight(1f)
                  .testTag("battle_bag_button"),
                onClick = { onSetPhase(DuelPhase.SELECT_BAG_ITEM) }
              )
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              BattleCommandButton(
                text = "PETS",
                color = Color(0xFF10B981),
                modifier = Modifier
                  .weight(1f)
                  .testTag("battle_pets_button"),
                onClick = { onSetPhase(DuelPhase.SELECT_SWITCH_PET) }
              )
              BattleCommandButton(
                text = if (duelState.battleMode == BattleMode.WILD_ENCOUNTER) "RUN" else "FORFEIT",
                color = Color(0xFF3B82F6),
                modifier = Modifier
                  .weight(1f)
                  .testTag("battle_run_button"),
                onClick = { onRunAway() }
              )
            }
          }
        }

        DuelPhase.SELECT_MOVE -> {
          // Moves Selection (4 Moves)
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            val moves = playerPet?.moves ?: emptyList()
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              moves.take(2).forEach { move ->
                MoveButton(
                  move = move,
                  modifier = Modifier.weight(1f),
                  onClick = { onMoveSelected(move) }
                )
              }
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              moves.drop(2).take(2).forEach { move ->
                MoveButton(
                  move = move,
                  modifier = Modifier.weight(1f),
                  onClick = { onMoveSelected(move) }
                )
              }
            }
            Button(
              onClick = { onSetPhase(DuelPhase.PLAYER_INPUT) },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
              shape = RoundedCornerShape(4.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
            ) {
              Text("◄ BACK", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
          }
        }

        DuelPhase.SELECT_BAG_ITEM -> {
          // Bag / Catch Orb Selection
          Column(modifier = Modifier.fillMaxSize()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "SELECT AN ITEM",
                color = Color(0xFFFBBF24),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = "◄ BACK",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                  .clickable { onSetPhase(DuelPhase.PLAYER_INPUT) }
                  .padding(4.dp)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              val availableItems = StandardItems.ALL_SHOP_ITEMS.filter { (inventory[it.id] ?: 0) > 0 }
              if (availableItems.isEmpty()) {
                item {
                  Text(
                    text = "Your bag is empty! Buy items at the shop.",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(8.dp)
                  )
                }
              } else {
                items(availableItems) { item ->
                  val count = inventory[item.id] ?: 0
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .background(Color(0xFF1E293B), RoundedCornerShape(4.dp))
                      .border(1.dp, Color(0xFF475569), RoundedCornerShape(4.dp))
                      .clickable {
                        if (item.category == ItemCategory.ORB) {
                          onCatchPet(item)
                        } else if (playerPet != null) {
                          onUseItem(item, playerPet)
                        }
                      }
                      .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(
                        text = item.name,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                      )
                      Text(
                        text = if (item.category == ItemCategory.ORB) "Throw to catch" else item.description,
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                      )
                    }
                    Text(
                      text = "x$count",
                      color = Color(0xFFFBBF24),
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Black,
                      fontFamily = FontFamily.Monospace
                    )
                  }
                }
              }
            }
          }
        }

        DuelPhase.SELECT_SWITCH_PET -> {
          // Switch Pet Selection
          Column(modifier = Modifier.fillMaxSize()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "CHOOSE A PET TO SEND OUT",
                color = Color(0xFF10B981),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              if (playerPet?.isFainted == false) {
                Text(
                  text = "◄ BACK",
                  color = Color(0xFF94A3B8),
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace,
                  modifier = Modifier
                    .clickable { onSetPhase(DuelPhase.PLAYER_INPUT) }
                    .padding(4.dp)
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              items(duelState.playerParty.indices.toList()) { index ->
                val pet = duelState.playerParty[index]
                val isCurrent = index == duelState.activePlayerPetIndex
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .background(
                      if (pet.isFainted) Color(0xFF1E1B24) else Color(0xFF1E293B),
                      RoundedCornerShape(4.dp)
                    )
                    .border(
                      1.dp,
                      if (isCurrent) Color(0xFFFBBF24) else Color(0xFF475569),
                      RoundedCornerShape(4.dp)
                    )
                    .clickable(enabled = !pet.isFainted && !isCurrent) {
                      onSwitchPet(index)
                    }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    PixelPetSprite(species = pet.species, size = 32.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                      Text(
                        text = pet.nickname,
                        color = if (pet.isFainted) Color(0xFF64748B) else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                      )
                      Text(
                        text = if (pet.isFainted) "FAINTED" else "HP: ${pet.currentHp}/${pet.maxHp}",
                        color = if (pet.isFainted) Color(0xFFEF4444) else Color(0xFFCBD5E1),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                      )
                    }
                  }
                  Text(
                    text = "Lv.${pet.level}",
                    color = Color(0xFFFBBF24),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                  )
                }
              }
            }
          }
        }

        DuelPhase.VICTORY -> {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = "★ VICTORY! ★",
              color = Color(0xFFFBBF24),
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "+${duelState.expGained} EXP   +$$${duelState.coinsGained} COINS",
              color = Color(0xFF4ADE80),
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(vertical = 4.dp)
            )
            Button(
              onClick = { onExitDuel() },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("battle_victory_continue_button")
            ) {
              Text(
                text = "CONTINUE",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }

        DuelPhase.DEFEAT -> {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = "YOU BLACKED OUT...",
              color = Color(0xFFEF4444),
              fontSize = 16.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = { onExitDuel() },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("battle_defeat_continue_button")
            ) {
              Text(
                text = "RUSH TO PET CENTER",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }

        else -> {
          // Animating / waiting state
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "...",
              color = Color(0xFF94A3B8),
              fontSize = 24.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }
  }
}

@Composable
private fun BattleCommandButton(
  text: String,
  color: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Box(
    modifier = modifier
      .height(58.dp)
      .background(color, RoundedCornerShape(6.dp))
      .border(2.dp, Color(0xFF0F172A), RoundedCornerShape(6.dp))
      .clickable { onClick() }
      .padding(horizontal = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      color = Color.White,
      fontSize = 15.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace,
      letterSpacing = 1.sp
    )
  }
}

@Composable
private fun MoveButton(
  move: PetMove,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Box(
    modifier = modifier
      .height(52.dp)
      .background(Color(0xFF1E293B), RoundedCornerShape(4.dp))
      .border(2.dp, move.type.color, RoundedCornerShape(4.dp))
      .clickable { onClick() }
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = move.name,
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace
        )
        PixelTypeBadge(type = move.type, fontSize = 8)
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "PWR ${move.power}",
          color = Color(0xFF94A3B8),
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
        Text(
          text = "PP ${move.currentPp}/${move.maxPp}",
          color = Color(0xFFFBBF24),
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }
  }
}
