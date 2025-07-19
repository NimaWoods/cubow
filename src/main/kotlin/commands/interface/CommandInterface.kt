package org.cubow.commands.`interface`

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

interface CommandInterface {
    public fun execute(event: SlashCommandInteractionEvent)
}