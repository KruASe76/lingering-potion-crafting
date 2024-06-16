package me.kruase.lingering_potion_crafting

import me.kruase.lingering_potion_crafting.commands.reload
import org.bukkit.plugin.java.JavaPlugin


class LingeringPotionCrafting : JavaPlugin() {
    companion object {
        lateinit var instance: LingeringPotionCrafting
        lateinit var userConfig: LPCConfig
    }

    override fun onEnable() {
        instance = this

        reload()

        getCommand("lpc")!!.setExecutor(LPCCommands())

        server.pluginManager.registerEvents(LPCEvents(), instance)
    }
}
