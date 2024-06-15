package me.kruase.kotlin_plugin_template

import org.bukkit.configuration.file.FileConfiguration
import java.io.File


data class TemplateConfig(private val config: FileConfiguration) {
    val messages = MessagesConfig(config)
}


fun Template.getUserConfig(): TemplateConfig {
    val configFile = File(dataFolder, "config.yml")
    val tempConfigFile = File(dataFolder, "temp-config.yml")
    val oldConfigFile = File(dataFolder, "old-config-${System.currentTimeMillis()}.yml")

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
        }

        TemplateConfig(config)
    } catch (e: Exception) {
        when (e) {
            is NullPointerException -> {
                logger.severe("Invalid $name config detected! Creating a new one (default)...")

                tempConfigFile.renameTo(oldConfigFile)

                logger.info("New (default) config created!")

                TemplateConfig(config)
            }
            else -> throw e
        }
    }
        .also { logger.info("Config loaded!") }
}


data class MessagesConfig(private val config: FileConfiguration) {
    val help: Map<String, String> =
        config
            .getConfigurationSection("messages.help")!!
            .getKeys(false)
            .associateWith { config.getString("messages.help.$it")!! }
    val error: Map<String, String> =
        config
            .getConfigurationSection("messages.error")!!
            .getKeys(false)
            .associateWith { config.getString("messages.error.$it")!! }
    val info: Map<String, String> =
        config
            .getConfigurationSection("messages.info")!!
            .getKeys(false)
            .associateWith { config.getString("messages.info.$it")!! }
}
