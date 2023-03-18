package teamcity.rest.build

import org.jetbrains.teamcity.rest.BuildId
import org.jetbrains.teamcity.rest.TeamCityInstance
import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.customInstanceByConnectionFile
import teamcity.rest.haveCustomInstance
import kotlin.test.assertTrue

@Test(groups = ["build"])
class BuildProblemTest : TestBase() {

    private lateinit var instance: TeamCityInstance

    override fun beforeClass() {
        super.beforeClass()
        assertTrue(haveCustomInstance())
        instance = customInstanceByConnectionFile()
    }

    @Test
    fun `fetch problems`() {
        val buildProblems = instance.build(BuildId("9")).buildProblems
        println(buildProblems.joinToString("\n"))
    }
}

