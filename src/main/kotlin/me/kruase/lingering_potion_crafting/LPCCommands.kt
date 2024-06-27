package me.kruase.lingering_potion_crafting

import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.mainConfig
import me.kruase.lingering_potion_crafting.commands.help
import me.kruase.lingering_potion_crafting.commands.reload
import me.kruase.lingering_potion_crafting.util.hasPluginPermission
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player


class LPCCommands : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        val fullArgs = args.dropLast(1)

        return when (fullArgs.getOrNull(0)) {
            null ->
                mainConfig.messages.help.keys
                    .filter { sender.hasPluginPermission(it.replace("-", ".")) } - "header"
            "help" ->
                if (!sender.hasPluginPermission(args[0])) emptyList()
                else when (fullArgs.getOrNull(1)) {
                    null ->
                        mainConfig.messages.help.keys
                            .filter { sender.hasPluginPermission(it.replace("-", ".")) } - "header"
                    else -> emptyList()
                }
            else -> emptyList()
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        try {
            when (args.getOrNull(0)) {
                null -> help(sender, emptyList())
                "help" -> help(sender, args.drop(1))
                "reload" -> reload(sender)
            }
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException, is NoSuchElementException ->
                    sender.sendMessage(
                        "${ChatColor.RED}${mainConfig.messages.error["invalid-command"] ?: "Error: invalid-command"}"
                    )
                is UnsupportedOperationException ->
                    sender.sendMessage(
                        "${ChatColor.RED}${mainConfig.messages.error["no-permission"] ?: "Error: no-permission"}"
                    )
                is IllegalStateException ->
                    sender.sendMessage("${ChatColor.RED}${e.message ?: "Unknown error"}")
                else -> throw e
            }
        }

        return true
    }

    private fun playerOnly(sender: CommandSender, command: (Player, List<String>) -> Unit, args: List<String>) {
        when (sender) {
            is Player ->
                command(sender, args)
            else ->
                sender
                    .sendMessage("${ChatColor.RED}${mainConfig.messages.error["player-only"] ?: "Error: player-only"}")
        }
    }
}
