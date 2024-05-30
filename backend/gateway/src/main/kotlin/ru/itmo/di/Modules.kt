package ru.itmo.di

import kotlinx.serialization.json.Json
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.koin.dsl.module
import redis.clients.jedis.Jedis
import ru.itmo.service.HouseService
import ru.itmo.service.RedisService
import ru.itmo.service.UserService
import java.sql.Connection
import java.sql.DriverManager

private val REDIS_HOST = "localhost"
private val REDIS_PORT = 6379

fun connectToPostgres(): Connection {
    Class.forName("org.postgresql.Driver")

    val url = "jdbc:postgresql://localhost:5432/postgres?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true"
    val user = "postgres"
    val password = "postgres"

    return DriverManager.getConnection(url, user, password)
}

fun createKoinModule() = module {
    single { connectToPostgres() }
    single<DSLContext> { DSL.using(get<Connection>(), org.jooq.SQLDialect.POSTGRES) }
    single { UserService(get<DSLContext>()) }
    single { HouseService(get<DSLContext>()) }
    single { Jedis(REDIS_HOST, REDIS_PORT) }
    single { RedisService(get(), Json { ignoreUnknownKeys = true }) }
}

