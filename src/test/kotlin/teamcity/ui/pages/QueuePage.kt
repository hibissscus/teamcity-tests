package teamcity.ui.pages

import com.codeborne.selenide.Selectors.byTagAndText
import com.codeborne.selenide.Selenide.element

// page_url = https://tests.teamcity.com/queue
class QueuePage {
    val pageUrl = "/queue"
    val pools = element(byTagAndText("div", "POOLS"))
    val allPools = element(byTagAndText("div", "All pools"))
    val default = element(byTagAndText("div", "Default"))
}