package me.kruase.lingering_potion_crafting.commands

import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.instance
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.mainConfig
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.recipeConfig
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.sendPlayerMessage
import me.kruase.lingering_potion_crafting.getMainConfig
import me.kruase.lingering_potion_crafting.getRecipeConfig
import me.kruase.lingering_potion_crafting.util.hasPluginPermission
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


fun reload(sender: CommandSender) {
    if (!sender.hasPluginPermission("reload")) throw UnsupportedOperationException()

    mainConfig = instance.getMainConfig()
    recipeConfig = instance.getRecipeConfig()

    if (sender is Player)
        sendPlayerMessage(sender, mainConfig.messages.info["config-loaded"])
}


fun reload() {
    reload(instance.server.consoleSender)
}
