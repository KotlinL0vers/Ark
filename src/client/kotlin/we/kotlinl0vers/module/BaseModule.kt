package we.kotlinl0vers.module

import net.minecraft.client.MinecraftClient
import we.kotlinl0vers.event.EventManager

open class BaseModule(val name: String, val type: ModuleType) {
    val mc : MinecraftClient = MinecraftClient.getInstance()
    var enabled: Boolean = false

    fun onEnable(){}

    fun onDisable(){}

    fun toggle() {
        enabled =!enabled
        if (enabled) {
            onEnable()
            EventManager.register(this)
        } else {
            onDisable()
            EventManager.unregister(this)
        }
    }
}