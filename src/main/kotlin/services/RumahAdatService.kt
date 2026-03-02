package org.delcom.services

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import org.delcom.data.AppException
import org.delcom.data.DataResponse
import org.delcom.data.RumahAdatRequest
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.IRumahAdatRepository
import java.io.File
import java.util.UUID

class RumahAdatService(private val repo: IRumahAdatRepository) {

    suspend fun getAll(call: ApplicationCall) {
        val search = call.request.queryParameters["search"] ?: ""
        val items = repo.getRumahAdatList(search)

        call.respond(
            DataResponse(
                "success",
                "Berhasil mengambil daftar rumah adat",
                mapOf(Pair("rumahAdat", items))
            )
        )
    }

    suspend fun getById(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID rumah adat tidak boleh kosong!")

        val item = repo.getRumahAdatById(id)
            ?: throw AppException(404, "Data rumah adat tidak tersedia!")

        call.respond(
            DataResponse(
                "success",
                "Berhasil mengambil data rumah adat",
                mapOf(Pair("rumahAdat", item))
            )
        )
    }

    private suspend fun getRequest(call: ApplicationCall): RumahAdatRequest {
        val req = RumahAdatRequest()

        val multipart = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 5)
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "nama" -> req.nama = part.value.trim()
                        "asal" -> req.asal = part.value.trim()
                        "deskripsi" -> req.deskripsi = part.value
                        "ciriKhas" -> req.ciriKhas = part.value
                        "fungsi" -> req.fungsi = part.value
                    }
                }

                is PartData.FileItem -> {
                    val ext = part.originalFileName
                        ?.substringAfterLast('.', "")
                        ?.let { if (it.isNotEmpty()) ".${it}" else "" }
                        ?: ""

                    val fileName = UUID.randomUUID().toString() + ext
                    val filePath = "uploads/rumah_adat/$fileName"

                    val file = File(filePath)
                    file.parentFile.mkdirs()

                    part.provider().copyAndClose(file.writeChannel())
                    req.pathGambar = filePath
                }

                else -> {}
            }
            part.dispose()
        }

        return req
    }

    private fun validate(req: RumahAdatRequest) {
        val v = ValidatorHelper(req.toMap())
        v.required("nama", "Nama tidak boleh kosong")
        v.required("asal", "Asal tidak boleh kosong")
        v.required("deskripsi", "Deskripsi tidak boleh kosong")
        v.required("ciriKhas", "Ciri khas tidak boleh kosong")
        v.required("fungsi", "Fungsi tidak boleh kosong")
        v.required("pathGambar", "Gambar tidak boleh kosong")
        v.validate()

        val file = File(req.pathGambar)
        if (!file.exists()) throw AppException(400, "Gambar rumah adat gagal diupload!")
    }

    suspend fun create(call: ApplicationCall) {
        val req = getRequest(call)
        validate(req)

        val exist = repo.getRumahAdatByName(req.nama)
        if (exist != null) {
            File(req.pathGambar).takeIf { it.exists() }?.delete()
            throw AppException(409, "Rumah adat dengan nama ini sudah terdaftar!")
        }

        val id = repo.addRumahAdat(req.toEntity())

        call.respond(
            DataResponse(
                "success",
                "Berhasil menambahkan data rumah adat",
                mapOf(Pair("rumahAdatId", id))
            )
        )
    }

    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID rumah adat tidak boleh kosong!")

        val old = repo.getRumahAdatById(id)
            ?: throw AppException(404, "Data rumah adat tidak tersedia!")

        val req = getRequest(call)
        if (req.pathGambar.isEmpty()) req.pathGambar = old.pathGambar

        validate(req)

        if (req.nama != old.nama) {
            val exist = repo.getRumahAdatByName(req.nama)
            if (exist != null) {
                File(req.pathGambar).takeIf { it.exists() }?.delete()
                throw AppException(409, "Rumah adat dengan nama ini sudah terdaftar!")
            }
        }

        if (req.pathGambar != old.pathGambar) {
            File(old.pathGambar).takeIf { it.exists() }?.delete()
        }

        val ok = repo.updateRumahAdat(id, req.toEntity())
        if (!ok) throw AppException(400, "Gagal memperbarui data rumah adat!")

        call.respond(DataResponse("success", "Berhasil mengubah data rumah adat", null))
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID rumah adat tidak boleh kosong!")

        val old = repo.getRumahAdatById(id)
            ?: throw AppException(404, "Data rumah adat tidak tersedia!")

        val ok = repo.removeRumahAdat(id)
        if (!ok) throw AppException(400, "Gagal menghapus data rumah adat!")

        File(old.pathGambar).takeIf { it.exists() }?.delete()

        call.respond(DataResponse("success", "Berhasil menghapus data rumah adat", null))
    }

    suspend fun getImage(call: ApplicationCall) {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest)

        val item = repo.getRumahAdatById(id) ?: return call.respond(HttpStatusCode.NotFound)
        val file = File(item.pathGambar)
        if (!file.exists()) return call.respond(HttpStatusCode.NotFound)

        call.respondFile(file)
    }
}
