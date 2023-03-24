package teamcity.rest.build

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.jetbrains.teamcity.rest.BuildId
import org.jetbrains.teamcity.rest.TeamCityQueryException
import org.testng.annotations.Test
import teamcity.rest.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

//todo
class BuildRun : TestBase() {

    @Test
    fun `test get artifacts`() {
        val build = customInstanceByConnectionFile().builds()
            .fromConfiguration(dependantBuildConfiguration)
            .limitResults(15)
            .all()
            .first { it.getArtifacts().isNotEmpty() }

        val artifacts = build.getArtifacts("internal")
        assertTrue(artifacts.any {
            it.fullName == "internal/gradle_test_report.zip" && it.name == "gradle_test_report.zip" && it.size != null
        })

        val artifactsRecursive = build.getArtifacts("internal", recursive = true)
        assertEquals(artifactsRecursive.size, artifacts.size)
    }

    @Test
    fun `should be able to find artifact recursively`() {
        val build = publicInstance().builds()
            .fromConfiguration(dependantBuildConfiguration)
            .withNumber("5")
            .includeFailed()
            .limitResults(1)
            .all()
            .first()

        val thrownException = catchThrowable { build.findArtifact("gradle_test_*.zip", "org/jetbrains", false) }

        assertThat(thrownException).isInstanceOf(TeamCityQueryException::class.java)
            .hasMessageContaining("gradle_test_*.zip not found in build 5")

        val existingArtifact = build.findArtifact("gradle_test_*.zip", "org/jetbrains", true)

        assertEquals(existingArtifact.name, "gradle_test_report.zip")
    }

    @Test
    fun `test snapshot dependencies`() {
        val build = publicInstance().builds()
            .fromConfiguration(dependantBuildConfiguration)
            .limitResults(1)
            .all().first()

        assertTrue(build.snapshotDependencies.isNotEmpty())
    }

    @Test
    fun `test get tags`() {
        val build = publicInstance().build(BuildId("241"))

        assertTrue(build.tags.isNotEmpty())
        assertTrue(build.tags.contains("1.0"))
    }

    @Test
    fun `test resulting parameters`() {
        val build = customInstanceByConnectionFile().build(BuildId("17"))
        val resultingParameters = build.getResultingParameters()

        assertTrue(resultingParameters.isNotEmpty())
        assertEquals(147, resultingParameters.count())
        assertEquals("1", resultingParameters.first { it.name == "build.counter" }.value)
    }
}
