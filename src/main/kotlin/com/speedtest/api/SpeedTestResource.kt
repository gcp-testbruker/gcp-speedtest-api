package com.speedtest.api

import com.google.gson.Gson
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/speedtest")
class SpeedTestResource(val pubSub: PubSubTemplate) {

    val gson: Gson = Gson()

    data class Speeds(val upload: Int, val download: Int)

    data class Client(val ip: String?, val lat: Long?, val lon: Long?, val isp: String?, var country: String?)

    data class Server(val host: String?, val lat: Long?, val lon: Long?, var country: String?, val distance: Int?, val ping: Int?, var id: String?)

    data class TestData(val speeds: Speeds, val client: Client, val server: Server)

    data class TestResult(val user: String, val device: Int, val timestamp: Long, val data: TestData)

    @PostMapping
    fun publishTestResult(@RequestBody testResult: TestResult) {
        this.pubSub.publish("speedtest", gson.toJson(testResult))

        println(testResult.user)
    }
}