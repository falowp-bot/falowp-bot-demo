package com.blr19c.falowp.demo.plugins.scheduling

import com.blr19c.falowp.bot.system.plugin.Plugin
import com.blr19c.falowp.bot.system.plugin.task.cronScheduling
import com.blr19c.falowp.bot.system.plugin.task.periodicScheduling
import kotlin.time.Duration.Companion.seconds

@Plugin(name = "定时演示")
class Scheduling {


    private val cron = cronScheduling("*/5 * * * * ?") {
        //println("5秒秒钟执行一次")
        //定时任务中发送消息不能使用sendReply,需要手动指定来源,或者发送给所有
        //this.sendGroup(sourceId = "")
        //this.sendPrivate(sourceId = "")
        //this.sendAllGroup()
    }

    private val periodic = periodicScheduling(10.seconds) {
        //println("10秒钟执行一次")
    }

    init {
        cron.register()
        periodic.register()
    }
}
