package me.kruase.lingering_potion_crafting

import me.kruase.lingering_potion_crafting.commands.reload
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class LingeringPotionCrafting : JavaPlugin() {
    companion object {
        lateinit var instance: LingeringPotionCrafting
        lateinit var mainConfig: LPCConfig
        lateinit var recipeConfig: LPCRecipeConfig
    }

    override fun onEnable() {
        instance = this

        reload()

        getCommand("lpc")!!.setExecutor(LPCCommands())

        server.pluginManager.registerEvents(LPCEvents(), instance)
    }
}
