package teamcity.rest

import org.jetbrains.teamcity.rest.*
import kotlin.random.Random

data class VcsRoot(
    val random: Int = Random.nextInt(10000, 50000),
    val id: VcsRootId = VcsRootId("VcsRoot$random"),
    val name: String = "VcsRootName$random",
    val type: VcsRootType = VcsRootType.GIT,
    val properties: Map<String, String> = mapOf()
)

data class User(
    val random: Int = Random.nextInt(10000, 50000),
    val id: UserId = UserId("$random"),
    val username: String = "Username$random",
    val name: String = "User$random",
    val email: String = "UserEmail$random@teamcity.com",
)

data class BuildConfiguration(
    val projectId: ProjectId,
    val random: Int = Random.nextInt(10000, 50000),
    val id: BuildConfigurationId = BuildConfigurationId("$random"),
    val name: String = "BuildConfigurationName$random",
    val paused: Boolean = false,
    val buildTags: List<String> = listOf(),
    val finishBuildTriggers: List<FinishBuildTrigger> = listOf(),
    val artifactDependencies: List<ArtifactDependency> = listOf(),
)

data class Project(
    val random: Int = Random.nextInt(10000, 50000),
    val id: ProjectId = ProjectId("$random"),
    val name: String = "ProjectName$random",
    val archived: Boolean = false,
    val parentProjectId: ProjectId? = null,
    val childProjects: List<Project> = listOf(),
    val buildConfigurations: List<BuildConfiguration> = listOf(),
    val parameters: List<Parameter> = listOf(),
)


val rootProjectId = ProjectId("_Root")
val testProject = Project(1, ProjectId("TeamcityTests"), "Teamcity Tests", false, rootProjectId)
val rootProject = Project(0, rootProjectId, "<Root project>", false, null, listOf(testProject))
val testProjectVcs = VcsRoot(
    0,
    VcsRootId("TeamcityTests_HttpsGithubComHibissscusTeamcityTestsGitRefsHeadsMain"),
    "https://github.com/hibissscus/teamcity-tests.git#refs/heads/main",
    VcsRootType.GIT,
    mapOf()
)
val testUserHibissscus = User(
    1,
    UserId("1"),
    "hibissscus",
    "Sergei Stepanov",
    "hibissscus@gmail.com"
)
val testBuildConfiguration = BuildConfiguration(testProject.id, 0, BuildConfigurationId("TeamcityTests_Test"), "Run Tests")
val testBuildRunConfiguration =
    BuildConfiguration(testProject.id, 0, BuildConfigurationId("TeamcityTests_TestProjectForRestBuild"), "TestProjectForRest_Build")


val reportProject = ProjectId("ProjectForReports")
val pausedBuildConfiguration = BuildConfigurationId("ProjectForReports_TestPaused")
val changesBuildConfiguration = BuildConfigurationId("ProjectForSidebarCounters_MultibranchChange")
val testsBuildConfiguration = BuildConfigurationId("ProjectForSidebarCounters_MultibranchTestResult")
val dependantBuildConfiguration = BuildConfigurationId("TeamcityTestMetadataDemo_TestMetadataDemo")
val manyTestsBuildConfiguration = BuildConfigurationId("TeamcityTestData_Test")