package we.kotlinl0vers.mixin.client;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import we.kotlinl0vers.ArkClient;
import we.kotlinl0vers.Rukiddingme;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
	@Inject(at = @At("RETURN"), method = "updateWindowTitle")
	private void init(CallbackInfo ci) {
		MinecraftClient.getInstance().getWindow().setTitle(ArkClient.INSTANCE.getWindowTitle());
	}

	@Inject(at = @At("HEAD"), method = "onFinishedLoading")
	private void startClient(CallbackInfo ci) {
		ArkClient.INSTANCE.init();
	}
}