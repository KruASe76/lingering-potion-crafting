package me.kruase.lingering_potion_crafting.commands

import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.instance
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.userConfig
import me.kruase.lingering_potion_crafting.getUserConfig
import me.kruase.lingering_potion_crafting.util.hasPluginPermission
import org.bukkit.command.CommandSender


fun reload(sender: CommandSender, args: Array<out String>) {
    if (!sender.hasPluginPermission("reload")) throw UnsupportedOperationException()

    if (args.isNotEmpty()) throw IllegalArgumentException()

    userConfig = instance.getUserConfig()
}


fun reload() {
    reload(instance.server.consoleSender, emptyArray())
}
