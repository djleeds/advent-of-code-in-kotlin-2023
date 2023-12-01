plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

dependencies {
    implementation("com.willowtreeapps.assertk:assertk:0.27.0")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
