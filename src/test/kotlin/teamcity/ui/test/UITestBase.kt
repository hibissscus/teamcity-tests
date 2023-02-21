package teamcity.ui.test

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide
import org.testng.annotations.*
import org.testng.util.Strings


open class UITestBase {

    @BeforeClass
    @Parameters("selenide.remote", "selenide.headless")
    open fun beforeClass(@Optional("") optRemote: String, @Optional("") optHeadless: String) {
        if (!Strings.isNullOrEmpty(optRemote)) Configuration.remote = optRemote
        if (!Strings.isNullOrEmpty(optHeadless)) Configuration.headless = optHeadless.toBoolean()
        SelenideLogger.addListener("allure", AllureSelenide())
    }
}