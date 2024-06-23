package we.kotlinl0vers.command

import we.kotlinl0vers.command.commands.HelpCommand

object CommandManager {
    //create a list to store all the commands
    val commands = ArrayList<BaseCommand>()

    fun initCommands(){
        commands.add(HelpCommand())
    }

    //this function will be called from a chat message listener
    fun handleMessage(message: String): Boolean {
        //check if the message starts with a command prefix and remove it
        val command = message.removePrefix("#")

        //check if the command exists in the list of commands
        val commandObj = commands.find { it.name == command }

        //if the command exists, execute it
        if (commandObj != null) {
            //trans the message to a list of arguments
            val args = message.removePrefix(command).trim().split(" ")
            commandObj.execute(args)
            return true
        }

        //if the command doesn't exist, return false
        return false
    }
}