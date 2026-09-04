package com.example.model

enum class ItemCategory {
  ORB,
  HEALING,
  REVIVE,
  ENHANCER
}

data class Item(
  val id: String,
  val name: String,
  val category: ItemCategory,
  val description: String,
  val price: Int,
  val catchBonus: Float = 1.0f,
  val healAmount: Int = 0,
  val icon: String = "orb"
)

object StandardItems {
  val PET_ORB = Item("orb_standard", "Pet Orb", ItemCategory.ORB, "Standard orb used to capture wild pets.", 100, catchBonus = 1.0f, icon = "orb_standard")
  val GREAT_ORB = Item("orb_great", "Great Orb", ItemCategory.ORB, "A high-performance orb with 1.5x catch rate.", 300, catchBonus = 1.5f, icon = "orb_great")
  val ULTRA_ORB = Item("orb_ultra", "Ultra Orb", ItemCategory.ORB, "An ultra-grade orb with 2.0x catch rate.", 600, catchBonus = 2.0f, icon = "orb_ultra")
  val MASTER_ORB = Item("orb_master", "Master Orb", ItemCategory.ORB, "Legendary orb that captures any pet without fail.", 2500, catchBonus = 99.0f, icon = "orb_master")

  val POTION = Item("potion_regular", "Potion", ItemCategory.HEALING, "Restores 30 HP to a single pet.", 50, healAmount = 30, icon = "potion")
  val SUPER_POTION = Item("potion_super", "Super Potion", ItemCategory.HEALING, "Restores 70 HP to a single pet.", 150, healAmount = 70, icon = "super_potion")
  val MAX_POTION = Item("potion_max", "Max Potion", ItemCategory.HEALING, "Fully restores the HP of a single pet.", 400, healAmount = 9999, icon = "max_potion")
  val REVIVE = Item("revive", "Revive", ItemCategory.REVIVE, "Revives a fainted pet with 50% HP restored.", 250, icon = "revive")
  val RARE_CANDY = Item("rare_candy", "Rare Candy", ItemCategory.ENHANCER, "Raises a pet's level by 1 instantly.", 800, icon = "candy")

  val ALL_SHOP_ITEMS = listOf(
    PET_ORB, GREAT_ORB, ULTRA_ORB, MASTER_ORB,
    POTION, SUPER_POTION, MAX_POTION, REVIVE, RARE_CANDY
  )
}
