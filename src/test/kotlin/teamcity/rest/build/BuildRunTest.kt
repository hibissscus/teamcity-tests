package teamcity.rest.build

import org.jetbrains.teamcity.rest.Build
import org.jetbrains.teamcity.rest.BuildId
import org.jetbrains.teamcity.rest.BuildState
import org.jetbrains.teamcity.rest.BuildStatus
import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.testBuildRunConfiguration
import teamcity.rest.testUserHibissscus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class BuildRunTest : TestBase() {

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

    private fun awaitState(id: BuildId, buildState: BuildState, timeoutMsec: Long): Build {
        val curTime = System.currentTimeMillis()
        var b: Build? = null
        var state: BuildState? = null
        while (buildState != state && System.currentTimeMillis() - curTime < timeoutMsec) {
            try {
                b = teamCityInstance.build(id)
                state = b.state
            } catch (_: KotlinNullPointerException) {
            }
            Thread.sleep(1000)
        }
        if (buildState != state) {
            throw RuntimeException("Timeout")
        }
        return b!!
    }

    private fun defaultBuildRun(): Build {
        return teamCityInstance.buildConfiguration(testBuildRunConfiguration.id)
            .runBuild(null, false, null, false, null, null, null, false, null, null)
    }

    private fun getBuildById(id: BuildId): Build {
        var flag = false
        var buildStatus: BuildStatus? = null
        var b: Build? = null
        var attempts = 10

        while (!flag && attempts-- > 0) {
            try {
                b = teamCityInstance.build(id)
                buildStatus = b.status
                flag = true
            } catch (e: KotlinNullPointerException) {
                Thread.sleep(1000)
            }
        }
        b?.let { println(it) }
        buildStatus?.let { println(it) }
        return b!!
    }
}
