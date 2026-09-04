package com.example.model

enum class DuelPhase {
  INTRO,
  PLAYER_INPUT,
  SELECT_MOVE,
  SELECT_BAG_ITEM,
  SELECT_SWITCH_PET,
  ANIMATING_PLAYER_ATTACK,
  ANIMATING_OPPONENT_ATTACK,
  ANIMATING_CAPTURE,
  PET_LEVEL_UP,
  VICTORY,
  DEFEAT,
  RUN_AWAY
}

enum class BattleMode {
  WILD_ENCOUNTER,
  TRAINER_DUEL,
  GYM_CHALLENGE,
  CUSTOM_DUEL
}

data class AttackAnimation(
  val attackerIsPlayer: Boolean,
  val move: PetMove,
  val damage: Int,
  val isCritical: Boolean,
  val effectivenessMultiplier: Float
)

data class CatchAttempt(
  val orbItem: Item,
  val wobbles: Int, // 0 to 3
  val isSuccess: Boolean
)

data class DuelState(
  val battleMode: BattleMode = BattleMode.WILD_ENCOUNTER,
  val opponentName: String = "Wild Pet",
  val playerParty: List<BattlePet> = emptyList(),
  val opponentParty: List<BattlePet> = emptyList(),
  val activePlayerPetIndex: Int = 0,
  val activeOpponentPetIndex: Int = 0,
  val phase: DuelPhase = DuelPhase.INTRO,
  val battleLog: List<String> = listOf("A wild battle has begun!"),
  val currentAnimation: AttackAnimation? = null,
  val currentCatchAttempt: CatchAttempt? = null,
  val expGained: Int = 0,
  val coinsGained: Int = 0,
  val levelUpPet: BattlePet? = null
) {
  val activePlayerPet: BattlePet?
    get() = playerParty.getOrNull(activePlayerPetIndex)

  val activeOpponentPet: BattlePet?
    get() = opponentParty.getOrNull(activeOpponentPetIndex)

  val hasAvailablePets: Boolean
    get() = playerParty.any { !it.isFainted }

  val opponentHasAvailablePets: Boolean
    get() = opponentParty.any { !it.isFainted }
}
