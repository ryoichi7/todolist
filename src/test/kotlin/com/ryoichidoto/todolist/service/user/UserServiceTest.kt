package com.ryoichidoto.todolist.service.user

import com.ryoichidoto.todolist.dao.user.User
import com.ryoichidoto.todolist.dao.user.UserRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserServiceTest {
    private lateinit var userService: UserService
    private lateinit var userRepository: UserRepository
    private lateinit var userMapper: UserMapper
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        passwordEncoder = mockk()
        userMapper = UserMapper(passwordEncoder)
        userService = UserService(userMapper, userRepository)
    }

    @Test
    fun `test getUserById success`() {
        // setup
        val user =
            Optional.of(
                User(
                    id = 1,
                    username = "test",
                    password = "password",
                    email = "test@test.com",
                ),
            )
        val expectedDto =
            ReadUserDto(
                id = 1,
                username = "test",
                email = "test@test.com",
            )
        every { userRepository.findById(any()) } returns user

        // run
        val result = userService.getUserById(1)

        // assert
        assertNotNull(result)
        assertEquals(expectedDto, result)
        verify(exactly = 1) { userRepository.findById(1) }
    }

    @Test
    fun `test getUserById throws`() {
        // setup
        every { userRepository.findById(any()) } returns Optional.empty()

        // run && assert
        assertThrows<UserNotFoundException> {
            userService.getUserById(1)
        }
        verify(exactly = 1) { userRepository.findById(1) }
    }

    @Test
    fun `test createUser success`() {
        // setup
        val createUserDto =
            CreateUserDto(
                username = "test",
                password = "password",
                email = "test@test.com",
            )
        val encodedPassword = "encodedPassword"
        every { passwordEncoder.encode("password") } returns encodedPassword
        val user =
            User(
                id = 1,
                username = createUserDto.username,
                email = createUserDto.email,
                password = encodedPassword,
            )
        val expectedDto =
            ReadUserDto(
                id = 1,
                username = createUserDto.username,
                email = createUserDto.email,
            )
        every {
            userRepository.existsByEmail("test@test.com")
        } returns false
        every {
            userRepository.save(
                match {
                    it.username == user.username &&
                        it.email == user.email &&
                        it.password == encodedPassword
                },
            )
        } returns user

        // run
        val result = userService.createUser(createUserDto)

        // assert
        assertEquals(expectedDto, result)
        verify(exactly = 1) { userRepository.existsByEmail("test@test.com") }
        verify(exactly = 1) { passwordEncoder.encode("password") }
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `test createUser throws when email already exists`() {
        // setup
        val createUserDto =
            CreateUserDto(
                username = "test",
                password = "password",
                email = "test@test.com",
            )
        every { userRepository.existsByEmail("test@test.com") } returns true

        // run & assert: ожидаем, что будет выброшено исключение
        assertThrows<UserAlreadyExistsException> {
            userService.createUser(createUserDto)
        }

        verify(exactly = 1) { userRepository.existsByEmail("test@test.com") }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `test updateUser success without password change`() {
        // setup
        val existingUser =
            User(
                id = 1,
                username = "test",
                password = "encodedPassword",
                email = "test@test.com",
            )
        every { userRepository.findById(1) } returns Optional.of(existingUser)

        val updateUserDto =
            UpdateUserDto(
                username = "updatedTest",
                email = "updated@test.com",
                password = null,
            )
        every { userRepository.existsByEmail("updated@test.com") } returns false

        val updatedUser =
            User(
                id = existingUser.id,
                username = updateUserDto.username!!,
                password = existingUser.password,
                email = updateUserDto.email!!,
            )

        every {
            userRepository.save(
                match {
                    it.id == 1L &&
                        it.username == updateUserDto.username &&
                        it.email == updateUserDto.email &&
                        it.password == existingUser.password
                },
            )
        } returns updatedUser

        val expectedDto =
            ReadUserDto(
                id = 1,
                username = updateUserDto.username!!,
                email = updateUserDto.email!!,
            )

        // run
        val result = userService.updateUser(1, updateUserDto)

        // assert
        assertEquals(expectedDto, result)
        verify(exactly = 1) { userRepository.findById(1) }
        verify(exactly = 1) { userRepository.existsByEmail("updated@test.com") }
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `test updateUser success with password change`() {
        // setup
        val existingUser =
            User(
                id = 1,
                username = "test",
                password = "oldEncodedPassword",
                email = "test@test.com",
            )
        every { userRepository.findById(1) } returns Optional.of(existingUser)

        val updateUserDto =
            UpdateUserDto(
                username = "updatedTest",
                email = "updated@test.com",
                password = "newPassword",
            )

        val newEncodedPassword = "newEncodedPassword"
        every { passwordEncoder.encode("newPassword") } returns newEncodedPassword

        val updatedUser =
            User(
                id = 1,
                username = updateUserDto.username!!,
                password = newEncodedPassword,
                email = updateUserDto.email!!,
            )
        every {
            userRepository.existsByEmail("updated@test.com")
        } returns false
        every {
            userRepository.save(
                match {
                    it.id == 1L &&
                        it.username == updateUserDto.username &&
                        it.email == updateUserDto.email &&
                        it.password == newEncodedPassword
                },
            )
        } returns updatedUser

        val expectedDto =
            ReadUserDto(
                id = 1,
                username = updateUserDto.username!!,
                email = updateUserDto.email!!,
            )

        // run
        val result = userService.updateUser(1, updateUserDto)

        // assert
        assertEquals(expectedDto, result)
        verify(exactly = 1) { userRepository.findById(1) }
        verify(exactly = 1) { userRepository.existsByEmail("updated@test.com") }
        verify(exactly = 1) { passwordEncoder.encode("newPassword") }
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `test updateUser throws when user not found`() {
        // setup
        val updateUserDto =
            UpdateUserDto(
                username = "updatedTest",
                email = "updated@test.com",
                password = "newPassword",
            )
        every { userRepository.findById(1) } returns Optional.empty()

        // run & assert
        assertThrows<UserNotFoundException> {
            userService.updateUser(1, updateUserDto)
        }
        verify(exactly = 1) { userRepository.findById(1) }
    }

    @Test
    fun `test deleteUser success`() {
        // setup
        val existingUser =
            User(
                id = 1,
                username = "test",
                password = "encodedPassword",
                email = "test@test.com",
            )
        every { userRepository.findById(1) } returns Optional.of(existingUser)
        every { userRepository.delete(existingUser) } just Runs

        // run
        userService.deleteUser(1)

        // assert
        verify(exactly = 1) { userRepository.findById(1) }
        verify(exactly = 1) { userRepository.delete(existingUser) }
    }

    @Test
    fun `test deleteUser throws when user not found`() {
        // setup
        every { userRepository.findById(1) } returns Optional.empty()

        // run & assert
        assertThrows<UserNotFoundException> {
            userService.deleteUser(1)
        }
        verify(exactly = 1) { userRepository.findById(1) }
    }
}
