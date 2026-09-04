package com.example.model

import androidx.compose.ui.graphics.Color

enum class ElementType(
  val displayName: String,
  val color: Color,
  val badgeColor: Color,
  val textColor: Color = Color.White
) {
  NORMAL("Normal", Color(0xFFA8A878), Color(0xFF6D6D4E)),
  FIRE("Fire", Color(0xFFF08030), Color(0xFF9C531F)),
  WATER("Water", Color(0xFF6890F0), Color(0xFF445E9C)),
  GRASS("Grass", Color(0xFF78C850), Color(0xFF4E8234)),
  ELECTRIC("Electric", Color(0xFFF8D030), Color(0xFFA1871F), Color(0xFF1E293B)),
  ICE("Ice", Color(0xFF98D8D8), Color(0xFF638D8D), Color(0xFF0F172A)),
  EARTH("Earth", Color(0xFFE0C068), Color(0xFF927D44)),
  FLYING("Flying", Color(0xFFA890F0), Color(0xFF6D5E9C)),
  PSYCHIC("Psychic", Color(0xFFF85888), Color(0xFFA13959)),
  DARK("Dark", Color(0xFF705848), Color(0xFF49392F)),
  DRAGON("Dragon", Color(0xFF7038F8), Color(0xFF4924A1)),
  STEEL("Steel", Color(0xFFB8B8D0), Color(0xFF787887)),
  POISON("Poison", Color(0xFFA040A0), Color(0xFF682A68)),
  FAIRY("Fairy", Color(0xFFEE99AC), Color(0xFF9B6470));

  fun effectivenessAgainst(defenderType: ElementType?): Float {
    if (defenderType == null) return 1.0f
    return when (this) {
      FIRE -> when (defenderType) {
        GRASS, ICE, STEEL -> 2.0f
        FIRE, WATER, EARTH, DRAGON -> 0.5f
        else -> 1.0f
      }
      WATER -> when (defenderType) {
        FIRE, EARTH -> 2.0f
        WATER, GRASS, DRAGON -> 0.5f
        else -> 1.0f
      }
      GRASS -> when (defenderType) {
        WATER, EARTH -> 2.0f
        FIRE, GRASS, POISON, FLYING, DRAGON, STEEL -> 0.5f
        else -> 1.0f
      }
      ELECTRIC -> when (defenderType) {
        WATER, FLYING -> 2.0f
        ELECTRIC, GRASS, DRAGON -> 0.5f
        EARTH -> 0.0f
        else -> 1.0f
      }
      ICE -> when (defenderType) {
        GRASS, EARTH, FLYING, DRAGON -> 2.0f
        FIRE, WATER, ICE, STEEL -> 0.5f
        else -> 1.0f
      }
      EARTH -> when (defenderType) {
        FIRE, ELECTRIC, POISON, STEEL -> 2.0f
        GRASS -> 0.5f
        FLYING -> 0.0f
        else -> 1.0f
      }
      FLYING -> when (defenderType) {
        GRASS -> 2.0f
        ELECTRIC, STEEL -> 0.5f
        else -> 1.0f
      }
      PSYCHIC -> when (defenderType) {
        POISON -> 2.0f
        PSYCHIC, STEEL -> 0.5f
        DARK -> 0.0f
        else -> 1.0f
      }
      DARK -> when (defenderType) {
        PSYCHIC -> 2.0f
        DARK, FAIRY -> 0.5f
        else -> 1.0f
      }
      DRAGON -> when (defenderType) {
        DRAGON -> 2.0f
        STEEL -> 0.5f
        FAIRY -> 0.0f
        else -> 1.0f
      }
      POISON -> when (defenderType) {
        GRASS, FAIRY -> 2.0f
        POISON, EARTH -> 0.5f
        STEEL -> 0.0f
        else -> 1.0f
      }
      STEEL -> when (defenderType) {
        ICE, FAIRY -> 2.0f
        FIRE, WATER, ELECTRIC, STEEL -> 0.5f
        else -> 1.0f
      }
      FAIRY -> when (defenderType) {
        DARK, DRAGON -> 2.0f
        FIRE, POISON, STEEL -> 0.5f
        else -> 1.0f
      }
      NORMAL -> when (defenderType) {
        STEEL -> 0.5f
        else -> 1.0f
      }
    }
  }

  fun combinedMultiplier(primaryDefender: ElementType, secondaryDefender: ElementType?): Float {
    val m1 = effectivenessAgainst(primaryDefender)
    val m2 = if (secondaryDefender != null) effectivenessAgainst(secondaryDefender) else 1.0f
    return m1 * m2
  }
}
