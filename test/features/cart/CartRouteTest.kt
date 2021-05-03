package features.cart

import com.example.features.account.data.AccountRepository
import com.example.features.cart.data.CartRepository
import com.example.features.wishlist.data.WishlistRepository
import com.example.module
import data.Constants.TEST_CART_ORDER
import data.Constants.TEST_USER_EMAIL
import di.testAuthModule
import features.auth.runWithTestUser
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CartRouteTest: KoinTest {

    private val accountRepository by  inject<AccountRepository>()
    private val wishlistRepository by  inject<WishlistRepository>()
    private val cartRepository by  inject<CartRepository>()

    @Test
    fun `access cart without login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            handleRequest(HttpMethod.Get, "/cart").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `access cart after login`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/cart").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `remove from cart success`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/cart/$TEST_CART_ORDER/remove").apply {
                    runBlocking {

                        // change this
                        val wishlistOrders = wishlistRepository.getUserWishlist(TEST_USER_EMAIL)
                        assertTrue { wishlistOrders.contains(TEST_CART_ORDER) }

                        // change this
                        val cartOrders = cartRepository.getUserCartOrders(TEST_USER_EMAIL)
                        cartOrders.forEach {
                            assertFalse { it.id == TEST_CART_ORDER }
                        }

                        val user = accountRepository.getUser(TEST_USER_EMAIL)!!
                        assertTrue { user.wishlist.contains(TEST_CART_ORDER) }
                        assertFalse { user.cartOrders.contains(TEST_CART_ORDER) }

                        assertEquals(HttpStatusCode.Found, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `remove from cart invalid ID`() {
        withTestApplication({ module(testing = true, koinModules = listOf(testAuthModule)) }) {
            runWithTestUser {
                handleRequest(HttpMethod.Get, "/cart/invalid-order-id/remove").apply {
                    runBlocking {

                        // change this
                        val wishlistOrders = wishlistRepository.getUserWishlist(TEST_USER_EMAIL)
                        assertFalse { wishlistOrders.contains(TEST_CART_ORDER) }

                        // change this
                        val cartOrders = cartRepository.getUserCartOrders(TEST_USER_EMAIL)

                        val user = accountRepository.getUser(TEST_USER_EMAIL)!!
                        assertFalse { user.wishlist.contains(TEST_CART_ORDER) }
                        assertTrue { user.cartOrders.contains(TEST_CART_ORDER) }

                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                    }
                }
            }
        }
    }
}