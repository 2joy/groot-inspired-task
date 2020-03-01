package ru.dgis.world.groot.task1

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import kotlinx.coroutines.delay
import java.io.BufferedWriter

class Cli : CliktCommand(
    name = "groot-inspired-task",
    help = "The input file must contain real numbers, each on a separate line. " +
            "The output file will be created in a temporary directory and is not of interest. " +
            "The essence of the task is to minimize execution time.",
    printHelpOnEmptyArgs = true
) {
    override fun run() = Unit
}

suspend fun y(x: Float): Float {
    delay(3000)
    return x * 2
}

fun g(y: Float) = false

fun h(y: Float, writer: BufferedWriter) = writer.appendln(y.toString())

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = Cli().subcommands(Fast(), Furious()).main(args)
    }
}