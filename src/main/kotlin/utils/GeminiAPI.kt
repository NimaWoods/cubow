package org.cubow.utils

import handler.Properties
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object GeminiAPI {
    
    fun getResponse(prompt: String): String {
        return responseMessage(fetchResponse(prompt))
    }

    private fun fetchResponse(prompt: String): String {

        val apiKey: String = Properties.getInstance().get("gemini_api_key") ?: throw IllegalStateException("gemini_api_key ist nicht gesetzt")
        val geminiSystemPrompt: String = Properties.getInstance().get("gemini_system_prompt") ?: throw IllegalStateException("gemini_system_prompt ist nicht gesetzt")
        val rules: String = Properties.getInstance().get("rules") ?: throw IllegalStateException("rules sind nicht gesetzt")

        val url: URL =
            URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
        val http: HttpURLConnection = url.openConnection() as HttpURLConnection
        http.setRequestMethod("POST")
        http.setDoOutput(true)
        http.setRequestProperty("Content-Type", "application/json")

        val data = """
                {
                  "system_instruction": {
                    "parts": [
                      {
                        "text": "$geminiSystemPrompt\\n\\nHier sind die Serverregeln:\\n$rules\\n\\n"
                      }
                    ]
                  },
                  "contents": [
                    {
                      "parts": [
                        {
                            "text": "$prompt"
                        }
                      ]
                    }
                  ]
                }
            """.trimIndent()

        val out: ByteArray = data.toByteArray(Charsets.UTF_8)

        val stream: OutputStream = http.getOutputStream()
        stream.write(out)
        stream.close()

        val responseCode = http.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            http.disconnect()
            throw RuntimeException("HTTP error code: $responseCode")
        }

        val response = http.inputStream.bufferedReader().use { it.readText() }
        http.disconnect()
        return response
    }

    private fun responseMessage(jsonString: String): String {
        try {
            if (jsonString.isEmpty()) {
                return "Error: Empty response received"
            }

            val jsonObject = JSONObject(jsonString)
            val candidates = jsonObject.getJSONArray("candidates")
            if (candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")

                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text")
                }
            }

            return "No response found"
        } catch (e: Exception) {
            return "Error parsing response: ${e.message}"
        }
    }
    
}