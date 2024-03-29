package com.example.features.`object`.data

import com.example.database.request.SpecialRequestDatasource
import com.example.database.user.UserDataSource
import com.example.features.`object`.requests.ObjectCreateRequest
import com.example.model.Object
import com.example.model.SpecialRequest
import com.example.util.enums.ObjectStatus.CART
import com.example.util.enums.ObjectStatus.NONE
import com.example.util.enums.Quality

class ObjectRepository(
    private val userDataSource: UserDataSource,
    private val specialRequestDatasource: SpecialRequestDatasource
) {
    suspend fun createObject(body: ObjectCreateRequest, email: String): Object {
        return userDataSource.createObject(body, email)
    }

    suspend fun getObjects(email: String): List<Object> {
        val user = userDataSource.getUser(email)
        return user.objects.reversed()
    }

    suspend fun getUserObjectById(email: String, id: String): Object? {
        val user = userDataSource.getUser(email)
        return user.objects.find { it.id == id }
    }

    suspend fun addUserObject(email: String, obj: Object) {
        val user = userDataSource.getUser(email)
        user.objects.add(obj)
        userDataSource.updateUser(user)
    }

    suspend fun deleteUserObject(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        val deleted = user.objects
            .removeIf { it.id == objectId && (it.status == NONE || it.status == CART) }
        if (deleted) userDataSource.updateUser(user)
        return deleted
    }

    suspend fun updateQuality(email: String, objectId: String, quality: Quality): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == CART || it.status == NONE }
            .find { it.id == objectId }
            ?.let { it.quality = quality } ?: return false
        userDataSource.updateUser(user)
        return true
    }

    suspend fun updateQuantity(email: String, objectId: String, quantity: Int): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == CART || it.status == NONE }
            .find { it.id == objectId }
            ?.let { it.quantity = quantity } ?: return false
        userDataSource.updateUser(user)
        return true
    }

    suspend fun updateMessage(email: String, id: String, message: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE || it.status == CART }
            .find { it.id == id }
            ?.let {
                it.message = message
            } ?: return false
        userDataSource.updateUser(user)
        return true
    }

    suspend fun sendSpecialRequest(request: SpecialRequest) {
        specialRequestDatasource.add(request)
    }

    suspend fun updateFilename(email: String, id: String, fileName: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .find { it.id == id }
            ?.let { it.name = fileName } ?: return false
        userDataSource.updateUser(user)
        return true
    }
}