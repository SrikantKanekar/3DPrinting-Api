package fakeDataSource

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.model.Object
import com.example.model.User
import com.example.model.Order
import com.example.util.enums.OrderStatus

class TestRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
) {
    suspend fun getUser(email: String): User {
        return userDataSource.getUser(email)
    }

    suspend fun doesUserExist(email: String): Boolean {
        val user = userDataSource.getUserOrNull(email)
        return user != null
    }

    suspend fun getUserObject(email: String, id: String): Object? {
        val user = userDataSource.getUser(email)
        return user.objects.find { it.id == id }
    }

    suspend fun getActiveOrder(orderId: String): Order? {
        return orderDataSource
            .getOrderById(orderId)
            ?.takeUnless {
                it.status == OrderStatus.DELIVERED
            }
    }

    suspend fun getCompletedOrder(orderId: String): Order? {
        return orderDataSource.getOrderById(orderId)
            ?.takeIf {
                it.status == OrderStatus.DELIVERED
            }
    }
}