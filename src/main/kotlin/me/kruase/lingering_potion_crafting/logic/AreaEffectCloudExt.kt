package me.kruase.lingering_potion_crafting.logic

import me.kruase.lingering_potion_crafting.LPCRecipe
import me.kruase.lingering_potion_crafting.LPCRecipeComponent
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.instance
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.mainConfig
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.recipeConfig
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.trackedEffectClouds
import me.kruase.lingering_potion_crafting.util.hasResultMark
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.Item


fun AreaEffectCloud.recipeTick() {
    if (!isValid) {
        data = null
        return
    }

    data?.recipe
        .also { recipe ->
            if (recipe != null) {
                if (recipeConfig.recipes[recipe.name]?.isEnabled != true)
                    return // if configuration has been reloaded during area_effect_cloud entity existence

                if (applyRecipe(recipe))
                    updateAfterApply(recipe)
            } else {
                recipeConfig.recipes.values
                    .filter { it.isEnabled }
                    .firstOrNull { basePotionType in it.effects && applyRecipe(it) }
                    ?.let { appliedRecipe ->
                        data!!.recipe = appliedRecipe

                        radiusPerTick = 0F
                        radiusOnUse = 0F

                        updateAfterApply(appliedRecipe)
                    }
            }
        }

    if (data?.remainingUsageCount == 0) {
        data = null
        remove()
    }
}


fun AreaEffectCloud.applyRecipe(recipe: LPCRecipe): Boolean {  // returns true if the recipe was indeed applied
    val componentMap: MutableMap<LPCRecipeComponent, Item?> = recipe.components.associateWith { null }.toMutableMap()

    getNearbyEntities(radius.toDouble(), 1.0, radius.toDouble())
        .filter { it is Item && it.itemStack.itemMeta?.persistentDataContainer?.hasResultMark() == false }
        .forEach { entity ->
            (entity as Item)
                .let { item ->
                    componentMap
                        .filterValues { it == null }
                        .keys
                        .firstOrNull { component -> item.matches(component) }
                        ?.let { matchedComponent -> componentMap[matchedComponent] = item }
                }
        }

    componentMap.values
        .let { items ->
            if (items.all { it != null }) {
                val location = items.first()!!.location

                location.world?.dropItemNaturally(location, recipe.result)
                    ?: throw IllegalStateException(
                        mainConfig.messages.error["unable-to-drop-item"] ?: "Error: unable-to-drop-item"
                    )

                items.forEach { item -> item!!.remove() }

                return true
            }

            return false
        }
}


fun AreaEffectCloud.updateAfterApply(recipe: LPCRecipe) {
    data!!
        .let { dataSnapshot ->
            data!!.remainingUsageCount = (dataSnapshot.remainingUsageCount ?: recipe.usageCount) - 1

            radius = mainConfig.cloudInitialRadius / recipe.usageCount * dataSnapshot.remainingUsageCount!!

            duration = mainConfig.cloudLifetimeTicks
            ticksLived = 1 // can't be zero...
        }
}


var AreaEffectCloud.data: EffectCloudData?
    get() = trackedEffectClouds[uniqueId]
    set(value) {
        if (value == null) {
            trackedEffectClouds[uniqueId]?.taskId
                ?.let { taskId -> instance.server.scheduler.cancelTask(taskId) }
            trackedEffectClouds.remove(uniqueId)
        }
        else
            trackedEffectClouds[uniqueId] = value
    }


data class EffectCloudData(
    val taskId: Int,
    var recipe: LPCRecipe? = null,
    var remainingUsageCount: Int? = null,
)
