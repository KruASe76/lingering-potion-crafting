package me.kruase.lingering_potion_crafting.util

import java.util.logging.Logger


fun Logger.infoNotNull(message: String?) {
    message?.let { info(it) }
}

fun Logger.warnNotNull(message: String?) {
    message?.let { warning(it) }
}
