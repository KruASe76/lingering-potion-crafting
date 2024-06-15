package me.kruase.kotlin_plugin_template

import me.kruase.kotlin_plugin_template.commands.reload
import org.bukkit.plugin.java.JavaPlugin


class Template : JavaPlugin() {
    companion object {
        lateinit var instance: Template
        lateinit var userConfig: TemplateConfig
    }

    override fun onEnable() {
        instance = this

        reload()

        getCommand("template")!!.setExecutor(TemplateCommands())

        server.pluginManager.registerEvents(TemplateEvents(), instance)
    }
}
