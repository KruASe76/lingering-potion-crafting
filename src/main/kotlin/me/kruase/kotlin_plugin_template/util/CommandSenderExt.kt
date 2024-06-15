package me.kruase.kotlin_plugin_template.util

import me.kruase.kotlin_plugin_template.Template.Companion.instance
import org.bukkit.command.CommandSender


fun CommandSender.hasPluginPermission(name: String): Boolean {
    return hasPermission("${instance.name.lowercase()}.$name")
}
