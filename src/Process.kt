class Process(private val appearanceTime: Int, private val expirationTime: Int, private val processorLoad: Int) {
    fun isExpired(clockTime: Int) = expirationTime <= clockTime
}