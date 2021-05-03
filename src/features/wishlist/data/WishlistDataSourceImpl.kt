package com.example.features.wishlist.data

import com.example.features.account.domain.User
import com.example.features.order.domain.Order
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import java.io.File

class WishlistDataSourceImpl(
    private val users: CoroutineCollection<User>,
    private val wishlistOrders: CoroutineCollection<Order>,
    private val cartOrders: CoroutineCollection<Order>
) : WishlistDataSource {

    override suspend fun getUserWishlist(email: String): ArrayList<String> {
        val user = users.findOne(User::email eq email)!!
        return user.wishlist
    }

    override suspend fun getOrderList(orderIds: ArrayList<String>): ArrayList<Order> {
        return ArrayList(
            orderIds.map {
                wishlistOrders.findOneById(it)!!
            }
        )
    }

    override suspend fun deleteWishlist(orderId: String): Boolean {
        val deleted = File("uploads/$orderId").delete()
        return wishlistOrders.deleteOneById(orderId).wasAcknowledged() and deleted
    }

    override suspend fun deleteUserWishlist(email: String, orderId: String): Boolean {
        val user = users.findOne(User::email eq email)!!
        val deleted = user.wishlist.remove(orderId)
        return users.updateOne(User::email eq email, user).wasAcknowledged() and deleted
    }

    override suspend fun addToCart(email: String, orderId: String): Boolean {
        val user = users.findOne(User::email eq email)!!
        val removed = user.wishlist.remove(orderId)
        if (removed) {
            user.cartOrders.add(orderId)
            val userResult = users.updateOne(User::email eq email, user).wasAcknowledged()

            val order = wishlistOrders.findOneById(orderId)!!
            wishlistOrders.deleteOneById(orderId)
            val orderResult = cartOrders.insertOne(order).wasAcknowledged()

            return userResult and orderResult
        }
        return false
    }
}