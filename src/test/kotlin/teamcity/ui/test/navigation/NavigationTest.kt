package teamcity.ui.test.navigation

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide.open
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import teamcity.ui.model.User
import teamcity.ui.pages.*
import teamcity.ui.test.UITestBase

@Test(groups = ["navigation"])
class NavigationTest : UITestBase() {

    private val loginPage = LoginPage()
    private val agentsPage = AgentsPage()
    private val changesPage = ChangesPage()
    private val projectPage = ProjectPage()
    private val queuePage = QueuePage()

    @BeforeClass
    fun setUp() {
        this.selenide()
        open(Configuration.baseUrl)
        loginPage.login(User.KING)
    }

    @Test
    fun `can open agents page`() {
        open(Configuration.baseUrl + agentsPage.pageUrl)
        agentsPage.overview.shouldHave(Condition.visible)
    }

    @Test
    fun `can open changes page`() {
        open(Configuration.baseUrl + changesPage.pageUrl)
        changesPage.h1Changes.shouldHave(Condition.visible)
    }

    @Test
    fun `can open project page`() {
        open(Configuration.baseUrl + projectPage.pageUrl)
        projectPage.allProjects.shouldHave(Condition.visible)
    }

    @Test
    fun `can open queue page`() {
        open(Configuration.baseUrl + queuePage.pageUrl)
        queuePage.allPools.shouldHave(Condition.visible)
    }
}