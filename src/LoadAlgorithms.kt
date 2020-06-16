fun belowThresholdBalancing(processors: List<Processor>, maxClock: Int): Pair<List<Double>, Triple<Int, Int, Int>> {
    var thresholdQueries: Int = 0
    var processMigrations: Int = 0
    for (clock: Int in 0 until maxClock) {
        processors.forEach {
            // checks if any new processes to add
            val incomingProcess: Process? = it.checkIncomingProcess(clock)
            // checks if any processes are finished - if so, deletes them
            it.checkProcesses(clock)
            // v - delegating a process
            if (incomingProcess != null) {
                var triesLeft: Int = BELOW_THRESHOLD_BALANCING_TRIES
                var chosenProcessor: Processor = processors.random()
                thresholdQueries++
                while (chosenProcessor == it || (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD && triesLeft > 0)) {
                    if (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD) {
                        triesLeft--
                    }
                    chosenProcessor = processors.random()
                    thresholdQueries++
                }
                if (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD) chosenProcessor = it
                if (chosenProcessor != it) processMigrations++
                if (!chosenProcessor.addProcess(incomingProcess)) it.processDeletions++
            }
        }
    }
    val averageLoads: List<Double> = List(PROCESSOR_COUNT) { processors[it].loadSum / maxClock.toDouble() }
    val processDeletions: Int = processors.sumBy { it.processDeletions }
    val returnStatistics: Triple<Int, Int, Int> = Triple(thresholdQueries, processMigrations, processDeletions)
    processors.forEach { it.reset() }
    return Pair(averageLoads, returnStatistics)
}

fun lowerLoadBalancing(processors: List<Processor>, maxClock: Int): Pair<List<Double>, Triple<Int, Int, Int>> {
    var thresholdQueries: Int = 0
    var processMigrations: Int = 0
    for (clock: Int in 0 until maxClock) {
        processors.forEach {
            // checks if any new processes to add
            val incomingProcess: Process? = it.checkIncomingProcess(clock)
            // checks if any processes are finished - if so, deletes them
            it.checkProcesses(clock)
            // v - delegating a process
            if (incomingProcess != null) {
                var chosenProcessor: Processor = it
                if (chosenProcessor.load > LOWER_LOAD_BALANCING_THRESHOLD) {
                    var triesLeft = LOWER_LOAD_BALANCING_TRIES
                    chosenProcessor = processors.random()
                    thresholdQueries++
                    while (chosenProcessor == it || (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD && triesLeft > 0)) {
                        if (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD) {
                            triesLeft--
                        }
                        chosenProcessor = processors.random()
                        thresholdQueries++
                    }
                }
                if (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD) chosenProcessor = it
                if (chosenProcessor != it) processMigrations++
                if (!chosenProcessor.addProcess(incomingProcess)) it.processDeletions++
            }
        }
    }
    val averageLoads: List<Double> = List(PROCESSOR_COUNT) { processors[it].loadSum / maxClock.toDouble() }
    val processDeletions: Int = processors.sumBy { it.processDeletions }
    val returnStatistics: Triple<Int, Int, Int> = Triple(thresholdQueries, processMigrations, processDeletions)
    processors.forEach { it.reset() }
    return Pair(averageLoads, returnStatistics)
}

fun loadTransferBalancing(processors: List<Processor>, maxClock: Int): Pair<List<Double>, Triple<Int, Int, Int>> {
    var thresholdQueries: Int = 0
    var processMigrations: Int = 0
    for (clock: Int in 0 until maxClock) {
        processors.forEach {
            // checks if any new processes to add
            val incomingProcess: Process? = it.checkIncomingProcess(clock)
            // checks if any processes are finished - if so, deletes them
            it.checkProcesses(clock)
            // load transfer logic
            if (it.load <= LOAD_TRANSFER_BALANCING_TRANSFEREE_THRESHOLD) {
                thresholdQueries++
                var transferTriesLeft: Int = LOAD_TRANSFER_BALANCING_TRANSFER_TRIES
                var transferring: Processor = processors.random()
                while(transferring == it || (transferring.load < LOAD_TRANSFER_BALANCING_TRANSFERRING_THRESHOLD && transferTriesLeft > 0)) {
                    if (transferring.load < LOAD_TRANSFER_BALANCING_TRANSFERRING_THRESHOLD) transferTriesLeft--
                    transferring = processors.random()
                    thresholdQueries++
                }
                if (transferring.load >= LOAD_TRANSFER_BALANCING_TRANSFERRING_THRESHOLD) {
                    thresholdQueries++
                    val droppedProcesses: List<Process> = transferring.dropHalfProcesses()
                    processMigrations += droppedProcesses.size
                    it.addProcesses(droppedProcesses)
                }
            }
            // v - delegating a process
            if (incomingProcess != null) {
                var chosenProcessor: Processor = it
                if (chosenProcessor.load > LOWER_LOAD_BALANCING_THRESHOLD) {
                    var triesLeft = LOWER_LOAD_BALANCING_TRIES
                    chosenProcessor = processors.random()
                    thresholdQueries++
                    while (chosenProcessor == it || (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD && triesLeft > 0)) {
                        if (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD) triesLeft--
                        chosenProcessor = processors.random()
                        thresholdQueries++
                    }
                }
                if (chosenProcessor.load > BELOW_THRESHOLD_BALANCING_THRESHOLD) chosenProcessor = it
                if (chosenProcessor != it) processMigrations++
                if (!chosenProcessor.addProcess(incomingProcess)) it.processDeletions++
            }
        }
    }
    val averageLoads: List<Double> = List(PROCESSOR_COUNT) { processors[it].loadSum / maxClock.toDouble() }
    val processDeletions: Int = processors.sumBy { it.processDeletions }
    val returnStatistics: Triple<Int, Int, Int> = Triple(thresholdQueries, processMigrations, processDeletions)
    processors.forEach { it.reset() }
    return Pair(averageLoads, returnStatistics)
}