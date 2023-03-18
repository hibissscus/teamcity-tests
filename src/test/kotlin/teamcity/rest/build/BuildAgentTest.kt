package teamcity.rest.build

import org.jetbrains.teamcity.rest.BuildState
import org.testng.annotations.Test
import teamcity.rest.testBuildRunConfiguration
import kotlin.test.assertTrue

@Test(groups = ["build"])
class BuildAgentTest : BuildTestBase() {

    @Test
    fun `test agent compatible with configuration`() {
        val defaultBuildRun = defaultBuildRun()
        awaitState(defaultBuildRun.id, BuildState.RUNNING, 30000L)
        val compatibleAgents = teamCityInstance.buildAgents()
            .compatibleWith(testBuildRunConfiguration.id)
            .all()

        assertTrue { compatibleAgents.any() }
    }
}