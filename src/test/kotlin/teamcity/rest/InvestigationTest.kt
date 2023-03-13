package teamcity.rest

import org.jetbrains.teamcity.rest.InvestigationScope
import org.jetbrains.teamcity.rest.InvestigationTargetType
import org.jetbrains.teamcity.rest.ProjectId
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class InvestigationTest : TestBase() {

    @Test
    fun `test limit`() {
        val investigations = customInstance
            .investigations()
            .limitResults(3)
            .all()
        assertEquals(investigations.count(), 3)
        investigations.forEach {
            callPublicPropertiesAndFetchMethods(it)
        }
    }

    @Test
    fun `test assignee`() {
        var hasAtLeastOneName = false
        val investigations = customInstance.investigations().limitResults(10).all()
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
    fun `test reporter`() {
        var hasAtLeastOneReporter = false
        val investigations = customInstance.investigations().limitResults(10).all()
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
    fun `test for project`() {
        val filteredInvestigations =
            customInstance.investigations().forProject(ProjectId("ProjectForSidebarCounters")).all()
        val allInvestigations = customInstance.investigations().all()
        assertTrue(filteredInvestigations.count() > 0)
        assertTrue(filteredInvestigations.count() < allInvestigations.count())
    }

    @Test
    fun `test assignment project for test investigations`() {
        val investigations = customInstance.investigations().withTargetType(InvestigationTargetType.TEST).all()
        assertTrue(investigations.all { inv -> inv.scope is InvestigationScope.InProject })
    }

    @Test
    fun `test assignment project for build problems investigations`() {
        val investigations =
            customInstance.investigations().withTargetType(InvestigationTargetType.BUILD_PROBLEM).all()
        assertTrue(investigations.all { inv -> inv.scope is InvestigationScope.InProject })
    }

    @Test
    fun `test assignment project for build type`() {
        val investigations =
            customInstance.investigations().withTargetType(InvestigationTargetType.BUILD_CONFIGURATION).all()
        assertTrue(investigations.all { inv -> inv.scope is InvestigationScope.InBuildConfiguration })
    }

    @Test
    fun `test with type`() {
        val allInvestigations = customInstance.investigations().all()

        var filteredInvestigations =
            customInstance.investigations().withTargetType(InvestigationTargetType.TEST).all()
        assertTrue(filteredInvestigations.any())
        assertTrue(filteredInvestigations.all { it.targetType == InvestigationTargetType.TEST })
        assertTrue(filteredInvestigations.count() < allInvestigations.count())

        filteredInvestigations =
            customInstance.investigations().withTargetType(InvestigationTargetType.BUILD_PROBLEM).all()
        assertTrue(filteredInvestigations.any())
        assertTrue(filteredInvestigations.all { it.targetType == InvestigationTargetType.BUILD_PROBLEM })
        assertTrue(filteredInvestigations.count() < allInvestigations.count())

        filteredInvestigations =
            customInstance.investigations().withTargetType(InvestigationTargetType.BUILD_CONFIGURATION).all()
        assertTrue(filteredInvestigations.any())
        assertTrue(filteredInvestigations.all { it.targetType == InvestigationTargetType.BUILD_CONFIGURATION })
        assertTrue(filteredInvestigations.count() < allInvestigations.count())
    }

    @Test
    fun `test all investigations`() {
        val investigations = customInstance.investigations().all()
        assertTrue(investigations.count() > 0)
    }
}
