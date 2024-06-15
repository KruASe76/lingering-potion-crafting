package me.kruase.kotlin_plugin_template.commands

import me.kruase.kotlin_plugin_template.Template.Companion.userConfig
import me.kruase.kotlin_plugin_template.util.hasPluginPermission
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
