package io.github.kobi32768.quotebot

enum class Error(
    title_ja: String, description_ja: String,
    val title_en: String, val description_en: String,
) {
    NOT_EXIST(
        "存在しないメッセージ",
        "`Quote Bot`が参加していないサーバーからの引用、あるいは、アーカイブされたスレッドからの引用の可能性があります。",
        "Message that not exist",
        "It's possible that the quotation is from a server that Quote Bot cannot see, or from an archived thread."
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
        "`@everyone`に閲覧権限がないため、引用にはForceオプション`-f`が必要です。",
        "Channel that need permission",
        "@everyone doesn't have permission. Need the Force Option to quote."
    ),
    CANNOT_REF(
        "閲覧を禁止されているチャンネル",
        "`Quote Bot`は閲覧権限がありません。",
        "Channel that can't be referenced",
        "Quote Bot doesn't have permission."
    ),
    NSFW(
        "NSFWチャンネルからの引用",
        "NSFWチャンネルからの引用はForceオプション`-f`が必要です。",
        "NSFW Channel",
        "Need the Force Option to quote from NSFW channel."
    ),
    FORCE_FAILED(
        "権限不足",
        "Forceオプションを使用するために必要な権限がありません。",
        "You need more permissions",
        "You don't have the required permissions to force quoting."
    ),
    UNEXPECTED_ERROR(
        "不明なエラー",
        "不明なエラーが発生しました。ログを参照してください",
        "Unexpected Error",
        "Unexpected error has occurred. see Log for info."
    ),
    ;

    val title = title_ja
    val description = description_ja
}
