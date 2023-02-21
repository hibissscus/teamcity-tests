package teamcity.rest.configuration

import org.jetbrains.teamcity.rest.UserId
import org.testng.annotations.Test
import teamcity.rest.testUserHibissscus
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserTest : TestBase() {

    @Test
    fun `user by id`() {
        val user = customInstance.user(testUserHibissscus.id)
        assertEquals(testUserHibissscus.id.stringId, user.id.stringId)
        assertEquals(testUserHibissscus.username, user.username)
        assertEquals(testUserHibissscus.name, user.name)
        assertEquals(testUserHibissscus.email, user.email)
    }

    @Test
    fun `user url from instance`() {
        val url = customInstance.user(UserId("1")).getHomeUrl()
        assertEquals("${customInstance.serverUrl}/admin/editUser.html?userId=1", url)
    }

    @Test
    fun `user by username`() {
        val user = customInstance.user(testUserHibissscus.username)
        assertEquals("1", user.id.stringId)
    }

    @Test
    fun `user list`() {
        val users = customInstance.users().all().toList()
        assertTrue { users.size > 2 }
        assertEquals("hibissscus", users.single { it.id.stringId == "1" }.username)
        assertEquals("hibissscus@gmail.com", users.single { it.id.stringId == "1" }.email)
    }
}