package teamcity.ui.test.login

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration.baseUrl
import com.codeborne.selenide.Selenide.open
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import teamcity.ui.model.User
import teamcity.ui.pages.LoginPage
import teamcity.ui.pages.ProjectPage
import teamcity.ui.test.UITestBase

@Test(groups = ["login"])
class LoginTest : UITestBase() {

    private val loginPage = LoginPage()
    private val projectPage = ProjectPage()

    @BeforeMethod
    fun beforeClass() {
        this.selenide()
        open(baseUrl)
    }

    @Test
    fun `login with empty user and password`() {
        loginPage.submitLogin.click()
        loginPage.errorMessage.shouldBe(Condition.visible)
        loginPage.username.shouldHave(Condition.cssClass("errorField"))
        loginPage.password.shouldHave(Condition.cssClass("errorField"))
        loginPage.errorMessage.shouldHave(Condition.text("Incorrect username or password."))
    }

    @Test
    fun `login with empty user`() {
        loginPage.password.sendKeys("********")
        loginPage.password.shouldHave(Condition.attribute("value", "********"))
        loginPage.submitLogin.click()
        loginPage.errorMessage.shouldBe(Condition.visible)
        loginPage.username.shouldHave(Condition.cssClass("errorField"))
        loginPage.password.shouldHave(Condition.cssClass("errorField"))
        loginPage.errorMessage.shouldHave(Condition.text("Incorrect username or password."))
    }

    @Test
    fun `login with wrong password`() {
        loginPage.username.sendKeys("username")
        loginPage.username.shouldHave(Condition.attribute("value", "username"))
        repeat(7) {
            loginPage.password.sendKeys("*")
            loginPage.submitLogin.click()
        }
        loginPage.errorMessage.shouldBe(Condition.visible)
        loginPage.errorMessage.shouldHave(Condition.text("failed login attempts"))
    }

    @Test(priority = 1)
    fun login() {
        loginPage.login(User.KING)
        projectPage.imageAvatar.shouldBe(Condition.visible)
    }

    @Test(dependsOnMethods = ["login"])
    fun logout() {
        projectPage.logout()
        loginPage.username.shouldHave(Condition.visible)
    }
}