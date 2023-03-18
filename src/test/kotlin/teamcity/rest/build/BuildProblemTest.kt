package teamcity.rest.build

import org.jetbrains.teamcity.rest.BuildState
import org.testng.annotations.Test
import kotlin.test.assertTrue

@Test(groups = ["build"])
class BuildProblemTest : BuildTestBase() {

    @Test
    fun `fetch problems`() {
        val defaultBuildRunError = defaultBuildRunError()
        awaitState(defaultBuildRunError.id, BuildState.FINISHED, 30000L)
        val buildProblems = teamCityInstance.build(defaultBuildRunError.id).buildProblems
        assertTrue(buildProblems.any())
    }
}

