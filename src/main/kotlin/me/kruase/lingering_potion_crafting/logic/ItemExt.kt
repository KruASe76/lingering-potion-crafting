package me.kruase.lingering_potion_crafting.logic

import de.tr7zw.nbtapi.NBT
import me.kruase.lingering_potion_crafting.LPCRecipeComponent
import org.bukkit.entity.Item


fun Item.matches(component: LPCRecipeComponent): Boolean {
    if (itemStack.type != component.material)
        return false

    if (itemStack.amount != component.amount)
        return false

    return NBT.readNbt(itemStack)
        .let { nbt ->
            component.nbt.keys.all { key -> nbt.getString(key) == component.nbt.getString(key) }
        }
}
