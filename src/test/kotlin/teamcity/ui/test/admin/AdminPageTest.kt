package teamcity.ui.test.admin

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide.open
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import teamcity.ui.model.User
import teamcity.ui.pages.LoginPage
import teamcity.ui.pages.admin.AdminPage
import teamcity.ui.test.UITestBase

@Test(groups = ["admin"])
class AdminPageTest : UITestBase() {

    private val loginPage = LoginPage()
    private val adminPage = AdminPage()

    @BeforeClass
    fun setUp() {
        open(Configuration.baseUrl)
        loginPage.login(User.KING)
    }

    @Test
    fun `all links are present on admin pages`() {
        open(Configuration.baseUrl + adminPage.pageUrl)
        // project-related settings
        adminPage.linkProjects.shouldHave(Condition.visible)
        adminPage.linkBuilds.shouldHave(Condition.visible)
        adminPage.linkBuildTime.shouldHave(Condition.visible)
        adminPage.linkDiskUsage.shouldHave(Condition.visible)
        adminPage.linkHealthStatus.shouldHave(Condition.visible)
        adminPage.linkAudit.shouldHave(Condition.visible)
        // user management
        adminPage.linkUsers.shouldHave(Condition.visible)
        adminPage.linkGroups.shouldHave(Condition.visible)
        adminPage.linkRoles.shouldHave(Condition.visible)
        adminPage.linkInvitations.shouldHave(Condition.visible)
        // server administration
        adminPage.linkSubscriptionAndResources.shouldHave(Condition.visible)
        adminPage.linkAuthentication.shouldHave(Condition.visible)
        adminPage.linkProjectsImport.shouldHave(Condition.visible)
        adminPage.linkUsageStatistics.shouldHave(Condition.visible)
    }

}