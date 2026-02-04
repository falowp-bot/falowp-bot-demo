package com.blr19c.falowp.demo.plugins.message

import com.blr19c.falowp.bot.adapter.nc.message.enums.NapCatFaceEmoji
import com.blr19c.falowp.bot.adapter.nc.message.expand.*
import com.blr19c.falowp.bot.system.api.SendMessage
import com.blr19c.falowp.bot.system.expand.encodeToBase64String
import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.message.MessageMatch
import com.blr19c.falowp.bot.system.plugin.message.message
import com.blr19c.falowp.bot.system.plugin.message.queueMessage
import com.blr19c.falowp.bot.system.readPluginResource
import com.blr19c.falowp.bot.system.web.commonWebdriverContext

@Plugin(name = "消息演示", desc = "消息演示")
class Message {


    private val message = message(Regex("消息演示")) {
        this.sendReply("消息演示")
    }

    private val atMeMessage = message(MessageMatch(Regex("AT我消息演示"), atMe = true)) {
        this.sendReply("AT我消息演示")
    }

    private val notTerminatedMessage = message(order = -1, terminateEvent = false) {
        println("不终止传播的消息处理")
    }

    private val queueMessage = queueMessage(Regex("队列消息演示")) {
        //队列消息会保证处理顺序，溢出的消息会交给队列顺序执行
        this.sendReply("队列消息演示")
    }

    private val customQueueMessage = queueMessage(
        MessageMatch(Regex("自定义队列消息演示")),
        queueCapacity = 2,
        //你依旧可以选择不终止消息事件传播
        terminateEvent = false,
        onSuccess = { this.sendReply("你可以自定义进入队列的消息提醒,也可以什么都不提醒") },
        onOverFlow = { this.sendReply("你可以自定义队列超长的提醒,也可以什么都不提醒") },
    ) {
        this.sendReply("自定义队列消息演示")
    }

    private val customReply = message(Regex("回复各种类型的消息")) {
        val message = SendMessage.builder()
            .at(this.receiveMessage)
            .text("文本消息")
            .image(readPluginResource("image.jpeg") { it.readBytes() }.encodeToBase64String())
            //你可以自己通过html造图
            .image(customImage())
            //.video("视频".toURI())
            //.voice("语音".toURI())
            .nudge(this.receiveMessage.sender.id)
            //.emoji("表情ID", "表情类型", "表情展示文本")
            .nc {
                //对应适配器可能存在扩展消息
                //你也可以自己扩展消息
                this.faceMessage(NapCatFaceEmoji.BIG_EMOJI_371)
                this.diceMessage()
                this.rpsMessage()
                this.shareUserMessage(receiveMessage.sender.id)
                this.biliVideoCardMessage(
                    "BV1b44y1q7Cb",
                    "一个b站视频",
                    "这是对应的描述",
                    //这个是封面
                    "https://i0.hdslb.com/bfs/archive/f664f5dc247a078350a5c65a34b84d15c7be1f14.jpg@.avif"
                )
            }
            .build()

        //你可以选择直接发送这一个消息组
        this.sendReply(message)
        //也可以选择引用来源消息
        this.sendReply("引用的回复", reference = true)
        //也可以选择合并转发这些消息
        this.sendReply(message, forward = true)
    }

    private fun customImage(): String {
        return commonWebdriverContext {
            this.newPage().use { page ->
                page.setContent("<h1>hello</h1>")
                page.screenshot().encodeToBase64String()
            }
        }
    }


    init {
        message.register()
        atMeMessage.register()
        notTerminatedMessage.register()
        queueMessage.register()
        customQueueMessage.register()
        customReply.register()
    }

}