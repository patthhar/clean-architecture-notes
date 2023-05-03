package dependencies

import Versions

object Dependencies {
  const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
  const val material = "com.google.android.material:material:${Versions.material}"
  const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
  const val firebaseFirestore =
    "com.google.firebase:firebase-firestore-ktx:${Versions.firebaseFirestore}"
  const val firebaseAnalytics =
    "com.google.firebase:firebase-analytics-ktx:${Versions.firebaseAnalytics}"
  const val firebaseAuth = "com.google.firebase:firebase-auth-ktx:${Versions.firebaseAuth}"
  const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx:${Versions.firebaseCrashlyticsKtx}"
}