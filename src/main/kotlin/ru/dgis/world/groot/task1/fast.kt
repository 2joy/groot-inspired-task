package ru.dgis.world.groot.task1

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

class Fast : CliktCommand(help = "Really fast implementation, but possibly useless in common cases") {
    private val input by option("-i", "--input", help = "Input file path (TXT)").required()

    private val capacity by option("-c", "--capacity", help = "The channel capacity. By default is 50000")
        .int().default(50000)

    override fun run() = measureTimeMillis {
        if (!Files.isRegularFile(Paths.get(input)))
            throw CliktError("Error: can't read input file")

        val output = createTempFile("groot-inspired-task-")
        val writer = output.bufferedWriter()

        val channel = Channel<Float>(capacity)

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