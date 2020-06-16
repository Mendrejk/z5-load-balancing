import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val processors: List<Processor> = generateProcessors(PROCESSOR_COUNT, GENERATION_TIME)
    val maxClock: Int = GENERATION_TIME + MAXIMAL_PROCESS_LENGTH + 1

    val belowThresholdBalancingResults: Pair<List<Double>, Triple<Int, Int, Int>> = belowThresholdBalancing(processors, maxClock)
    val lowerLoadBalancingResults: Pair<List<Double>, Triple<Int, Int, Int>> = lowerLoadBalancing(processors, maxClock)

    println("process count: ${processors.sumBy { it.processList.size }}")
    println("below threshold balancing:" +
            "\naverage loads: ${belowThresholdBalancingResults.first}" +
            "\naverage load: ${belowThresholdBalancingResults.first.average()}" +
            "\nstandard load deviation: ${standardDeviation(belowThresholdBalancingResults.first)}" +
            "\nthreshold queries: ${belowThresholdBalancingResults.second.first}" +
            "\nprocess migrations: ${belowThresholdBalancingResults.second.second}" +
            "\nprocess deletions: ${belowThresholdBalancingResults.second.third}" +
            "\n----------------------------")
    println("lower load balancing:" +
            "\naverage loads: ${lowerLoadBalancingResults.first}" +
            "\naverage load: ${lowerLoadBalancingResults.first.average()}" +
            "\nstandard load deviation: ${standardDeviation(lowerLoadBalancingResults.first)}" +
            "\nthreshold queries: ${lowerLoadBalancingResults.second.first}" +
            "\nprocess migrations: ${lowerLoadBalancingResults.second.second}" +
            "\nprocess deletions: ${belowThresholdBalancingResults.second.third}" +
            "\n----------------------------")
}

fun standardDeviation(data: List<Double>): Double {
    val mean: Double = data.average()
    val deviation: Double = data.fold(0.0) { accumulator: Double, next: Double ->
        accumulator + (next - mean).pow(2.0)
    }
    return sqrt(deviation / data.size)
}