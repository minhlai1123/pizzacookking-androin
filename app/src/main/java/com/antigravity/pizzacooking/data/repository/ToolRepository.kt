package com.antigravity.pizzacooking.data.repository

import com.antigravity.pizzacooking.data.models.Tool

object ToolRepository {
    private val masterTools: Map<String, Tool> = listOf(
        Tool("TOOL-001", "Rolling Pin", "PREPARATION", listOf("ROLL", "FLATTEN"), listOf("ING-001", "ING-003", "ING-004", "ING-005", "ING-007", "ING-009", "ING-010"), "SWIPE"),
        Tool("TOOL-002", "Chef Knife", "CUTTING", listOf("SLICE", "CHOP", "NOTCH"), listOf("ING-002", "ING-030", "ING-031", "ING-032", "ING-033", "ING-040", "ING-041", "ING-050", "ING-051"), "SWIPE"),
        Tool("TOOL-003", "Wire Whisk", "MIXING", listOf("WHISK", "BEAT", "FROTH"), listOf("ING-013", "ING-017"), "SWIPE"),
        Tool("TOOL-004", "Glass Blender", "APPLIANCE", listOf("BLEND", "PUREE"), listOf("ING-013", "ING-050", "ING-051", "ING-052"), "HOLD"),
        Tool("TOOL-005", "Box Grater", "GRATING", listOf("GRATE", "SHRED"), listOf("ING-021", "ING-022", "ING-025", "ING-026"), "SWIPE"),
        Tool("TOOL-006", "Sauce Ladle", "SPREADING", listOf("SCOOP", "SPREAD", "SWIRL"), listOf("ING-011", "ING-012", "ING-013", "ING-014", "ING-015", "ING-018", "ING-020"), "SWIPE"),
        Tool("TOOL-007", "Pizza Peel", "BAKING", listOf("SLIDE_IN", "RETRIEVE"), emptyList(), "SWIPE"),
        Tool("TOOL-008", "Roller Cutter", "SERVING", listOf("SLICE_WHEEL", "DIVIDE"), emptyList(), "SWIPE"),
        Tool("TOOL-009", "Piping Bag", "DECORATING", listOf("PIPE", "DRAW_PATTERN"), listOf("ING-060"), "SWIPE"),
        Tool("TOOL-010", "Squeeze Bottle", "DISPENSING", listOf("SQUEEZE", "ZIGZAG"), listOf("ING-016", "ING-019", "ING-061"), "SWIPE"),
        Tool("TOOL-011", "Honey Dipper", "DRIZZLING", listOf("TWIRL", "DRIZZLE"), listOf("ING-062"), "SWIPE"),
        Tool("TOOL-012", "Shaped Cookie Cutters", "SHAPING", listOf("STAMP", "PRESS_CUT"), listOf("ING-006", "ING-009", "ING-023"), "TAP"),
        Tool("TOOL-013", "Glass Popcorn Pan", "COOKING", listOf("HEAT_POP", "SHAKE"), listOf("ING-070"), "HOLD"),
        Tool("TOOL-014", "Wooden Truffle Slicer", "SHAVING", listOf("SHAVE", "FLAKE"), listOf("ING-035"), "SWIPE"),
        Tool("TOOL-015", "Silicone Chili Tongs", "HANDLING", listOf("PICK_PLACE", "PINCH"), listOf("ING-036", "ING-037", "ING-038"), "DRAG_AND_DROP")
    ).associateBy { it.toolId }

    fun getAllTools(): List<Tool> = masterTools.values.toList()

    fun getToolById(id: String): Tool? = masterTools[id]

    fun hasTool(id: String): Boolean = masterTools.containsKey(id)
}
