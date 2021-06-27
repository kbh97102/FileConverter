package com.example.fileconverter

import android.content.Context
import android.widget.Toast
import java.io.*
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder
import java.nio.charset.StandardCharsets

class Converter {

    companion object {
        @Volatile
        private var instance: Converter? = null

        @JvmStatic
        fun getInstance(): Converter =
            instance ?: synchronized(this) {
                instance ?: Converter().also {
                    instance = it
                }
            }
    }

    private lateinit var encodingList: ArrayList<Charset>

    init {
        encodingList = ArrayList()
        encodingList.add(Charsets.US_ASCII)
        encodingList.add(Charsets.UTF_16)
        encodingList.add(Charsets.UTF_16BE)
        encodingList.add(Charsets.UTF_32)
        encodingList.add(Charsets.UTF_32BE)
        encodingList.add(Charsets.UTF_32LE)
        encodingList.add(Charsets.UTF_8)
    }

    fun convertEncoding(targetEncoding: String, file: File, context: Context) {
        val fileEncoding = detectCharset(file)

        var decoder: CharsetDecoder

        fileEncoding?.let {
            decoder = fileEncoding.newDecoder()
            decoder.reset()
            val decodedText = decoder.decode(ByteBuffer.wrap(file.readBytes()))

            val encoder = StandardCharsets.UTF_8.newEncoder()
            encoder.reset()

            val result = encoder.encode(decodedText)

            val saveFile = File("./src/after.txt")

            if (!saveFile.exists()) {
                saveFile.createNewFile()
            }

            val outputStream = BufferedOutputStream(FileOutputStream(saveFile))
            outputStream.write(result.array())
            outputStream.flush()

            outputStream.close()


        } ?: run {
            Toast.makeText(context, "No supply encoding try another option", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun convertEncoding(
        targetEncoding: String,
        sourceEncoding: String,
        file: File,
        context: Context
    ) {
        var decoder: CharsetDecoder? = null


        for (type in encodingList) {
            if (type.toString() == sourceEncoding) {
                decoder = type.newDecoder()
                decoder.reset()
                break
            }
        }

        if (decoder == null) {
            Toast.makeText(context, "No offer encoding", Toast.LENGTH_LONG).show()
            return
        }

        val decodedText = decoder.decode(ByteBuffer.wrap(file.readBytes()))

        val encoder = StandardCharsets.UTF_8.newEncoder()
        encoder.reset()

        val result = encoder.encode(decodedText)

        val saveFile = File("./src/after.txt")

        if (!saveFile.exists()) {
            saveFile.createNewFile()
        }

        val outputStream = BufferedOutputStream(FileOutputStream(saveFile))
        outputStream.write(result.array())
        outputStream.flush()

        outputStream.close()
    }

    fun detectCharset(file: File): Charset? {

        val charsetList = arrayListOf<Charset>()
        charsetList.add(Charsets.US_ASCII)
        charsetList.add(Charsets.UTF_32)
        charsetList.add(Charsets.UTF_16LE)

        for (type in charsetList) {
            val input = BufferedInputStream(FileInputStream(file))
            println("Current Type $type")
            val decoder = type.newDecoder()
            decoder.reset()

            var buffer = ByteArray(file.length().toInt())
            var decoded = false
            while ((input.read(buffer) != -1) && !decoded) {
                decoded = identify(buffer, decoder)
            }

            input.close()

            if (decoded) {
                return type
            } else {
                println("Decode fail $type")
            }
        }
        return null
    }

    private fun identify(buffer: ByteArray, decoder: CharsetDecoder): Boolean {
        try {
            val charBuffer = decoder.decode(ByteBuffer.wrap(buffer))
            println(charBuffer.toString())
        } catch (e: CharacterCodingException) {
            return false
        }
        return true
    }


}