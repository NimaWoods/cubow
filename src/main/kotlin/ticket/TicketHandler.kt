package org.cubow.ticket

import entity.EmbedBuilder
import handler.Properties
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.concrete.Category
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import org.cubow.BotManager
import org.cubow.utils.GeminiAPI
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class TicketHandler {

    val logger = Logger.getLogger(this.javaClass.name)

    fun initializeTicketPanel() {
        val properties: Properties = Properties.getInstance()

        val ticketPanelChannelId: String? = properties.get("ticket_panel_channel")
        val ticketPanelTitle: String? = properties.get("ticket_panel_title")
        val ticketPanelDescription: String? = properties.get("ticket_panel_description")

        val ticketPanelChannel: TextChannel? = ticketPanelChannelId?.let { BotManager.jda.getTextChannelById(it) }

        val eb = EmbedBuilder()
        eb.setTitle(ticketPanelTitle ?: "Ticket")
        eb.setDescription(ticketPanelDescription ?: "Ticket-Panel")

        val ticketPanelButtonCreate: String? = properties.get("ticket_panel_button_create")

        if (ticketPanelButtonCreate == null) {
            throw IllegalArgumentException("ticket_panel_button_create is not set in properties")
        }

        // Überprüfen, ob wir ein Embed-Panel bereits gesendet haben
        val shouldSendPanel = ticketPanelChannel?.let { channel ->
            try {
                // Letzte Nachricht abrufen
                val messages = channel.history.retrievePast(1).complete()

                // Überprüfen, ob keine Nachrichten vorhanden sind oder die letzte Nachricht kein Embed vom Bot ist
                messages.isEmpty() ||
                        messages[0].embeds.isEmpty() ||
                        !messages[0].author.id.equals(BotManager.jda.selfUser.id)
            } catch (e: Exception) {
                logger.warning("Fehler beim Abrufen der Nachrichtenhistorie: ${e.message}")
                true
            }
        } ?: true

        if (shouldSendPanel) {
            try {
                ticketPanelChannel?.sendMessageEmbeds(eb.build())
                    ?.addActionRow(
                        Button.primary("ticket_create", ticketPanelButtonCreate)
                    )
                    ?.queue()
            } catch (e: Exception) {
                logger.warning("Fehler beim Senden des Ticket-Panels: ${e.message}")
            }
        }
    }

    public fun createTicket(description: String, member: Member, guild: Guild): TextChannel? {
        val properties: Properties = Properties.getInstance()
        val ticketChannelname: String? = properties.get("ticket_channelname")
        val ticketCategoryId: String? = properties.get("ticket_category") ?: throw IllegalStateException("ticket_category ist nicht gesetzt")
        val ticketTitle: String? = properties.get("ticket_title") ?: throw IllegalStateException("ticket_title ist nicht gesetzt")
        val ticketDescription: String? = properties.get("ticket_description") ?: throw IllegalStateException("ticket_description ist nicht gesetzt")
        val ticketButtonClose: String? = properties.get("ticket_button_close") ?: throw IllegalStateException("ticket_button_close ist nicht gesetzt")
        val ticketButtonClaim: String? = properties.get("ticket_button_claim") ?: throw IllegalStateException("ticket_button_claim ist nicht gesetzt")
        val ticketSupporter: String? = properties.get("ticket_supporter")
        val ticketUserdescriptionTitle: String? = properties.get("ticket_userdescription_title")

        val membername = member.user.effectiveName
        val memberid = member.id
        val memberavatar = member.user.effectiveAvatarUrl
        val membermention = member.asMention

        val ticketCategory: Category? = ticketCategoryId?.let { BotManager.jda.getCategoryById(it) }
            ?: throw IllegalStateException("Ticket-Kategorie wurde nicht gefunden")

        // Null-Check für ticketChannelname mit Ersatz
        val safeChannelName = ticketChannelname?.replace("\$membername", membername) ?: "🎫︱Ticket-$membername"

        // Synchron erstellen mit complete() statt queue()
        val channel = ticketCategory?.createTextChannel(safeChannelName)?.complete()

        if (channel == null) {
            return null
        }

        val eb = EmbedBuilder()
        eb.setTitle(ticketTitle)
        eb.setDescription(ticketDescription)
        eb.addField("Ticket Beschreibung", description, true)
        eb.setAuthor(membername, null, memberavatar)

        channel.sendMessageEmbeds(eb.build())
            .addActionRow(
                Button.danger("ticket_close", ticketButtonClose ?: "Schließen"),
                Button.primary("ticket_claim", ticketButtonClaim ?: "Beanspruchen")
            )
            .queue()

        channel.upsertPermissionOverride(ticketCategory.getGuild().publicRole)
            .setDenied(Permission.VIEW_CHANNEL)
            .queue()

        channel.upsertPermissionOverride(member)
            .setAllowed(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND)
            .queue()

        ticketSupporter?.split(",")?.forEach {
            guild.getMemberById(it)?.let { supporterMember ->
                channel.upsertPermissionOverride(supporterMember)
                    .setAllowed(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND)
                    .queue()
            }
        }

        channel.sendTyping().queue()

        GeminiAPI.getResponse("Member Ping: " + member.asMention + "Ticket Beschreibung: " + description).let { response ->
            if (response.isNotEmpty()) {
                val ebResponse = EmbedBuilder()
                ebResponse.setTitle("KI Antwort")
                ebResponse.setDescription(response)
                ebResponse.setAuthor("Gemini 2.5-Flash", "https://gemini.google.com")

                channel.sendMessageEmbeds(ebResponse.build()).queue()
            } else {
                throw IllegalStateException("Gemini-Response ist leer")
            }
        }

        return channel
    }

    fun closeTicket(event: ButtonInteractionEvent) {
        val eb = EmbedBuilder()
        eb.setTitle("Ticket schließen")
        eb.setDescription("Möchtest du das Ticket wirklich schließen?")


        event.replyEmbeds(eb.build())
            .setEphemeral(true)
            .addActionRow(
                Button.success("ticket_confirm_close", "Ja"),
                Button.danger("ticket_cancel_close", "Nein"),
            )
            .queue()
    }

    fun confirmCloseTicket(event: ButtonInteractionEvent) {
        val channel: TextChannel = event.channel.asTextChannel()
        event.reply("Ticket wird in 5 Sekunden geschlossen.").setEphemeral(true).queue()
        channel.delete().queueAfter(5, TimeUnit.SECONDS)
    }

    fun openTicketModal(event: ButtonInteractionEvent) {
        val ticketDescription: TextInput = TextInput.create("ticket_description", "Ticket Beschreibung", TextInputStyle.PARAGRAPH)
            .setPlaceholder("Beschreibe wie wir dir helfen können")
            .setRequiredRange(1, 1500)
            .build()

        val modal: Modal = Modal.create("ticket_create", "Ticket erstellen")
            .addComponents(ActionRow.of(ticketDescription))
            .build()

        event.replyModal(modal).queue()
    }
}