package teamcity.rest

import org.apache.log4j.*
import org.jetbrains.teamcity.rest.TeamCityInstance
import org.jetbrains.teamcity.rest.TeamCityInstanceFactory
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.valueParameters

const val teamcityConnectionFilePath = "teamcity_connection.properties"
const val publicInstanceUrl = "https://e2e.teamcity.com"

fun setupLog4jDebug() {
    LogManager.resetConfiguration()
    Logger.getRootLogger().removeAllAppenders()
    Logger.getRootLogger().addAppender(ConsoleAppender(PatternLayout("TEST[%d] %6p [%15.15t] - %30.30c - %m %n")))
    Logger.getLogger("teamcity").level = Level.DEBUG
    Logger.getLogger("org.apache.http").level = Level.ERROR
}

fun publicInstance() = TeamCityInstanceFactory.guestAuth(publicInstanceUrl).withLogResponses()

fun customInstance(serverUrl: String, token: String) = TeamCityInstanceFactory.tokenAuth(serverUrl, token).withLogResponses()

fun haveCustomInstance(): Boolean = ConnectionPropertiesFileLoader(teamcityConnectionFilePath).validate()

fun customInstanceByConnectionFile(): TeamCityInstance {
    val connectionPropertiesFileLoader = ConnectionPropertiesFileLoader(teamcityConnectionFilePath)
    return if (connectionPropertiesFileLoader.validate()) {
        val connectionConfig = connectionPropertiesFileLoader.fetch()
        customInstance(
                connectionConfig.serverUrl, connectionConfig.token
        )
    } else {
        publicInstance()
    }
}

internal class ConnectionPropertiesFileLoader(filePath: String) {

    private val connectionFile: File?

    init {
        val classLoader = javaClass.classLoader
        connectionFile = classLoader.getResource(filePath)?.let { File(it.file) }
    }

    fun fetch(): ConnectionConfig {
        if (!validate()) {
            throw IllegalStateException("Properties are invalid")
        }
        val connectionProperties = Properties()
        connectionProperties.load(connectionFile?.let { FileInputStream(it) })
        return ConnectionConfig(
                connectionProperties.getProperty(SERVER_URL), connectionProperties.getProperty(TOKEN)
        )
    }

    fun validate(): Boolean {
        if (connectionFile == null || !connectionFile.exists()) return false
        val connectionProperties = Properties()
        connectionProperties.load(FileInputStream(connectionFile))
        return validateConnectionProperties(connectionProperties)
    }

    private fun validateConnectionProperties(connectionProperties: Properties): Boolean {
        return validPropertyValue(connectionProperties.getProperty(SERVER_URL)) && validPropertyValue(
                connectionProperties.getProperty(TOKEN)
        )
    }

    private fun validPropertyValue(value: String?): Boolean {
        return !value.isNullOrEmpty()
    }

    companion object {
        val SERVER_URL = "serverUrl"
        val TOKEN = "token"
    }
}

internal class ConnectionConfig(val serverUrl: String, val token: String)

inline fun <reified T> callPublicPropertiesAndFetchMethods(instance: T) {
    instance.toString()

    for (member in T::class.members) {
        when (member) {
            is KProperty<*> -> {
                member.getter.call(instance)
                println("${member.name} = ${member.getter.call(instance)}")
            }

            is KFunction<*> -> if (member.name.startsWith("fetch") || member.name.startsWith("get")) {
                if (member.valueParameters.isEmpty()) {
                    member.call(instance)
                    println("${member.name} = ${member.call(instance)}")
                }
            }
        }
    }
}
