package me.kruase.kotlin_plugin_template

import me.kruase.kotlin_plugin_template.Template.Companion.instance
import me.kruase.kotlin_plugin_template.Template.Companion.userConfig
import me.kruase.kotlin_plugin_template.commands.help
import me.kruase.kotlin_plugin_template.util.hasPluginPermission
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player


class TemplateCommands : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        val fullArgs = args.dropLast(1)

        return when (fullArgs.getOrNull(0)) {
            null ->
                userConfig.messages.help.keys
                    .filter { sender.hasPluginPermission(it.replace("-", ".")) } - "header"
            "help" ->
                if (!sender.hasPluginPermission(args[0])) emptyList()
                else when (fullArgs.getOrNull(1)) {
                    null ->
                        userConfig.messages.help.keys
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
                "reload" -> {
                    if (!sender.hasPluginPermission("reload")) throw UnsupportedOperationException()

                    userConfig = instance.getUserConfig()
                }
            }
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException, is NoSuchElementException ->
                    sender.sendMessage(
                        "${ChatColor.RED}${userConfig.messages.error["invalid-command"] ?: "Error: invalid-command"}"
                    )
                is UnsupportedOperationException ->
                    sender.sendMessage(
                        "${ChatColor.RED}${userConfig.messages.error["no-permission"] ?: "Error: no-permission"}"
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
                    .sendMessage("${ChatColor.RED}${userConfig.messages.error["player-only"] ?: "Error: player-only"}")
        }
    }
}
