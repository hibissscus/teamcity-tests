package teamcity.rest.configuration

import org.jetbrains.teamcity.rest.InvestigationScope
import org.jetbrains.teamcity.rest.InvestigationTargetType
import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.testProject
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InvestigationTest : TestBase() {

    @Test
    fun `investigation exist`() {
        val investigations = teamCityInstance.investigations().all()
        assertTrue(investigations.count() > 0)
    }

    @Test
    fun `assignee to someone`() {
        var hasAtLeastOneName = false
        val investigations = teamCityInstance.investigations().limitResults(10).all()
        for (investigation in investigations) {
            assertNotNull(investigation.assignee)
            assertNotNull(investigation.assignee.id)
            assertNotNull(investigation.assignee.username)
            if (investigation.assignee.name != null) {
                hasAtLeastOneName = true
            }
        }
        assertTrue(hasAtLeastOneName)
    }

    @Test
    fun `has a reporter`() {
        var hasAtLeastOneReporter = false
        val investigations = teamCityInstance.investigations().limitResults(10).all()
        for (investigation in investigations) {
            if (investigation.reporter != null) {
                hasAtLeastOneReporter = true
                assertNotNull(investigation.reporter?.id)
                assertNotNull(investigation.reporter?.username)
            }
        }
        assertTrue(hasAtLeastOneReporter)
    }

    @Test
    fun `exists for specific project`() {
        val filteredInvestigations =
                teamCityInstance.investigations().forProject(testProject.id).all()
        val allInvestigations = teamCityInstance.investigations().all()
        assertTrue(filteredInvestigations.count() > 0)
        assertTrue(filteredInvestigations.count() <= allInvestigations.count())
    }

    @Test
    fun `test assignment project for test investigations`() {
        val investigations = teamCityInstance.investigations().withTargetType(InvestigationTargetType.TEST).all()
        assertTrue(investigations.all { inv -> inv.scope is InvestigationScope.InProject })
    }

    @Test
    fun `test assignment project for build problems investigations`() {
        val investigations =
                teamCityInstance.investigations().withTargetType(InvestigationTargetType.BUILD_PROBLEM).all()
        assertTrue(investigations.all { inv -> inv.scope is InvestigationScope.InProject })
    }

    @Test
    fun `test assignment project for build type`() {
        val investigations =
                teamCityInstance.investigations().withTargetType(InvestigationTargetType.BUILD_CONFIGURATION).all()
        assertTrue(investigations.all { inv -> inv.scope is InvestigationScope.InBuildConfiguration })
    }
}
