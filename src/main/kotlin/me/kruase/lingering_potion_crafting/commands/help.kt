package me.kruase.lingering_potion_crafting.commands

import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.userConfig
import me.kruase.lingering_potion_crafting.util.hasPluginPermission
import org.bukkit.command.CommandSender


fun help(sender: CommandSender, args: List<String>) {
    if (!sender.hasPluginPermission("help")) throw UnsupportedOperationException()

    assert(args.size <= 1)

    when (args.getOrNull(0)) {
        null ->
            userConfig.messages.help.keys
                .filter { sender.hasPluginPermission(it.replace("-", ".")) || it == "header"}
                .forEach { sender.sendMessage(arrayOf(userConfig.messages.help[it])) }
        in userConfig.messages.help.keys - "header" -> sender.sendMessage(arrayOf(userConfig.messages.help[args[0]]))
        else -> throw AssertionError()
    }
}
