package org.cubow.utils

import handler.Properties
import net.dv8tion.jda.api.entities.Member

object PermissionUtils {

    fun hasTicketModeratePermission(member: Member): Boolean {
    val properties = Properties.getInstance()

    val ticketSupporterString: String = properties.get("ticket_supporter") ?: return false
    val ticketSupporter: List<String> = ticketSupporterString.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    val roles = member.roles.map { it.id }

    // Wenn das Mitglied eine der ticketSupporter-Rollen hat, darf es moderieren
    return roles.any { ticketSupporter.contains(it) }
}

}