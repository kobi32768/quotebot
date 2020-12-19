package io.github.kobi32768.quotebot

enum class Error(val title: String, val description: String) {
    NOT_EXIST(
        "存在しないメッセージ",
        "`Quote Bot`が参加していないサーバーからの引用の可能性があります"
    ),
    NOT_EXIST_MSG(
        "存在しないメッセージ",
        "当該メッセージは存在しません"
    ),
    CROSS_GUILD(
        "他サーバーからの引用",
        "他サーバーからの引用はForceオプション`-f`が必要です"
    ),
    FORBIDDEN(
        "閲覧を禁止されているチャンネル",
        "`@everyone`は閲覧権限がありません"
    ),
    CANNOT_REF(
        "閲覧を禁止されているチャンネル",
        "`Quote Bot`は閲覧権限がありません"
    ),
    NSFW(
        "NSFWチャンネル",
        "NSFWチャンネルからは引用できません"
    );

//    val unlocalizedName = Utility().toCamelCase(name)
//    val location = "./src/main/resources/error/${unlocalizedName}"
}
