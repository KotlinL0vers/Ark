package we.kotlinl0vers.event.events

import net.minecraft.client.gui.DrawContext
import we.kotlinl0vers.event.EventBase

data class EventRender2D(val drawContext: DrawContext) : EventBase() {
}