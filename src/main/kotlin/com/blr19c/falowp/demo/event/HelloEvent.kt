package com.blr19c.falowp.demo.event

import com.blr19c.falowp.bot.system.plugin.Plugin

data class HelloEvent(
    val message: String
) : Plugin.Listener.Event