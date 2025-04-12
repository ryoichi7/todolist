package com.ryoichidoto.todolist.service.user

object ExceptionMessages {
    const val USER_ALREADY_EXISTS_FORMAT = "User with email '%s' already exists"
    const val USER_NOT_FOUND_ID_FORMAT = "User with id=%d not found."
    const val USER_NOT_FOUND_EMAIL_FORMAT = "User with email='%s' not found."
}

sealed class UserException(
    message: String,
) : RuntimeException(message)

class UserAlreadyExistsException(
    email: String,
) : UserException(ExceptionMessages.USER_ALREADY_EXISTS_FORMAT.format(email))

class UserNotFoundException private constructor(
    message: String,
) : UserException(message) {
    companion object {
        fun byId(id: Long) =
            UserNotFoundException(
                ExceptionMessages.USER_NOT_FOUND_ID_FORMAT.format(id),
            )

        fun byEmail(email: String) =
            UserNotFoundException(
                ExceptionMessages.USER_NOT_FOUND_EMAIL_FORMAT.format(email),
            )
    }
}
