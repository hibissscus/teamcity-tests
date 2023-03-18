package teamcity.rest.build

import org.jetbrains.teamcity.rest.BuildState
import org.testng.annotations.Test
import teamcity.rest.testBuildRunConfiguration
import teamcity.rest.testUserHibissscus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@Test(groups = ["build"])
class BuildRunTest : BuildTestBase() {

    @Test
    fun `run build`() {
        val defaultBuildRun = defaultBuildRun()
        val buildById = getBuildById(defaultBuildRun.id)
        assertEquals(defaultBuildRun, buildById)
    }

    @Test
    fun `run build and get info`() {
        val triggeredBuild = defaultBuildRun()
        assertEquals(testBuildRunConfiguration.name, triggeredBuild.name)
        assertEquals(null, triggeredBuild.canceledInfo)
    }

    @Test
    fun `run build with parameters`() {
        val triggeredBuild = teamCityInstance.buildConfiguration(testBuildRunConfiguration.id).runBuild(
            parameters = mapOf("a" to "b"), agentId = null, revisions = null, dependencies = null
        )
        assertTrue(getBuildById(triggeredBuild.id).parameters.any { p -> p.name == "a" && p.value == "b" })
    }

    @Test
    fun `trigger build and cancel`() {
        val triggeredBuild = defaultBuildRun()
        triggeredBuild.cancel(comment = "cancel")
        awaitState(triggeredBuild.id, BuildState.FINISHED, 30000L)
        assertNotNull(triggeredBuild.canceledInfo)
        assertEquals(testUserHibissscus.username, triggeredBuild.canceledInfo?.user?.username)
    }


    @Test
    fun `run build and finishing`() {
        val triggeredBuild = defaultBuildRun()
        val build = awaitState(triggeredBuild.id, BuildState.FINISHED, 30000L)
        assertEquals("SUCCESS", build.status?.name)
        assertEquals("FINISHED", build.state.name)
    }

    @Test
    fun `trigger build from other build`() {
        val triggeredBuild = teamCityInstance.buildConfiguration(testBuildRunConfiguration.id).runBuild(
            parameters = mapOf("a" to "b"), agentId = null, revisions = null, dependencies = null
        )
        val build = getBuildById(triggeredBuild.id)

        val newTriggeredBuild = teamCityInstance.buildConfiguration(testBuildRunConfiguration.id).runBuild(
            parameters = build.parameters.associate { it.name to it.value }, agentId = null, revisions = null, dependencies = null
        )

        val newBuild = awaitState(newTriggeredBuild.id, BuildState.FINISHED, 30000L)
        assertEquals("SUCCESS", newBuild.status?.name)
        assertEquals("FINISHED", newBuild.state.name)
        assertTrue(newBuild.parameters.any { p -> p.name == "a" && p.value == "b" })
    }
}
