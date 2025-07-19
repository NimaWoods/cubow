package org.cubow.listener

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.cubow.commands.CommandHandler
import org.cubow.handler.ButtonHandler
import org.cubow.handler.ModalHandler
import java.util.logging.Logger

class EventListener : ListenerAdapter() {

    val logger: Logger = Logger.getLogger(this.javaClass.name)

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        logger.info("Slash Command Interaction: ${event.name}")
        CommandHandler().slashCommandInteractionEvent(event)
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        logger.info("Button Interaction: ${event.button.id}")
        ButtonHandler().buttonInteractionEvent(event)
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        logger.info("Modal Interaction: ${event.modalId}")
        ModalHandler().modalInteractionEvent(event)
    }

}