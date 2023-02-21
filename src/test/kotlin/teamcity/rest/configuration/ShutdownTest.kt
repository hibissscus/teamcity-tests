package teamcity.rest.configuration

import org.testng.annotations.Test

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
