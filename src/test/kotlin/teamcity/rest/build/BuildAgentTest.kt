package teamcity.rest.build

import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.testBuildAgentPool
import teamcity.rest.testProject
import kotlin.test.assertTrue

@Test(groups = ["build"])
class BuildAgentTest : TestBase() {

    @Test
    fun `test no registered agents`() {
        assertTrue(teamCityInstance.buildAgents().all().none())
    }

    @Test
    fun `check agent pool has some build agents`() {
        assertTrue(teamCityInstance.buildAgentPools().all().any())
    }

    @Test
    fun `check agent pools has specific one`() {
        assertTrue(teamCityInstance.buildAgentPools().all().any { it.name == testBuildAgentPool })
    }

    @Test
    fun `check agent pool assigned to the project`() {
        assertTrue(teamCityInstance.buildAgentPools().all()
            .any { it -> it.name == "TestBuildAgentPool" && it.projects.any { it.name == testProject.name } })
    }
}