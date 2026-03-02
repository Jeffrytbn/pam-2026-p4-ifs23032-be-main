package org.delcom.data

import kotlinx.serialization.Serializable
import org.delcom.entities.RumahAdat

@Serializable
data class RumahAdatRequest(
    var nama: String = "",
    var asal: String = "",
    var deskripsi: String = "",
    var ciriKhas: String = "",
    var fungsi: String = "",
    var pathGambar: String = "",
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nama" to nama,
            "asal" to asal,
            "deskripsi" to deskripsi,
            "ciriKhas" to ciriKhas,
            "fungsi" to fungsi,
            "pathGambar" to pathGambar
        )
    }

    fun toEntity(): RumahAdat {
        return RumahAdat(
            nama = nama,
            asal = asal,
            deskripsi = deskripsi,
            ciriKhas = ciriKhas,
            fungsi = fungsi,
            pathGambar =  pathGambar,
        )
    }
}
