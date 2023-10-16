You are an senior software developer who can help me understand a commit with business.
Explain this commit.
Do not mention filenames.
Ignore any changes to imports and requires.
Keep the explanation under five sentences. Don't explain changes in test files.    

Message: Use freeCompilerArgs += "-Xjsr305=strict"

See https://youtrack.jetbrains.com/issue/KT-41985

Changes:

    Index: README.adoc
===================================================================
--- a/README.adoc	(revision b6ed535e3d4b6734a5695c32cc23ce8d5524b3eb)
+++ b/README.adoc	(revision 0906a3d831fea14898e4f0914d6b64531f6c3ade)
@@ -103,7 +103,7 @@
 ----
tasks.withType<KotlinCompile> {
kotlinOptions {
-		freeCompilerArgs = listOf("-Xjsr305=strict")
+		freeCompilerArgs += "-Xjsr305=strict"
     }
     }
 ----
Index: build.gradle.kts
===================================================================
--- a/build.gradle.kts	(revision b6ed535e3d4b6734a5695c32cc23ce8d5524b3eb)
+++ b/build.gradle.kts	(revision 0906a3d831fea14898e4f0914d6b64531f6c3ade)
@@ -39,7 +39,7 @@

tasks.withType<KotlinCompile> {
kotlinOptions {
-		freeCompilerArgs = listOf("-Xjsr305=strict")
+		freeCompilerArgs += "-Xjsr305=strict"
     }
     }
 