package we.kotlinl0vers.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object ChatUtil {
    fun sendChatMessage(message: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.of("\u00A7b[Ark Client] \u00A7f"+message));
    }
}