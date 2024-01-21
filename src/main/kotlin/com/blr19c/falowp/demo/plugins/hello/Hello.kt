package com.blr19c.falowp.demo.plugins.hello

import com.blr19c.falowp.bot.system.Log
import com.blr19c.falowp.bot.system.listener.hooks.MessagePluginExecutionHook
import com.blr19c.falowp.bot.system.listener.hooks.ReceiveMessageHook
import com.blr19c.falowp.bot.system.plugin.MessagePluginRegisterMatch
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.Plugin.Listener.Event.Companion.eventListener
import com.blr19c.falowp.bot.system.plugin.Plugin.Listener.Hook.Companion.beforeHook
import com.blr19c.falowp.bot.system.plugin.Plugin.Message.message
import com.blr19c.falowp.bot.system.plugin.Plugin.Task.periodicScheduling
import com.blr19c.falowp.bot.system.plugin.hook.HookTypeEnum
import com.blr19c.falowp.bot.system.plugin.hook.awaitReply
import com.blr19c.falowp.bot.system.plugin.hook.runtimeHook
import com.blr19c.falowp.demo.event.HelloEvent
import kotlin.time.Duration.Companion.seconds

@Plugin(name = "Hello")
class Hello : Log {

    //消息
    private val hello = message(Regex("Hello")) {
        this.sendReply("Hello")
    }

    //钩子
    private val helloHook = beforeHook<MessagePluginExecutionHook> { (receiveMessage, register) ->
        val botApi = this.botApi()
        if (register.originalClass == Hello::class && receiveMessage.private()) {
            botApi.sendReply("Hi")
            return@beforeHook
        }
        this.process()
    }

    //定时
    private val helloTask = periodicScheduling(10.seconds) {
        this.publishEvent(HelloEvent(System.currentTimeMillis().toString()))
    }

    //事件
    private val helloEvent = eventListener<HelloEvent> { (message) ->
        log().info("当前时间:$message")
        //this.sendAllGroup(SendMessage.builder("当前时间:$message").build())
    }

    //运行时钩子
    private val runTimeHook = message(Regex("你好")) {
        this.sendReply("下一句请继续说你好")
        this.runtimeHook<ReceiveMessageHook>(HookTypeEnum.BEFORE) { (receiveMessage), unRegister ->
            val botApi = this.botApi()
            if (receiveMessage.content.message == "你好") {
                botApi.sendReply("运行时你好")
                unRegister.unregister()
                return@runtimeHook
            }
            botApi.sendReply("回答错误")
        }
    }

    //更简单的监听消息-运行时钩子
    private val runTimeHookSimple = message(Regex("简单的你好")) {
        this.sendReply("下一句请继续说简单的你好")
        this.awaitReply(MessagePluginRegisterMatch(Regex("简单的你好"))) {
            this.sendReply("运行时简单的你好")
        }
    }

    init {
        hello.register()
        helloHook.register()
        helloTask.register()
        helloEvent.register()
        runTimeHook.register()
        runTimeHookSimple.register()
    }
}