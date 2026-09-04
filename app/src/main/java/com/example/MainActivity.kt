package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.GameScreen
import com.example.ui.GameViewModel
import com.example.ui.screens.BattleScreen
import com.example.ui.screens.DuelHubScreen
import com.example.ui.screens.PartyScreen
import com.example.ui.screens.PetDexScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.StarterSelectScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: GameViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        PixelGameApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun PixelGameApp(viewModel: GameViewModel) {
  val uiState by viewModel.uiState.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.notificationMessage) {
    uiState.notificationMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearNotification()
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets.safeDrawing,
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      // Show bottom navigation bar only outside of battle and starter selection
      if (uiState.currentScreen != GameScreen.STARTER_SELECT &&
          uiState.currentScreen != GameScreen.DUEL_ARENA
      ) {
        PixelBottomNavigation(
          currentScreen = uiState.currentScreen,
          onNavigate = { screen -> viewModel.navigateTo(screen) }
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFF090D16))
    ) {
      when (uiState.currentScreen) {
        GameScreen.STARTER_SELECT -> {
          StarterSelectScreen(
            onStarterSelected = { species ->
              viewModel.pickStarter(species)
            }
          )
        }

        GameScreen.DUEL_ARENA -> {
          uiState.activeDuel?.let { duel ->
            BattleScreen(
              duelState = duel,
              inventory = uiState.inventory,
              onMoveSelected = { move -> viewModel.executePlayerMove(move) },
              onCatchPet = { orb -> viewModel.catchWildPet(orb) },
              onSwitchPet = { index -> viewModel.switchActivePet(index) },
              onUseItem = { item, pet -> viewModel.useItemInBattle(item, pet) },
              onRunAway = { viewModel.runAway() },
              onExitDuel = { viewModel.exitDuel() },
              onSetPhase = { phase -> viewModel.setPhase(phase) }
            )
          } ?: run {
            viewModel.navigateTo(GameScreen.DUEL_HUB)
          }
        }

        GameScreen.DUEL_HUB -> {
          DuelHubScreen(
            party = uiState.party,
            coins = uiState.coins,
            duelsWon = uiState.duelsWon,
            onStartWildDuel = { biome -> viewModel.startWildDuel(biome) },
            onStartGymDuel = { leader -> viewModel.startGymDuel(leader) },
            onStartRandomCustomDuel = {
              val randomId = (1..500).random()
              val avgLevel = if (uiState.party.isNotEmpty()) uiState.party.map { it.level }.average().toInt() else 10
              viewModel.startCustomDuel(randomId, avgLevel)
            },
            onOpenPetDex = { viewModel.navigateTo(GameScreen.PET_DEX) }
          )
        }

        GameScreen.PET_DEX -> {
          PetDexScreen(
            seenPetIds = uiState.seenPetIds,
            caughtPetIds = uiState.caughtPetIds,
            onStartDuelWithPet = { speciesId, level ->
              viewModel.startCustomDuel(speciesId, level)
            }
          )
        }

        GameScreen.PARTY_MANAGEMENT -> {
          PartyScreen(
            party = uiState.party,
            inventory = uiState.inventory,
            onHealAll = { viewModel.healEntireParty() },
            onUseItemOnPet = { item, pet ->
              viewModel.useItemOutOfBattle(item, pet)
            }
          )
        }

        GameScreen.POKE_SHOP -> {
          ShopScreen(
            coins = uiState.coins,
            inventory = uiState.inventory,
            onBuyItem = { item, qty -> viewModel.buyShopItem(item, qty) }
          )
        }
      }
    }
  }
}

@Composable
fun PixelBottomNavigation(
  currentScreen: GameScreen,
  onNavigate: (GameScreen) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFF0F172A))
      .border(
        width = 2.dp,
        color = Color(0xFF334155),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
      )
      .navigationBarsPadding()
      .padding(horizontal = 8.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically
  ) {
    NavTabItem(
      label = "DUEL",
      icon = Icons.Default.FitnessCenter,
      isSelected = currentScreen == GameScreen.DUEL_HUB,
      onClick = { onNavigate(GameScreen.DUEL_HUB) },
      modifier = Modifier.testTag("nav_duel_hub")
    )
    NavTabItem(
      label = "PETDEX",
      icon = Icons.Default.ListAlt,
      isSelected = currentScreen == GameScreen.PET_DEX,
      onClick = { onNavigate(GameScreen.PET_DEX) },
      modifier = Modifier.testTag("nav_petdex")
    )
    NavTabItem(
      label = "TEAM",
      icon = Icons.Default.Pets,
      isSelected = currentScreen == GameScreen.PARTY_MANAGEMENT,
      onClick = { onNavigate(GameScreen.PARTY_MANAGEMENT) },
      modifier = Modifier.testTag("nav_party")
    )
    NavTabItem(
      label = "MART",
      icon = Icons.Default.ShoppingBag,
      isSelected = currentScreen == GameScreen.POKE_SHOP,
      onClick = { onNavigate(GameScreen.POKE_SHOP) },
      modifier = Modifier.testTag("nav_shop")
    )
  }
}

@Composable
private fun NavTabItem(
  label: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val activeColor = Color(0xFFFBBF24)
  val inactiveColor = Color(0xFF64748B)

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 4.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = if (isSelected) activeColor else inactiveColor,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      color = if (isSelected) activeColor else inactiveColor,
      fontSize = 10.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace,
      letterSpacing = 0.5.sp
    )
  }
}
