package yummi.test

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode

internal val JSON = JsonNodeFactory.instance!!

internal fun JsonNodeFactory.objectNode(vararg properties: Pair<String, JsonNode>) =
    this.objectNode().setAll<ObjectNode>(mapOf(*properties))