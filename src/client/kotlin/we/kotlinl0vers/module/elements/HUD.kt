package we.kotlinl0vers.module.elements

import we.kotlinl0vers.event.EventTarget
import we.kotlinl0vers.event.events.EventRender2D
import we.kotlinl0vers.module.BaseModule
import we.kotlinl0vers.module.ModuleType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HUD : BaseModule("HUD", ModuleType.Player) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    @EventTarget
    fun onUpdate(e : EventRender2D){
        e.drawContext.drawTextWithShadow(mc.textRenderer, "Ark Client \u00A77[\u00A7f"+LocalTime.now().format(formatter).toString()+"\u00A77]", 1, 1, 0xffffff)
    }
}