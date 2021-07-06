package tests.accountRoute

import data.TestConstants
import fakeDataSource.TestRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.handlePostRequest
import tests.runTest
import tests.runWithLoggedUser
import kotlin.test.assertEquals

class UpdateAccount: KoinTest {

    @Test
    fun `should return updated username`() {
        runTest {
            runWithLoggedUser {
                handlePostRequest(
                    "/account/update",
                    listOf("username" to TestConstants.UPDATED_USERNAME)
                ) {
                    runBlocking {
                        val testRepository by inject<TestRepository>()
                        val user = testRepository.getUser(TestConstants.TEST_USER_EMAIL)
                        assertEquals(TestConstants.UPDATED_USERNAME, user.username)
                    }
                }
            }
        }
    }
}