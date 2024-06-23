package we.kotlinl0vers.module.elements

import we.kotlinl0vers.event.EventTarget
import we.kotlinl0vers.event.events.EventRender2D
import we.kotlinl0vers.module.BaseModule
import we.kotlinl0vers.module.ModuleType

class HUD : BaseModule("HUD", ModuleType.Player) {
    @EventTarget
    fun onUpdate(e : EventRender2D){
        e.drawContext.drawTextWithShadow(mc.textRenderer, "Ark Client", 1, 1, 0xffffff)
    }
}