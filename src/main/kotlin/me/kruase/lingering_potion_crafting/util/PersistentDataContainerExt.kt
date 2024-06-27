package me.kruase.lingering_potion_crafting.util

import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.instance
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType


private val resultMarkKey = NamespacedKey(instance, "recipe_result")
private val byteType = PersistentDataType.BYTE
private const val value: Byte = 1


fun PersistentDataContainer.addResultMark() {
    set(resultMarkKey, byteType, value)
}

fun PersistentDataContainer.hasResultMark(): Boolean {
    return has(resultMarkKey, byteType)
}
