package teamcity.rest

import org.jetbrains.teamcity.rest.BuildLocator
import org.jetbrains.teamcity.rest.BuildStatus
import org.testng.annotations.Test
import kotlin.test.assertTrue

class BranchesTest : TestBase() {

    @Test
    fun `default branch`() {
        getBuildsNoBranches()
            .withAllBranches()
            .withStatus(BuildStatus.SUCCESS)
            .limitResults(20)
            .all().forEach {
                assertTrue(it.branch.isDefault)
            }
    }

    @Test
    fun `list works`() {
        getBuildsBranches()
            .withAllBranches()
            .withStatus(BuildStatus.SUCCESS)
            .limitResults(20)
            .all().forEach {
                it.parameters
                it.changes.joinToString("\n")
                it.revisions
                it.getArtifacts()
            }
    }

    @Test
    fun `multi branches`() {
        val branches = mutableSetOf<String>()
        getBuildsBranches()
            .withAllBranches()
            .withStatus(BuildStatus.FAILURE)
            .limitResults(50)
            .all().forEach {
                branches += it.branch.name!!
                println(it)
            }

        assertTrue(branches.size > 1, "Actual branches: $branches")
    }

    @Test
    fun `single branch`() {
        val branches = mutableSetOf<String>()
        getBuildsBranches()
            .withStatus(BuildStatus.SUCCESS)
            .limitResults(50)
            .all().forEach {
                branches += it.branch.name!!
                println(it)
            }

        assertTrue(branches.size == 1, "Actual branches: $branches")
    }

    private fun getBuildsBranches(): BuildLocator {
        return publicInstance()
            .builds()
            .fromConfiguration(changesBuildConfiguration)
    }

    private fun getBuildsNoBranches(): BuildLocator {
        return publicInstance()
            .builds()
            .fromConfiguration(testBuildRunConfiguration.id)
    }

}
