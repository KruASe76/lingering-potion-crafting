package me.kruase.lingering_potion_crafting

import de.tr7zw.nbtapi.NBT
import de.tr7zw.nbtapi.iface.ReadWriteNBT
import me.kruase.lingering_potion_crafting.util.addResultMark
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType
import java.io.File


class LPCRecipeConfig(config: FileConfiguration, dataFolder: File) {
    val recipes: Map<String, LPCRecipe> =
        config
            .getKeys(false)
            .associateWith { key -> LPCRecipe(key, config.getConfigurationSection(key)!!) }

    private val currentRecipeConfigFile = File(dataFolder, "recipes.yml")
    private val previousRecipeConfigFile = File(dataFolder, "recipes-previous.yml")

    fun save() {
        val recipeConfig = YamlConfiguration()

        recipes.forEach { (name, recipe) ->
            recipeConfig.set(
                name,
                mapOf(
                    "enabled" to recipe.isEnabled,
                    "effects" to recipe.effects.map { it.name.lowercase() },
                    "usages" to recipe.usageCount,
                    "result" to recipe.resultAsComponent.map,
                    "components" to recipe.components.map(LPCRecipeComponent::map)
                )
            )
        }

        currentRecipeConfigFile.renameTo(previousRecipeConfigFile)  // kept for rollback ability
        recipeConfig.save(currentRecipeConfigFile)
    }
}

data class LPCRecipe(val name: String, private val section: ConfigurationSection) {
    val isEnabled: Boolean = section.getBoolean("enabled")

    val effects: List<PotionType> = section.getStringList("effects").map { PotionType.valueOf(it.uppercase()) }
    val usageCount: Int = section.getInt("usages")

    internal val resultAsComponent: LPCRecipeComponent =
        LPCRecipeComponent(section.getConfigurationSection("result")!!.getValues(false))
    val result: ItemStack = ItemStack(
        Material.matchMaterial(section.getString("result.id")!!)!!,
        section.getInt("result.amount")
    )
        .apply {
            NBT.modify(this) { nbt ->
                nbt.mergeCompound(NBT.parseNBT(section.getString("result.nbt")!!))
            }
            itemMeta = itemMeta!!.apply { persistentDataContainer.addResultMark() }
        }

    val components: List<LPCRecipeComponent> =
        section
            .getMapList("components")
            .map { LPCRecipeComponent(it) }
}

data class LPCRecipeComponent(internal val map: Map<*, *>) {
    val material: Material = Material.matchMaterial(map["id"]!! as String)!!
    val nbt: ReadWriteNBT = NBT.parseNBT(map["nbt"]!! as String)
    val amount: Int = map["amount"]!! as Int
}


fun LingeringPotionCrafting.getRecipeConfig(): LPCRecipeConfig {
    val filename = "recipes"

    val configFile = File(dataFolder, "$filename.yml")
    val tempConfigFile = File(dataFolder, "$filename-temp.yml")
    val oldConfigFile = File(dataFolder, "$filename-old-${System.currentTimeMillis()}.yml")

    return try {
        if (!configFile.exists())
            saveResource(configFile.name, true)

        // validating current config
        val currentConfigKeys =
            YamlConfiguration.loadConfiguration(configFile)
                .getKeys(true)

        configFile.renameTo(tempConfigFile)
        saveResource(configFile.name, true)

        val defaultConfigKeys =
            YamlConfiguration.loadConfiguration(configFile)
                .getKeys(true)

        if ((defaultConfigKeys - currentConfigKeys).isNotEmpty())
            throw NullPointerException()
        else
            LPCRecipeConfig(YamlConfiguration.loadConfiguration(tempConfigFile), dataFolder)
                .also {
                    configFile.delete()
                    tempConfigFile.renameTo(configFile)
                }
    } catch (e: Exception) {
        when (e) {
            is InvalidConfigurationException, is NullPointerException -> {
                logger.severe("Invalid $name recipe config detected! Creating a new one (default)...")

                tempConfigFile.renameTo(oldConfigFile)

                logger.info("New (default) recipe config created!")

                LPCRecipeConfig(YamlConfiguration.loadConfiguration(configFile), dataFolder)
            }
            else -> throw e
        }
    }
        .also { logger.info("Recipe config loaded!") }
}
