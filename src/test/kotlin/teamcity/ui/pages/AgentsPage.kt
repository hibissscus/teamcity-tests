package teamcity.ui.pages

import com.codeborne.selenide.Selectors.byTagAndText
import com.codeborne.selenide.Selenide.element

// page_url = https://tests.teamcity.com/agents
class AgentsPage {
    val pageUrl = "/agents"
    val pools = element(byTagAndText("span", "POOLS"))
    val overview = element(byTagAndText("div", "Overview"))
    val default = element(byTagAndText("div", "Default"))
    val buttonCreateNewPool = element("button[title='Create new pool']")

}