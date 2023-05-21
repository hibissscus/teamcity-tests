package teamcity.ui.pages

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selectors.byAttribute
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selenide.element

// page_url = https://e2e.teamcity.com/favorite/projects
class ProjectPage {
    val pageUrl = "/favorite/projects"
    val allProjects = element(Selectors.byTagAndText("span", "All Projects"))
    val projects = element(Selectors.byTagAndText("span", "PROJECTS"))
    val archivedToggle = element(byAttribute("data-test", "ring-toggle-input"))
    val imageAvatar = element("img[data-test='avatar']")
    val logout = element(byText("Logout"))

    fun logout() {
        imageAvatar.shouldHave(Condition.visible)
        imageAvatar.click()
        logout.shouldHave(Condition.visible)
        logout.click()
    }
}