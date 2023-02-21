package teamcity.rest.configuration

import org.jetbrains.teamcity.rest.TeamCityInstance
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeSuite
import teamcity.rest.customInstanceByConnectionFile
import teamcity.rest.haveCustomInstance
import teamcity.rest.publicInstance
import teamcity.rest.setupLog4jDebug
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
