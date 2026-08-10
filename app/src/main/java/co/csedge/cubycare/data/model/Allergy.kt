package co.csedge.cubycare.data.model

data class Allergy(
    val name: String,
    val duration: String,
    val causes: String,
    val isCurable: Boolean,
    val homeRemedies: String,
    val pediatricianName: String,
    val pediatricianSpecialization: String,
    val pediatricianLocation: String,
    val hospitalTimings: String,
    val exactHospitalAddress: String,
    val mainSymptoms: String = "",
    val avoidance: String = "",
    val flowchartSteps: List<FlowchartStep>
)
