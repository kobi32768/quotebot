package io.github.kobi32768.quotebot

enum class Error(
    title_ja: String, description_ja: String,
    val title_en: String, val description_en: String,
) {
    NOT_EXIST(
            "存在しないメッセージ",
            "`Quote Bot`が参加していないサーバーからの引用の可能性があります。",
            "Message that not exist",
            "It's possible that the quotation is from a server that quotebot cannot see."
    ),
    NOT_EXIST_MSG(
            "存在しないメッセージ",
            "当該メッセージは存在しません。",
            "Message that not exist",
            "That message is not exist."
    ),
    CROSS_GUILD(
            "他サーバーからの引用",
            "他サーバーからの引用はForceオプション`-f`が必要です。",
            "Quotation from other server",
            "Need the Force Option to quote from other server."
    ),
    FORBIDDEN(
            "閲覧を禁止されているチャンネル",
            "`@everyone`は閲覧権限がありません。",
            "Channel that need permission",
            "@everyone doesn't have permission."
    ),
    CANNOT_REF(
            "閲覧を禁止されているチャンネル",
            "`Quote Bot`は閲覧権限がありません。",
            "Channel that can't be referenced",
            "Quote Bot doesn't have permission"
    ),
    NSFW(
            "NSFWチャンネル",
            "NSFWチャンネルからは引用できません。",
            "NSFW Channel",
            "Can't be quoted from NSFW channel."
    ),
    FORCE_FAILED(
            "権限不足",
            "Forceオプションを使用するために必要な権限がありません。",
            "You need more permissions",
            "You don't have the required permissions to force quoting."
    );

    val title = title_ja
    val description = description_ja
}
