@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "chocolate-factory"

include(":cocoa-core")
include(":cocoa-local-embedding")

include(":dsl:design")
include(":rag-modules:document")
include(":rag-modules:store-milvus")
include(":rag-modules:store-pinecone")
include(":rag-modules:store-elasticsearch")

include(":code:interpreter")
include(":code:code-splitter")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

