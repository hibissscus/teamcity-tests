package teamcity.ui.pages

import com.codeborne.selenide.Selenide.element

// page_url = https://tests.teamcity.com/favorite/projects
class MainPage {
    val imageAvatar = element("img[data-test='avatar']")

}