package org.cubow.handler

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import org.cubow.ticket.TicketHandler

class ModalHandler {

    fun modalInteractionEvent(event: ModalInteractionEvent) {
        when (event.modalId) {
            "ticket_create" -> {
                event.deferReply(true).queue()
                // Sicheres Auslesen des Modal-Inputs
                val ticketDescription = event.getValue("ticket_description")?.asString ?: "Kein Beschreibung"
                val textChannel: TextChannel? = TicketHandler().createTicket(
                        ticketDescription,
                        event.member!!, 
                        event.guild!!
                    )
                
                if (textChannel == null) {
                    throw IllegalStateException("Ticket-Kanal konnte nicht erstellt werden")
                }
                
                event.getHook().editOriginal("Ticket erstellt! " + textChannel.asMention).queue()
            }
        }
    }

}