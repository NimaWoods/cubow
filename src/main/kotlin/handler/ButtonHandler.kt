package org.cubow.handler

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import org.cubow.ticket.TicketHandler

class ButtonHandler {
    fun buttonInteractionEvent(event: ButtonInteractionEvent) {
        when(event.button.id) {
            "ticket_create" -> {
                TicketHandler().openTicketModal(event)
            }
            "ticket_close" -> {
                TicketHandler().closeTicket(event)
            }
            "ticket_confirm_close" -> {
                TicketHandler().confirmCloseTicket(event)
            }
            "ticket_cancel_close" -> {
                event.message.delete().queue()
            }
            "ticket_claim" -> {
                TicketHandler().claimTicket(event)
            }
        }
    }
}