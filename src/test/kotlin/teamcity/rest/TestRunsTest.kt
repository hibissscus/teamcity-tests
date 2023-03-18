package teamcity.rest

import org.jetbrains.teamcity.rest.Build
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestRunsTest : TestBase() {

    @Test
    fun `test limit`() {
        val testBuild: Build = teamCityInstance
            .builds()
            .fromConfiguration(testBuildConfiguration.id)
            .latest() ?: throw IllegalArgumentException("At least one build should be found")

        val testRuns = teamCityInstance.testRuns().forBuild(testBuild.id).limitResults(3).all()
        assertEquals(testRuns.count(), 2)
        testRuns.forEach {
            callPublicPropertiesAndFetchMethods(it)
        }
    }

    @Test
    fun `test for build`() {
        val testBuild: Build = teamCityInstance
            .builds()
            .fromConfiguration(testBuildConfiguration.id)
            .latest() ?: throw IllegalArgumentException("At least one build should be found")

        val testRuns = teamCityInstance.testRuns().forBuild(testBuild.id).limitResults(3).all()
        assertTrue(testRuns.any())
        assertTrue(testRuns.all { x -> teamCityInstance.build(x.buildId).buildConfigurationId == testBuildConfiguration.id })
    }
}
