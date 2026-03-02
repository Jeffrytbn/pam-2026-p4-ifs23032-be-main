package org.delcom.dao

import org.delcom.tables.RumahAdatTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class RumahAdatDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, RumahAdatDAO>(RumahAdatTable)

    var nama by RumahAdatTable.nama
    var pathGambar by RumahAdatTable.pathGambar
    var asal by RumahAdatTable.asal
    var deskripsi by RumahAdatTable.deskripsi
    var ciriKhas by RumahAdatTable.ciriKhas
    var fungsi by RumahAdatTable.fungsi
    var createdAt by RumahAdatTable.createdAt
    var updatedAt by RumahAdatTable.updatedAt
}
