package me.kruase.kotlin_plugin_template.util

import java.util.logging.Logger


fun Logger.infoNotNull(message: String?) {
    message?.let { info(it) }
}

fun Logger.warnNotNull(message: String?) {
    message?.let { warning(it) }
}
