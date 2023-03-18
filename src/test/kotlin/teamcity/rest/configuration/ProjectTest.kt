package teamcity.rest.configuration

import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.publicInstanceUrl
import teamcity.rest.testBuildConfiguration
import teamcity.rest.testProject
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@Test(groups = ["configuration"])
class ProjectTest : TestBase() {

    @Test
    fun `get project by id`() {
        val project = teamCityInstance.project(testProject.id)
        assertEquals(testProject.id, project.id)
        assertEquals(testProject.name, project.name)
        assertFalse(project.archived)
    }

    @Test
    fun `build configuration by id`() {
        val configuration = teamCityInstance.buildConfiguration(testBuildConfiguration.id)
        assertEquals(testBuildConfiguration.id, configuration.id)
        assertEquals("Run Tests", configuration.name)
        assertEquals(testProject.id, configuration.projectId)
    }

    @Test
    fun `web url with default parameters`() {
        val project = teamCityInstance.project(testProject.id)
        assertEquals(
            "$publicInstanceUrl/project.html?projectId=${testProject.id}",
            project.getHomeUrl()
        )
    }

    @Test
    fun `web url with branch`() {
        val project = teamCityInstance.project(testProject.id)
        assertEquals(
            "$publicInstanceUrl/project.html?projectId=${testProject.id}&branch=%3Cdefault%3E",
            project.getHomeUrl(branch = "<default>")
        )
    }
}