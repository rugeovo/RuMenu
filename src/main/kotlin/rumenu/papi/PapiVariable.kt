package rumenu.papi

import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion
import rumenu.profile.FileConfig.variableTable

object PapiVariable: PlaceholderExpansion {

    override val identifier: String = "rm"
    override fun onPlaceholderRequest(player: Player?, args: String): String {
        val uuid = player?.uniqueId.toString()
        return uuid.let { variableTable.get(it,args) }
    }

}