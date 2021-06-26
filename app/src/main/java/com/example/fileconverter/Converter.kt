package com.example.fileconverter

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder

class Converter {

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