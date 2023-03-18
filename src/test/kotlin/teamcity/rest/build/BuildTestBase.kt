package teamcity.rest.build

import org.jetbrains.teamcity.rest.Build
import org.jetbrains.teamcity.rest.BuildId
import org.jetbrains.teamcity.rest.BuildState
import org.jetbrains.teamcity.rest.BuildStatus
import teamcity.rest.TestBase
import teamcity.rest.testBuildRunConfiguration
import teamcity.rest.testBuildRunErrorConfiguration

open class BuildTestBase : TestBase() {

    fun defaultBuildRun(): Build {
        return teamCityInstance.buildConfiguration(testBuildRunConfiguration.id)
            .runBuild(null, false, null, false, null, null, null, false, null, null)
    }

    fun defaultBuildRunError(): Build {
        return teamCityInstance.buildConfiguration(testBuildRunErrorConfiguration.id)
            .runBuild(null, false, null, false, null, null, null, false, null, null)
    }

    fun awaitState(id: BuildId, buildState: BuildState, timeoutMsec: Long): Build {
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

    fun getBuildById(id: BuildId): Build {
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
