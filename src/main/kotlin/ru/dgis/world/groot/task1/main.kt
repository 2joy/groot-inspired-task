package ru.dgis.world.groot.task1

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.*
import java.io.BufferedWriter
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

class Cli : CliktCommand(
    name = "groot-inspired-task",
    help = "The input file must contain real numbers, each on a separate line. " +
            "The output file will be created in a temporary directory and is not of interest. " +
            "The essence of the task is to minimize execution time.",
    printHelpOnEmptyArgs = true
) {
    private val input by option("-i", "--input", help = "Input file path (TXT)").required()

    override fun run() = measureTimeMillis {
        if (!Files.isRegularFile(Paths.get(input)))
            throw CliktError("Error: can't read input file")

        val output = createTempFile("groot-inspired-task-")
        val writer = output.bufferedWriter()

        val channel = Channel<Float>(50000)

        var processed = 0
        runBlocking {
            launch {
                File(input).useLines { lines ->
                    lines.forEach { x ->
                        channel.send(x.toFloat())
                    }
                }
                channel.close()
            }

            @UseExperimental(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
            channel.consumeEach { x ->
                launch {
                    val y = y(x)
                    g(y)
                    h(y, writer)

                    processed++
                    if (processed % 10000 == 0)
                        print("\rProcessed: $processed")
                }
            }
        }
        println("\rProcessed: $processed")
        writer.flush()
        writer.close()
    }
        .run { println("Duration: $this") }
}

suspend fun y(x: Float): Float {
    delay(300)
    return x * 2
}

fun g(y: Float) = false

fun h(y: Float, writer: BufferedWriter) = writer.appendln(y.toString())

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = Cli().main(args)
    }
}