package teamcity.rest

import org.jetbrains.teamcity.rest.TeamCityInstance
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeSuite
import kotlin.test.assertTrue

open class TestBase {

    protected lateinit var publicInstance: TeamCityInstance
    protected lateinit var customInstance: TeamCityInstance

    @BeforeSuite
    open fun beforeSuite() {
        println("beforeSuite")
    }

    @BeforeClass
    open fun beforeClass() {
        setupLog4jDebug()
        assertTrue(haveCustomInstance())
        publicInstance = publicInstance()
        customInstance = customInstanceByConnectionFile()
    }
}
