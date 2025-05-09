package com.nepreconsultintg.edigital.dependences

import com.nepreconsultintg.edigital.repository.LoginRepository
import com.nepreconsultintg.edigital.datasource.EdigitalDataSource
import com.nepreconsultintg.edigital.viewmodels.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModules = module {
    singleOf(::LoginRepository)
    singleOf(::EdigitalDataSource)
    viewModelOf(::LoginViewModel)
}