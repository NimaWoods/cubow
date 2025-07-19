package org.cubow.commands.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.cubow.commands.`interface`.CommandInterface

object PingCommand: CommandInterface {
    override fun execute(event: SlashCommandInteractionEvent) {
        event.reply("Pong!").queue()
    }
}