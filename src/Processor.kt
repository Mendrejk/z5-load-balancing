class Processor(processFrequency: Int, generationTicks: Int) {
    private val processList: List<Process>
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
    }
}

fun generateProcessors(howMany: Int, generationTime: Int): List<Processor> = List(howMany) {
        Processor((MINIMAL_PROCESS_FREQUENCY..MAXIMAL_PROCESS_FREQUENCY).random(), generationTime)
    }