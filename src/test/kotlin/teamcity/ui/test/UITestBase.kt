package teamcity.ui.test

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide
import org.testng.annotations.Optional
import org.testng.annotations.Parameters
import org.testng.util.Strings


open class UITestBase {

    @Parameters("selenide.baseUrl", "selenide.remote", "selenide.headless")
    fun selenide(
        @Optional("") optBaseUrl: String = "https://tests.teamcity.com",
        @Optional("") optRemote: String = "",
        @Optional("") optHeadless: String = "false"
    ) {
        if (!Strings.isNullOrEmpty(optBaseUrl)) Configuration.baseUrl = optBaseUrl
        if (!Strings.isNullOrEmpty(optRemote)) Configuration.remote = optRemote
        if (!Strings.isNullOrEmpty(optHeadless)) Configuration.headless = optHeadless.toBoolean()
        SelenideLogger.addListener("allure", AllureSelenide())
    }
}