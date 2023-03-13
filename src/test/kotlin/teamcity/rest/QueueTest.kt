package teamcity.rest

import org.jetbrains.teamcity.rest.ProjectId
import org.testng.annotations.Test

class QueueTest : TestBase() {

    @Test
    fun `test queue all properties thus evaluating them`() {
        publicInstance().buildQueue().queuedBuilds().forEach {
            it.toString()
            println(it)
        }
    }

    @Test
    fun `test project queue all properties thus evaluating them`() {
        publicInstance().buildQueue().queuedBuilds(ProjectId("Test Project")).forEach {
            it.toString()
            println(it)
        }
    }
}