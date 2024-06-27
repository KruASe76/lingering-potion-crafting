package me.kruase.lingering_potion_crafting

import me.kruase.lingering_potion_crafting.commands.reload
import me.kruase.lingering_potion_crafting.logic.EffectCloudData
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class LingeringPotionCrafting : JavaPlugin() {
    companion object {
        lateinit var instance: LingeringPotionCrafting
        lateinit var mainConfig: LPCConfig
        lateinit var recipeConfig: LPCRecipeConfig

        val trackedEffectClouds: MutableMap<UUID, EffectCloudData> = mutableMapOf()

        fun sendPlayerMessage(player: Player, message: String?) {
            if (message == null)
                return

            player.sendMessage(
                "${ChatColor.GOLD}[${ChatColor.GREEN}${instance.name}${ChatColor.GOLD}]${ChatColor.RESET} $message"
            )
        }
    }

    override fun onEnable() {
        instance = this

        reload()

        getCommand("lpc")!!.setExecutor(LPCCommands())

        server.pluginManager.registerEvents(LPCEvents(), instance)
    }
}
