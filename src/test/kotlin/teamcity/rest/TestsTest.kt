package teamcity.rest

import org.testng.annotations.Test


class TestsTest : TestBase() {

    @Test
    fun `test deprecated list tests`() {
        val tests = customInstance.builds()
            .fromConfiguration(testBuildConfiguration.id)
            .limitResults(3)
            .all()
            .first().testRuns().toList()

        println("Total tests: ${tests.size}")
        println(tests.joinToString("\n"))
    }

    @Test
    fun `test runs tests`() {
        val tests = customInstance.builds()
            .fromConfiguration(testBuildConfiguration.id)
            .limitResults(3)
            .all()
            .first().testRuns().toList()

        println("Total tests: ${tests.size}")
        println(tests.joinToString("\n"))
    }
}
