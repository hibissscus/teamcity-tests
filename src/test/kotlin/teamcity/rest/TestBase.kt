package teamcity.rest

import org.jetbrains.teamcity.rest.TeamCityInstance
import org.testng.annotations.BeforeSuite
import kotlin.test.assertTrue

open class TestBase {

    protected lateinit var teamCityInstance: TeamCityInstance

    @BeforeSuite
    open fun beforeClass() {
        setupLog4jDebug()
        assertTrue(haveCustomInstance())
        teamCityInstance = customInstanceByConnectionFile()
    }
}
