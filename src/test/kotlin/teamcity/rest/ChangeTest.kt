package teamcity.rest

import org.jetbrains.teamcity.rest.BuildConfigurationId
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ChangeTest : TestBase() {

    @Test
    fun `test web url`() {
        val configuration = publicInstance().buildConfiguration(changesBuildConfiguration)
        val change = publicInstance().builds()
            .fromConfiguration(configuration.id)
            .limitResults(10)
            .all()
            .firstOrNull { it.changes.isNotEmpty() }
            .let { build ->
                assert(build != null) {
                    "Unable to find a build with changes (tried top 10) in ${configuration.getHomeUrl(branch = "<default>")}"
                }
                build!!.changes.first()
            }
        assertEquals(
            "$publicInstanceUrl/viewModification.html?modId=${change.id.stringId}",
            change.getHomeUrl()
        )
        assertEquals(
            "$publicInstanceUrl/viewModification.html?modId=${change.id.stringId}&personal=true&buildTypeId=xxx",
            change.getHomeUrl(specificBuildConfigurationId = BuildConfigurationId("xxx"), includePersonalBuilds = true)
        )
    }

    @Test
    fun `change by vcs revision`() {
        val build = publicInstance().builds()
            .fromConfiguration(changesBuildConfiguration)
            .limitResults(10)
            .all()
            .first { it.changes.isNotEmpty() }
        val change = build.changes.first()

        assertEquals(
            change.toString(),
            publicInstance().change(changesBuildConfiguration, change.version).toString()
        )
        assertTrue(change.firstBuilds().map { it.toString() }.contains(build.toString()))
    }

    @Test
    fun `build by vcs revision`() {
        val build = publicInstance().builds()
            .fromConfiguration(changesBuildConfiguration)
            .limitResults(10)
            .all()
            .first { it.changes.isNotEmpty() }
        val change = build.changes.first()

        val builds = publicInstance().builds()
            .fromConfiguration(changesBuildConfiguration)
            .withVcsRevision(change.version)
            .all()
        assertTrue(builds.map { it.toString() }.contains(build.toString()))
    }

    @Test
    fun `associated vcs root reported`() {
        val build = publicInstance().builds()
            .fromConfiguration(changesBuildConfiguration)
            .limitResults(10)
            .all()
            .firstOrNull { it.changes.isNotEmpty() && it.changes.any { change -> change.vcsRootInstance != null } }

        assertNotNull(build, "no vcsRootInstance found")
    }
}