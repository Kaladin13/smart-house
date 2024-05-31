package ru.itmo.service

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jooq.DSLContext
import ru.itmo.jooq.generated.tables.references.USER
import ru.itmo.model.UserDto
import java.util.*

class UserService(private val dsl: DSLContext) {

    fun registerUser(userDto: UserDto): UUID {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, userDto.password.toCharArray())

        return dsl.insertInto(USER)
            .set(USER.LOGIN, userDto.login)
            .set(USER.PASSWORD_HASH, hashedPassword)
            .returning(USER.ID)
            .fetchOne()
            ?.id!!
    }

    fun loginUser(userDto: UserDto): String {
        val userRecord = dsl.selectFrom(USER)
            .where(USER.LOGIN.eq(userDto.login))
            .fetchOne() ?: throw IllegalArgumentException("User not found")

        val verified = BCrypt.verifyer().verify(userDto.password.toCharArray(), userRecord.passwordHash)
        if (!verified.verified) {
            throw IllegalArgumentException("Invalid password")
        }

        return userRecord.id.toString()
    }
}
