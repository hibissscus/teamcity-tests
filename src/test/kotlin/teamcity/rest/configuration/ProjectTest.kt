package teamcity.rest.configuration

import org.testng.annotations.Test
import teamcity.rest.publicInstanceUrl
import teamcity.rest.testBuildConfiguration
import teamcity.rest.testProject
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ProjectTest : TestBase() {

    @Test
    fun `get project by id`() {
        val project = customInstance.project(testProject.id)
        assertEquals(testProject.id, project.id)
        assertEquals(testProject.name, project.name)
        assertFalse(project.archived)
    }

    @Test
    fun `build configuration by id`() {
        val configuration = customInstance.buildConfiguration(testBuildConfiguration.id)
        assertEquals(testBuildConfiguration.id, configuration.id)
        assertEquals("Run Tests", configuration.name)
        assertEquals(testProject.id, configuration.projectId)
    }

    @Test
    fun `web url with default parameters`() {
        val project = customInstance.project(testProject.id)
        assertEquals(
            "$publicInstanceUrl/project.html?projectId=${testProject.id}",
            project.getHomeUrl()
        )
    }

    @Test
    fun `web url with branch`() {
        val project = customInstance.project(testProject.id)
        assertEquals(
            "$publicInstanceUrl/project.html?projectId=${testProject.id}&branch=%3Cdefault%3E",
            project.getHomeUrl(branch = "<default>")
        )
    }
}