package teamcity.ui.pages.admin

import com.codeborne.selenide.Selenide.element
import org.openqa.selenium.By

// page_url = https://tests.teamcity.com/admin/admin.html
class AdminPage {
    val pageUrl = "/admin/admin.html"

    // project-related settings
    val linkProjects = element(By.xpath("//*[contains(@href,'item=projects')]"))
    val linkBuilds = element(By.xpath("//*[contains(@href,'item=builds')]"))
    val linkBuildTime = element(By.xpath("//*[contains(@href,'item=buildTime')]"))
    val linkDiskUsage = element(By.xpath("//*[contains(@href,'item=diskUsage')]"))
    val linkHealthStatus = element(By.xpath("//*[contains(@href,'item=healthStatus')]"))
    val linkAudit = element(By.xpath("//*[contains(@href,'item=audit')]"))

    // user management
    val linkUsers = element(By.xpath("//*[contains(@href,'item=users')]"))
    val linkGroups = element(By.xpath("//*[contains(@href,'item=groups')]"))
    val linkRoles = element(By.xpath("//*[contains(@href,'item=roles')]"))
    val linkInvitations = element(By.xpath("//*[contains(@href,'item=Invite Users')]"))

    // server administration
    val linkSubscriptionAndResources = element(By.xpath("//*[contains(@href,'item=subscriptionAndResources')]"))
    val linkAuthentication = element(By.xpath("//*[contains(@href,'item=auth')]"))
    val linkProjectsImport = element(By.xpath("//*[contains(@href,'item=import')]"))
    val linkUsageStatistics = element(By.xpath("//*[contains(@href,'item=usageStatistics')]"))
}