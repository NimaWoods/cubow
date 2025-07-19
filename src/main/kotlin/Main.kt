package org.cubow

import handler.Properties
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.cubow.commands.CommandRegister
import org.cubow.listener.EventListener
import org.cubow.ticket.TicketHandler

object BotManager {
    lateinit var jda: JDA
        private set

    fun initialize(token: String) {
        val activityText = "www.playcraftex.net"

        val builder = JDABuilder.createDefault(token)
            .setActivity(Activity.playing(activityText))
            .setChunkingFilter(ChunkingFilter.ALL)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY)
            .enableIntents(
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES
            )

        jda = builder.build()
        jda.awaitReady()

        jda.addEventListener(EventListener())

        TicketHandler().initializeTicketPanel()
    }
}

fun main() {
    val properties: Properties = Properties.getInstance()
    val botToken: String = properties.get("bot_token") ?: throw IllegalStateException("bot_token ist nicht gesetzt")

    BotManager.initialize(botToken)

    initializeJDA()
}

fun initializeJDA() {
    CommandRegister().registerCommands()
}