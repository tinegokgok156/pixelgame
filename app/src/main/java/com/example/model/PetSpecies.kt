package com.example.model

data class PetSpecies(
  val id: Int,
  val name: String,
  val primaryType: ElementType,
  val secondaryType: ElementType? = null,
  val baseHp: Int,
  val baseAttack: Int,
  val baseDefense: Int,
  val baseSpeed: Int,
  val spriteSeed: Long,
  val description: String,
  val baseMoves: List<PetMove>,
  val catchRate: Float = 0.45f,
  val rarityTier: RarityTier = RarityTier.COMMON
) {
  val formattedId: String
    get() = "#%03d".format(id)

  val totalBaseStats: Int
    get() = baseHp + baseAttack + baseDefense + baseSpeed
}

enum class RarityTier(val label: String) {
  COMMON("Common"),
  UNCOMMON("Uncommon"),
  RARE("Rare"),
  APEX("Apex"),
  LEGENDARY("Legendary")
}
