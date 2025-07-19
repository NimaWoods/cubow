package handler

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Properties as JavaProperties

class Properties private constructor() {
    private val props: JavaProperties = JavaProperties()
    private val configFileName = "config.properties"

    init {
        val configPropsFile = File(configFileName)
        if (!configPropsFile.exists()) {
            // Lade Defaults aus Ressourcen mit UTF-8
            val defaultPropsStream = javaClass.getResourceAsStream("/default.properties")
            defaultPropsStream?.use { stream ->
                InputStreamReader(stream, Charsets.UTF_8).use { reader ->
                    props.load(reader)
                }
            }
            // Schreibe Defaults in config.properties (UTF-8, Format erhalten)
            OutputStreamWriter(FileOutputStream(configPropsFile), Charsets.UTF_8).use { out ->
                val lines = javaClass.getResourceAsStream("/default.properties")?.bufferedReader(Charsets.UTF_8)?.readLines()
                lines?.forEach { out.write(it + "\n") }
            }
        } else {
            InputStreamReader(FileInputStream(configPropsFile), Charsets.UTF_8).use { reader ->
                props.load(reader)
            }
        }
    }

    fun get(key: String): String? = props.getProperty(key)
    fun getOrDefault(key: String, default: String): String = props.getProperty(key, default)
    fun asMap(): Map<String, String> = props.stringPropertyNames().associateWith { props.getProperty(it) }

    fun set(key: String, value: String) {
        props.setProperty(key, value)
        savePreservingFormat()
    }

    private fun savePreservingFormat() {
        val file = File(configFileName)
        if (!file.exists()) return
        val lines = file.readLines().toMutableList()
        val keySet = props.stringPropertyNames().toSet()
        val updatedKeys = mutableSetOf<String>()
        val regex = Regex("""^([ \t]*)([^#!\s][^=\s]*)[ \t]*=[ \t]*(.*)""")
        for (i in lines.indices) {
            val match = regex.matchEntire(lines[i])
            if (match != null) {
                val key = match.groupValues[2]
                if (props.containsKey(key)) {
                    lines[i] = "${match.groupValues[1]}$key=${props.getProperty(key)}"
                    updatedKeys.add(key)
                }
            }
        }
        // Neue Keys anhängen (falls vorhanden)
        val missingKeys = keySet - updatedKeys
        if (missingKeys.isNotEmpty()) {
            if (lines.isNotEmpty() && lines.last().isNotBlank()) lines.add("")
            for (key in missingKeys) {
                lines.add("$key=${props.getProperty(key)}")
            }
        }
        OutputStreamWriter(FileOutputStream(file), Charsets.UTF_8).use { writer ->
            writer.write(lines.joinToString("\n"))
        }
    }

    companion object {
        @Volatile
        private var instance: Properties? = null
        fun getInstance(): Properties = instance ?: synchronized(this) {
            instance ?: Properties().also { instance = it }
        }
    }
}