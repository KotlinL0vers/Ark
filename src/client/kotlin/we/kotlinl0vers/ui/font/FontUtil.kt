package we.kotlinl0vers.ui.font

import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.awt.Font

class FontUtil {
    companion object {
        fun getFontFromTTF(fontLocation: Identifier?, fontSize: Int, fontType: Int): Font? {
            var output: Font? = Font.getFont("Tahoma")
            try {
                val resource = MinecraftClient.getInstance().getResourceManager().getResource(fontLocation).orElseThrow {
                    IllegalArgumentException("Font resource not found: $fontLocation")
                }
                output = Font.createFont(
                    fontType,
                    resource.inputStream
                )
                output = output.deriveFont(fontSize.toFloat())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return output
        }
    }
}
