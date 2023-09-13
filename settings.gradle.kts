@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "chocolate-factory"

include(":cocoa-core")
include(":code-interpreter")
include(":cocoa-local-embedding")

include(":dsl:design")
include(":rag-modules:code-splitter")
include(":rag-modules:milvus")
include(":rag-modules:pinecone")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

