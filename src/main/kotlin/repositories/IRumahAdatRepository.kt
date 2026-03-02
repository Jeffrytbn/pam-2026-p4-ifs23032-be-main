package org.delcom.repositories

import org.delcom.entities.RumahAdat

interface IRumahAdatRepository {
    suspend fun getRumahAdatList(search: String): List<RumahAdat>
    suspend fun getRumahAdatById(id: String): RumahAdat?
    suspend fun getRumahAdatByName(name: String): RumahAdat?
    suspend fun addRumahAdat(data: RumahAdat): String
    suspend fun updateRumahAdat(id: String, newData: RumahAdat): Boolean
    suspend fun removeRumahAdat(id: String): Boolean
}
