class Process(val appearanceTime: Int, val expirationTime: Int, val processorLoad: Int) {
    fun isExpired(clockTime: Int) = expirationTime <= clockTime
}