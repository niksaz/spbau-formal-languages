package ru.spbau.mit

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths

class LTests {
    @Test
    fun testIfLang() {
        verifyFor("if.lang", "if.ast")
    }

    @Test
    fun testPrecedenceLang() {
        verifyFor("precedence.lang", "precedence.ast")
    }

    @Test
    fun testProcLang() {
        verifyFor("proc.lang", "proc.ast")
    }

    private fun verifyFor(langFileName: String, astFileName: String) {
        val byteOutputStream = ByteArrayOutputStream()
        val printStream = PrintStream(byteOutputStream, true)
        parseAndPrintAst(testResourcesPath.resolve(langFileName).toString(), printStream)
        val bytes = byteOutputStream.toByteArray()

        val expectedBytes = Files.readAllBytes(testResourcesPath.resolve(astFileName))
        assertThat(bytes).isEqualTo(expectedBytes)

    }

    companion object {
        val testResourcesPath = Paths.get("src", "test", "resources")!!
    }
}