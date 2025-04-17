package com.nepreconsultintg.edigital

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform