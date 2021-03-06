package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel

fun MessageData.isEveryoneViewable(): Boolean {
    val channel = this.channel
    val everyone = this.guild.roleCache.last()

    return (channel.isRoleAllowed(everyone, Permission.VIEW_CHANNEL) ||
            channel.isRoleAllowed(everyone, Permission.MESSAGE_HISTORY))
}

private fun TextChannel.isRoleAllowed(role: Role, permission: Permission): Boolean {
    val overrides = this.rolePermissionOverrides
    val index = this.getRoleIndex(role)

    // Return default when not overridden
    return if (index == -1) {
        role.permissions
    } else {
        overrides[index].allowed
    }.contains(permission)
}

private fun TextChannel.getRoleIndex(role: Role): Int {
    return this.rolePermissionOverrides
        .map { it.role }
        .indexOf(role)
}
