package data

import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.`object`.domain.SlicingDetails
import com.example.features.account.domain.User
import com.example.features.checkout.domain.Address
import com.example.features.notification.domain.Notification
import com.example.features.order.domain.PrintingStatus.*
import data.TestConstants.TEST_CART_OBJECT
import data.TestConstants.TEST_COMPLETED_OBJECT
import data.TestConstants.TEST_CONFIRMED_ORDER
import data.TestConstants.TEST_DELIVERED_ORDER
import data.TestConstants.TEST_DELIVERING_ORDER
import data.TestConstants.TEST_NOTIFICATION_ID
import data.TestConstants.TEST_NOTIFICATION_MESSAGE
import data.TestConstants.TEST_NOTIFICATION_TITLE
import data.TestConstants.TEST_OBJECT_EXTENSION
import data.TestConstants.TEST_OBJECT_FILE_URL
import data.TestConstants.TEST_OBJECT_IMAGE_URL
import data.TestConstants.TEST_OBJECT_NAME
import data.TestConstants.TEST_PENDING_OBJECT
import data.TestConstants.TEST_PLACED_ORDER
import data.TestConstants.TEST_PRINTED_OBJECT
import data.TestConstants.TEST_PRINTING_OBJECT
import data.TestConstants.TEST_PROCESSING_ORDER
import data.TestConstants.TEST_SLICED_OBJECT
import data.TestConstants.TEST_TRACKING_OBJECT
import data.TestConstants.TEST_UNSLICED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_HASHED_PASSWORD
import data.TestConstants.TEST_USER_USERNAME
import java.util.*

val testUser = User(
    email = TEST_USER_EMAIL,
    password = TEST_USER_HASHED_PASSWORD,
    username = TEST_USER_USERNAME,
    address = Address(
        firstname = "firstname",
        lastname = "lastname",
        phoneNumber = 1234567890,
        address = "address",
        city = "city",
        state = "state",
        country = "country",
        pinCode = 123456
    ),
    objects = ArrayList(
        listOf(
            Object(
                id = TEST_UNSLICED_OBJECT,
                name = TEST_OBJECT_NAME,
                fileUrl = TEST_OBJECT_FILE_URL,
                fileExtension = TEST_OBJECT_EXTENSION,
                imageUrl = TEST_OBJECT_IMAGE_URL,
                status = NONE,
            ),
            Object(
                id = TEST_SLICED_OBJECT,
                name = TEST_OBJECT_NAME,
                fileUrl = TEST_OBJECT_FILE_URL,
                fileExtension = TEST_OBJECT_EXTENSION,
                imageUrl = TEST_OBJECT_IMAGE_URL,
                status = NONE,
                slicingDetails = SlicingDetails(uptoDate = true)
            ),
            Object(
                id = TEST_CART_OBJECT,
                name = TEST_OBJECT_NAME,
                fileUrl = TEST_OBJECT_FILE_URL,
                fileExtension = TEST_OBJECT_EXTENSION,
                imageUrl = TEST_OBJECT_IMAGE_URL,
                status = CART,
                quantity = 2,
                slicingDetails = SlicingDetails(
                    uptoDate = true,
                    time = 1,
                    materialWeight = 1,
                    materialCost = 1,
                    electricityCost = 1,
                    totalPrice = 2
                )
            ),
            Object(
                id = TEST_TRACKING_OBJECT,
                name = TEST_OBJECT_NAME,
                fileUrl = TEST_OBJECT_FILE_URL,
                fileExtension = TEST_OBJECT_EXTENSION,
                imageUrl = TEST_OBJECT_IMAGE_URL,
                status = TRACKING
            ),
            Object(
                id = TEST_COMPLETED_OBJECT,
                name = TEST_OBJECT_NAME,
                fileUrl = TEST_OBJECT_FILE_URL,
                fileExtension = TEST_OBJECT_EXTENSION,
                imageUrl = TEST_OBJECT_IMAGE_URL,
                status = COMPLETED,
                printingStatus = PRINTED
            ),
            Object(
                id = TEST_PENDING_OBJECT,
                name = TEST_OBJECT_NAME,
                fileUrl = TEST_OBJECT_FILE_URL,
                fileExtension = TEST_OBJECT_EXTENSION,
                imageUrl = TEST_OBJECT_IMAGE_URL,
                status = TRACKING,
                printingStatus = PENDING
            ),
            Object(
                id = TEST_PRINTING_OBJECT,
                name = TEST_OBJECT_NAME,
                fileUrl = TEST_OBJECT_FILE_URL,
                fileExtension = TEST_OBJECT_EXTENSION,
                imageUrl = TEST_OBJECT_IMAGE_URL,
                status = TRACKING,
                printingStatus = PRINTING
            ),
            Object(
                id = TEST_PRINTED_OBJECT,
                name = TEST_OBJECT_NAME,
                fileUrl = TEST_OBJECT_FILE_URL,
                fileExtension = TEST_OBJECT_EXTENSION,
                imageUrl = TEST_OBJECT_IMAGE_URL,
                status = TRACKING,
                printingStatus = PRINTED
            )
        )
    ),
    orderIds = ArrayList(
        listOf(
            TEST_PLACED_ORDER,
            TEST_CONFIRMED_ORDER,
            TEST_PROCESSING_ORDER,
            TEST_DELIVERING_ORDER,
            TEST_DELIVERED_ORDER
        )
    ),
    notification = ArrayList(
        listOf(
            Notification(
                title = TEST_NOTIFICATION_TITLE,
                message = TEST_NOTIFICATION_MESSAGE,
                id = TEST_NOTIFICATION_ID
            )
        )
    )
)

val testUsers = List(2) {
    User(
        email = UUID.randomUUID().toString(),
        password = UUID.randomUUID().toString(),
        username = UUID.randomUUID().toString(),
        address = Address(
            city = UUID.randomUUID().toString(),
            state = UUID.randomUUID().toString(),
            country = UUID.randomUUID().toString()
        ),
        objects = ArrayList(
            List(2) {
                Object(
                    id = UUID.randomUUID().toString(),
                    name = TEST_OBJECT_NAME,
                    fileUrl = TEST_OBJECT_FILE_URL,
                    fileExtension = TEST_OBJECT_EXTENSION,
                    imageUrl = TEST_OBJECT_IMAGE_URL
                )
            }
        )
    )
}

val testUserData = (testUsers + testUser).shuffled()