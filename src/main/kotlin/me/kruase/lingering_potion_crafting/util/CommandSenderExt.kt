package me.kruase.lingering_potion_crafting.util

import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.instance
import org.bukkit.command.CommandSender


fun CommandSender.hasPluginPermission(name: String): Boolean {
    return hasPermission("${instance.name.lowercase()}.$name")
}
