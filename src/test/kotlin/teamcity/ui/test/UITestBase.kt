package teamcity.ui.test

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide
import org.testng.annotations.BeforeClass
import org.testng.annotations.Optional
import org.testng.annotations.Parameters
import org.testng.util.Strings


open class UITestBase {

    @BeforeClass
    @Parameters("selenide.baseUrl", "selenide.remote", "selenide.headless")
    fun selenide(
        @Optional("https://tests.teamcity.com") optBaseUrl: String,
        @Optional("") optRemote: String,
        @Optional("false") optHeadless: String
    ) {
        if (!Strings.isNullOrEmpty(optBaseUrl)) Configuration.baseUrl = optBaseUrl
        if (!Strings.isNullOrEmpty(optRemote)) Configuration.remote = optRemote
        if (!Strings.isNullOrEmpty(optHeadless)) Configuration.headless = optHeadless.toBoolean()
        println("baseUrl:" + Configuration.baseUrl + " remote:" + Configuration.remote + " headless:" + Configuration.headless)
        SelenideLogger.addListener("allure", AllureSelenide())
        Configuration.timeout = 10000
    }
}