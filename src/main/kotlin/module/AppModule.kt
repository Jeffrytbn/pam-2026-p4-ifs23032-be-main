package org.delcom.module

import org.delcom.repositories.IPlantRepository
import org.delcom.repositories.IRumahAdatRepository
import org.delcom.repositories.PlantRepository
import org.delcom.repositories.RumahAdatRepository
import org.delcom.services.PlantService
import org.delcom.services.ProfileService
import org.delcom.services.RumahAdatService
import org.koin.dsl.module

val appModule = module {
    // Plant Repository
    single<IPlantRepository> { PlantRepository() }

    // Plant Service
    single { PlantService(get()) }

    // Rumah Adat Repository
    single<IRumahAdatRepository> { RumahAdatRepository() }

    // Rumah Adat Service
    single { RumahAdatService(get()) }

    // Profile Service
    single { ProfileService() }
}
