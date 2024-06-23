package we.kotlinl0vers

import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ServerInfo
import net.minecraft.client.resource.language.I18n
import we.kotlinl0vers.command.CommandManager
import we.kotlinl0vers.module.ModuleManager
import we.kotlinl0vers.module.elements.HUD

object ArkClient {
    val version = "0.0.1"
    fun init() {
        ModuleManager.addModules()
        ModuleManager.getModule(HUD::class.java)!!.toggle()
        CommandManager.initCommands()
    }

    fun getWindowTitle() : String {
        val stringBuilder = StringBuilder("Ark Client - $version - Minecraft ")

        stringBuilder.append(SharedConstants.getGameVersion().name)
        val clientPlayNetworkHandler: ClientPlayNetworkHandler? = MinecraftClient.getInstance().getNetworkHandler()
        if (clientPlayNetworkHandler != null && clientPlayNetworkHandler.connection.isOpen) {
            stringBuilder.append(" - ")
            val serverInfo: ServerInfo? = MinecraftClient.getInstance().getCurrentServerEntry()
            if (MinecraftClient.getInstance().server != null && !MinecraftClient.getInstance().server?.isRemote()!!) {
                stringBuilder.append(I18n.translate("title.singleplayer", *arrayOfNulls(0)))
            } else if (serverInfo != null && serverInfo.isRealm) {
                stringBuilder.append(I18n.translate("title.multiplayer.realms", *arrayOfNulls(0)))
            } else if (MinecraftClient.getInstance().server == null && (serverInfo == null || !serverInfo.isLocal)) {
                stringBuilder.append(I18n.translate("title.multiplayer.other", *arrayOfNulls(0)))
            } else {
                stringBuilder.append(I18n.translate("title.multiplayer.lan", *arrayOfNulls(0)))
            }
        }

        return stringBuilder.toString()
    }
}