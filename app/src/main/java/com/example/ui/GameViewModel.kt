package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GamePreferences
import com.example.data.GymLeader
import com.example.data.MoveRepository
import com.example.data.PetRepository
import com.example.model.AttackAnimation
import com.example.model.BattleMode
import com.example.model.BattlePet
import com.example.model.CatchAttempt
import com.example.model.DuelPhase
import com.example.model.DuelState
import com.example.model.Item
import com.example.model.ItemCategory
import com.example.model.PetMove
import com.example.model.PetSpecies
import com.example.model.StandardItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

enum class GameScreen {
  STARTER_SELECT,
  DUEL_ARENA,
  PET_DEX,
  PARTY_MANAGEMENT,
  DUEL_HUB,
  POKE_SHOP
}

data class GameUiState(
  val currentScreen: GameScreen = GameScreen.DUEL_HUB,
  val party: List<BattlePet> = emptyList(),
  val pcBox: List<BattlePet> = emptyList(),
  val coins: Int = 800,
  val duelsWon: Int = 0,
  val inventory: Map<String, Int> = emptyMap(),
  val seenPetIds: Set<Int> = emptySet(),
  val caughtPetIds: Set<Int> = emptySet(),
  val activeDuel: DuelState? = null,
  val notificationMessage: String? = null,
  val isFirstLaunch: Boolean = false
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
  private val prefs = GamePreferences(application.applicationContext)

  private val _uiState = MutableStateFlow(GameUiState())
  val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

  init {
    loadInitialData()
  }

  private fun loadInitialData() {
    val isFirst = prefs.isFirstLaunch()
    val savedParty = prefs.loadParty()
    val coins = prefs.getCoins()
    val inventory = prefs.getInventory()
    val seen = prefs.getSeenPetIds()
    val caught = prefs.getCaughtPetIds()
    val duelsWon = prefs.getDuelsWon()

    if (isFirst || savedParty.isEmpty()) {
      _uiState.update {
        it.copy(
          currentScreen = GameScreen.STARTER_SELECT,
          isFirstLaunch = true,
          coins = coins,
          inventory = inventory,
          seenPetIds = seen,
          caughtPetIds = caught,
          duelsWon = duelsWon
        )
      }
    } else {
      _uiState.update {
        it.copy(
          currentScreen = GameScreen.DUEL_HUB,
          isFirstLaunch = false,
          party = savedParty,
          coins = coins,
          inventory = inventory,
          seenPetIds = seen,
          caughtPetIds = caught,
          duelsWon = duelsWon
        )
      }
    }
  }

  fun pickStarter(species: PetSpecies) {
    val starter = BattlePet.createWild(species, 5)
    starter.nickname = species.name
    val party = listOf(starter)
    prefs.saveParty(party)
    prefs.markPetCaught(species.id)
    prefs.setFirstLaunchCompleted()

    _uiState.update {
      it.copy(
        currentScreen = GameScreen.DUEL_HUB,
        party = party,
        isFirstLaunch = false,
        caughtPetIds = prefs.getCaughtPetIds(),
        seenPetIds = prefs.getSeenPetIds(),
        notificationMessage = "You chose ${species.name}! Let your journey begin!"
      )
    }
  }

  fun navigateTo(screen: GameScreen) {
    _uiState.update { it.copy(currentScreen = screen) }
  }

  fun clearNotification() {
    _uiState.update { it.copy(notificationMessage = null) }
  }

  // DUEL CREATION
  fun startWildDuel(biome: String = "Forest") {
    val party = _uiState.value.party
    val alivePetIndex = party.indexOfFirst { !it.isFainted }.let { if (it >= 0) it else 0 }
    val avgLevel = if (party.isNotEmpty()) party.map { it.level }.average().toInt().coerceIn(3, 85) else 5
    val wildPet = PetRepository.createWildEncounter(biome, avgLevel)

    prefs.markPetSeen(wildPet.species.id)

    val duel = DuelState(
      battleMode = BattleMode.WILD_ENCOUNTER,
      opponentName = "Wild ${wildPet.species.name}",
      playerParty = party,
      opponentParty = listOf(wildPet),
      activePlayerPetIndex = alivePetIndex,
      activeOpponentPetIndex = 0,
      phase = DuelPhase.INTRO,
      battleLog = listOf("A wild ${wildPet.species.name} (Lv.${wildPet.level}) appeared!")
    )

    _uiState.update {
      it.copy(
        activeDuel = duel,
        currentScreen = GameScreen.DUEL_ARENA,
        seenPetIds = prefs.getSeenPetIds()
      )
    }

    viewModelScope.launch {
      delay(1200)
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              phase = DuelPhase.PLAYER_INPUT,
              battleLog = d.battleLog + "What will ${d.activePlayerPet?.nickname} do?"
            )
          )
        } ?: current
      }
    }
  }

  fun startGymDuel(leader: GymLeader) {
    val party = _uiState.value.party
    val alivePetIndex = party.indexOfFirst { !it.isFainted }.let { if (it >= 0) it else 0 }

    val opponentParty = leader.teamSpeciesIds.map { (speciesId, level) ->
      val species = PetRepository.getPetById(speciesId)
      prefs.markPetSeen(species.id)
      BattlePet.createWild(species, level)
    }

    val duel = DuelState(
      battleMode = BattleMode.GYM_CHALLENGE,
      opponentName = leader.name,
      playerParty = party,
      opponentParty = opponentParty,
      activePlayerPetIndex = alivePetIndex,
      activeOpponentPetIndex = 0,
      phase = DuelPhase.INTRO,
      battleLog = listOf(
        "${leader.name}: \"${leader.dialog}\"",
        "${leader.name} sent out ${opponentParty.first().species.name}!"
      )
    )

    _uiState.update {
      it.copy(
        activeDuel = duel,
        currentScreen = GameScreen.DUEL_ARENA,
        seenPetIds = prefs.getSeenPetIds()
      )
    }

    viewModelScope.launch {
      delay(1500)
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              phase = DuelPhase.PLAYER_INPUT,
              battleLog = d.battleLog + "Go, ${d.activePlayerPet?.nickname}!"
            )
          )
        } ?: current
      }
    }
  }

  fun startCustomDuel(speciesId: Int, level: Int = 10) {
    val party = _uiState.value.party
    val alivePetIndex = party.indexOfFirst { !it.isFainted }.let { if (it >= 0) it else 0 }
    val species = PetRepository.getPetById(speciesId)
    prefs.markPetSeen(species.id)
    val pet = BattlePet.createWild(species, level)

    val duel = DuelState(
      battleMode = BattleMode.CUSTOM_DUEL,
      opponentName = "Trainer Duelist",
      playerParty = party,
      opponentParty = listOf(pet),
      activePlayerPetIndex = alivePetIndex,
      activeOpponentPetIndex = 0,
      phase = DuelPhase.INTRO,
      battleLog = listOf("Duelist sent out ${species.name} (Lv.$level)!")
    )

    _uiState.update {
      it.copy(
        activeDuel = duel,
        currentScreen = GameScreen.DUEL_ARENA,
        seenPetIds = prefs.getSeenPetIds()
      )
    }

    viewModelScope.launch {
      delay(1200)
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              phase = DuelPhase.PLAYER_INPUT,
              battleLog = d.battleLog + "Duel started! Select your move!"
            )
          )
        } ?: current
      }
    }
  }

  // COMBAT ACTIONS
  fun executePlayerMove(move: PetMove) {
    val duel = _uiState.value.activeDuel ?: return
    val playerPet = duel.activePlayerPet ?: return
    val opponentPet = duel.activeOpponentPet ?: return
    if (playerPet.isFainted || opponentPet.isFainted) return

    viewModelScope.launch {
      // 1. Player attacks
      val isHit = (1..100).random() <= move.accuracy
      if (!isHit) {
        _uiState.update { current ->
          current.activeDuel?.let { d ->
            current.copy(
              activeDuel = d.copy(
                phase = DuelPhase.ANIMATING_PLAYER_ATTACK,
                battleLog = d.battleLog + "${playerPet.nickname} used ${move.name}, but it missed!"
              )
            )
          } ?: current
        }
        delay(1200)
      } else {
        val multiplier = move.type.combinedMultiplier(
          opponentPet.species.primaryType,
          opponentPet.species.secondaryType
        )
        val isCrit = Random.nextFloat() < 0.08f
        val critMult = if (isCrit) 1.5f else 1.0f

        val baseDamage = ((2.0 * playerPet.level / 5.0 + 2.0) * move.power * (playerPet.attack.toDouble() / max(1, opponentPet.defense).toDouble()) / 50.0 + 2.0)
        val finalDamage = (baseDamage * multiplier * critMult * (0.85 + Random.nextFloat() * 0.15)).roundToInt().coerceAtLeast(1)

        val effectivenessText = when {
          multiplier >= 2.0f -> "It's super effective!"
          multiplier <= 0.5f && multiplier > 0.0f -> "It's not very effective..."
          multiplier == 0.0f -> "It had no effect!"
          else -> ""
        }
        val critText = if (isCrit) "A critical hit!" else ""

        val attackAnim = AttackAnimation(
          attackerIsPlayer = true,
          move = move,
          damage = finalDamage,
          isCritical = isCrit,
          effectivenessMultiplier = multiplier
        )

        opponentPet.takeDamage(finalDamage)

        val logLines = mutableListOf("${playerPet.nickname} used ${move.name}!")
        if (critText.isNotEmpty()) logLines.add(critText)
        if (effectivenessText.isNotEmpty()) logLines.add(effectivenessText)

        _uiState.update { current ->
          current.activeDuel?.let { d ->
            current.copy(
              activeDuel = d.copy(
                phase = DuelPhase.ANIMATING_PLAYER_ATTACK,
                currentAnimation = attackAnim,
                battleLog = d.battleLog + logLines
              )
            )
          } ?: current
        }

        delay(1500)
      }

      // Check if opponent fainted
      if (opponentPet.isFainted) {
        handleOpponentFainted()
        return@launch
      }

      // 2. Opponent attacks back
      executeOpponentTurn()
    }
  }

  private suspend fun executeOpponentTurn() {
    val duel = _uiState.value.activeDuel ?: return
    val playerPet = duel.activePlayerPet ?: return
    val opponentPet = duel.activeOpponentPet ?: return
    if (opponentPet.isFainted) return

    val moves = opponentPet.moves.filter { it.power > 0 }
    val chosenMove = if (moves.isNotEmpty()) moves.random() else MoveRepository.TACKLE

    _uiState.update { current ->
      current.activeDuel?.let { d ->
        current.copy(
          activeDuel = d.copy(
            phase = DuelPhase.ANIMATING_OPPONENT_ATTACK,
            currentAnimation = null
          )
        )
      } ?: current
    }

    delay(600)

    val multiplier = chosenMove.type.combinedMultiplier(
      playerPet.species.primaryType,
      playerPet.species.secondaryType
    )
    val isCrit = Random.nextFloat() < 0.08f
    val critMult = if (isCrit) 1.5f else 1.0f

    val baseDamage = ((2.0 * opponentPet.level / 5.0 + 2.0) * chosenMove.power * (opponentPet.attack.toDouble() / max(1, playerPet.defense).toDouble()) / 50.0 + 2.0)
    val finalDamage = (baseDamage * multiplier * critMult * (0.85 + Random.nextFloat() * 0.15)).roundToInt().coerceAtLeast(1)

    playerPet.takeDamage(finalDamage)

    val attackAnim = AttackAnimation(
      attackerIsPlayer = false,
      move = chosenMove,
      damage = finalDamage,
      isCritical = isCrit,
      effectivenessMultiplier = multiplier
    )

    val logLines = mutableListOf("${opponentPet.species.name} used ${chosenMove.name}!")
    if (isCrit) logLines.add("A critical hit on your pet!")
    if (multiplier >= 2.0f) logLines.add("It's super effective!")

    _uiState.update { current ->
      current.activeDuel?.let { d ->
        current.copy(
          activeDuel = d.copy(
            currentAnimation = attackAnim,
            battleLog = d.battleLog + logLines
          )
        )
      } ?: current
    }

    delay(1500)

    // Check if player's pet fainted
    if (playerPet.isFainted) {
      handlePlayerPetFainted()
    } else {
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              phase = DuelPhase.PLAYER_INPUT,
              currentAnimation = null,
              battleLog = d.battleLog + "What will ${playerPet.nickname} do next?"
            )
          )
        } ?: current
      }
    }
  }

  private suspend fun handleOpponentFainted() {
    val duel = _uiState.value.activeDuel ?: return
    val opponentPet = duel.activeOpponentPet ?: return
    val playerPet = duel.activePlayerPet ?: return

    val expReward = (opponentPet.level * opponentPet.species.totalBaseStats / 6).coerceAtLeast(35)
    val coinsReward = opponentPet.level * 30 + 50

    val leveledUp = playerPet.gainExp(expReward)
    prefs.saveParty(duel.playerParty)

    val log = mutableListOf(
      "${opponentPet.species.name} fainted!",
      "${playerPet.nickname} gained $expReward EXP points!"
    )
    if (leveledUp) {
      log.add("★ ${playerPet.nickname} grew to Level ${playerPet.level}! Stats increased!")
    }

    // Check if opponent has remaining pets
    val nextOpponentIndex = duel.opponentParty.indexOfFirst { !it.isFainted }
    if (nextOpponentIndex >= 0) {
      val nextPet = duel.opponentParty[nextOpponentIndex]
      prefs.markPetSeen(nextPet.species.id)
      log.add("${duel.opponentName} sent out ${nextPet.species.name}!")

      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              activeOpponentPetIndex = nextOpponentIndex,
              currentAnimation = null,
              battleLog = d.battleLog + log,
              phase = DuelPhase.PLAYER_INPUT
            )
          )
        } ?: current
      }
    } else {
      // Victory!
      prefs.incrementDuelsWon()
      val newCoins = prefs.getCoins() + coinsReward
      prefs.setCoins(newCoins)
      log.add("You won the duel! Earned $$coinsReward prize money!")

      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              phase = DuelPhase.VICTORY,
              currentAnimation = null,
              battleLog = d.battleLog + log,
              expGained = expReward,
              coinsGained = coinsReward,
              levelUpPet = if (leveledUp) playerPet else null
            ),
            coins = newCoins,
            duelsWon = prefs.getDuelsWon()
          )
        } ?: current
      }
    }
  }

  private fun handlePlayerPetFainted() {
    val duel = _uiState.value.activeDuel ?: return
    val faintedPet = duel.activePlayerPet ?: return
    prefs.saveParty(duel.playerParty)

    val nextAliveIndex = duel.playerParty.indexOfFirst { !it.isFainted }
    if (nextAliveIndex >= 0) {
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              phase = DuelPhase.SELECT_SWITCH_PET,
              currentAnimation = null,
              battleLog = d.battleLog + "${faintedPet.nickname} fainted! Choose another pet!"
            )
          )
        } ?: current
      }
    } else {
      // All party fainted: Defeat
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              phase = DuelPhase.DEFEAT,
              currentAnimation = null,
              battleLog = d.battleLog + "All of your pets fainted... You blacked out!"
            )
          )
        } ?: current
      }
    }
  }

  // CATCHING MECHANIC
  fun catchWildPet(orbItem: Item) {
    val duel = _uiState.value.activeDuel ?: return
    if (duel.battleMode != BattleMode.WILD_ENCOUNTER) {
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(battleLog = d.battleLog + "You cannot catch a trainer's pet!")
          )
        } ?: current
      }
      return
    }
    val wildPet = duel.activeOpponentPet ?: return

    val inv = _uiState.value.inventory.toMutableMap()
    val count = inv[orbItem.id] ?: 0
    if (count <= 0) {
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(activeDuel = d.copy(battleLog = d.battleLog + "You have no ${orbItem.name} left!"))
        } ?: current
      }
      return
    }

    inv[orbItem.id] = count - 1
    prefs.saveInventory(inv)

    val hpFactor = (3.0 * wildPet.maxHp - 2.0 * wildPet.currentHp) / (3.0 * wildPet.maxHp)
    val catchChance = (hpFactor * wildPet.species.catchRate * orbItem.catchBonus).coerceIn(0.15, 1.0)
    val isSuccess = Random.nextDouble() < catchChance

    val wobbles = if (isSuccess) 3 else (0..2).random()
    val attempt = CatchAttempt(orbItem, wobbles, isSuccess)

    _uiState.update { current ->
      current.copy(
        inventory = inv,
        activeDuel = current.activeDuel?.copy(
          phase = DuelPhase.ANIMATING_CAPTURE,
          currentCatchAttempt = attempt,
          battleLog = current.activeDuel.battleLog + "You threw a ${orbItem.name}!"
        )
      )
    }

    viewModelScope.launch {
      delay(1800)
      if (isSuccess) {
        prefs.markPetCaught(wildPet.species.id)
        val currentParty = _uiState.value.party.toMutableList()
        val pcList = _uiState.value.pcBox.toMutableList()

        val capturedPet = wildPet.copy()
        if (currentParty.size < 6) {
          currentParty.add(capturedPet)
          prefs.saveParty(currentParty)
        } else {
          pcList.add(capturedPet)
        }

        _uiState.update { current ->
          current.copy(
            party = currentParty,
            pcBox = pcList,
            caughtPetIds = prefs.getCaughtPetIds(),
            activeDuel = current.activeDuel?.copy(
              phase = DuelPhase.VICTORY,
              battleLog = current.activeDuel.battleLog + listOf(
                "Gotcha! ${wildPet.species.name} was caught!",
                "${wildPet.species.name}'s data was added to the PetDex!"
              )
            )
          )
        }
      } else {
        _uiState.update { current ->
          current.activeDuel?.let { d ->
            current.copy(
              activeDuel = d.copy(
                battleLog = d.battleLog + "Oh no! The wild ${wildPet.species.name} broke free!"
              )
            )
          } ?: current
        }
        delay(1000)
        executeOpponentTurn()
      }
    }
  }

  fun switchActivePet(index: Int) {
    val duel = _uiState.value.activeDuel ?: return
    if (index !in duel.playerParty.indices) return
    val newPet = duel.playerParty[index]
    if (newPet.isFainted) return

    val oldIndex = duel.activePlayerPetIndex
    if (oldIndex == index && duel.phase != DuelPhase.SELECT_SWITCH_PET) return

    viewModelScope.launch {
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              activePlayerPetIndex = index,
              phase = DuelPhase.ANIMATING_PLAYER_ATTACK,
              battleLog = d.battleLog + "Go! ${newPet.nickname}!"
            )
          )
        } ?: current
      }
      delay(1000)

      // Switching costs a turn unless it was because previous fainted
      val wasForcedSwitch = duel.playerParty.getOrNull(oldIndex)?.isFainted == true
      if (wasForcedSwitch) {
        _uiState.update { current ->
          current.activeDuel?.let { d ->
            current.copy(
              activeDuel = d.copy(
                phase = DuelPhase.PLAYER_INPUT,
                battleLog = d.battleLog + "What will ${newPet.nickname} do?"
              )
            )
          } ?: current
        }
      } else {
        executeOpponentTurn()
      }
    }
  }

  fun useItemInBattle(item: Item, targetPet: BattlePet) {
    val duel = _uiState.value.activeDuel ?: return
    val inv = _uiState.value.inventory.toMutableMap()
    val count = inv[item.id] ?: 0
    if (count <= 0) return

    when (item.category) {
      ItemCategory.HEALING -> {
        if (targetPet.isFainted) return
        targetPet.heal(item.healAmount)
      }
      ItemCategory.REVIVE -> {
        if (!targetPet.isFainted) return
        targetPet.currentHp = targetPet.maxHp / 2
      }
      ItemCategory.ENHANCER -> {
        targetPet.gainExp(targetPet.expForNextLevel)
      }
      ItemCategory.ORB -> return
    }

    inv[item.id] = count - 1
    prefs.saveInventory(inv)
    prefs.saveParty(duel.playerParty)

    viewModelScope.launch {
      _uiState.update { current ->
        current.copy(
          inventory = inv,
          activeDuel = current.activeDuel?.copy(
            phase = DuelPhase.ANIMATING_PLAYER_ATTACK,
            battleLog = current.activeDuel.battleLog + "Used ${item.name} on ${targetPet.nickname}!"
          )
        )
      }
      delay(1000)
      executeOpponentTurn()
    }
  }

  fun runAway() {
    val duel = _uiState.value.activeDuel ?: return
    if (duel.battleMode != BattleMode.WILD_ENCOUNTER) {
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(activeDuel = d.copy(battleLog = d.battleLog + "You cannot run from a duel!"))
        } ?: current
      }
      return
    }

    viewModelScope.launch {
      _uiState.update { current ->
        current.activeDuel?.let { d ->
          current.copy(
            activeDuel = d.copy(
              phase = DuelPhase.RUN_AWAY,
              battleLog = d.battleLog + "Got away safely!"
            )
          )
        } ?: current
      }
      delay(1000)
      exitDuel()
    }
  }

  fun exitDuel() {
    _uiState.update {
      it.copy(
        activeDuel = null,
        currentScreen = GameScreen.DUEL_HUB
      )
    }
  }

  fun healEntireParty() {
    val party = _uiState.value.party
    party.forEach { it.fullHeal() }
    prefs.saveParty(party)
    _uiState.update {
      it.copy(
        party = party,
        notificationMessage = "Your entire party has been fully restored!"
      )
    }
  }

  fun buyShopItem(item: Item, quantity: Int = 1) {
    val cost = item.price * quantity
    if (_uiState.value.coins < cost) {
      _uiState.update { it.copy(notificationMessage = "Not enough coins!") }
      return
    }

    val inv = _uiState.value.inventory.toMutableMap()
    inv[item.id] = (inv[item.id] ?: 0) + quantity
    val newCoins = _uiState.value.coins - cost

    prefs.saveInventory(inv)
    prefs.setCoins(newCoins)

    _uiState.update {
      it.copy(
        coins = newCoins,
        inventory = inv,
        notificationMessage = "Purchased $quantity x ${item.name}!"
      )
    }
  }

  fun useItemOutOfBattle(item: Item, pet: BattlePet) {
    val inv = _uiState.value.inventory.toMutableMap()
    val count = inv[item.id] ?: 0
    if (count <= 0) return

    when (item.category) {
      ItemCategory.HEALING -> {
        if (pet.isFainted || pet.currentHp >= pet.maxHp) return
        pet.heal(item.healAmount)
      }
      ItemCategory.REVIVE -> {
        if (!pet.isFainted) return
        pet.currentHp = pet.maxHp / 2
      }
      ItemCategory.ENHANCER -> {
        pet.gainExp(pet.expForNextLevel)
      }
      ItemCategory.ORB -> return
    }

    inv[item.id] = count - 1
    prefs.saveInventory(inv)
    prefs.saveParty(_uiState.value.party)

    _uiState.update {
      it.copy(
        inventory = inv,
        notificationMessage = "Used ${item.name} on ${pet.nickname}!"
      )
    }
  }

  fun setPhase(phase: DuelPhase) {
    _uiState.update { current ->
      current.activeDuel?.let { d ->
        current.copy(activeDuel = d.copy(phase = phase))
      } ?: current
    }
  }
}
