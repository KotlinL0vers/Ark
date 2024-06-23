package we.kotlinl0vers.command.commands

import we.kotlinl0vers.command.BaseCommand
import we.kotlinl0vers.command.CommandManager
import we.kotlinl0vers.utils.ChatUtil

class HelpCommand : BaseCommand("help", "Displays help information for the client", "#help") {
    override fun execute(args: List<String>) {
        println("Available commands:")
        for (command in CommandManager.commands) {
            ChatUtil.sendChatMessage(command.name + " - " + command.description + " - " + command.usage)
        }
    }
}