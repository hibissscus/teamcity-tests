package teamcity.rest.build

import org.jetbrains.teamcity.rest.Build
import org.jetbrains.teamcity.rest.BuildId
import org.jetbrains.teamcity.rest.BuildState
import org.jetbrains.teamcity.rest.BuildStatus
import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.testBuildRunConfiguration
import kotlin.test.assertNotNull


class BuildRunTest : TestBase() {

    @Test
    fun `run build`() {
        val build = customBuildRun()
        println(build)
    }

    @Test
    fun `run build and get info`() {
        val triggeredBuild = customBuildRun()
        println(triggeredBuild.name)
        println(triggeredBuild.canceledInfo)
    }

    @Test
    fun `run build with parameters`() {
        val triggeredBuild = customInstance.buildConfiguration(testBuildRunConfiguration.id).runBuild(
            parameters = mapOf("a" to "b"),
            agentId = null, revisions = null, dependencies = null
        )
        triggeredBuild.parameters.forEach { println("${it.name}=${it.value}") }
    }

    @Test
    fun `trigger build and cancel`() {
        val triggeredBuild = customBuildRun()
        triggeredBuild.cancel(comment = "cancel")
        awaitState(triggeredBuild.id, BuildState.FINISHED, 30000L)
        assertNotNull(triggeredBuild.canceledInfo)
    }


    @Test
    fun `run build and finishing`() {
        val triggeredBuild = customBuildRun()
        val build = awaitState(triggeredBuild.id, BuildState.FINISHED, 30000L)
        println(build)
        println(build.state)
    }

    @Test
    fun `trigger build from other build`() {
        val triggeredBuild = customInstance.buildConfiguration(testBuildRunConfiguration.id).runBuild(
            parameters = mapOf("a" to "b"),
            agentId = null, revisions = null, dependencies = null
        )
        val build = getBuild(triggeredBuild.id)

        val newTriggeredBuild = customInstance.buildConfiguration(testBuildRunConfiguration.id).runBuild(
            parameters = build.parameters.associate { it.name to it.value },
            agentId = null, revisions = null, dependencies = null
        )

        val newBuild = awaitState(newTriggeredBuild.id, BuildState.FINISHED, 30000L)
        println(newBuild)
        newBuild.parameters.forEach { println("${it.name}=${it.value}") }
    }

    private fun awaitState(id: BuildId, buildState: BuildState, timeoutMsec: Long): Build {
        val curTime = System.currentTimeMillis()
        var b: Build? = null
        var state: BuildState? = null
        while (buildState != state && System.currentTimeMillis() - curTime < timeoutMsec) {
            try {
                b = customInstance.build(id)
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

    private fun customBuildRun(): Build {
        return customInstance.buildConfiguration(testBuildRunConfiguration.id).runBuild(null, false, null, false, null, null, null, false, null, null)

    }

    private fun getBuild(id: BuildId): Build {
        // get build by build id
        var flag = false
        var buildStatus: BuildStatus? = null
        var b: Build? = null
        var attempts = 10

        while (!flag && attempts-- > 0) {
            try {
                b = customInstance.build(id)
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
