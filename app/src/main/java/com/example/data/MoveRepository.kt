package com.example.data

import com.example.model.ElementType
import com.example.model.MoveCategory
import com.example.model.PetMove

object MoveRepository {
  // Normal
  val TACKLE = PetMove("tackle", "Tackle", ElementType.NORMAL, 40, 100, 35, MoveCategory.PHYSICAL, "A physical charge tackle.", "IMPACT")
  val QUICK_ATTACK = PetMove("quick_attack", "Quick Attack", ElementType.NORMAL, 40, 100, 30, MoveCategory.PHYSICAL, "Strikes with blinding speed.", "IMPACT")
  val BODY_SLAM = PetMove("body_slam", "Body Slam", ElementType.NORMAL, 85, 100, 15, MoveCategory.PHYSICAL, "Drops full weight on target.", "IMPACT")
  val HYPER_BEAM = PetMove("hyper_beam", "Hyper Beam", ElementType.NORMAL, 150, 90, 5, MoveCategory.SPECIAL, "An apocalyptic energy beam.", "PSYCHIC")
  val SCRATCH = PetMove("scratch", "Scratch", ElementType.NORMAL, 40, 100, 35, MoveCategory.PHYSICAL, "Scratches with sharp claws.", "IMPACT")
  val SWIFT = PetMove("swift", "Swift", ElementType.NORMAL, 60, 100, 20, MoveCategory.SPECIAL, "Shoots star-shaped rays.", "IMPACT")

  // Fire
  val EMBER = PetMove("ember", "Ember", ElementType.FIRE, 40, 100, 25, MoveCategory.SPECIAL, "Fires small hot embers.", "FIRE")
  val FLAME_WHEEL = PetMove("flame_wheel", "Flame Wheel", ElementType.FIRE, 60, 100, 25, MoveCategory.PHYSICAL, "Rolls forward shrouded in fire.", "FIRE")
  val FLAMETHROWER = PetMove("flamethrower", "Flamethrower", ElementType.FIRE, 90, 100, 15, MoveCategory.SPECIAL, "Scorches foes with fierce flame.", "FIRE")
  val FIRE_BLAST = PetMove("fire_blast", "Fire Blast", ElementType.FIRE, 110, 85, 5, MoveCategory.SPECIAL, "An all-consuming fiery cross blast.", "FIRE")

  // Water
  val WATER_GUN = PetMove("water_gun", "Water Gun", ElementType.WATER, 40, 100, 25, MoveCategory.SPECIAL, "Squirts water forcefully.", "WATER")
  val BUBBLE_BEAM = PetMove("bubble_beam", "Bubble Beam", ElementType.WATER, 65, 100, 20, MoveCategory.SPECIAL, "Sprays a barrage of forceful bubbles.", "WATER")
  val SURF = PetMove("surf", "Surf", ElementType.WATER, 90, 100, 15, MoveCategory.SPECIAL, "Crushes foes beneath a tidal wave.", "WATER")
  val HYDRO_PUMP = PetMove("hydro_pump", "Hydro Pump", ElementType.WATER, 110, 80, 5, MoveCategory.SPECIAL, "Blasts a massive torrent of water.", "WATER")

  // Grass
  val VINE_WHIP = PetMove("vine_whip", "Vine Whip", ElementType.GRASS, 45, 100, 25, MoveCategory.PHYSICAL, "Lashes foe with slender vines.", "GRASS")
  val RAZOR_LEAF = PetMove("razor_leaf", "Razor Leaf", ElementType.GRASS, 55, 95, 25, MoveCategory.PHYSICAL, "Cuts with sharp-edged leaves.", "GRASS")
  val SEED_BOMB = PetMove("seed_bomb", "Seed Bomb", ElementType.GRASS, 80, 100, 15, MoveCategory.PHYSICAL, "Slams foe with a barrage of seeds.", "GRASS")
  val SOLAR_BEAM = PetMove("solar_beam", "Solar Beam", ElementType.GRASS, 120, 100, 10, MoveCategory.SPECIAL, "A brilliant blast of concentrated solar rays.", "GRASS")

  // Electric
  val THUNDER_SHOCK = PetMove("thunder_shock", "Thunder Shock", ElementType.ELECTRIC, 40, 100, 30, MoveCategory.SPECIAL, "Jolts foe with an electric shock.", "ELECTRIC")
  val SPARK = PetMove("spark", "Spark", ElementType.ELECTRIC, 65, 100, 20, MoveCategory.PHYSICAL, "Tackles target charged with sparks.", "ELECTRIC")
  val THUNDERBOLT = PetMove("thunderbolt", "Thunderbolt", ElementType.ELECTRIC, 90, 100, 15, MoveCategory.SPECIAL, "Blasts target with a fierce lightning bolt.", "ELECTRIC")
  val THUNDER = PetMove("thunder", "Thunder", ElementType.ELECTRIC, 110, 70, 10, MoveCategory.SPECIAL, "Calls down a jagged storm lightning bolt.", "ELECTRIC")

  // Ice
  val ICE_SHARD = PetMove("ice_shard", "Ice Shard", ElementType.ICE, 40, 100, 30, MoveCategory.PHYSICAL, "Hurls fast chunks of razor ice.", "ICE")
  val ICE_BEAM = PetMove("ice_beam", "Ice Beam", ElementType.ICE, 90, 100, 10, MoveCategory.SPECIAL, "Freezes target with a glacial beam.", "ICE")
  val BLIZZARD = PetMove("blizzard", "Blizzard", ElementType.ICE, 110, 70, 5, MoveCategory.SPECIAL, "Howls a sub-zero howling blizzard.", "ICE")

  // Earth
  val MUD_SLAP = PetMove("mud_slap", "Mud Slap", ElementType.EARTH, 20, 100, 10, MoveCategory.SPECIAL, "Hurls mud at the target.", "EARTH")
  val ROCK_SLIDE = PetMove("rock_slide", "Rock Slide", ElementType.EARTH, 75, 90, 10, MoveCategory.PHYSICAL, "Buries the opponent in boulders.", "EARTH")
  val EARTHQUAKE = PetMove("earthquake", "Earthquake", ElementType.EARTH, 100, 100, 10, MoveCategory.PHYSICAL, "Sets off a devastating ground tremor.", "EARTH")

  // Flying
  val GUST = PetMove("gust", "Gust", ElementType.FLYING, 40, 100, 35, MoveCategory.SPECIAL, "Flaps a gust of fierce wind.", "FLYING")
  val WING_ATTACK = PetMove("wing_attack", "Wing Attack", ElementType.FLYING, 60, 100, 35, MoveCategory.PHYSICAL, "Strikes foe with wide wings.", "FLYING")
  val AIR_SLASH = PetMove("air_slash", "Air Slash", ElementType.FLYING, 75, 95, 15, MoveCategory.SPECIAL, "Blades of cutting air slice the foe.", "FLYING")

  // Psychic
  val CONFUSION = PetMove("confusion", "Confusion", ElementType.PSYCHIC, 50, 100, 25, MoveCategory.SPECIAL, "Telekinetic wave confounds the foe.", "PSYCHIC")
  val PSYBEAM = PetMove("psybeam", "Psybeam", ElementType.PSYCHIC, 65, 100, 20, MoveCategory.SPECIAL, "Fires a peculiar mind beam.", "PSYCHIC")
  val PSYCHIC = PetMove("psychic", "Psychic", ElementType.PSYCHIC, 90, 100, 10, MoveCategory.SPECIAL, "Massive telekinetic force crushes the foe.", "PSYCHIC")

  // Dark
  val BITE = PetMove("bite", "Bite", ElementType.DARK, 60, 100, 25, MoveCategory.PHYSICAL, "Bites with dark vicious fangs.", "DARK")
  val NIGHT_SLASH = PetMove("night_slash", "Night Slash", ElementType.DARK, 70, 100, 15, MoveCategory.PHYSICAL, "Slits the foe in the shadows.", "DARK")
  val DARK_PULSE = PetMove("dark_pulse", "Dark Pulse", ElementType.DARK, 80, 100, 15, MoveCategory.SPECIAL, "Emits a horrible aura imbued with dark thoughts.", "DARK")

  // Dragon
  val DRAGON_BREATH = PetMove("dragon_breath", "Dragon Breath", ElementType.DRAGON, 60, 100, 20, MoveCategory.SPECIAL, "Exhales a draconic burst of fury.", "DRAGON")
  val DRAGON_CLAW = PetMove("dragon_claw", "Dragon Claw", ElementType.DRAGON, 80, 100, 15, MoveCategory.PHYSICAL, "Slashes with razor dragon talons.", "DRAGON")
  val OUTRAGE = PetMove("outrage", "Outrage", ElementType.DRAGON, 120, 100, 10, MoveCategory.PHYSICAL, "Rampages uncontrollably with draconic fury.", "DRAGON")

  // Poison
  val POISON_STING = PetMove("poison_sting", "Poison Sting", ElementType.POISON, 35, 100, 35, MoveCategory.PHYSICAL, "Stabs with a venomous needle.", "POISON")
  val SLUDGE_BOMB = PetMove("sludge_bomb", "Sludge Bomb", ElementType.POISON, 90, 100, 10, MoveCategory.SPECIAL, "Hurls toxic sludge at the foe.", "POISON")

  // Steel
  val METAL_CLAW = PetMove("metal_claw", "Metal Claw", ElementType.STEEL, 50, 95, 35, MoveCategory.PHYSICAL, "Rakes with hardened steel claws.", "STEEL")
  val IRON_HEAD = PetMove("iron_head", "Iron Head", ElementType.STEEL, 80, 100, 15, MoveCategory.PHYSICAL, "Slams target with an iron-hard head.", "STEEL")
  val FLASH_CANNON = PetMove("flash_cannon", "Flash Cannon", ElementType.STEEL, 80, 100, 10, MoveCategory.SPECIAL, "Focuses light into an artillery blast.", "STEEL")

  // Fairy
  val FAIRY_WIND = PetMove("fairy_wind", "Fairy Wind", ElementType.FAIRY, 40, 100, 30, MoveCategory.SPECIAL, "Stirs up a magical fairy breeze.", "FAIRY")
  val MOONBLAST = PetMove("moonblast", "Moonblast", ElementType.FAIRY, 95, 100, 15, MoveCategory.SPECIAL, "Borrowing lunar power, blasts the target.", "FAIRY")

  fun getMovesForType(primary: ElementType, secondary: ElementType?): List<PetMove> {
    val moves = mutableListOf<PetMove>()
    moves.add(TACKLE)

    val primaryMoves = when (primary) {
      ElementType.FIRE -> listOf(EMBER, FLAME_WHEEL, FLAMETHROWER, FIRE_BLAST)
      ElementType.WATER -> listOf(WATER_GUN, BUBBLE_BEAM, SURF, HYDRO_PUMP)
      ElementType.GRASS -> listOf(VINE_WHIP, RAZOR_LEAF, SEED_BOMB, SOLAR_BEAM)
      ElementType.ELECTRIC -> listOf(THUNDER_SHOCK, SPARK, THUNDERBOLT, THUNDER)
      ElementType.ICE -> listOf(ICE_SHARD, ICE_BEAM, BLIZZARD)
      ElementType.EARTH -> listOf(MUD_SLAP, ROCK_SLIDE, EARTHQUAKE)
      ElementType.FLYING -> listOf(GUST, WING_ATTACK, AIR_SLASH)
      ElementType.PSYCHIC -> listOf(CONFUSION, PSYBEAM, PSYCHIC)
      ElementType.DARK -> listOf(BITE, NIGHT_SLASH, DARK_PULSE)
      ElementType.DRAGON -> listOf(DRAGON_BREATH, DRAGON_CLAW, OUTRAGE)
      ElementType.POISON -> listOf(POISON_STING, SLUDGE_BOMB)
      ElementType.STEEL -> listOf(METAL_CLAW, IRON_HEAD, FLASH_CANNON)
      ElementType.FAIRY -> listOf(FAIRY_WIND, MOONBLAST)
      ElementType.NORMAL -> listOf(SCRATCH, QUICK_ATTACK, BODY_SLAM, HYPER_BEAM)
    }
    moves.add(primaryMoves.first())
    if (primaryMoves.size > 1) {
      moves.add(primaryMoves[1])
    }

    if (secondary != null) {
      val secondaryMoves = when (secondary) {
        ElementType.FIRE -> listOf(EMBER, FLAMETHROWER)
        ElementType.WATER -> listOf(WATER_GUN, SURF)
        ElementType.GRASS -> listOf(RAZOR_LEAF, SOLAR_BEAM)
        ElementType.ELECTRIC -> listOf(THUNDER_SHOCK, THUNDERBOLT)
        ElementType.ICE -> listOf(ICE_BEAM, BLIZZARD)
        ElementType.EARTH -> listOf(ROCK_SLIDE, EARTHQUAKE)
        ElementType.FLYING -> listOf(WING_ATTACK, AIR_SLASH)
        ElementType.PSYCHIC -> listOf(PSYBEAM, PSYCHIC)
        ElementType.DARK -> listOf(BITE, DARK_PULSE)
        ElementType.DRAGON -> listOf(DRAGON_CLAW, OUTRAGE)
        ElementType.POISON -> listOf(SLUDGE_BOMB)
        ElementType.STEEL -> listOf(IRON_HEAD)
        ElementType.FAIRY -> listOf(MOONBLAST)
        ElementType.NORMAL -> listOf(BODY_SLAM)
      }
      moves.add(secondaryMoves.random())
    } else {
      if (primaryMoves.size > 2) {
        moves.add(primaryMoves[2])
      } else {
        moves.add(SWIFT)
      }
    }

    return moves.distinctBy { it.id }.take(4)
  }
}
