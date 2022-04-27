/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERTSim;

/**
 *
 * @author thetr
 */
public final class ERSettings
{
    double SimulationDurationInHours;

    int PatientArrivalIntervalTimeInMinutes;
    int PatientArrivalPercentChance;

    int PatientAssistanceIntervalTimeInMinutes;
    int PatientAssistanceMinimumTimeInMinutes;
    int PatientAssistanceMaximumTimeInMinutes;

    boolean PatientAssistanceTimeIsInverselyProportionalToESI;

    public enum ERScenarioPresets {Normal,LocalEmergency,SlowNight,FluOutbreak,TownhouseFire,StadiumCollapse,Magnitude7Earthquake,StateOfWar}

    ERScenarioPresets ERScenario;

    int ScenarioPatientArrivals;
    int ScenarioTimeInMinutes;
    private int[] ScenarioSpecificWeightedESIChances = {100,100,100,100,100,1};

    public ERSettings()
    {
        this(ERScenarioPresets.Normal);
    }

    public ERSettings(ERScenarioPresets scenariotosetup)
    {
        SimulationDurationInHours = 6;
        PatientAssistanceIntervalTimeInMinutes = 5;
        PatientAssistanceMinimumTimeInMinutes = 10;
        PatientAssistanceMaximumTimeInMinutes = 45;

        PatientAssistanceTimeIsInverselyProportionalToESI = true;

        ERScenario = scenariotosetup;

        initializeScenarioSettings();
    }

        private void initializeScenarioSettings()
        {

            switch (ERScenario)
            {
                case Normal:
                    setupNormalSettings();
                    break;
                case LocalEmergency:
                    setupLocalEmergency();
                    break;
                case SlowNight:
                    setupSlowNight();
                    break;
                case FluOutbreak:
                    setupFluOutbreak();
                    break;
                case TownhouseFire:
                    setupTownhouseFire();
                    break;
                case StadiumCollapse:
                    setupStadiumCollapse();
                    break;
                case Magnitude7Earthquake:
                    setupMagnitude7Earthquake();
                    break;
                case StateOfWar:
                    setupStateOfWar();
                    break;
                default:
            }
        }

            private void setupNormalSettings()
            {
                ScenarioSpecificWeightedESIChances = new int[] {100,100,100,100,100,1};
                ScenarioPatientArrivals = -1;
                ScenarioTimeInMinutes = -1;

                PatientArrivalIntervalTimeInMinutes = 5;
                PatientArrivalPercentChance = 25;
            }

            private void setupLocalEmergency()
            {
                ScenarioSpecificWeightedESIChances = new int[] {100,100,100,100,50,5};
                ScenarioPatientArrivals = (int) (Math.random() * 45) + 5;
                ScenarioTimeInMinutes = (int) (Math.random() * 180) + 30;

                PatientArrivalIntervalTimeInMinutes = 5;
                PatientArrivalPercentChance = 65;
            }

            private void setupSlowNight()
            {
                ScenarioSpecificWeightedESIChances = new int[] {10,20,30,40,100,1};
                ScenarioPatientArrivals = -1;
                ScenarioTimeInMinutes = (int) (Math.random() * 6 * 60) + 6 * 60;

                PatientArrivalIntervalTimeInMinutes = 10;
                PatientArrivalPercentChance = 10;
            }

            private void setupFluOutbreak()
            {
                ScenarioSpecificWeightedESIChances = new int[] {100,100,100,100,70,1};
                ScenarioPatientArrivals = -1;
                ScenarioTimeInMinutes = (int) (Math.random() * 7 * 12 * 60) + 7 * 12 * 60;

                PatientArrivalIntervalTimeInMinutes = 5;
                PatientArrivalPercentChance = 35;
            }

            private void setupTownhouseFire()
            {
                ScenarioSpecificWeightedESIChances = new int[] {120,150,110,80,30,5};
                ScenarioPatientArrivals = (int) (Math.random() * 198) + 2;
                ScenarioTimeInMinutes = -1;

                PatientArrivalIntervalTimeInMinutes = 4;
                PatientArrivalPercentChance = 75;
            }

            private void setupStadiumCollapse()
            {
                ScenarioSpecificWeightedESIChances = new int[] {100,100,100,40,20,10};
                ScenarioPatientArrivals = (int) (Math.random() * 975) + 25;
                ScenarioTimeInMinutes = -1;

                PatientArrivalIntervalTimeInMinutes = 3;
                PatientArrivalPercentChance = 85;
            }

            private void setupMagnitude7Earthquake()
            {
                ScenarioSpecificWeightedESIChances = new int[] {150,150,120,70,05,15};
                ScenarioPatientArrivals = (int) (Math.random() * 9900) + 100;
                ScenarioTimeInMinutes = -1;

                PatientArrivalIntervalTimeInMinutes = 2;
                PatientArrivalPercentChance = 90;
            }

            private void setupStateOfWar()
            {
                ScenarioSpecificWeightedESIChances = new int[] {80,100,100,60,30,20};
                ScenarioPatientArrivals = -1;
                ScenarioTimeInMinutes = (int) (Math.random() * 4 * 7 * 12 * 60) + 7 * 12 * 60;

                PatientArrivalIntervalTimeInMinutes = 1;
                PatientArrivalPercentChance = 50;
            }

    public void updateScenario(ERScenarioPresets scenariotosetup)
    {
        initializeScenarioSettings();
    }

    public EmergencySeverityIndex weightedRandomESI()
    {
        int valueSum = 0;
        int valueCount = ScenarioSpecificWeightedESIChances.length;
        int[] valueRangeUpperBound = new int[valueCount];
        int randomValue = 0;
        int index = 0;

        for (int i = 0; i < valueCount; i++)
        {
            valueSum += ScenarioSpecificWeightedESIChances[i];
            valueRangeUpperBound[i] = valueSum;
        }

        randomValue = (int) (Math.random() * valueSum);

        while (index < valueRangeUpperBound.length - 1 && randomValue >= valueRangeUpperBound[index])
            index++;

        return EmergencySeverityIndex.values()[index];
    }

    @Override
    public String toString()
    {
        String stringToReturn = "\t⌐-------------------------------------------------------------¬\n";
        stringToReturn += "\t| " + "Settings:\n";
        stringToReturn += "\t| " + "SimulationDurationInHours: " + SimulationDurationInHours + "\n\n";
        stringToReturn += "\t| " + "PatientArrivalIntervalTimeInMinutes: " + PatientArrivalIntervalTimeInMinutes + "\n";
        stringToReturn += "\t| " + "PatientArrivalPercentChance: " + PatientArrivalPercentChance + "\n\n";
        stringToReturn += "\t| " + "PatientAssistanceIntervalTimeInMinutes: " + PatientAssistanceIntervalTimeInMinutes + "\n";
        stringToReturn += "\t| " + "PatientAssistanceMinimumTimeInMinutes: " + PatientAssistanceMinimumTimeInMinutes + "\n";
        stringToReturn += "\t| " + "PatientAssistanceMaximumTimeInMinutes: " + PatientAssistanceMaximumTimeInMinutes + "\n\n\n";
        stringToReturn += "\t| " + "PatientAssistanceTimeIsInverselyProportionalToESI: " + (PatientAssistanceTimeIsInverselyProportionalToESI ? "true" : "false") + "\n\n\n";
        stringToReturn += "\t| " + "ERScenario: " + ERScenario + "\n";
        stringToReturn += "\t| " + "ScenarioPatientArrivals: " + ScenarioPatientArrivals + "\n";
        stringToReturn += "\t| " + "ScenarioTimeInMinutes: " + ScenarioTimeInMinutes + "\n\n\n";

        for (int i = 0;i < EmergencySeverityIndex.values().length;i++)
            stringToReturn += "\t| " + "ScenarioSpecificWeightedESIChances[" + EmergencySeverityIndex.values()[i] + "]: " + ScenarioSpecificWeightedESIChances[i] + "%\n";

        stringToReturn += "\t└-------------------------------------------------------------┘\n";
        stringToReturn += "\t╘=============================================================╝\n\n";


        return stringToReturn;
    }
}
