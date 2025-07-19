package entity

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color
import java.time.Instant

class EmbedBuilder : EmbedBuilder {
    /**
     * Default constructor that applies company defaults.
     *
     * @see .applyCompanyDefaults
     */
    constructor() : super() {
        applyCompanyDefaults()
    }

    constructor(title: String?, description: String?) : super() {
        applyCompanyDefaults()
        setTitle(title)
        setDescription(description)
    }

    constructor(embed: MessageEmbed?) : super(embed)

    /**
     * Applies default values for the embed builder.
     *
     *
     * Sets the footer text and icon, color, and timestamp to the current time.
     *
     * @return this EmbedBuilder instance for method chaining.
     */
    fun applyCompanyDefaults(): entity.EmbedBuilder {
        setFooter(DEFAULT_FOOTER_TEXT, DEFAULT_FOOTER_ICON)
        setColor(DEFAULT_COLOR)
        setTimestamp(Instant.now())
        return this
    }

    /**
     * Erstellt eine Fehlermeldung mit dem angegebenen Text und der Ausnahme.
     *
     * @param message Die Fehlermeldung
     * @param t Die Ausnahme (kann null sein)
     * @return this EmbedBuilder instance for method chaining
     */
    fun errorMessage(message: String?, t: Throwable?): entity.EmbedBuilder {
        setTitle("️⚠️ FEHLER " + (if (t != null) " " + t.javaClass.getSimpleName() else ""))
        setColor(Color.RED)
        setDescription(message + (if (t != null) t.message else ""))
        return this
    }

    fun warningMessage(message: String?): entity.EmbedBuilder {
        setTitle("️Warnung " + " ")
        setColor(Color.YELLOW)
        setDescription(message)
        return this
    }

    fun successMessage(message: String?): entity.EmbedBuilder {
        setTitle("✅ Erfolgreich")
        setColor(Color.GREEN)
        setDescription(message)
        return this
    }

    fun noPermissionMessage(): entity.EmbedBuilder {
        return warningMessage("Du hast nicht die Berechtigungen, um das zu tun.")
    }

    companion object {
        const val DEFAULT_FOOTER_TEXT: String = "Cubow Bot | Craftex"
        const val DEFAULT_FOOTER_ICON: String = "https://cloud.playcraftex.net/public.php/dav/files/XSkYJKbqrL4MFHE/"
        val DEFAULT_COLOR: Color = Color(0x682ECC)
    }
}
