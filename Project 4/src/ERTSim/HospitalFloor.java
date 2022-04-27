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
public final class HospitalFloor
{
    HospitalRoom[] ERrooms;
    HospitalWaitingRoom WaitingRoom;
    HospitalRoom Freezer;

    private ERSettings FloorSimSettings;

    private boolean debugMode = false;

    // Note: Also have to change values in constructors.
    private final int DEFAULT_NUMBER_OF_ER_ROOMS = 4;
    private final int DEFAULT_CAPACITY_OF_WAITING_ROOM = 200;
    private final int DEFAULT_CAPACITY_OF_FREEZER = 50;

    public HospitalFloor(int numberofERrooms, int patientcapacityofwaitingroom, int capacityoffreezer, ERSettings simulationsettings)
    {
        if (numberofERrooms >= 0)
            ERrooms = new HospitalRoom[numberofERrooms];
        else
            ERrooms = new HospitalRoom[0];

        for (int i = 0;i < ERrooms.length;i++)
            ERrooms[i] = new HospitalRoom("ER Room " + i, 1);

        if (patientcapacityofwaitingroom >= 0)
            WaitingRoom = new HospitalWaitingRoom("Waiting Room", patientcapacityofwaitingroom);
        else
            WaitingRoom = new HospitalWaitingRoom("Waiting Room with Capacity: 0", 0);

        if (capacityoffreezer >= 0)
            Freezer = new HospitalRoom("Freezer with Capacity: " + capacityoffreezer, capacityoffreezer);
        else
            Freezer = new HospitalRoom("Freezer with Capacity: 0", 0);

        FloorSimSettings = simulationsettings;
    }

        public HospitalFloor()
        {
            this(4,200,50,new ERSettings());
        }

        public HospitalFloor(int numberofERrooms)
        {
            this(numberofERrooms,200,50,new ERSettings());
        }

        public HospitalFloor(int numberofERrooms, int patientcapacityofwaitingroom)
        {
            this(numberofERrooms,patientcapacityofwaitingroom,10,new ERSettings());
        }

        public HospitalFloor(int numberofERrooms, int patientcapacityofwaitingroom, int capacityoffreezer)
        {
            this(numberofERrooms,patientcapacityofwaitingroom,capacityoffreezer,new ERSettings());
        }

            private void initializeERrooms()
            {
                for (int i = 0; i < ERrooms.length; i++)
                    ERrooms[i] = new HospitalRoom();
            }

        public void setFloorSimSettings(ERSettings settingstoset)
        {
            FloorSimSettings = settingstoset;
        }

        public void updateFloorSimScenario(ERSettings.ERScenarioPresets newScenario)
        {
            FloorSimSettings.updateScenario(newScenario);
        }

        public int calculateEstimatedAssistanceTimeInMinutes(ERPatient patienttoassist)
        {
            int estNeededAssistanceInMinutes;

            if (FloorSimSettings.PatientAssistanceTimeIsInverselyProportionalToESI)
                estNeededAssistanceInMinutes = generateStricterEstimatedAssistanceTimeInMinutes(patienttoassist);
            else
                estNeededAssistanceInMinutes = generateDNAbasedEstimatedAssistanceTimeInMinutes(patienttoassist);

            if (estNeededAssistanceInMinutes < FloorSimSettings.PatientAssistanceMinimumTimeInMinutes)
                estNeededAssistanceInMinutes = FloorSimSettings.PatientAssistanceMinimumTimeInMinutes;
            else if (estNeededAssistanceInMinutes > FloorSimSettings.PatientAssistanceMaximumTimeInMinutes)
                estNeededAssistanceInMinutes = FloorSimSettings.PatientAssistanceMaximumTimeInMinutes;

            if (debugMode == true) System.out.println("Patient " + patienttoassist + " needs an actual estimated " + estNeededAssistanceInMinutes + " minutes of assistance in the ER.\n");

            return estNeededAssistanceInMinutes;
        }

            private int generateStricterEstimatedAssistanceTimeInMinutes(ERPatient patienttoassist)
            {
                int minutesMinimum = FloorSimSettings.PatientAssistanceMinimumTimeInMinutes;
                int minutesRange = FloorSimSettings.PatientAssistanceMaximumTimeInMinutes - minutesMinimum;
                int numberOfESIVals = EmergencySeverityIndex.values().length - 1; // -1 because not using ded
                int esiEffectiveValue = numberOfESIVals - patienttoassist.getESIstatus().ordinal();
                double ESIproportion = (double) esiEffectiveValue / numberOfESIVals;

                int randomPart = (int) (Math.random() * 7 - 4);
                int proportionalPart = (int) (ESIproportion * minutesRange);

                int estNeededAssistanceInMinutes = Math.abs(Math.round(minutesMinimum + proportionalPart + randomPart)) + 1;

                return estNeededAssistanceInMinutes;
            }

            private int generateDNAbasedEstimatedAssistanceTimeInMinutes(ERPatient patienttoassist)
            {
                int minutesMinimum = FloorSimSettings.PatientAssistanceMinimumTimeInMinutes;
                int minutesRange = FloorSimSettings.PatientAssistanceMaximumTimeInMinutes - minutesMinimum;
                int numberOfESIVals = EmergencySeverityIndex.values().length - 1; // -1 because not using ded
                int esiEffectiveValue = numberOfESIVals - patienttoassist.getESIstatus().ordinal();
                double ESIproportion = esiEffectiveValue / numberOfESIVals;

                int randomPart = (int) (patienttoassist.moduloBaseOnDNA(minutesRange) / 2);
                int proportionalPart = (int) (ESIproportion * minutesRange / 2);

                int estNeededAssistanceInMinutes = minutesMinimum + randomPart + proportionalPart;

                return estNeededAssistanceInMinutes;
            }

        public EmergencySeverityIndex generateESIstatusOfNewPatient()
        {
            return FloorSimSettings.weightedRandomESI();
        }

        public int getPatientArrivalIntervalTimeInMinutes()
        {
            return FloorSimSettings.PatientArrivalIntervalTimeInMinutes;
        }

        public int getPatientArrivalPercentChance()
        {
            return FloorSimSettings.PatientArrivalPercentChance;
        }

        public int getPatientAssistanceIntervalTimeInMinutes()
        {
            return FloorSimSettings.PatientAssistanceIntervalTimeInMinutes;
        }

        public String getScenarioSummary()
        {
            // public enum ERScenarioPresets {Normal,LocalEmergency,SlowNight,FluOutbreak,TownhouseFire,StadiumCollapse,Magnitude7Earthquake,StateOfWar}
            String stringToReturn = "";
            String[] scenarioSummaries = new String[] {"nothing unusual happening right now, so it should be a normal shift",
                                                       "a local emergency relatively nearby, so we expect an increase of patients",
                                                       "nothing much happening. It might be a slow shift, fingers-crossed",
                                                       "a flu outbreak in this county, so be extra mindful of everyone's microbiome",
                                                       "a serious fire in the next town over, I heard. It's not going to be a pretty day",
                                                       "a tragic happening at the stadium. The ambulances are on their way. Expect we'll be overwelmed",
                                                       "a friggin earthquake on the news!! This is going to be the toughest shift of your life. Do your best",
                                                       "still political unrest in our wartorn region. I hope it ends soon. Let's do our part"};

            stringToReturn = scenarioSummaries[FloorSimSettings.ERScenario.ordinal()];

            return stringToReturn;
        }

        public int getScenarioTimeInMinutes()
        {
            return FloorSimSettings.ScenarioTimeInMinutes;
        }

        public int getScenarioPatientArrivals()
        {
            return FloorSimSettings.ScenarioPatientArrivals;
        }

}
