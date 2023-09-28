@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "chocolate-factory"

include(":server")

include(":cocoa-core")

include(":dsl:design")

include(":rag-modules:document")
include(":rag-modules:rag-script")
include(":rag-modules:store-milvus")
include(":rag-modules:store-pinecone")
include(":rag-modules:store-elasticsearch")

include(":llm-modules:sentence-transformers")
include(":llm-modules:openai")
include(":llm-modules:connection-manager")
include(":llm-modules:prompt-script")

include(":code:interpreter")
include(":code:code-splitter")

include(":tools:web-tools")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

