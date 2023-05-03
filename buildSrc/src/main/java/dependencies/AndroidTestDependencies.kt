package dependencies

import Versions

object AndroidTestDependencies {
  const val instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  const val junit = "androidx.test.ext:junit:${Versions.androidTestJunit}"
  const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
}