package org.example.logs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LokiLogger {
    private val logger: Logger = LoggerFactory.getLogger(LokiLogger::class.java)

    fun log(str: String) {
        logger.info(str)
    }
}