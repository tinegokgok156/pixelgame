package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PetRepository
import com.example.model.ElementType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Pixel Pets Duel", appName)
  }

  @Test
  fun `verify 500 pets generated`() {
    val allPets = PetRepository.getAllPets()
    assertEquals(500, allPets.size)
    assertEquals(1, allPets.first().id)
    assertEquals(500, allPets.last().id)

    // Check random pets exist and have proper stats
    val pet250 = PetRepository.getPetById(250)
    assertNotNull(pet250)
    assertTrue(pet250.baseHp > 0)
    assertTrue(pet250.baseAttack > 0)
    assertTrue(pet250.baseMoves.isNotEmpty())
  }

  @Test
  fun `verify type effectiveness chart`() {
    // Water against Fire is super effective (2.0x)
    val waterVsFire = ElementType.WATER.effectivenessAgainst(ElementType.FIRE)
    assertEquals(2.0f, waterVsFire)

    // Fire against Water is not very effective (0.5x)
    val fireVsWater = ElementType.FIRE.effectivenessAgainst(ElementType.WATER)
    assertEquals(0.5f, fireVsWater)

    // Grass against Water is super effective (2.0x)
    val grassVsWater = ElementType.GRASS.effectivenessAgainst(ElementType.WATER)
    assertEquals(2.0f, grassVsWater)
  }
}

