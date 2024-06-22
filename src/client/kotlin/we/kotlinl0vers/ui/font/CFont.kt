package we.kotlinl0vers.ui.font

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

open class CFont(
    var font: Font?,
    var antiAlias: Boolean,
    var fractionalMetrics: Boolean
) {
    private val imgSize = 512f
    protected val charData : Array<CharData?> = Array(256) { CharData() }
    private var fontHeight = -1
    var charOffset = 0
    var tex: DynamicTexture? = null

    init {
        tex = font?.let { setupTexture(it, antiAlias, fractionalMetrics, charData) }
    }

    fun setupTexture(font: Font, antiAlias: Boolean, fractionalMetrics: Boolean, chars: Array<CharData?>): DynamicTexture? {
        val img = generateFontImage(font, antiAlias, fractionalMetrics, chars)
        return try {
            DynamicTexture(img)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun generateFontImage(font: Font, antiAlias: Boolean, fractionalMetrics: Boolean, chars: Array<CharData?>): BufferedImage {
        val imgSize = imgSize.toInt()
        val bufferedImage = BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB)
        val g = bufferedImage.graphics as Graphics2D
        g.font = font
        g.color = Color(255, 255, 255, 0)
        g.fillRect(0, 0, imgSize, imgSize)
        g.color = Color.WHITE
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, if (fractionalMetrics) RenderingHints.VALUE_FRACTIONALMETRICS_ON else RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, if (antiAlias) RenderingHints.VALUE_TEXT_ANTIALIAS_ON else RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, if (antiAlias) RenderingHints.VALUE_ANTIALIAS_ON else RenderingHints.VALUE_ANTIALIAS_OFF)
        val fontMetrics = g.fontMetrics
        var charHeight = 0
        var positionX = 0
        var positionY = 1

        for (i in chars.indices) {
            val ch = i.toChar()
            val charData = CharData()
            val dimensions = fontMetrics.getStringBounds(ch.toString(), g)
            charData.width = dimensions.bounds.width + 8
            charData.height = dimensions.bounds.height

            if (positionX + charData.width >= imgSize) {
                positionX = 0
                positionY += charHeight
                charHeight = 0
            }

            if (charData.height > charHeight) {
                charHeight = charData.height
            }

            charData.storedX = positionX
            charData.storedY = positionY

            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height
            }

            chars[i] = charData
            g.drawString(ch.toString(), positionX + 2, positionY + fontMetrics.ascent)
            positionX += charData.width
        }

        return bufferedImage
    }

    fun drawChar(matrices: MatrixStack, chars: Array<CharData?>, c: Char, x: Float, y: Float) {
        try {
            chars[c.toInt()]?.let { charData ->
                drawQuad(
                    matrices, x, y,
                    charData.width.toFloat(), charData.height.toFloat(),
                    charData.storedX.toFloat(), charData.storedY.toFloat(),
                    charData.width.toFloat(), charData.height.toFloat()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawQuad(matrices: MatrixStack, x: Float, y: Float, width: Float, height: Float, srcX: Float, srcY: Float, srcWidth: Float, srcHeight: Float) {
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL)

        val renderSRCX = srcX / imgSize
        val renderSRCY = srcY / imgSize
        val renderSRCWidth = srcWidth / imgSize
        val renderSRCHeight = srcHeight / imgSize

        // 添加所有必要的顶点属性
        bufferBuilder.vertex(matrices.peek().positionMatrix, x + width, y, 0f)
            .color(255, 255, 255, 255)
            .texture(renderSRCX + renderSRCWidth, renderSRCY)
            .light(0xF000F0) // 添加光照
            .normal(1f, 0f, 0f) // 添加法线

        bufferBuilder.vertex(matrices.peek().positionMatrix, x, y, 0f)
            .color(255, 255, 255, 255)
            .texture(renderSRCX, renderSRCY)
            .light(0xF000F0) // 添加光照
            .normal(1f, 0f, 0f) // 添加法线

        bufferBuilder.vertex(matrices.peek().positionMatrix, x, y + height, 0f)
            .color(255, 255, 255, 255)
            .texture(renderSRCX, renderSRCY + renderSRCHeight)
            .light(0xF000F0) // 添加光照
            .normal(1f, 0f, 0f) // 添加法线

        bufferBuilder.vertex(matrices.peek().positionMatrix, x + width, y + height, 0f)
            .color(255, 255, 255, 255)
            .texture(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight)
            .light(0xF000F0) // 添加光照
            .normal(1f, 0f, 0f) // 添加法线

        BufferRenderer.draw(bufferBuilder.end())
    }

    fun getStringHeight(text: String): Int {
        return getHeight()
    }

    fun getHeight(): Int {
        return (fontHeight - 8) / 2
    }

    class CharData {
        var width = 0
        var height = 0
        var storedX = 0
        var storedY = 0
    }
}
