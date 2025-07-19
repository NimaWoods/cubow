package org.cubow.commands.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.cubow.commands.`interface`.CommandInterface
import org.cubow.utils.GeminiAPI


object GeminiCommand : CommandInterface {
    override fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()

        val prompt: String? = event.getOption("prompt")?.asString

        if (prompt != null) {
            val responseMessage = GeminiAPI.getResponse(prompt)
            if (responseMessage.isNotEmpty()) {
                event.hook.editOriginal(responseMessage).queue()
            }
        } else {
            throw IllegalArgumentException("No prompt given")
        }
    }
}