package we.kotlinl0vers.ui.font

import com.mojang.blaze3d.platform.TextureUtil
import net.minecraft.client.texture.AbstractTexture
import net.minecraft.client.texture.NativeImage
import net.minecraft.resource.ResourceManager
import java.awt.image.BufferedImage

class DynamicTexture(private val width: Int, private val height: Int) : AbstractTexture() {
    private val textureData: NativeImage = NativeImage(NativeImage.Format.RGBA, width, height, false)

    constructor(bufferedImage: BufferedImage) : this(bufferedImage.width, bufferedImage.height) {
        for (y in 0 until bufferedImage.height) {
            for (x in 0 until bufferedImage.width) {
                val rgb = bufferedImage.getRGB(x, y)
                textureData.setColor(x, y, rgb)
            }
        }
        this.updateDynamicTexture()
    }

    init {
        TextureUtil.prepareImage(this.getGlId(), width, height)
    }

    fun updateDynamicTexture() {
        this.bindTexture()
        this.textureData.upload(0, 0, 0, false)
    }

    override fun load(manager: ResourceManager?) {
    }
}
