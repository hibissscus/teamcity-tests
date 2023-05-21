package teamcity.ui.pages

import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selenide.element

// page_url = https://e2e.teamcity.com/changes
class ChangesPage {
    val pageUrl = "/changes"
    val h1Changes = element(byText("Changes"))
}