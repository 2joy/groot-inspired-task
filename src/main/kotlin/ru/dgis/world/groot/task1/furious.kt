package ru.dgis.world.groot.task1

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

class Furious : CliktCommand(help = "The classic approach to handling channels") {
    private val input by option("-i", "--input", help = "Input file path (TXT)").required()

    private val workers by option("-w", "--workers",
        help = "The number of channel consumers (the same for all channels). By default is 50000")
        .int().default(50000)

    private val capacity by option("-c", "--capacity", help = "Channels capacity. By default is 50000")
        .int().default(50000)

    override fun run() = measureTimeMillis {
        if (!Files.isRegularFile(Paths.get(input)))
            throw CliktError("Error: can't read input file")

        val output = createTempFile("groot-inspired-task-furious-")
        val writer = output.bufferedWriter()

        val channelY = Channel<Float>(capacity)
        val channelG = Channel<Float>(capacity)
        val channelH = Channel<Float>(capacity)

        var processed = 0
        runBlocking {
            launch {
                File(input).useLines { lines ->
                    lines.forEach { x ->
                        channelY.send(x.toFloat())
                    }
                }
                channelY.close()
            }

            // Y
            val request = launch {
                fun workerY() = launch {
                    for (x in channelY) {
                        val y = y(x)
                        channelH.send(y)
                        channelG.send(y)
                    }
                }
                repeat(workers) {
                    workerY()
                }
            }

            // G
            fun workerG() = launch {
                for (y in channelG) {
                    g(y)

                    processed++
                    if (processed % 10000 == 0)
                        print("\rProcessed: $processed")
                }
            }
            repeat(workers) {
                workerG()
            }

            // H
            fun workerH() = launch {
                for (y in channelH) {
                    h(y, writer)
                }
            }
            repeat(workers) {
                workerH()
            }

            // Wait for completion of the request, including all its children
            request.join()
            channelH.close()
            channelG.close()
        }
        println("\rProcessed: $processed")
        writer.flush()
        writer.close()
    }
        .run { println("Duration: $this") }
}