package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel

class DiscordRole {
    fun isEveryoneViewable(data: MessageData): Boolean {
        val channel = data.channel
        val everyone = data.guild.roleCache.last()

        return (isRoleAllowed(channel, everyone, Permission.VIEW_CHANNEL) ||
                isRoleAllowed(channel, everyone, Permission.MESSAGE_HISTORY))
    }

    private fun isRoleAllowed(channel: TextChannel, role: Role, permission: Permission): Boolean {
        val overrides = channel.rolePermissionOverrides
        val index = getRoleIndex(channel, role)

        return if (index == -1) {
            // Return default when not overridden
            role.permissions
        } else {
            overrides[index].allowed
        }.contains(permission)
    }

    private fun getRoleIndex(channel: TextChannel, role: Role): Int {
        return channel.rolePermissionOverrides
            .map { it.role }
            .indexOf(role)
    }
}
