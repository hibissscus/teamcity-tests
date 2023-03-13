package teamcity.rest.configuration

import org.testng.annotations.Test
import teamcity.rest.TestBase

@Test(groups = ["configuration"])
class ShutdownTest : TestBase() {

    @Test
    fun `test empty`() {
        publicInstance.close()
    }

    @Test
    fun `test used`() {
        publicInstance.use {
            it.rootProject()
        }
    }
}
