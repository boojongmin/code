package com.way2bit.board

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BoardApplication

fun main(args: Array<String>) {
	runApplication<BoardApplication>(*args)
}
