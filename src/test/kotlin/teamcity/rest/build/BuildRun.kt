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

class BuildRun : TestBase() {

    @Test
    fun `test to string`() {
        val builds = customInstanceByConnectionFile().builds()
            .fromConfiguration(testBuildConfiguration.id)
            .limitResults(3)
            .all()

        builds.forEach {
            it.getArtifacts()
            callPublicPropertiesAndFetchMethods(it)
        }
    }

    @Test
    fun `since date and until date`() {
        val monthAgo = GregorianCalendar()
        monthAgo.add(Calendar.MONTH, -1)
        val weekAgo = GregorianCalendar()
        monthAgo.add(Calendar.DAY_OF_MONTH, -7)

        val builds = publicInstance().builds()
            .fromConfiguration(testBuildConfiguration.id)
            .limitResults(3)
            .since(monthAgo.toInstant())
            .until(weekAgo.toInstant())
            .all()

        for (build in builds) {
            assert(build.startDateTime!! >= monthAgo.toZonedDateTime() && build.startDateTime!! <= weekAgo.toZonedDateTime())
        }
    }

    @Test
    fun `test build fetch revisions`() {
        publicInstance().builds()
            .fromConfiguration(testBuildConfiguration.id)
            .limitResults(10)
            .all()
            .forEach {
                val revisions = it.revisions
                assertTrue(revisions.isNotEmpty())
            }
    }

    @Test
    fun `test fetch status`() {
        val build = publicInstance().builds()
            .fromConfiguration(testBuildConfiguration.id)
            .limitResults(1)
            .all().first()

        build.statusText
    }

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
    fun `test get webUrl`() {
        val build = publicInstance().builds()
            .fromConfiguration(changesBuildConfiguration)
            .limitResults(1)
            .all().first()

        assertEquals("$publicInstanceUrl/viewLog.html?buildId=${build.id.stringId}", build.getHomeUrl())
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
    fun `test pagination`() {
        val iterator = publicInstance().builds()
            .fromConfiguration(manyTestsBuildConfiguration)
            .all()
            .iterator()

        var i = 0
        while (i++ < 100) {
            assertTrue(iterator.hasNext())
            assertNotNull(iterator.next())
        }
    }

    @Test
    fun `test parameters`() {
        val build = customInstanceByConnectionFile().build(BuildId("241"))
        val parameters = build.parameters

        assertTrue(parameters.isNotEmpty())
        assertEquals(4, parameters.count())
        assertEquals("0", parameters.first { it.name == "system.FAILED_TESTS_PERCENTAGE" }.value)
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
