package teamcity.rest

import org.jetbrains.teamcity.rest.TeamCityInstance
import org.testng.annotations.BeforeClass
import kotlin.test.assertTrue

open class TestBase {

    protected lateinit var teamCityInstance: TeamCityInstance

    @BeforeClass
    open fun beforeClass() {
        setupLog4jDebug()
        assertTrue(haveCustomInstance())
        teamCityInstance = customInstanceByConnectionFile()
    }
}
