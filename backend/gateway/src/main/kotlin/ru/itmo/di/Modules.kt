package ru.itmo.di

import io.ktor.server.application.*
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.koin.dsl.module
import ru.itmo.plugins.CityService
import ru.itmo.service.UserService
import java.sql.Connection
import java.sql.DriverManager


fun connectToPostgres(application: Application): Connection {
    Class.forName("org.postgresql.Driver")

    val url = "jdbc:postgresql://localhost:5432/postgres?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true"
    val user = "postgres"
    val password = "postgres"

    return DriverManager.getConnection(url, user, password)
}

fun createKoinModule(application: Application) = module {
    single { connectToPostgres(application) }
    single<DSLContext> { DSL.using(get<Connection>(), org.jooq.SQLDialect.POSTGRES) }
    single { CityService(get<Connection>()) }
    single { UserService(get<DSLContext>()) }
}

