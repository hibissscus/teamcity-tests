package teamcity.rest.restassured

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.lessThan
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.ui.model.User


@Test(groups = ["rest"])
class AuthTest : TestBase() {

    @BeforeTest
    fun setup() {
        RestAssured.baseURI = "https://tests.teamcity.com/"
        RestAssured.port = 443
    }

    @Test
    fun whenRequestHead_thenOK() {
        given()
            .auth()
            .preemptive()
            .basic(User.KING.username, User.KING.password)
            .get("https://tests.teamcity.com/")
            .then()
            .statusCode(200)
            .time(lessThan(5000L))
    }

}
