package com.kien.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
/**
 * PetClubApplication class
 * Application class for the Pet Club app.
 * This class initializes things at the application level, such as dependency injection with Hilt.
 *
 * @created Date: 27/02/2024, Author: Thinh Huynh
 */
@HiltAndroidApp
class PetClubApplication : Application()