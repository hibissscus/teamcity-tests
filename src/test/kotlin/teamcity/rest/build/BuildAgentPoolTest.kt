package teamcity.rest.build

import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.callPublicPropertiesAndFetchMethods
import teamcity.rest.publicInstance


@Test(groups = ["build"])
class BuildAgentPoolTest : TestBase() {

    @Test
    fun `test all`() {
        publicInstance().buildAgentPools().all().forEach {
            callPublicPropertiesAndFetchMethods(it)
        }
    }
}
