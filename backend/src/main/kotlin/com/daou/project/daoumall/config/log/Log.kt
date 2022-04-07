package com.daou.project.daoumall.config.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class Log(val message : String) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(javaClass)
        
        fun info(message: String) {
            log.info(message)
        }

        fun warn(message: String) {
            log.warn(message)
        }

        fun debug(message: String) {
            log.debug(message)
        }
    }
}