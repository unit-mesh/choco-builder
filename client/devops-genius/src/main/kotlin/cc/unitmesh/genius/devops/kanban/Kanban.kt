package cc.unitmesh.genius.devops.kanban

import cc.unitmesh.genius.devops.Issue

interface Kanban {
    fun fetch(id: String): Issue
}
