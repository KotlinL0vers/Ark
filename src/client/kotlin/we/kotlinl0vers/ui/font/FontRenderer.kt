package we.kotlinl0vers.ui.font

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.Font
import kotlin.math.max

class FontRenderer : CFont {
    protected var boldChars: Array<CharData?> = arrayOfNulls<CharData>(256)
    protected var italicChars: Array<CharData?> = arrayOfNulls<CharData>(256)
    protected var boldItalicChars: Array<CharData?> = arrayOfNulls<CharData>(256)

    private val colorCode = IntArray(32)

    constructor(font: Font, antiAlias: Boolean, fractionalMetrics: Boolean) : super(
        font,
        antiAlias,
        fractionalMetrics
    ) {
        setupMinecraftColorcodes()
        setupBoldItalicIDs()
    }

    constructor(NameFontTTF: String, size: Int, fonttype: Int, antiAlias: Boolean, fractionalMetrics: Boolean) : super(
        FontUtil.getFontFromTTF(
            Identifier.of("arkclient",
                "${NameFontTTF.lowercase()}.ttf"
            ), size, fonttype
        ), antiAlias, fractionalMetrics
    ) {
        setupMinecraftColorcodes()
        setupBoldItalicIDs()
    }

    fun drawString(matrixStack: MatrixStack, text: String?, x: Float, y: Float, color: Int): Float {
        return drawString(matrixStack,text, x.toDouble(), y.toDouble(), color, false)
    }

    fun drawStringWithShadow(matrixStack: MatrixStack, text: String?, x: Double, y: Double, color: Int): Float {
        matrixStack.push()
        val shadowWidth = drawString(matrixStack, text, x + 1.0, y + 0.5, color, true)
        val mainWidth = drawString(matrixStack, text, x, y, color, false)
        matrixStack.pop()
        return maxOf(shadowWidth, mainWidth)
    }

    fun drawCenteredString(matrixStack: MatrixStack, text: String?, x: Float, y: Float, color: Int): Float {
        return drawString(matrixStack, text, x - getStringWidth(text) / 2.0f, y, color)
    }

    fun drawCenteredString(matrixStack: MatrixStack, text: String?, x: Double, y: Double, color: Int): Float {
        return drawString(matrixStack, text, x - getStringWidth(text) / 2.0, y, color)
    }

    fun drawStringWithOutline(matrixStack: MatrixStack, text: String?, x: Double, y: Double, color: Int) {
        drawString(matrixStack, text, x - 0.5, y, 0x000000)
        drawString(matrixStack, text, x + 0.5, y, 0x000000)
        drawString(matrixStack, text, x, y - 0.5, 0x000000)
        drawString(matrixStack, text, x, y + 0.5, 0x000000)
        drawString(matrixStack, text, x, y, color)
    }

    fun drawCenteredStringWithOutline(matrixStack: MatrixStack, text: String?, x: Double, y: Double, color: Int) {
        drawCenteredString(matrixStack, text, x - 0.5, y, 0x000000)
        drawCenteredString(matrixStack, text, x + 0.5, y, 0x000000)
        drawCenteredString(matrixStack, text, x, y - 0.5, 0x000000)
        drawCenteredString(matrixStack, text, x, y + 0.5, 0x000000)
        drawCenteredString(matrixStack, text, x, y, color)
    }

    fun drawCenteredStringWithShadow(matrixStack: MatrixStack, text: String?, x: Double, y: Double, color: Int): Float {
        matrixStack.push()
        val shadowWidth = drawString(matrixStack, text, x - getStringWidth(text) / 2.0 + 0.45, y + 0.5, color, true)
        val mainWidth = drawString(matrixStack, text, x - getStringWidth(text) / 2.0, y, color, false)
        matrixStack.pop()
        return maxOf(shadowWidth, mainWidth)
    }

    @JvmOverloads
    fun drawString(matrixStack: MatrixStack, text: String?, x: Double, y: Double, color: Int, shadow: Boolean = false): Float {
        var x = x
        var y = y
        var color = color
        val mc: MinecraftClient = MinecraftClient.getInstance()
        x -= 1.0

        if (text == null) {
            return 0.0f
        }

        if (color == 553648127) {
            color = 16777215
        }

        if ((color and -0x4000000) == 0) {
            color = color or -16777216
        }

        if (shadow) {
            color = (color and 0xFCFCFC) shr 2 or (color and Color(20, 20, 20, 200).rgb)
        }

        var currentData: Array<CharData?> = this.charData
        val alpha = (color shr 24 and 0xFF) / 255.0f
        var randomCase = false
        var bold = false
        var italic = false
        var strikethrough = false
        var underline = false
        val render = true
        x *= 2.0
        y = (y - 3.0) * 2.0

        if (render) {
            RenderSystem.assertOnRenderThread()
            RenderSystem.enableBlend()
            RenderSystem.blendFunc(770, 771)
            RenderSystem.setShader(GameRenderer::getPositionColorProgram)
            RenderSystem.setShaderColor(
                (color shr 16 and 0xFF) / 255.0f,
                (color shr 8 and 0xFF) / 255.0f,
                (color and 0xFF) / 255.0f, alpha
            )
            matrixStack.push()
            matrixStack.scale(0.5f, 0.5f, 0.5f)
            //GL11.glPushMatrix()
            //GL11.glScalef(0.5f, 0.5f, 0.5f)

            val size = text.length
            //GL11.glEnable(GL11.GL_TEXTURE_2D)
            RenderSystem.bindTexture(tex!!.glId)

            //GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex!!.glId)

            var i = 0
            while (i < size) {
                val character = text[i]
                if ((character.toString() == "\u00a7") && (i < size)) {
                    var colorIndex = 21

                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text[i + 1])
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    if (colorIndex < 16) {
                        bold = false
                        italic = false
                        randomCase = false
                        underline = false
                        strikethrough = false
                        RenderSystem.bindTexture(tex!!.glId)
                        // GL11.glfunc_179144_i(GL11.GL_TEXTURE_2D,
                        // tex.getGlTextureId());
                        currentData = this.charData

                        if ((colorIndex < 0) || (colorIndex > 15)) {
                            colorIndex = 15
                        }

                        if (shadow) {
                            colorIndex += 16
                        }

                        val colorcode = colorCode[colorIndex]
                        RenderSystem.setShaderColor(
                            (colorcode shr 16 and 0xFF) / 255.0f,
                            (colorcode shr 8 and 0xFF) / 255.0f,
                            (colorcode and 0xFF) / 255.0f, alpha
                        )
                    } else if (colorIndex == 16) {
                        randomCase = true
                    } else if (colorIndex == 17) {
                        bold = true

                        if (italic) {
                            RenderSystem.bindTexture(texItalicBold!!.glId)
                            // GL11.glfunc_179144_i(GL11.GL_TEXTURE_2D,
                            // texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars
                        } else {
                            RenderSystem.bindTexture(texBold!!.glId)
                            // GL11.glfunc_179144_i(GL11.GL_TEXTURE_2D,
                            // texBold.getGlTextureId());
                            currentData = this.boldChars
                        }
                    } else if (colorIndex == 18) {
                        strikethrough = true
                    } else if (colorIndex == 19) {
                        underline = true
                    } else if (colorIndex == 20) {
                        italic = true

                        if (bold) {
                            RenderSystem.bindTexture(texItalicBold!!.glId)
                            // GL11.glfunc_179144_i(GL11.GL_TEXTURE_2D,
                            // texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars
                        } else {
                            RenderSystem.bindTexture(texItalic!!.glId)
                            // GL11.glfunc_179144_i(GL11.GL_TEXTURE_2D,
                            // texItalic.getGlTextureId());
                            currentData = this.italicChars
                        }
                    } else if (colorIndex == 21) {
                        bold = false
                        italic = false
                        randomCase = false
                        underline = false
                        strikethrough = false
                        RenderSystem.setShaderColor(
                            (color shr 16 and 0xFF) / 255.0f,
                            (color shr 8 and 0xFF) / 255.0f,
                            (color and 0xFF) / 255.0f, alpha
                        )
                        RenderSystem.bindTexture(tex!!.glId)
                        // GL11.glfunc_179144_i(GL11.GL_TEXTURE_2D,
                        // tex.getGlTextureId());
                        currentData = this.charData
                    }

                    i++
                } else if ((character.code < currentData.size) && (character.code >= 0)) {
//                    GL11.glBegin(GL11.GL_TRIANGLES)

                    drawChar(matrixStack,currentData, character, x.toFloat(), y.toFloat())
//                    GL11.glEnd()

                    if (strikethrough) {
                        currentData[character.code]?.let { data ->
                            drawLine(
                                x,
                                y + data.height / 2,
                                x + data.width - 8.0,
                                y + data.height / 2,
                                1.0f
                            )
                        }
                    }

                    if (underline) {
                        currentData[character.code]?.let { data ->
                            drawLine(
                                x,
                                y + data.height - 2.0,
                                x + data.width - 8.0,
                                y + data.height - 2.0,
                                1.0f
                            )
                        }
                    }


                    x += currentData[character.code]!!.width - 8 + this.charOffset
                }
                i++
            }

//            GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE)
//            GL11.glPopMatrix()
            matrixStack.pop()
        }

        return x.toFloat() / 2.0f
    }

    fun getStringWidth(text: String?): Int {
        if (text == null) {
            return 0
        }
        var width = 0
        var currentData: Array<CharData?> = this.charData
        var bold = false
        var italic = false
        val size = text.length

        var i = 0
        while (i < size) {
            val character = text[i]

            if ((character.toString() == "\u00a7") && (i < size)) {
                val colorIndex = "0123456789abcdefklmnor".indexOf(character)

                if (colorIndex < 16) {
                    bold = false
                    italic = false
                } else if (colorIndex == 17) {
                    bold = true

                    currentData = if (italic) {
                        boldItalicChars
                    } else {
                        boldChars
                    }
                } else if (colorIndex == 20) {
                    italic = true

                    currentData = if (bold) {
                        boldItalicChars
                    } else {
                        italicChars
                    }
                } else if (colorIndex == 21) {
                    bold = false
                    italic = false
                    currentData = this.charData
                }

                i++
            } else if ((character.code < currentData.size) && (character.code >= 0)) {
                width += currentData[character.code]!!.width - 8 + this.charOffset
            }
            i++
        }

        return width / 2
    }

    fun getStringWidthCust(text: String?): Int {
        if (text == null) {
            return 0
        }

        var width = 0
        var currentData: Array<CharData?> = this.charData
        var bold = false
        var italic = false
        val size = text.length

        var i = 0
        while (i < size) {
            val character = text[i]

            if ((character.toString() == "§") && (i < size)) {
                val colorIndex = "0123456789abcdefklmnor".indexOf(character)

                if (colorIndex < 16) {
                    bold = false
                    italic = false
                } else if (colorIndex == 17) {
                    bold = true

                    currentData = if (italic) {
                        boldItalicChars
                    } else {
                        boldChars
                    }
                } else if (colorIndex == 20) {
                    italic = true

                    currentData = if (bold) {
                        boldItalicChars
                    } else {
                        italicChars
                    }
                } else if (colorIndex == 21) {
                    bold = false
                    italic = false
                    currentData = this.charData
                }

                i++
            } else if ((character.code < currentData.size) && (character.code >= 0)) {
                width += (currentData[character.code]?.width ?: 0) - 8 + this.charOffset
            }
            i++
        }

        return (width - this.charOffset) / 2
    }

    protected var texBold: DynamicTexture? = null
    protected var texItalic: DynamicTexture? = null
    protected var texItalicBold: DynamicTexture? = null

    private fun setupBoldItalicIDs() {
        texBold = setupTexture(this.font!!.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars)
        texItalic = setupTexture(this.font!!.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars)
        texItalicBold =
            setupTexture(this.font!!.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars)
    }

    private fun drawLine(x: Double, y: Double, x1: Double, y1: Double, width: Float) {
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glLineWidth(width)
        GL11.glBegin(GL11.GL_LINES)
        GL11.glVertex2d(x, y)
        GL11.glVertex2d(x1, y1)
        GL11.glEnd()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
    }
    private fun setupMinecraftColorcodes() {
        for (index in 0..31) {
            val noClue = (index shr 3 and 0x1) * 85
            var red = (index shr 2 and 0x1) * 170 + noClue
            var green = (index shr 1 and 0x1) * 170 + noClue
            var blue = (index shr 0 and 0x1) * 170 + noClue

            if (index == 6) {
                red += 85
            }

            if (index >= 16) {
                red /= 4
                green /= 4
                blue /= 4
            }

            colorCode[index] = ((red and 0xFF) shl 16 or ((green and 0xFF) shl 8) or (blue and 0xFF))
        }
    }
}