package co.csedge.cubycare.data.model

data class Disorder(
    val name: String,
    val duration: String,
    val reasons: String,
    val isCurable: Boolean,
    val simpleMedication: String,
    val pediatricianName: String,
    val pediatricianSpecialization: String,
    val pediatricianLocation: String,
    val hospitalTimings: String,
    val exactHospitalAddress: String,
    val mainSymptoms: String = "",
    val avoidance: String = "",
    val flowchartSteps: List<FlowchartStep>
)

data class FlowchartStep(
    val title: String,
    val description: String
)
