package cc.unitmesh.prompt.validate;

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class CodeCompletionValidatorTest {

    @Test
    fun `validate should return true when data structures are found for Java language`() {
        val llmResult = """{
  "language": "java",
  "beforeCursor": "package com.afs.unittest;\n\nimport com.afs.unittest.exception.UnexpectedProjectTypeException;\nimport com.afs.unittest.expense.ExpenseType;\nimport com.afs.unittest.Project.Project;\nimport com.afs.unittest.Project.ProjectType;\n\nimport static com.afs.unittest.expense.ExpenseType.*;\n\nclass ExpenseService {\n    ExpenseType getExpenseCodeByProjectTypeAndName(Project project) {",
  "relatedCode": "// class UnexpectedProjectTypeException {\n// \n//  }\n// \n// class ExpenseType {\n// \n//  }\n// \n// class Project {\n//    : ProjectType\n//    : String\n// \n//    'getter/setter: getProjectName\n// \n//  }\n// \n// class ProjectType {\n// \n//  }\n// ",
  "output": "        ProjectType projectType = project.getProjectType();\n        if(projectType == ProjectType.INTERNAL){\n            return INTERNAL_PROJECT_EXPENSE;\n        }else{\n            if(projectType == ProjectType.EXTERNAL){\n                String projectName = project.getProjectName();\n                if(projectName.equals(\"Project A\")){\n                    return EXPENSE_TYPE_A;\n                }\n\n                if(projectName.equals(\"Project B\")){\n                    return EXPENSE_TYPE_B;\n                }\n\n                return OTHER_EXPENSE;\n            }\n\n            throw new UnexpectedProjectTypeException(\"You enter invalid project type\");\n        }\n    }"
}"""

        val jsonObject: JsonObject = JsonParser.parseString(llmResult).getAsJsonObject()
        val output = jsonObject.get("output").asString

        val validator = CodeCompletionValidator(output, "$.beforeCursor", jsonObject, "java")

        // given

        // when
        val result = validator.validate()

        // then
        assertTrue(result)
    }
    @Test
    fun `validate should return false and log error when data structures are not found for Java language`() {
        val llmResult = """{
  "language": "typescript",
  "beforeCursor": "let fs = require('fs')\nlet LRU = require('lru-cache')\nlet cache = new LRU({\n  max: 500\n})\nlet path = require('path')\n\nimport Utils from './utils'\n\nlet DEFAULT_CONFIG = {\n  language: 'en',\n  path: Utils.getWorkDir() + '/docs/adr/',\n  prefix: '',\n  digits: 4,\n  editor: 'code',\n  force_nfc: false\n}\n\nfunction getAllConfig (defaultValue: string) {",
  "relatedCode": "",
  "output": "  if (!fs.existsSync(Utils.getWorkDir() + '/.adr.json')) {\n    return defaultValue\n  }\n  let config = fs.readFileSync(Utils.getWorkDir() + '/.adr.json', 'utf8')\n  try {\n    let parsedConfig = JSON.parse(config)\n    cache.set('config', parsedConfig)\n\n    return parsedConfig\n  } catch (e) {\n    console.error(e)\n    return defaultValue\n  }\n}"
}"""

        val jsonObject: JsonObject = JsonParser.parseString(llmResult).getAsJsonObject()
        val output = jsonObject.get("output").asString

        val validator = CodeCompletionValidator(output, "$.beforeCursor", jsonObject, "java")

        // when
        val result = validator.validate()

        // then
        assertFalse(result)
        // assert log error was called
    }
//
//    @Test
//    fun `validate should return true when data structures are found for Kotlin language`() {
//        // given
//        val kotlinValidator = CodeCompletionValidator("llmResult", "$.selection", JsonElement(), "kotlin")
//
//        // when
//        val result = kotlinValidator.validate()
//
//        // then
//        assertTrue(result)
//    }
//
//    @Test
//    fun `validate should return false and log error when data structures are not found for Kotlin language`() {
//        // given
//        val invalidKotlinValidator = CodeCompletionValidator("llmResult", "$.selection", JsonElement(), "kotlin")
//
//        // when
//        val result = invalidKotlinValidator.validate()
//
//        // then
//        assertFalse(result)
//        // assert log error was called
//    }
//
//    @Test
//    fun `validate should return true when language is not Java or Kotlin`() {
//        // given
//        val otherLanguageValidator = CodeCompletionValidator("llmResult", "$.selection", JsonElement(), "python")
//
//        // when
//        val result = otherLanguageValidator.validate()
//
//        // then
//        assertTrue(result)
//    }
//
//    @Test
//    fun `validate should log error for unsupported language`() {
//        // given
//        val invalidLanguageValidator = CodeCompletionValidator("llmResult", "$.selection", JsonElement(), "invalid")
//
//        // when
//        val result = invalidLanguageValidator.validate()
//
//        // then
//        assertFalse(result)
//        // assert log error was called
//    }
}