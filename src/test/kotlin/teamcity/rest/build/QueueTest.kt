package teamcity.rest.build

import org.jetbrains.teamcity.rest.BuildState
import org.testng.annotations.Test
import teamcity.rest.testProject
import kotlin.test.assertEquals

@Test(groups = ["build"])
class QueueTest : BuildTestBase() {

    @Test
    fun `test new build gets into queue`() {
        val defaultBuildRun = defaultBuildRun()
        awaitState(defaultBuildRun.id, BuildState.QUEUED, 30000L)
        teamCityInstance.buildQueue().queuedBuilds().forEach {
            assertEquals(it, defaultBuildRun)
        }
    }

    @Test
    fun `test specific project gets into queue`() {
        val defaultBuildRun = defaultBuildRun()
        awaitState(defaultBuildRun.id, BuildState.QUEUED, 30000L)
        teamCityInstance.buildQueue().queuedBuilds(testProject.id).forEach {
            assertEquals(it, defaultBuildRun)
        }
    }
}