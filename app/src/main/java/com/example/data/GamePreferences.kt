package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.BattlePet
import com.example.model.PetSpecies
import org.json.JSONArray
import org.json.JSONObject

class GamePreferences(context: Context) {
  private val prefs: SharedPreferences = context.getSharedPreferences("pixel_pets_game", Context.MODE_PRIVATE)

  fun isFirstLaunch(): Boolean = prefs.getBoolean("is_first_launch", true)
  fun setFirstLaunchCompleted() = prefs.edit().putBoolean("is_first_launch", false).apply()

  fun getCoins(): Int = prefs.getInt("coins", 800)
  fun setCoins(amount: Int) = prefs.edit().putInt("coins", amount).apply()

  fun getDuelsWon(): Int = prefs.getInt("duels_won", 0)
  fun incrementDuelsWon() = prefs.edit().putInt("duels_won", getDuelsWon() + 1).apply()

  fun getSeenPetIds(): Set<Int> {
    val set = prefs.getStringSet("seen_pets", emptySet()) ?: emptySet()
    return set.mapNotNull { it.toIntOrNull() }.toSet()
  }

  fun markPetSeen(id: Int) {
    val current = getSeenPetIds().toMutableSet()
    current.add(id)
    prefs.edit().putStringSet("seen_pets", current.map { it.toString() }.toSet()).apply()
  }

  fun getCaughtPetIds(): Set<Int> {
    val set = prefs.getStringSet("caught_pets", emptySet()) ?: emptySet()
    return set.mapNotNull { it.toIntOrNull() }.toSet()
  }

  fun markPetCaught(id: Int) {
    val current = getCaughtPetIds().toMutableSet()
    current.add(id)
    prefs.edit().putStringSet("caught_pets", current.map { it.toString() }.toSet()).apply()
    markPetSeen(id)
  }

  fun getInventory(): Map<String, Int> {
    val json = prefs.getString("inventory", null)
    if (json == null) {
      // Default starter inventory
      return mapOf(
        "orb_standard" to 10,
        "orb_great" to 3,
        "potion_regular" to 5,
        "potion_super" to 2,
        "revive" to 2
      )
    }
    val map = mutableMapOf<String, Int>()
    try {
      val obj = JSONObject(json)
      obj.keys().forEach { key ->
        map[key] = obj.getInt(key)
      }
    } catch (e: Exception) {
      // fallback
    }
    return map
  }

  fun saveInventory(inv: Map<String, Int>) {
    val obj = JSONObject()
    inv.forEach { (k, v) -> obj.put(k, v) }
    prefs.edit().putString("inventory", obj.toString()).apply()
  }

  fun saveParty(party: List<BattlePet>) {
    val arr = JSONArray()
    party.forEach { pet ->
      val obj = JSONObject().apply {
        put("speciesId", pet.species.id)
        put("nickname", pet.nickname)
        put("level", pet.level)
        put("currentHp", pet.currentHp)
        put("currentExp", pet.currentExp)
      }
      arr.put(obj)
    }
    prefs.edit().putString("party_data", arr.toString()).apply()
  }

  fun loadParty(): List<BattlePet> {
    val json = prefs.getString("party_data", null) ?: return emptyList()
    val list = mutableListOf<BattlePet>()
    try {
      val arr = JSONArray(json)
      for (i in 0 until arr.length()) {
        val obj = arr.getJSONObject(i)
        val speciesId = obj.getInt("speciesId")
        val species = PetRepository.getPetById(speciesId)
        val level = obj.getInt("level")
        val nickname = obj.optString("nickname", species.name)
        val exp = obj.optInt("currentExp", 0)
        val hp = obj.optInt("currentHp", 1)

        val pet = BattlePet.createWild(species, level).copy(
          nickname = nickname,
          currentExp = exp
        )
        pet.currentHp = hp.coerceIn(0, pet.maxHp)
        list.add(pet)
      }
    } catch (e: Exception) {
      return emptyList()
    }
    return list
  }
}
