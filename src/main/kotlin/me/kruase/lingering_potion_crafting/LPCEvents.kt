package me.kruase.lingering_potion_crafting

import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.instance
import me.kruase.lingering_potion_crafting.LingeringPotionCrafting.Companion.mainConfig
import me.kruase.lingering_potion_crafting.logic.EffectCloudData
import me.kruase.lingering_potion_crafting.logic.data
import me.kruase.lingering_potion_crafting.logic.recipeTick
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent


class LPCEvents : Listener {
    @EventHandler
    fun onEffectCloudSpawn(event: EntitySpawnEvent) {
        if (event.entityType != EntityType.AREA_EFFECT_CLOUD)
            return

        (event.entity as AreaEffectCloud)
            .run {
                data = EffectCloudData(
                    instance.server.scheduler.scheduleSyncRepeatingTask(
                        instance,
                        { recipeTick() },
                        0,
                        mainConfig.craftPeriodicityTicks
                    )
                )
            }
    }
}
