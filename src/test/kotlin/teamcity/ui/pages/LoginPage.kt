package teamcity.ui.pages

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide.element
import teamcity.ui.model.User

// page_url = https://tests.teamcity.com/
class LoginPage {
    val submitLogin = element("input[class$='loginButton']")
    val username = element("input[id='username']")
    val password = element("input[id='password']")
    val remember = element("input[id='remember']")
    val errorMessage = element("div[id='errorMessage']")
    val imageAvatar = element("img[data-test='avatar']")

    fun clear() {
        username.clear()
        password.clear()
    }

    fun login(usernameText: String, passwordText: String) {
        username.clear()
        username.sendKeys(usernameText)
        username.shouldHave(Condition.attribute("value", usernameText))
        password.clear()
        password.sendKeys(passwordText)
        password.shouldHave(Condition.attribute("value", passwordText))
        submitLogin.click()
    }

    fun login(user: User) {
        login(user.username, user.password)
        imageAvatar.shouldHave(Condition.visible)
    }

}