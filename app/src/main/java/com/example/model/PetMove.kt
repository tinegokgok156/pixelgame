package com.example.model

enum class MoveCategory {
  PHYSICAL,
  SPECIAL,
  STATUS
}

data class PetMove(
  val id: String,
  val name: String,
  val type: ElementType,
  val power: Int,
  val accuracy: Int = 100,
  val maxPp: Int = 20,
  val category: MoveCategory = MoveCategory.PHYSICAL,
  val description: String = "",
  val effectKey: String = "IMPACT",
  var currentPp: Int = maxPp
) {
  fun copyWithMaxPp(): PetMove {
    return this.copy(currentPp = maxPp)
  }
}
