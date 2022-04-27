package ERTSim;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thetr
 * twigpiiii
 */
public final class ERTriageSimulation
{
    public static void main(String[] args)
    {
        ERStats SimulationStatistics;
        ERSettings SimulationSettings = new ERSettings(ERSettings.ERScenarioPresets.Magnitude7Earthquake);
//      Required Settings
        SimulationSettings.SimulationDurationInHours = (double) 6;
        /*
        SimulationSettings.PatientArrivalIntervalTimeInMinutes = 5;
        SimulationSettings.PatientArrivalPercentChance = 25;
        SimulationSettings.PatientAssistanceMinimumTimeInMinutes = 10;
        SimulationSettings.PatientAssistanceMaximumTimeInMinutes = 45;
        SimulationSettings.PatientAssistanceIntervalTimeInMinutes = 5;
        SimulationSettings.PatientAssistanceTimeIsInverselyProportionalToESI = true;
//      Playing Around Settings
/*
        SimulationSettings.SimulationDurationInHours = (double) 6;
        SimulationSettings.PatientArrivalIntervalTimeInMinutes = 5;
        SimulationSettings.PatientArrivalPercentChance = 25;
        SimulationSettings.PatientAssistanceMinimumTimeInMinutes = 10;
        SimulationSettings.PatientAssistanceMaximumTimeInMinutes = 0;
        SimulationSettings.PatientAssistanceIntervalTimeInMinutes = 5;
        SimulationSettings.PatientAssistanceTimeIsInverselyProportionalToESI = true;
*/

        ER EmergencyDepartmentSim = new ER(ER.Hospital.DasCrazKlinik);

        SimulationStatistics = EmergencyDepartmentSim.runER(SimulationSettings);

        System.out.println(SimulationStatistics);
    }
}
