package me.kruase.lingering_potion_crafting

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.io.File


data class LPCConfig(private val config: FileConfiguration) {
    val messages = MessagesConfig(config.getConfigurationSection("messages")!!)
    val cloudLifetimeTicks = config.getInt("cloud-lifetime") * 20
}

data class MessagesConfig(private val section: ConfigurationSection) {
    val help: Map<String, String> =
        section
            .getConfigurationSection("help")!!
            .getKeys(false)
            .associateWith { section.getString("help.$it")!! }
    val error: Map<String, String> =
        section
            .getConfigurationSection("error")!!
            .getKeys(false)
            .associateWith { section.getString("error.$it")!! }
    val info: Map<String, String> =
        section
            .getConfigurationSection("info")!!
            .getKeys(false)
            .associateWith { section.getString("info.$it")!! }
}


fun LingeringPotionCrafting.getMainConfig(): LPCConfig {
    val configFile = File(dataFolder, "config.yml")
    val tempConfigFile = File(dataFolder, "config-temp.yml")
    val oldConfigFile = File(dataFolder, "config-old-${System.currentTimeMillis()}.yml")

    return try {
        saveDefaultConfig()
        reloadConfig()

        // validating current config
        val currentConfigKeys = config.getKeys(true)

        configFile.renameTo(tempConfigFile)
        saveDefaultConfig()
        reloadConfig()

        val defaultConfigKeys = config.getKeys(true)

        if ((defaultConfigKeys - currentConfigKeys).isNotEmpty())
            throw NullPointerException()
        else {
            configFile.delete()
            tempConfigFile.renameTo(configFile)
            reloadConfig()

            LPCConfig(config)
        }
    } catch (e: Exception) {
        when (e) {
            is NullPointerException -> {
                logger.severe("Invalid $name config detected! Creating a new one (default)...")

                tempConfigFile.renameTo(oldConfigFile)

                logger.info("New (default) config created!")

                LPCConfig(config)
            }
            else -> throw e
        }
    }
        .also { logger.info("Config loaded!") }
}
