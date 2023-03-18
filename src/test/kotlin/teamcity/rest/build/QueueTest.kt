package teamcity.rest.build

import org.jetbrains.teamcity.rest.ProjectId
import org.testng.annotations.Test
import kotlin.test.assertEquals

@Test(groups = ["build"])
class QueueTest : BuildTestBase() {

    @Test
    fun `test new build gets into queue`() {
        val defaultBuildRun = defaultBuildRun()
        teamCityInstance.buildQueue().queuedBuilds().forEach {
            assertEquals(it, defaultBuildRun)
        }
    }

    @Test
    fun `test project queue all properties thus evaluating them`() {
        teamCityInstance.buildQueue().queuedBuilds(ProjectId("Test Project")).forEach {
            it.toString()
            println(it)
        }
    }
}