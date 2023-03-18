package teamcity.rest.build

import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.callPublicPropertiesAndFetchMethods
import teamcity.rest.publicInstance
import teamcity.rest.testBuildRunConfiguration
import kotlin.test.assertTrue

@Test(groups = ["build"])
class BuildAgentTest : TestBase() {

    @Test
    fun `test all`() {
        publicInstance().buildAgents().all().forEach {
            callPublicPropertiesAndFetchMethods(it)
        }
    }

    @Test
    fun `compatible with configuration`() {
        val compatibleAgents = teamCityInstance.buildAgents()
            .compatibleWith(testBuildRunConfiguration.id)
            .all()

        assertTrue { compatibleAgents.any() }
    }
}