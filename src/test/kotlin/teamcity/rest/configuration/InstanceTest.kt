package teamcity.rest.configuration

import org.testng.annotations.Test
import teamcity.rest.TestBase

@Test(groups = ["configuration"])
class InstanceTest : TestBase() {

    @Test
    fun `test closeable`() {
        teamCityInstance.close()
    }

    @Test
    fun `test used`() {
        teamCityInstance.use {
            it.rootProject()
        }
    }
}
