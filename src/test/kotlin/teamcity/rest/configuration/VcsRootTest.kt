package teamcity.rest.configuration

import org.jetbrains.teamcity.rest.VcsRootId
import org.jetbrains.teamcity.rest.VcsRootLocator
import org.testng.annotations.Test
import teamcity.rest.TestBase
import teamcity.rest.customInstanceByConnectionFile
import teamcity.rest.testProjectVcs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Test(groups = ["configuration"])
class VcsRootTest : TestBase() {

    @Test
    fun `vcs roots are loaded`() {
        val vcsRootLocator = vcsRoots()
        val vcsRoots = vcsRootLocator.all().toList()
        assertTrue(vcsRoots.isNotEmpty(), "Some vcs roots should be loaded")
    }

    @Test
    fun `vcs root is loaded by id`() {
        val vcsRootId = VcsRootId(testProjectVcs.id.stringId)
        val vcsRoot = customInstanceByConnectionFile().vcsRoot(vcsRootId)
        assertNotNull(vcsRoot, "Vcs root should be loaded")
    }

    @Test
    fun `test get url`() {
        val vcsRoot = vcsRoots().all().first()
        val url = vcsRoot.url
        assertNotNull(url, "Vcs root url should be loaded")
    }

    @Test
    fun `test get default branch`() {
        val vcsRoot = vcsRoots().all().first()
        val url = vcsRoot.defaultBranch
        assertNotNull(url, "Vcs root default branch should be loaded")
    }

    private fun vcsRoots(): VcsRootLocator {
        return teamCityInstance.vcsRoots()
    }

}