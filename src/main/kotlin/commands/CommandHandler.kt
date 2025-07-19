package org.cubow.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.cubow.commands.handler.GeminiCommand
import org.cubow.commands.handler.PingCommand
import entity.EmbedBuilder

class CommandHandler {

    public fun slashCommandInteractionEvent(event: SlashCommandInteractionEvent) {
        try {
            when(event.name) {
                "ping" -> {
                    PingCommand.execute(event)
                }
                "gemini" -> {
                    GeminiCommand.execute(event)
                }
                else -> {
                    // Unbekannter Command
                    val errorEmbed = EmbedBuilder()
                        .warningMessage("Unbekannter Command: ${event.name}")
                        .build()
                    event.replyEmbeds(errorEmbed).setEphemeral(true).queue()
                }
            }
        } catch (e: Exception) {
            // Fehlerbehandlung: Erstelle Error-Embed und sende es an den User
            try {
                val errorEmbed = EmbedBuilder()
                    .errorMessage("Ein Fehler ist beim Ausführen des Commands aufgetreten.\n", e)
                    .build()
                
                // Prüfe ob die Interaction bereits beantwortet wurde
                if (!event.isAcknowledged) {
                    event.replyEmbeds(errorEmbed).setEphemeral(true).queue()
                } else {
                    event.hook.editOriginalEmbeds(errorEmbed).queue()
                }
            } catch (replyException: Exception) {
                // Falls auch das Senden der Fehlermeldung fehlschlägt, logge es
                println("Fehler beim Senden der Fehlermeldung: ${replyException.message}")
                e.printStackTrace()
            }
        }
    }

}