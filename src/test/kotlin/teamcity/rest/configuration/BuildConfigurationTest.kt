package teamcity.rest.configuration

import org.testng.annotations.Test
import teamcity.rest.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Test(groups = ["configuration"])
class BuildConfigurationTest : TestBase() {

    @Test
    fun `paused configuration has paused parameter`() {
        val pausedBuildConfiguration = publicInstance().buildConfiguration(pausedBuildConfiguration)
        assertTrue(pausedBuildConfiguration.paused)
    }

    @Test
    fun `active configuration has parameter of pause as false`() {
        val pausedBuildConfiguration = publicInstance().buildConfiguration(changesBuildConfiguration)
        assertFalse(pausedBuildConfiguration.paused)
    }

    @Test
    fun `webUrl with default parameters`() {
        val conf = publicInstance().buildConfiguration(changesBuildConfiguration)
        assertEquals(
            "$publicInstanceUrl/viewType.html?buildTypeId=${changesBuildConfiguration.stringId}",
            conf.getHomeUrl()
        )
    }

    @Test
    fun `webUrl with branch`() {
        val conf = publicInstance().buildConfiguration(changesBuildConfiguration)
        assertEquals(
            "$publicInstanceUrl/viewType.html?buildTypeId=${changesBuildConfiguration.stringId}&branch=%3Cdefault%3E",
            conf.getHomeUrl(branch = "<default>")
        )
    }
}
