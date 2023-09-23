package io.github.kobi32768.quotebot

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.utils.concurrent.Task
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.intrinsics.startCoroutineUninterceptedOrReturn

fun List<String>.containsAny(vararg substring: String): Boolean {
    return substring.any { this.contains(it) }
}

fun MessageReceivedEvent.isForce(): Boolean {
    return this.message.contentDisplay.split(' ').containsAny("-f", "--force")
}

fun compress64(id: String): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    var id10 = id.toLong()
    return buildString {
        while (id10 / 64 != 0L) {
            append(chars[(id10 % 64).toInt()])
            id10 /= 64
        }
        reverse()
    }
}

/**
 * awaits the task. this is similar to []
 */
suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { contIn ->
    var cont: CancellableContinuation<T>?
    cont = contIn
    cont.invokeOnCancellation {
        cancel()
        cont = null
    }
    onSuccess { cont?.resume(it) }
    onError { cont?.resumeWithException(it) }
}

/**
 * Starts coroutine.
 * This function will never handle result of the coroutine.
 */
fun startCoroutine(coroutine: suspend () -> Unit) {
    coroutine.startCoroutineUninterceptedOrReturn(IgnoredContinuation)
}

private object IgnoredContinuation : Continuation<Unit> {
    override val context: CoroutineContext
        get() = Dispatchers.Default
    override fun resumeWith(result: Result<Unit>) {}
}

