package com.blr19c.falowp.demo.plugins.hook

import com.blr19c.falowp.bot.system.listener.hooks.ReceiveMessageHook
import com.blr19c.falowp.bot.system.listener.hooks.SendMessageHook
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.hook.*
import com.blr19c.falowp.bot.system.plugin.message.MessageMatch
import com.blr19c.falowp.bot.system.plugin.message.message
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

@Plugin(name = "钩子演示")
class Hook {


    private val beforeHook = beforeHook<SendMessageHook> {
        println("发送消息的前置Hook")
        //可以发送消息
        //this.botApi().sendReply()
        //可以终止流程
        //this.terminate()
    }

    private val afterReturningHook = afterReturningHook<SendMessageHook> {
        println("发送消息的后置Hook")
    }

    private val afterThrowingHook = afterThrowingHook<SendMessageHook> {
        println("发送消息的异常Hook")
    }

    private val afterFinallyHook = afterFinallyHook<SendMessageHook> {
        println("发送消息的最终Hook")
    }

    private val afterHook = aroundHook<SendMessageHook> {
        println("发送消息的环绕开始Hook")
        this.process()
        println("发送消息的环绕结束Hook")
    }

    private val message = message(Regex("钩子测试")) {
        this.sendReply("请说:继续钩子测试")
        //运行时钩子-等待回复
        val result = withTimeoutOrNull(10.seconds) {
            awaitReply(MessageMatch(regex = Regex("继续钩子测试"))) {
                this.sendReply("继续钩子测试成功")
                return@awaitReply "继续钩子测试成功-返回结果"
            }
        } ?: this.sendReply("继续钩子测试超时未回复")
        println(result)
    }

    private val customMessage = message(Regex("自定义钩子测试")) {
        this.sendReply("请说:自定义钩子测试")
        this.runtimeHook<ReceiveMessageHook>(HookTypeEnum.BEFORE) { hook, unRegister ->
            println("接收到了自定义钩子测试消息$hook")
            //注销钩子
            unRegister.unregister()
        }
    }

    private val sendMessageHook = sendMessageHook { messageList ->
        //在发送消息前对消息作出一些处理,例如修改消息
        messageList //+ TextSendMessage("在发送消息前对消息作出一些处理")
    }

    private val customHook = message(Regex("自定义钩子源")) {
        //自定义一个可以被修改钩子源
        val customHook = CustomHook(this.receiveMessage.id)

        withPluginHook(this, customHook) {
            this.sendReply("当前的消息ID${customHook.id}")
        }

    }

    private val beforeCustomHook = beforeHook<CustomHook> { hook ->
        //通过hook修改自己的钩子源
        hook.id += "我修改了消息ID"
    }

    class CustomHook(var id: String) : Plugin.Listener.Hook


    init {
        beforeHook.register()
        afterReturningHook.register()
        afterThrowingHook.register()
        afterFinallyHook.register()
        afterHook.register()
        message.register()
        customMessage.register()
        sendMessageHook.register()
        customHook.register()
        beforeCustomHook.register()
    }

}