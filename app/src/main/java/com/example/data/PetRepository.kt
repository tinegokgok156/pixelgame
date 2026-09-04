package com.example.data

import com.example.model.BattlePet
import com.example.model.ElementType
import com.example.model.PetSpecies
import com.example.model.RarityTier
import kotlin.math.sin

data class GymLeader(
  val name: String,
  val title: String,
  val badgeName: String,
  val specialtyType: ElementType,
  val dialog: String,
  val teamSpeciesIds: List<Pair<Int, Int>> // pair of (speciesId, level)
)

object PetRepository {

  private val ALL_SPECIES: List<PetSpecies> by lazy {
    generateAll500Pets()
  }

  fun getAllPets(): List<PetSpecies> = ALL_SPECIES

  fun getPetById(id: Int): PetSpecies {
    val clamped = id.coerceIn(1, 500)
    return ALL_SPECIES[clamped - 1]
  }

  fun getStarters(): List<PetSpecies> {
    return listOf(getPetById(1), getPetById(4), getPetById(7))
  }

  fun searchPets(query: String, filterType: ElementType?): List<PetSpecies> {
    return ALL_SPECIES.filter { pet ->
      val matchesType = filterType == null || pet.primaryType == filterType || pet.secondaryType == filterType
      val matchesQuery = query.isBlank() ||
          pet.name.contains(query, ignoreCase = true) ||
          pet.id.toString() == query.trim() ||
          pet.formattedId.contains(query, ignoreCase = true) ||
          pet.primaryType.displayName.contains(query, ignoreCase = true)
      matchesType && matchesQuery
    }
  }

  fun createWildEncounter(biome: String, targetLevel: Int): BattlePet {
    val eligiblePool = when (biome.lowercase()) {
      "forest", "grass" -> ALL_SPECIES.filter { it.primaryType in listOf(ElementType.GRASS, ElementType.POISON, ElementType.NORMAL) }
      "volcano", "cave" -> ALL_SPECIES.filter { it.primaryType in listOf(ElementType.FIRE, ElementType.EARTH, ElementType.STEEL) }
      "ocean", "lake" -> ALL_SPECIES.filter { it.primaryType in listOf(ElementType.WATER, ElementType.ICE) }
      "thunder", "plains" -> ALL_SPECIES.filter { it.primaryType in listOf(ElementType.ELECTRIC, ElementType.FLYING, ElementType.NORMAL) }
      "crypt", "shadow" -> ALL_SPECIES.filter { it.primaryType in listOf(ElementType.DARK, ElementType.PSYCHIC, ElementType.POISON) }
      "spire", "sky" -> ALL_SPECIES.filter { it.primaryType in listOf(ElementType.DRAGON, ElementType.FAIRY, ElementType.FLYING) }
      else -> ALL_SPECIES
    }
    val species = if (eligiblePool.isNotEmpty()) eligiblePool.random() else ALL_SPECIES.random()
    val level = (targetLevel + (-1..2).random()).coerceIn(2, 90)
    return BattlePet.createWild(species, level)
  }

  fun getGymLeaders(): List<GymLeader> {
    return listOf(
      GymLeader(
        name = "Terra Brock",
        title = "Rock & Earth Warden",
        badgeName = "Boulder Badge",
        specialtyType = ElementType.EARTH,
        dialog = "My earth pets have weathered a thousand sandstorms! Can your bond shatter solid stone?",
        teamSpeciesIds = listOf(14 to 12, 53 to 14, 15 to 16)
      ),
      GymLeader(
        name = "Marina Cascade",
        title = "Ocean Wave Mistress",
        badgeName = "Cascade Badge",
        specialtyType = ElementType.WATER,
        dialog = "The tides bend to my rhythm. Let's see if your pets can swim or sink!",
        teamSpeciesIds = listOf(7 to 20, 88 to 22, 9 to 24)
      ),
      GymLeader(
        name = "Volta Surge",
        title = "Lightning Brigadier",
        badgeName = "Thunder Badge",
        specialtyType = ElementType.ELECTRIC,
        dialog = "Ten thousand volts of raw static! Don't blink or you'll get fried!",
        teamSpeciesIds = listOf(10 to 28, 126 to 30, 12 to 32)
      ),
      GymLeader(
        name = "Ignis Blaze",
        title = "Pyre Sovereign",
        badgeName = "Inferno Badge",
        specialtyType = ElementType.FIRE,
        dialog = "The blazing heart of volcano fire burns within my pets! Feel the furnace heat!",
        teamSpeciesIds = listOf(4 to 36, 172 to 38, 6 to 42)
      ),
      GymLeader(
        name = "Astrid Drake",
        title = "Grand Dragon Champion",
        badgeName = "Draco Crest",
        specialtyType = ElementType.DRAGON,
        dialog = "Dragons rule the ancient skies! Only the true Pet Master can withstand our draconic roar!",
        teamSpeciesIds = listOf(3 to 48, 250 to 50, 500 to 55)
      )
    )
  }

  private fun generateAll500Pets(): List<PetSpecies> {
    val list = ArrayList<PetSpecies>(500)

    val types = ElementType.values()

    // 50 specific hand-crafted iconic starters and milestones
    val iconicStarters = mapOf(
      1 to Triple("Florasaur", ElementType.GRASS, null),
      2 to Triple("Ivysprout", ElementType.GRASS, ElementType.POISON),
      3 to Triple("Bloomdrak", ElementType.GRASS, ElementType.DRAGON),
      4 to Triple("Emberpup", ElementType.FIRE, null),
      5 to Triple("Pyrowolf", ElementType.FIRE, null),
      6 to Triple("Infernocrag", ElementType.FIRE, ElementType.EARTH),
      7 to Triple("Aquafox", ElementType.WATER, null),
      8 to Triple("Torrentail", ElementType.WATER, null),
      9 to Triple("Tsunamiwyrm", ElementType.WATER, ElementType.DRAGON),
      10 to Triple("Zapchick", ElementType.ELECTRIC, ElementType.FLYING),
      11 to Triple("Voltstriker", ElementType.ELECTRIC, ElementType.FLYING),
      12 to Triple("Thundereagle", ElementType.ELECTRIC, ElementType.FLYING),
      13 to Triple("Pebblecrab", ElementType.EARTH, null),
      14 to Triple("Bouldermite", ElementType.EARTH, ElementType.STEEL),
      15 to Triple("Terragolem", ElementType.EARTH, ElementType.STEEL),
      16 to Triple("Frostpaw", ElementType.ICE, null),
      17 to Triple("Glacilynx", ElementType.ICE, null),
      18 to Triple("Blizzarreaper", ElementType.ICE, ElementType.DARK),
      19 to Triple("Mindling", ElementType.PSYCHIC, null),
      20 to Triple("Psypupil", ElementType.PSYCHIC, null),
      21 to Triple("Astroarchon", ElementType.PSYCHIC, ElementType.FAIRY),
      22 to Triple("Shadowbat", ElementType.DARK, ElementType.FLYING),
      23 to Triple("Nightwing", ElementType.DARK, ElementType.FLYING),
      24 to Triple("Vampyrex", ElementType.DARK, ElementType.DRAGON),
      25 to Triple("Pixiebell", ElementType.FAIRY, null),
      26 to Triple("Nimblestride", ElementType.NORMAL, null),
      27 to Triple("Venomwasp", ElementType.POISON, ElementType.FLYING),
      28 to Triple("Ironscarab", ElementType.STEEL, ElementType.EARTH),
      29 to Triple("Dracolyte", ElementType.DRAGON, null),
      30 to Triple("Hydraxor", ElementType.DRAGON, ElementType.WATER),
      50 to Triple("Cinderlynx", ElementType.FIRE, null),
      100 to Triple("Voltcentaur", ElementType.ELECTRIC, ElementType.STEEL),
      150 to Triple("Mewtron", ElementType.PSYCHIC, null),
      200 to Triple("Chronosol", ElementType.FIRE, ElementType.FAIRY),
      250 to Triple("Leviagorgon", ElementType.WATER, ElementType.DARK),
      300 to Triple("Yggdrasylph", ElementType.GRASS, ElementType.FAIRY),
      350 to Triple("Magmatitan", ElementType.FIRE, ElementType.EARTH),
      400 to Triple("Nebuladrake", ElementType.DRAGON, ElementType.PSYCHIC),
      450 to Triple("Voidphantom", ElementType.DARK, ElementType.STEEL),
      498 to Triple("Celestior", ElementType.FAIRY, ElementType.DRAGON),
      499 to Triple("Abyssarch", ElementType.DARK, ElementType.DRAGON),
      500 to Triple("Omnidragon", ElementType.DRAGON, ElementType.FLYING)
    )

    val prefixes = listOf(
      "Pyro", "Aqua", "Terra", "Volt", "Flora", "Cryo", "Aero", "Psy",
      "Umbra", "Draco", "Ferro", "Toxi", "Sola", "Luna", "Electro", "Magma",
      "Hydro", "Zephyr", "Shadow", "Giga", "Nova", "Vortex", "Glacio", "Chrono",
      "Astro", "Titan", "Spectral", "Blaze", "Breeze", "Surge", "Venom", "Apex",
      "Aura", "Solar", "Storm", "Thunder", "Inferno", "Cinder", "Tidal", "Frost",
      "Granite", "Phantom", "Mystic", "Iron", "Crystal", "Radiant", "Dusk", "Dawn"
    )

    val roots = listOf(
      "fang", "claw", "wing", "tail", "fin", "horn", "scale", "paw",
      "beak", "crest", "spire", "shell", "blade", "spark", "drake", "fox",
      "hound", "serpent", "beetle", "raptor", "golem", "wisp", "lynx", "lion",
      "bear", "hawk", "viper", "mantis", "whale", "turtle", "bull", "shark",
      "stag", "crane", "panther", "falcon", "crab", "wasp", "wolf", "moth"
    )

    val suffixes = listOf(
      "or", "ix", "on", "us", "ax", "ar", "ex", "is", "eon", "isaur",
      "ling", "ite", "orion", "adon", "oraptor", "adon", "ant", "oid",
      "oceros", "odon", "ian", "alis", "eron", "oth", "ox", "arx"
    )

    for (id in 1..500) {
      val (name, primaryType, secondaryType) = if (iconicStarters.containsKey(id)) {
        iconicStarters[id]!!
      } else {
        val pref = prefixes[(id * 7 + 13) % prefixes.size]
        val root = roots[(id * 11 + 5) % roots.size]
        val suff = suffixes[(id * 3 + 2) % suffixes.size]
        val generatedName = if (id % 3 == 0) "$pref$root" else "$pref$suff"

        val pType = types[(id * 3 + 1) % types.size]
        val sType = if ((id % 4 == 0 || id % 7 == 0) && id % 14 != 0) {
          val candidate = types[(id * 5 + 7) % types.size]
          if (candidate != pType) candidate else null
        } else null

        Triple(generatedName, pType, sType)
      }

      val evolutionStage = when {
        id in listOf(498, 499, 500) || id % 50 == 0 -> 3 // Legendary/Apex
        id % 3 == 0 -> 2 // Apex form
        id % 3 == 2 -> 1 // Intermediate
        else -> 0 // Basic
      }

      val rarity = when {
        id == 500 || id in listOf(498, 499, 150, 250, 350, 450) -> RarityTier.LEGENDARY
        id % 25 == 0 -> RarityTier.APEX
        id % 5 == 0 -> RarityTier.RARE
        id % 2 == 0 -> RarityTier.UNCOMMON
        else -> RarityTier.COMMON
      }

      val statMultiplier = when (rarity) {
        RarityTier.LEGENDARY -> 1.5f
        RarityTier.APEX -> 1.3f
        RarityTier.RARE -> 1.15f
        RarityTier.UNCOMMON -> 1.0f
        RarityTier.COMMON -> 0.9f
      }

      val pseudoRand = (sin(id.toDouble() * 12.9898) * 43758.5453).let { it - it.toInt() }.let { if (it < 0) -it else it }
      val pseudoRand2 = (sin((id + 50).toDouble() * 78.233) * 43758.5453).let { it - it.toInt() }.let { if (it < 0) -it else it }

      val baseHp = ((45 + pseudoRand * 40) * statMultiplier).toInt()
      val baseAtk = ((40 + pseudoRand2 * 50) * statMultiplier).toInt()
      val baseDef = ((40 + (1 - pseudoRand) * 45) * statMultiplier).toInt()
      val baseSpd = ((35 + (1 - pseudoRand2) * 55) * statMultiplier).toInt()

      val moves = MoveRepository.getMovesForType(primaryType, secondaryType)

      val typeDesc = if (secondaryType != null) {
        "${primaryType.displayName}/${secondaryType.displayName}"
      } else {
        primaryType.displayName
      }

      val desc = when (evolutionStage) {
        3 -> "A legendary $typeDesc pet spoken of in ancient folklore. It radiates immense elemental sovereignty."
        2 -> "A formidable fully-evolved $typeDesc pet with seasoned combat prowess and fierce territorial instincts."
        1 -> "A spirited intermediate $typeDesc creature undergoing intense elemental maturation."
        else -> "A spirited wild $typeDesc pet with lively curiosity and hidden potential waiting to awaken in duels."
      }

      val catchRate = when (rarity) {
        RarityTier.LEGENDARY -> 0.08f
        RarityTier.APEX -> 0.18f
        RarityTier.RARE -> 0.30f
        RarityTier.UNCOMMON -> 0.45f
        RarityTier.COMMON -> 0.65f
      }

      val spriteSeed = (id * 1000003L) xor (primaryType.ordinal.toLong() shl 16) xor 0xCAFEBABE

      list.add(
        PetSpecies(
          id = id,
          name = name,
          primaryType = primaryType,
          secondaryType = secondaryType,
          baseHp = baseHp.coerceIn(35, 160),
          baseAttack = baseAtk.coerceIn(35, 160),
          baseDefense = baseDef.coerceIn(35, 160),
          baseSpeed = baseSpd.coerceIn(35, 160),
          spriteSeed = spriteSeed,
          description = desc,
          baseMoves = moves,
          catchRate = catchRate,
          rarityTier = rarity
        )
      )
    }

    return list
  }
}
