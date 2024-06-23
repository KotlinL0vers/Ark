package we.kotlinl0vers.mixin.client;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import we.kotlinl0vers.command.CommandManager;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(SignedMessage message, MessageType.Parameters params, CallbackInfo ci) {
        if(CommandManager.INSTANCE.handleMessage(message.getContent().getString())){
           ci.cancel();
        }
    }
}
