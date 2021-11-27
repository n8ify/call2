package xyz.n8ify.call2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.metrics.export.prometheus.EnablePrometheusMetrics

@SpringBootApplication
//@EnablePrometheusMetrics
class Call2Application

fun main(args: Array<String>) {
    runApplication<Call2Application>(*args)
}
