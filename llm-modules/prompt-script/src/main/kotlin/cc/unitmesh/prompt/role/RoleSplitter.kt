package cc.unitmesh.prompt.role

class RoleSplitter {
    /**
     * for example:
     * ###system###
     * You are a helpful assistant.
     *
     * ###user###
     * ${question}
     *
     * will be split to:
     * mapOf(
     *    "system" to "You are a helpful assistant.",
     *    "user" to "${question}"
     * )
     */
    fun split(input: String): Map<String, String> {
        val sections = mutableMapOf<String, String>()
        val lines = input.lines()
        var currentSection = ""
        val contentBuilder = StringBuilder()

        for (line in lines) {
            if (line.startsWith("###") && line.endsWith("###")) {
                // Found a section header
                if (currentSection.isNotEmpty()) {
                    sections[currentSection] = contentBuilder.toString()
                    contentBuilder.clear()
                }
                currentSection = line.substring(3, line.length - 3)
            } else {
                // Append line to the current section's content
                contentBuilder.append(line).append("\n")
            }
        }

        // Add the last section if it exists
        if (currentSection.isNotEmpty()) {
            sections[currentSection] = contentBuilder.toString()
        }

        return sections
    }
}