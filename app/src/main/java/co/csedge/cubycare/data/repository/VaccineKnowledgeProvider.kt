package co.csedge.cubycare.data.repository

data class VaccineEducationalInfo(
    val vaccineName: String,
    val fullMedicalName: String,
    val diseasePrevented: String,
    val whyNeeded: String,
    val howItWorks: String,
    val administrationRoute: String,
    val normalReactions: String,
    val flowchartSteps: List<FlowchartStepData>
)

data class FlowchartStepData(
    val stepNumber: String,
    val title: String,
    val description: String
)

object VaccineKnowledgeProvider {

    fun getInfoForVaccine(rawName: String): VaccineEducationalInfo {
        val name = VaccineScheduleProvider.sanitizeVaccineName(rawName)
        
        return when {
            name.contains("BCG", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "BCG",
                fullMedicalName = "Bacillus Calmette–Guérin Vaccine",
                diseasePrevented = "Tuberculosis (TB) & TB Meningitis",
                whyNeeded = "Tuberculosis is a dangerous bacterial infection that attacks lungs, bones, and brain in infants. BCG is critical at birth because newborns have delicate immune systems highly vulnerable to severe TB infections.",
                howItWorks = "BCG contains a weakened strain of Mycobacterium bovis. Upon administration, it activates infantile T-lymphocytes, stimulating cellular immunity to recognize and destroy tuberculosis bacteria before they can spread.",
                administrationRoute = "Intradermal injection in the upper left arm at birth.",
                normalReactions = "A small raised bump appears after 2–4 weeks, which may form a tiny blister or scab and leaves a small permanent scar. This is completely normal and shows immunity is developing.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Weakened BCG Administered", "A harmless, attenuated Mycobacterium bacterium is injected into the top layer of skin at birth."),
                    FlowchartStepData("2", "Dendritic Cells Sound Alarm", "Local immune cells engulf the BCG antigens and present them to baby's helper T-cells in the lymph nodes."),
                    FlowchartStepData("3", "Cell-Mediated Immunity Activated", "T-cells release protective cytokines and build a specialized cellular defense network."),
                    FlowchartStepData("4", "Permanent Protection Established", "Long-lasting memory T-cells guard the lungs and nervous system against future TB exposure.")
                )
            )

            name.contains("OPV", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "OPV",
                fullMedicalName = "Oral Polio Vaccine (Zero / Booster Dose)",
                diseasePrevented = "Poliomyelitis (Infantile Paralysis)",
                whyNeeded = "Poliovirus attacks the central nervous system, causing irreversible muscle paralysis or death. OPV is essential to protect the intestinal tract where polio viruses replicate.",
                howItWorks = "Oral drops deliver weakened live poliovirus to the gut mucosa. The intestine produces secretory IgA antibodies that neutralize poliovirus right at the entry point of infection.",
                administrationRoute = "Oral drops administered directly into baby's mouth.",
                normalReactions = "Mild loose stools or irritability in rare cases. No injections or soreness involved.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Oral Drops Administered", "Two drops of oral polio vaccine are swallowed by the infant at birth or scheduled weeks."),
                    FlowchartStepData("2", "Gut Mucosal Immunity Triggered", "Vaccine strains replicate safely in the intestines, stimulating IgA antibody production."),
                    FlowchartStepData("3", "Systemic Antibodies Produced", "Bloodstream antibodies form to block any poliovirus from reaching the spinal nerve cells."),
                    FlowchartStepData("4", "Complete Paralysis Shield", "The baby develops dual gut mucosal and systemic paralysis immunity against Polio.")
                )
            )

            name.contains("Hep B", ignoreCase = true) || name.contains("Hepatitis B", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "Hepatitis B",
                fullMedicalName = "Hepatitis B Recombinant Surface Antigen Vaccine",
                diseasePrevented = "Hepatitis B Viral Liver Infection & Cirrhosis",
                whyNeeded = "Hepatitis B is a viral infection that infects the liver and can cause lifelong chronic liver failure or cancer. Giving the birth dose prevents mother-to-child vertical transmission during delivery.",
                howItWorks = "Contains purified Hepatitis B surface antigen (HBsAg). The immune system recognizes this protein shell and creates neutralizing anti-HBs antibodies without any risk of viral infection.",
                administrationRoute = "Intramuscular injection into the outer mid-thigh muscle.",
                normalReactions = "Mild soreness or redness at the thigh injection site for 1-2 days.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "HBsAg Antigen Injected", "Purified surface protein of Hepatitis B virus is injected safely into thigh muscle."),
                    FlowchartStepData("2", "B-Cells Detect Surface Protein", "Circulating B-lymphocytes recognize HBsAg and initiate antibody synthesis."),
                    FlowchartStepData("3", "Anti-HBs Antibodies Form", "High titers of neutralizing anti-HBs antibodies are generated to guard the liver cells."),
                    FlowchartStepData("4", "Lifelong Liver Protection", "Immune memory prevents Hepatitis B virus from infecting liver hepatocytes for life.")
                )
            )

            name.contains("DTaP", ignoreCase = true) || name.contains("DTP", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "DTaP / DTP",
                fullMedicalName = "Diphtheria, Tetanus & Acellular Pertussis Vaccine",
                diseasePrevented = "Diphtheria, Tetanus (Lockjaw) & Pertussis (Whooping Cough)",
                whyNeeded = "Diphtheria forms a suffocating membrane in the throat; Tetanus causes severe painful muscle spasms (lockjaw); Pertussis causes violent coughing fits that cause infants to stop breathing.",
                howItWorks = "Combines toxoids (inactivated bacterial toxins) for Diphtheria and Tetanus with purified Pertussis antigens. It trains the body to neutralize bacterial poisons before damage occurs.",
                administrationRoute = "Intramuscular injection in anterolateral thigh or upper arm.",
                normalReactions = "Mild fever, mild swelling or tenderness at injection site for 24-48 hours. Warm compress and prescribed pediatric paracetamol help.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Toxoids & Antigens Introduced", "Inactivated toxins of Diphtheria and Tetanus plus Pertussis components are injected."),
                    FlowchartStepData("2", "Antitoxin Secretion Started", "Immune system generates specific antitoxin antibodies against bacterial poisons."),
                    FlowchartStepData("3", "Pertussis Defense Active", "Antibodies bind to pertussis bacteria, preventing attachment to respiratory airways."),
                    FlowchartStepData("4", "Triple Protection Established", "Child is fully protected against throat blockage, lockjaw spasms, and whooping cough.")
                )
            )

            name.contains("IPV", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "IPV",
                fullMedicalName = "Inactivated Polio Vaccine (Salk Vaccine)",
                diseasePrevented = "Polio Nerve Paralysis (Types 1, 2 & 3)",
                whyNeeded = "IPV contains killed poliovirus strains that provide strong bloodstream immunity, ensuring complete nerve and brain protection against wild or mutated polio strains.",
                howItWorks = "Inactivated (killed) polioviruses of all 3 types are introduced into muscle. B-cells produce neutralizing IgG antibodies in the bloodstream.",
                administrationRoute = "Intramuscular injection in thigh.",
                normalReactions = "Slight redness or mild soreness at injection site.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Killed Poliovirus Introduced", "Inactivated poliovirus types 1, 2, and 3 are injected into muscle tissue."),
                    FlowchartStepData("2", "Systemic IgG Production", "Immune cells respond by producing high-affinity IgG antibodies in circulating blood."),
                    FlowchartStepData("3", "Bloodstream Barrier Created", "Circulating antibodies neutralize any polio virus before it can cross into motor nerve cells."),
                    FlowchartStepData("4", "Zero Paralysis Guarantee", "Motor neurons in the spinal cord remain completely safe from viral attack.")
                )
            )

            name.contains("Rotavirus", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "Rotavirus Vaccine",
                fullMedicalName = "Live Attenuated Rotavirus Oral Vaccine",
                diseasePrevented = "Severe Rotavirus Diarrhea, Vomiting & Dehydration",
                whyNeeded = "Rotavirus is the #1 cause of severe, life-threatening watery diarrhea and dehydration in infants under 2 years old, frequently requiring hospitalization.",
                howItWorks = "Oral liquid drops contain weakened rotavirus strains that colonize the infant small intestine safely, building protective mucosal and systemic immunity against wild rotavirus.",
                administrationRoute = "Oral liquid drops swallowed by baby.",
                normalReactions = "Mild temporary irritability or mild loose stools in rare instances.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Oral Liquid Drops Swallowed", "Sweet oral vaccine liquid is administered directly into infant's mouth."),
                    FlowchartStepData("2", "Intestinal Mucosa Shield", "Weakened viral particles stimulate gut lining Peyer's patches to release IgA antibodies."),
                    FlowchartStepData("3", "Villus Damage Blocked", "Antibodies prevent wild rotavirus from attaching to and damaging intestinal epithelial villi."),
                    FlowchartStepData("4", "Dehydration Prevention", "Baby is protected from watery gastroenteritis, severe vomiting, and dehydration.")
                )
            )

            name.contains("PCV", ignoreCase = true) || name.contains("Pneumococcal", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "PCV (Pneumococcal)",
                fullMedicalName = "Pneumococcal Conjugate Vaccine",
                diseasePrevented = "Pneumonia, Bacterial Meningitis, Sepsis & Middle Ear Infections",
                whyNeeded = "Streptococcus pneumoniae bacteria cause severe lung infections (pneumonia), ear pain, and brain lining inflammation (meningitis) in young infants.",
                howItWorks = "Conjugates polysaccharide capsular sugars from pneumococcal serotypes to a carrier protein. This enables immature infant immune systems to generate robust B-cell antibody memory.",
                administrationRoute = "Intramuscular injection into thigh.",
                normalReactions = "Low grade fever, temporary fussiness, or localized thigh soreness.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Conjugated Polysaccharides Injected", "Bacterial surface sugars linked to carrier protein are injected into muscle."),
                    FlowchartStepData("2", "B & T-Cell Dual Activation", "Carrier protein stimulates T-helper cells, helping B-cells manufacture potent anti-capsular antibodies."),
                    FlowchartStepData("3", "Opsonization & Phagocytosis", "Antibodies coat pneumococcal bacteria, making them easily destroyed by white blood cells."),
                    FlowchartStepData("4", "Lungs & Brain Shielded", "Prevents pneumococcal bacteria from invading lung alveoli, bloodstream, or meninges.")
                )
            )

            name.contains("Hib", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "Hib Vaccine",
                fullMedicalName = "Haemophilus Influenzae Type B Conjugate Vaccine",
                diseasePrevented = "Hib Bacterial Meningitis, Epiglottitis & Pneumonia",
                whyNeeded = "Hib bacteria can invade the brain membranes within hours causing brain damage, deafness, or life-threatening throat swelling (epiglottitis).",
                howItWorks = "Combines Hib polyribosylribitol phosphate (PRP) capsular antigen with protein carrier to generate high levels of protective bactericidal antibodies.",
                administrationRoute = "Intramuscular injection into thigh.",
                normalReactions = "Mild fever or redness at injection site.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Hib PRP Antigen Injected", "Capsular polysaccharide conjugated with protein is injected into thigh muscle."),
                    FlowchartStepData("2", "PRP Antibodies Generated", "High levels of anti-PRP protective antibodies fill bloodstream and spinal fluid."),
                    FlowchartStepData("3", "Bacterial Capsule Dissolved", "Antibodies dissolve bacterial capsules upon any Hib exposure."),
                    FlowchartStepData("4", "Brain & Airway Protected", "Infant stays safe from bacterial meningitis and emergency airway blockage.")
                )
            )

            name.contains("MMR", ignoreCase = true) || name.contains("Measles", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "MMR",
                fullMedicalName = "Measles, Mumps & Rubella Combined Live Vaccine",
                diseasePrevented = "Measles (Rubeola), Mumps (Salivary Gland Swelling) & Rubella (German Measles)",
                whyNeeded = "Measles causes dangerous high fever, severe brain inflammation (encephalitis) and pneumonia; Mumps causes painful salivary swelling and deafness; Rubella causes birth defects.",
                howItWorks = "Contains live attenuated viruses for Measles, Mumps, and Rubella. The immune system develops triple protective antibodies for long-lasting viral immunity.",
                administrationRoute = "Subcutaneous injection in upper arm.",
                normalReactions = "Mild fever or light skin spots 7-10 days after shot (completely harmless immune sign).",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Triple Attenuated Virus Injected", "Weakened strains of Measles, Mumps, and Rubella are injected subcutaneously."),
                    FlowchartStepData("2", "Triple Immune Response", "Immune system recognizes all three viral antigens simultaneously."),
                    FlowchartStepData("3", "Neutralizing Antibodies Synthesized", "High titers of specific anti-measles, anti-mumps, and anti-rubella IgG antibodies accumulate."),
                    FlowchartStepData("4", "Lifelong Triple Shield", "Child is immune against high measles fever, mumps neck swelling, and rubella rash.")
                )
            )

            name.contains("Flu", ignoreCase = true) || name.contains("Influenza", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "Influenza (Flu)",
                fullMedicalName = "Quadrivalent Inactivated Influenza Vaccine",
                diseasePrevented = "Seasonal Flu, Bronchitis & High Fever Complications",
                whyNeeded = "Influenza virus mutates annually and causes severe respiratory illness, high fever, and hospitalization in toddlers and young children.",
                howItWorks = "Introduces inactivated antigens for 4 current flu virus strains (H1N1, H3N2, B strains), stimulating protective hemagglutination-inhibiting antibodies.",
                administrationRoute = "Intramuscular injection in arm or thigh.",
                normalReactions = "Mild arm/thigh soreness or low fever for 1 day.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Seasonal Antigens Injected", "Inactivated proteins from 4 circulating flu strains are injected."),
                    FlowchartStepData("2", "Hemagglutinin Antibodies Form", "Body creates specialized antibodies that block flu virus hemagglutinin spikes."),
                    FlowchartStepData("3", "Respiratory Lining Protected", "Antibodies line nose and lung mucosal membranes to catch flu viruses early."),
                    FlowchartStepData("4", "Winter Protection", "Reduces risk of flu fever, bronchitis, and emergency hospital visits.")
                )
            )

            name.contains("Varicella", ignoreCase = true) || name.contains("Chickenpox", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "Varicella (Chickenpox)",
                fullMedicalName = "Varicella-Zoster Live Attenuated Vaccine",
                diseasePrevented = "Chickenpox (Itchy Blisters, Fever & Skin Infections)",
                whyNeeded = "Chickenpox causes hundreds of painful, intensely itchy fluid-filled blisters across the body, high fever, and risk of severe secondary bacterial skin infections.",
                howItWorks = "Live attenuated Varicella-Zoster Oka strain stimulates robust T-cell and antibody immunity, preventing chickenpox rash and latent nerve infection.",
                administrationRoute = "Subcutaneous injection in upper arm.",
                normalReactions = "Mild tenderness or 2-3 harmless small spots near injection site.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Weakened Varicella Virus Injected", "Safe, attenuated chickenpox virus is introduced under the skin."),
                    FlowchartStepData("2", "T-Cell & B-Cell Defense", "Immune system develops both cellular and humoral immunity against Varicella-Zoster."),
                    FlowchartStepData("3", "Vascular & Skin Protection", "Antibodies neutralize chickenpox virus before it can spread to skin capillaries."),
                    FlowchartStepData("4", "Itch-Free Skin Immunity", "Child is protected from itchy rash, fever, and chickenpox scarring.")
                )
            )

            name.contains("Typhoid", ignoreCase = true) -> VaccineEducationalInfo(
                vaccineName = "Typhoid Conjugate (TCV)",
                fullMedicalName = "Typhoid Vi Capsular Polysaccharide Conjugate Vaccine",
                diseasePrevented = "Typhoid Fever & Intestinal Perforation",
                whyNeeded = "Salmonella Typhi bacteria spread through water and food, causing prolonged high fever, severe abdominal pain, and intestinal bleeding in young children.",
                howItWorks = "Conjugates Vi capsular antigen to tetanus toxoid protein, boosting immunological memory and protective antibody levels against Salmonella Typhi.",
                administrationRoute = "Intramuscular injection in upper arm.",
                normalReactions = "Mild soreness at injection site for 1 day.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Vi Conjugate Antigen Injected", "Salmonella Typhi surface antigen linked to carrier protein is injected into muscle."),
                    FlowchartStepData("2", "Anti-Vi Antibodies Generated", "B-cells produce long-lasting IgG anti-Vi polysaccharide protective antibodies."),
                    FlowchartStepData("3", "Bacterial Invasiveness Blocked", "Antibodies bind to typhoid bacteria, preventing them from invading bowel lymphoid tissue."),
                    FlowchartStepData("4", "Safe Food & Water Immunity", "Child is protected against prolonged high typhoid fever and gastrointestinal complications.")
                )
            )

            else -> VaccineEducationalInfo(
                vaccineName = name,
                fullMedicalName = "$name Pediatric Vaccine",
                diseasePrevented = "Childhood Infections & Illnesses",
                whyNeeded = "$name is recommended by pediatric guidelines to protect your child against serious preventable bacterial or viral infections during early developmental years.",
                howItWorks = "Introduces safe, non-harmful antigens into your child's body. The immune system learns to recognize these antigens, producing specialized antibodies and memory cells for long-term defense.",
                administrationRoute = "Administered by healthcare professionals via oral drops or mild injection.",
                normalReactions = "Mild fever or local site tenderness for 1-2 days is a normal sign that the immune system is actively responding.",
                flowchartSteps = listOf(
                    FlowchartStepData("1", "Safe Vaccine Introduced", "A tiny, harmless piece of the germ (antigen) is introduced safely into the body."),
                    FlowchartStepData("2", "Immune System Detects Antigens", "Specialized white blood cells sound the alarm and identify the antigen structure."),
                    FlowchartStepData("3", "Antibodies Synthesized", "B-cells manufacture custom antibodies designed specifically to neutralize this germ."),
                    FlowchartStepData("4", "Long-Term Immunity Shield", "The body stores memory cells. If exposed in the future, the immune system defeats the germ instantly.")
                )
            )
        }
    }
}
