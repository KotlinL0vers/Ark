package we.kotlinl0vers.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import we.kotlinl0vers.ui.font.Fonts;

@Mixin(value = TitleScreen.class)
public class MixinTitleScreen {
    @Inject(method = "render", at = @At("RETURN"))
    private void render(DrawContext dc,int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Add your code here
        Fonts.INSTANCE.getArial().drawString(dc.getMatrices(), "Hello, world!", 0, 0, -1);
    }
}
