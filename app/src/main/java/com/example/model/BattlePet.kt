package com.example.model

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

data class BattlePet(
  val uniqueId: String,
  val species: PetSpecies,
  var nickname: String = species.name,
  var level: Int = 5,
  var currentHp: Int = 1,
  var moves: List<PetMove> = species.baseMoves.map { it.copyWithMaxPp() },
  var currentExp: Int = 0,
  var isShiny: Boolean = false
) {
  val maxHp: Int
    get() = calculateStat(species.baseHp, level, isHp = true)

  val attack: Int
    get() = calculateStat(species.baseAttack, level)

  val defense: Int
    get() = calculateStat(species.baseDefense, level)

  val speed: Int
    get() = calculateStat(species.baseSpeed, level)

  val isFainted: Boolean
    get() = currentHp <= 0

  val hpPercentage: Float
    get() = if (maxHp > 0) (currentHp.toFloat() / maxHp.toFloat()).coerceIn(0f, 1f) else 0f

  val expForNextLevel: Int
    get() = level * level * 20

  val expProgress: Float
    get() = if (expForNextLevel > 0) (currentExp.toFloat() / expForNextLevel.toFloat()).coerceIn(0f, 1f) else 0f

  init {
    if (currentHp <= 0 || currentHp > maxHp) {
      currentHp = maxHp
    }
  }

  fun fullHeal() {
    currentHp = maxHp
    moves = moves.map { it.copyWithMaxPp() }
  }

  fun heal(amount: Int) {
    currentHp = min(maxHp, currentHp + amount)
  }

  fun takeDamage(damage: Int): Int {
    val actualDamage = min(currentHp, max(1, damage))
    currentHp = max(0, currentHp - actualDamage)
    return actualDamage
  }

  fun gainExp(expGained: Int): Boolean {
    currentExp += expGained
    var leveledUp = false
    while (currentExp >= expForNextLevel && level < 100) {
      val oldMax = maxHp
      currentExp -= expForNextLevel
      level++
      val diff = maxHp - oldMax
      currentHp += max(1, diff)
      leveledUp = true
    }
    return leveledUp
  }

  companion object {
    fun calculateStat(base: Int, level: Int, isHp: Boolean = false): Int {
      return if (isHp) {
        floor((2.0 * base * level) / 100.0).toInt() + level + 10
      } else {
        floor((2.0 * base * level) / 100.0).toInt() + 5
      }
    }

    fun createWild(species: PetSpecies, level: Int): BattlePet {
      val pet = BattlePet(
        uniqueId = "${species.id}_${System.currentTimeMillis()}_${(100..999).random()}",
        species = species,
        level = level,
        moves = species.baseMoves.map { it.copyWithMaxPp() }
      )
      pet.currentHp = pet.maxHp
      return pet
    }
  }
}
