package teamcity.rest.configuration

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.lessThan
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.ui.model.User


@Test(groups = ["rest"])
class AuthTest : TestBase() {

    @BeforeMethod
    fun setup() {
        RestAssured.baseURI = teamCityInstance.serverUrl
        RestAssured.port = 443
    }

    @Test
    fun `basic auth`() {
        given()
                .auth()
                .preemptive()
                .basic(User.KING.username, User.KING.password)
                .get(teamCityInstance.serverUrl)
                .then()
                .statusCode(200)
                .time(lessThan(5000L))
    }

}
