@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "chocolate-factory"

include(":server")

include(":cocoa-core")

include(":docs-builder")

include(":dsl:design")

include(":rag-modules:document")
include(":rag-modules:rag-script")
include(":rag-modules:store-milvus")
include(":rag-modules:store-pinecone")
include(":rag-modules:store-elasticsearch")

include(":llm-modules:sentence-transformers")
include(":llm-modules:openai")
include(":llm-modules:connection")
include(":llm-modules:prompt-script")

include(":code:interpreter")
include(":code:code-splitter")

include(":llm-tools:web-tools")

//include(":client:prompt-script-cli")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

