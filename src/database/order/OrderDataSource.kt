package com.example.database.order

import com.example.model.Order
import com.example.util.enums.OrderStatus

interface OrderDataSource {

    suspend fun creteNewOrder(userEmail: String): Order

    suspend fun insertOrder(order: Order): Boolean

    suspend fun getOrderById(orderId: String): Order?

    suspend fun getOrdersOfUser(userEmail: String): List<Order>

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean

    suspend fun updateOrderDelivery(orderId: String, date: String): Boolean

    suspend fun getAllActiveOrders(): List<Order>
}