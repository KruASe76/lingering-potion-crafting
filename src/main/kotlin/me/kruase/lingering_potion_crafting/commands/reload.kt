package me.kruase.lingering_potion_crafting.commands

import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.instance
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.mainConfig
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.recipeConfig
import me.kruase.lingering_potion_crafting.getMainConfig
import me.kruase.lingering_potion_crafting.getRecipeConfig
import me.kruase.lingering_potion_crafting.util.hasPluginPermission
import org.bukkit.command.CommandSender


fun reload(sender: CommandSender, args: Array<out String>) {
    if (!sender.hasPluginPermission("reload")) throw UnsupportedOperationException()

    if (args.isNotEmpty()) throw IllegalArgumentException()

    mainConfig = instance.getMainConfig()
    recipeConfig = instance.getRecipeConfig()
}


fun reload() {
    reload(instance.server.consoleSender, emptyArray())
}
