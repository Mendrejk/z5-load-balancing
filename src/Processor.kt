class Processor(processFrequency: Int, generationTicks: Int) {
    val processList: List<Process>

    private val scopeProcesses: MutableList<Process>
    private val currentProcesses: MutableList<Process>
    var load: Int
        private set
    var processDeletions: Int
    var loadSum: Int

    init {
        val processList: MutableList<Process> = mutableListOf()
        for (clock: Int in (1..generationTicks)) {
            if (processFrequency >= (1..100).random()) {
                processList.add(Process(clock,
                    clock + (MINIMAL_PROCESS_LENGTH..MAXIMAL_PROCESS_LENGTH).random(),
                    (MINIMAL_PROCESS_LOAD..MAXIMAL_PROCESS_LOAD).random()))
            }
        }
        this.processList = processList

        scopeProcesses = mutableListOf()
        currentProcesses = mutableListOf()
        load = 0
        processDeletions = 0
        loadSum = 0
        reset()
    }

    fun checkIncomingProcess(clock: Int): Process? {
        // loadSum is increased every tick
        loadSum += load
        if (scopeProcesses.isEmpty()) return null
        if (scopeProcesses.first().appearanceTime == clock) {
            return scopeProcesses.removeAt(0)
        }
        return null
    }

    fun addProcess(process: Process): Boolean {
        if (load + process.processorLoad <= 100) {
            currentProcesses.add(process)
            load += process.processorLoad
            return true
        }
        return false
    }

    fun checkProcesses(clock: Int) {
        currentProcesses.removeIf { it.expirationTime <= clock }
        load = currentProcesses.sumBy { it.processorLoad }
    }

    fun reset(): Unit {
        scopeProcesses.clear()
        processList.forEach { scopeProcesses.add(Process(it.appearanceTime, it.expirationTime, it.processorLoad)) }

        processDeletions = 0
        load = 0
        loadSum = 0
    }
}

fun generateProcessors(howMany: Int, generationTime: Int): List<Processor> = List(howMany) {
        Processor((MINIMAL_PROCESS_FREQUENCY..MAXIMAL_PROCESS_FREQUENCY).random(), generationTime)
    }