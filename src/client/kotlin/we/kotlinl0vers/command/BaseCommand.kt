package we.kotlinl0vers.command

open class BaseCommand(val name: String, val description: String, val usage: String) {
    open fun execute(args: List<String>) {}
}