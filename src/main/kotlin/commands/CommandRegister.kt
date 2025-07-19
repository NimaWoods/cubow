package org.cubow.commands

import handler.Properties
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.cubow.BotManager.jda

class CommandRegister {

    public fun registerCommands() {
        updateTestCommands()
        updateCommands()
    }

    fun updateTestCommands() {
        val properties = Properties.getInstance()

        val serverId: String? = properties.get("server_id")

        if (serverId == null) {
            throw IllegalArgumentException("server_id is not set in properties")
        }

        jda.getGuildById(serverId)!!.updateCommands().addCommands(
            Commands.slash("ping", "Pong!"),
            Commands.slash("gemini", "Gemini")
                .addOption(OptionType.STRING, "prompt", "Prompt für Gemini Testing", true)
        ).queue()
    }

    fun updateCommands() {
        jda.updateCommands().addCommands()
    }

}