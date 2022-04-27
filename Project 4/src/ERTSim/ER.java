/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERTSim;

import java.text.DecimalFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.ArrayList;

/**
 *
 * @author thetr
 */
public final class ER
{
    private final String[][] TNP = new String[][] {{"Collin","Michelle"},{"Blackbeard","Cheng"},{"F. Gordon","Caster"},{"Nikola","Curie"},{"Liston","Soto Lidia"},{"Lee","Brooke"}};
    private final String[] ClockAdj = new String[] {"friendly","menacing","sleek","intricate","odd-looking","broken"};
    private final String[] WeWillCallYou = new String[] {"as soon as we can","before my beard grows another inch","in just a few moments","as soon as time allows","shortly. Be strong","at some point"};
    private final String[] FarewellToYou = new String[] {"Be safe, friend.","Come again soon, Matey.","Ciao.","Stay sharp.","Auf Wiedersehen.",""};

    public enum Hospital{TwigpiHospital,G_Allow_Hospital,HospitalExpress,StrokeOfGeniusHospital,DasCrazKlinik,LemMemHospital}

    private int RNGseed = (int) (Math.random() * Integer.MAX_VALUE);

    private Hospital FacilityName;
   private String nameOfTNP;
    private HospitalFloor ERMainFloor;
    private LinkedList<ERPatient> PatientDischargeList;

    private int SimulationMinutesElapsed = 0;
    private Clock SimulationStartTime;

    private boolean debugMode = false;

    public ER(Hospital nameoffacility)
    {
        FacilityName = nameoffacility;
        nameOfTNP = TNP[FacilityName.ordinal()][RNGseed % 2];


        if (debugMode == true) System.out.println("ER Simulation Running with " + nameoffacility + " settings.");

        setERMainFloorConfiguration();

        PatientDischargeList = new LinkedList<>();
    }

    public ER()
    {
        this(Hospital.HospitalExpress);
    }

        private void setERMainFloorConfiguration()
        {
            switch(FacilityName)
            {
                case TwigpiHospital:
                    ERMainFloor = new HospitalFloor(22,500,200);
                    break;
                case G_Allow_Hospital:
                    ERMainFloor = new HospitalFloor(3);
                    break;
                case HospitalExpress:
                    ERMainFloor = new HospitalFloor(8);
                    break;
                case StrokeOfGeniusHospital:
                    ERMainFloor = new HospitalFloor(9);
                    break;
                case DasCrazKlinik:
                    ERMainFloor = new HospitalFloor(4,25);
                    break;
                case LemMemHospital:
                    ERMainFloor = new HospitalFloor(6);
                    break;
                default:
                    ERMainFloor = new HospitalFloor();
            }
        }

    public ERStats runER(ERSettings settingsforERsim)
    {
        ERStats ERstatsToReturn;

        int totalMinutesToRunFor = (int) (60 * settingsforERsim.SimulationDurationInHours);

        System.out.println("\nU _____ u   ____          ____                   __  __     _   _    _         _       _____   U  ___ u   ____     \n\\| ___\"|/U |  _\"\\ u      / __\"| u      ___     U|' \\/ '|uU |\"|u| |  |\"|    U  /\"\\  u  |_ \" _|   \\/\"_ \\/U |  _\"\\ u  \n |  _|\"   \\| |_) |/     <\\___ \\/      |_\"_|    \\| |\\/| |/ \\| |\\| |U | | u   \\/ _ \\/     | |     | | | | \\| |_) |/  \n | |___    |  _ <        u___) |       | |      | |  | |   | |_| | \\| |/__  / ___ \\    /| |\\.-,_| |_| |  |  _ <    \n |_____|   |_| \\_\\       |____/>>    U/| |\\u    |_|  |_|  <<\\___/   |_____|/_/   \\_\\  u |_|U \\_)-\\___/   |_| \\_\\   \n <<   >>   //   \\\\_       )(  (__).-,_|___|_,-.<<,-,,-.  (__) )(    //  \\\\  \\\\    >>  _// \\\\_     \\\\     //   \\\\_  \n(__) (__) (__)  (__)     (__)      \\_)-' '-(_/  (./  \\.)     (__)  (_\")(\"_)(__)  (__)(__) (__)   (__)   (__)  (__) \n\n");

        System.out.println("ER Simulation with settings as follows:\n\n" + settingsforERsim + "\n");

        ERMainFloor.setFloorSimSettings(settingsforERsim);

        if (debugMode == true) System.out.println("Simulation will run for " + totalMinutesToRunFor + " minutes.\n");

        ERstatsToReturn = simulateER(totalMinutesToRunFor);

        System.out.println("  ____      ____    U  ___ u   ____     ____        _      __  __    __  __  U _____ u ____    \nU|  _\"\\ uU |  _\"\\ u  \\/\"_ \\/U /\"___|uU |  _\"\\ u U  /\"\\  uU|' \\/ '|uU|' \\/ '|u\\| ___\"|/|  _\"\\   \n\\| |_) |/ \\| |_) |/  | | | |\\| |  _ / \\| |_) |/  \\/ _ \\/ \\| |\\/| |/\\| |\\/| |/ |  _|\" /| | | |  \n |  __/    |  _ <.-,_| |_| | | |_| |   |  _ <    / ___ \\  | |  | |  | |  | |  | |___ U| |_| |\\ \n |_|       |_| \\_\\\\_)-\\___/   \\____|   |_| \\_\\  /_/   \\_\\ |_|  |_|  |_|  |_|  |_____| |____/ u \n ||>>_     //   \\\\_    \\\\     _)(|_    //   \\\\_  \\\\    >><<,-,,-.  <<,-,,-.   <<   >>  |||_    \n(__)__)   (__)  (__)  (__)   (__)__)  (__)  (__)(__)  (__)(./  \\.)  (./  \\.) (__) (__)(__)_)   \n           ____   __   __      _____                            ____    ____                   \n        U | __\")u \\ \\ / /     |_ \" _| __        __     ___   U /\"___|uU|  _\"\\ u  ___           \n         \\|  _ \\/  \\ V /        | |   \\\"\\      /\"/    |_\"_|  \\| |  _ /\\| |_) |/ |_\"_|          \n          | |_) | U_|\"|_u      /| |\\  /\\ \\ /\\ / /\\     | |    | |_| |  |  __/    | |           \n          |____/    |_|       u |_|U U  \\ V  V /  U  U/| |\\u   \\____|  |_|     U/| |\\u         \n         _|| \\\\_.-,//|(_      _// \\\\_.-,_\\ /\\ /_,-.-,_|___|_,-._)(|_   ||>>_.-,_|___|_,-.      \n        (__) (__)\\_) (__)    (__) (__)\\_)-'  '-(_/ \\_)-' '-(_/(__)__) (__)__)\\_)-' '-(_/       ");

        return ERstatsToReturn;
    }

        private ERStats simulateER(int numberofminutestorun)
        {
            ERStats ERstatsToReturn = new ERStats();

            SimulationStartTime = Clock.fixed(Clock.systemDefaultZone().instant(), Clock.systemDefaultZone().getZone());

            System.out.println(nameOfTNP + ": Alright staff! Let's make this shift at " + FacilityName + " a good one. "
                    + "There is " + ERMainFloor.getScenarioSummary() + ". "
                    + "We've got all " + ERMainFloor.ERrooms.length + " ER rooms free, and we're going to help today's patients in record time!\n");

            for (int m = 1; m <= numberofminutestorun; m++)
            {
                SimulationMinutesElapsed = m;

                if (debugMode == true) System.out.println("\nSimulation Minute: " + SimulationMinutesElapsed + "\n");

                ERstatsToReturn = doPatientArrivalProcess(ERstatsToReturn);
                ERstatsToReturn = doPatientAssistanceAndDischargeProcess(ERstatsToReturn);

                ERstatsToReturn = enforceScenarioEndConditions(ERstatsToReturn);
            }

            ERstatsToReturn.setPatientsStillWaiting(ERMainFloor.WaitingRoom.getPatientOccupantAmount());
            ERstatsToReturn.setPatientsStillBeingTended(countPatientsStillBeingTended());
            ERstatsToReturn.setTotalSimulationRunTimeInMinutes(SimulationMinutesElapsed);

            System.out.println("End of Simulation.\n");

            return ERstatsToReturn;
        }

            private void lookAtTheClock()
            {
                DateTimeFormatter timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
                DecimalFormat twoDigit = new DecimalFormat("00");
                String timeToDisplay = (FacilityName.ordinal() != 5) ? timeFormat.format(Clock.offset(SimulationStartTime, Duration.ofMinutes(SimulationMinutesElapsed)).instant()) : "88:" + twoDigit.format(SimulationMinutesElapsed % 60);

                System.out.println("The " + ClockAdj[FacilityName.ordinal()] + " clock on the wall: " + timeToDisplay + "\n");
            }

            private ERStats doPatientArrivalProcess(ERStats ERstatsToReturn)
            {
                boolean newPatientArrival = false;

                if (debugMode == true) System.out.println("[Simulating Patient Arrival at Minute: " + SimulationMinutesElapsed + "]\n");

                if (isTickInterval(ERMainFloor.getPatientArrivalIntervalTimeInMinutes()))
                {
                    ERstatsToReturn.setTotalSimulationRunTimeInMinutes(SimulationMinutesElapsed);
                    ERstatsToReturn.incrementRunTimeSinceScenarioStartByTickAmount(ERMainFloor.getPatientArrivalIntervalTimeInMinutes());

                    lookAtTheClock();

                    newPatientArrival = checkPatientArrival(ERMainFloor.getPatientArrivalPercentChance());
                    if (newPatientArrival == true)
                    {
                        ERstatsToReturn.addPatientArrivalToTotal();
                        ERstatsToReturn.incrementByOnePatientArrivalsSinceScenarioStart();

                        receivePatientToWaitingRoom();
                    }
                }

                return ERstatsToReturn;
            }

                /**
                 *
                 * @param percentchanceofpatientarrival An integer usually between 0 and 100 (inclusive).
                 * @return
                 */
                private boolean checkPatientArrival(int percentchanceofpatientarrival)
                {
                    boolean hasNewPatientArrived;

                    hasNewPatientArrived = ((Math.random() * 100) < percentchanceofpatientarrival);

                    if (debugMode == true) System.out.println((hasNewPatientArrived ? "A new patient " : "No new patient ") + "has arrived (" + percentchanceofpatientarrival + "% chance).\n");

                    return hasNewPatientArrived;
                }

                private void receivePatientToWaitingRoom()
                {
                    ERPatient arrivingPatient =  new ERPatient(ERMainFloor.generateESIstatusOfNewPatient());
                    boolean patientCanWalk = ( arrivingPatient.getESIstatus() == EmergencySeverityIndex._5_Nonurgent
                            || arrivingPatient.getESIstatus() == EmergencySeverityIndex._4_LessUrgent
                            || (arrivingPatient.getESIstatus() == EmergencySeverityIndex._3_Urgent && Math.random() * 2 == 0) );

                    if (debugMode == true) System.out.println("Receiving a new patient to the waiting room at the " + SimulationMinutesElapsed + " minute mark.\n");
                    System.out.println("A patient" + (patientCanWalk ? " walks " : " is brought ") + "through the door.\n");

                    ERMainFloor.WaitingRoom.addPatientToRoomList(SimulationMinutesElapsed,arrivingPatient);

                    System.out.println("The triage nurse handles the forms: \n\n" + arrivingPatient.toFullString(SimulationStartTime));
                    switch (arrivingPatient.getESIstatus())
                    {
                        case _6_Ded:
                            System.out.println("The TNP looks over dismally at " + arrivingPatient + "'s unmoving form.\n");
                            break;
                        case _5_Nonurgent:
                            System.out.println("The TNP looks annoyed.\n");
                        default:
                            if (patientCanWalk)
                                System.out.println(nameOfTNP + ": " + arrivingPatient + ", your form is filled out correctly. Your status number is " + arrivingPatient.getESIstatusNumber() + ". We will call you " + WeWillCallYou[FacilityName.ordinal()] + ".\n");
                            else
                                System.out.println(nameOfTNP + ": " + arrivingPatient + "'s form is filled out with status number " + arrivingPatient.getESIstatusNumber() + " and will be given priority.\n");
                    }
                }

            private ERStats doPatientAssistanceAndDischargeProcess(ERStats ERstatsToReturn)
            {
                int ERroomToUse = 0;

                if (debugMode == true) System.out.println("[Simulating Patient Assistance and Patient Discharge at Minute: " + SimulationMinutesElapsed + "]\n");

                if (isTickInterval(ERMainFloor.getPatientAssistanceIntervalTimeInMinutes()))
                {
                    ERstatsToReturn.setTotalSimulationRunTimeInMinutes(SimulationMinutesElapsed);

                    LinkedList<ERPatient> patientsInRoom, patientsToDischarge;

                    if (debugMode == true) System.out.println("Tending patients in ER Rooms.\n");

                    for (int i = 0;i < ERMainFloor.ERrooms.length;i++)
                    {
                        if (ERMainFloor.ERrooms[i].isReadyForAPatient() == false)
                        {
                            patientsInRoom = ERMainFloor.ERrooms[i].getRoomRoster();
                            patientsInRoom = assistPatientsInRoom(patientsInRoom);
                            ERMainFloor.ERrooms[i].updateRoomRoster(patientsInRoom);
                            patientsToDischarge = checkForPatientsFinishedBeingAssisted(patientsInRoom);
                            if (patientsToDischarge.isEmpty() == false)
                                ERMainFloor.ERrooms[i].removePatientsFromRoomList(patientsToDischarge);

                            ERstatsToReturn = dischargePatientsFromHospital(patientsToDischarge,ERstatsToReturn);
                        }
                    }

                    if (ERMainFloor.WaitingRoom.ERqueue.isEmpty())
                        System.out.println("The waiting room is empty.\n");
                    else
                        printOutWaitingRoomQueue();

                    while (ERroomToUse >= 0 && ERMainFloor.WaitingRoom.patientIsWaiting())   // ERroomToUse = -1 means no ER rooms are free.
                    {
                        ERroomToUse = nextEmptyERroomIndex();
                        if (0 <= ERroomToUse && ERroomToUse < ERMainFloor.ERrooms.length)
                            ERstatsToReturn = moveNextWaitingPatientToRoom(ERroomToUse, ERstatsToReturn);
                    }

                    // ERstatsToReturn = updatePatientsInWaitingRoom(ERstatsToReturn);
                }

                return ERstatsToReturn;
            }

                private ERStats moveNextWaitingPatientToRoom(int ERroomindex, ERStats currentERstats)
                {
                    ERPatient nextPatientToAssist = ERMainFloor.WaitingRoom.fetchNextPatient(SimulationMinutesElapsed);
                    int patientWaitTime = nextPatientToAssist.getTotalWaitTime();

                    if (debugMode == true) System.out.println("[Simulating Patient Transfer to ER]\n");

                    System.out.println(nameOfTNP + ": " + nextPatientToAssist + " is next.");

                    nextPatientToAssist.setNeededAssistanceLeftInMinutes(ERMainFloor.calculateEstimatedAssistanceTimeInMinutes(nextPatientToAssist));

                    System.out.println(nameOfTNP + ": We're bringing you to " + ERMainFloor.ERrooms[ERroomindex].getRoomName() + ".\n");

                    ERMainFloor.ERrooms[ERroomindex].addPatientToRoomList(nextPatientToAssist);

                    currentERstats.addPatientAdmittanceToTotal();
                    currentERstats.addToPatientWaitTimeTotalInMinutes(patientWaitTime);

                    lookAtTheClock();

                    if (patientWaitTime > currentERstats.getLongestWaitTimeInMinutes())
                            currentERstats.setLongestWaitTimeInMinutes(patientWaitTime);

                    return currentERstats;
                }

                private ERStats updatePatientsInWaitingRoom(ERStats ERstatsToReturn)
                {
                    //if (debugMode == true) System.out.println("Updating patients in the queue.");
                    //ERMainFloor.WaitingRoom.ERqueue.forEach(patient -> {if (patient.patientLeavesBeforeBeingAdmittedDuringTick(ERMainFloor.getPatientAssistanceIntervalTimeInMinutes()))
                    //                                                        ERstatsToReturn.;****
                    //                                                    patient.patientDeceasedDuringTick(ERMainFloor.getPatientAssistanceIntervalTimeInMinutes());});
                    return ERstatsToReturn;
                }

                private LinkedList<ERPatient> assistPatientsInRoom(LinkedList<ERPatient> listofpatientsinroom)
                {
                    LinkedList<ERPatient> assistedPatientsToReturn = new LinkedList<>();

                    for (ERPatient patientToAssist : listofpatientsinroom)
                    {
                        if (debugMode == true) System.out.println("Assisting Patient:\n\n" + patientToAssist.toFullString(SimulationStartTime) + "\n");

                        patientToAssist.decreaseAssistanceLeftByATick(ERMainFloor.getPatientAssistanceIntervalTimeInMinutes());
                        assistedPatientsToReturn.add(patientToAssist);
                    }

                    if (debugMode == true) System.out.println("List of Patients assisted: " + assistedPatientsToReturn + "\n");

                    return assistedPatientsToReturn;
                }

                private LinkedList<ERPatient> checkForPatientsFinishedBeingAssisted(LinkedList<ERPatient> listofpatientsinroom)
                {
                    LinkedList<ERPatient> dischargedPatientsToReturn = new LinkedList<>();

                    for (ERPatient patientToCheck : listofpatientsinroom)
                    {
                        if (debugMode == true) System.out.println("Patient " + patientToCheck + " now has an estimated " + patientToCheck.getNeededAssistanceLeftInMinutes() + " minute" + (patientToCheck.getNeededAssistanceLeftInMinutes() == 1 ? "" : "s") + " of needed assistance time left.\n");

                        if (patientToCheck.isFinishedBeingAssisted(ERMainFloor.getPatientAssistanceIntervalTimeInMinutes()))
                        {
                            if (debugMode == true) System.out.println("Patient to be discharged from room: " + patientToCheck + "\n");

                            System.out.println("Patient " + patientToCheck + " is finished being assisted.\n");

                            dischargedPatientsToReturn.add(patientToCheck);
                        }
                    }

                    return dischargedPatientsToReturn;
                }

                private ERStats dischargePatientsFromHospital(LinkedList<ERPatient> patientstodischarge, ERStats currentERstats)
                {
                    for (ERPatient nextPatientToDischarge : patientstodischarge)
                    {
                        nextPatientToDischarge.recordDeparture(SimulationMinutesElapsed);

                        if (nextPatientToDischarge.patientStatusIsDed())
                        {
                            String nameSnippet = nextPatientToDischarge.getName().substring(nextPatientToDischarge.getName().length()-3, nextPatientToDischarge.getName().length());
                            System.out.println("Overheard: ~..." + nameSnippet + "didn't make it...~");
                            currentERstats = takeCareOfDedPatient(nextPatientToDischarge, currentERstats);
                        }
                        else
                        {
                            System.out.println("ER Nurse: Here is your paperwork, " + nextPatientToDischarge.getFirstName() + ". " + FarewellToYou[FacilityName.ordinal()] + "\n");
                            System.out.println("Paperwork: \n\n" + nextPatientToDischarge.toFullString(SimulationStartTime) + "\n");
                            lookAtTheClock();

                            PatientDischargeList.add(nextPatientToDischarge);
                            currentERstats.addDischargedPatientToTotal();
                        }

                        if (nextPatientToDischarge.getTotalWaitTime() > currentERstats.getLongestWaitTimeInMinutes())
                            currentERstats.setLongestWaitTimeInMinutes(nextPatientToDischarge.getTotalWaitTime());
                    }

                    return currentERstats;
                }

                    private ERStats takeCareOfDedPatient(ERPatient dedpatient, ERStats currentERstats)
                    {
                        currentERstats.addDeceasedPatientToTotal();

                        System.out.println("ER Nurse: Rest in peace, " + dedpatient + ".\n");

                        if (ERMainFloor.Freezer.addPatientToRoomList(dedpatient))
                        {
                            if (debugMode == true) System.out.println("Deceased Patient " + dedpatient + " was interred into the freezer.\n");

                            System.out.println("A slight chill is felt in the hallways.\n");

                            currentERstats.addInterredFreezerResidentToTotal();
                        }
                        else
                        {
                            if (debugMode == true) System.out.println("Deceased Patient " + dedpatient + " was not interred into the freezer. We're discharging 'em right away.\n");
                            PatientDischargeList.add(dedpatient);
                            currentERstats.addDischargedPatientToTotal();
                        }

                        return currentERstats;
                    }

                private void printOutWaitingRoomQueue()
                {
                    PriorityQueue<ERPatient> ERqueue = ERMainFloor.WaitingRoom.getERqueue();
                    ArrayList<ERPatient> stupid = new ArrayList<>();
                    stupid.addAll(ERqueue);

                    System.out.println("The TNP glances at the waiting list: \n\n");
                    System.out.println("\t⌐-------------------------------------¬\n\t|");
                    System.out.println("\t|   Number of Patients Waiting: " + ERqueue.size() + "\n");
                    while (ERqueue.isEmpty() == false)
                        System.out.println("\t|   " + "[ESI: " + ERqueue.peek().getESIstatusNumber() + "] " + ERqueue.poll());

                    System.out.println("\t|\n\t└-------------------------------------┘");
                    System.out.println("\t╘=====================================╝\n");

                    if (ERMainFloor.WaitingRoom.ERqueue.isEmpty())
                        ERMainFloor.WaitingRoom.ERqueue.addAll(stupid);
                }

                private int nextEmptyERroomIndex()
                {
                    boolean keepCheckingRooms = true;

                    int indexToReturn = -1;
                    int i = 0;

                    if (debugMode == true) System.out.println("Checking for next empty ER Room.\n");

                    while (keepCheckingRooms == true)
                    {
                        if (ERMainFloor.ERrooms[i].isReadyForAPatient())
                        {
                            indexToReturn = i;
                            keepCheckingRooms = false;
                        }
                        i++;
                        if (i >= ERMainFloor.ERrooms.length)
                            keepCheckingRooms = false;
                    }

                    return indexToReturn;
                }

            private boolean isTickInterval(int intervaloftickinminutes)
            {
                boolean answerToReturn = false;

                if (intervaloftickinminutes == 0
                        || SimulationMinutesElapsed % intervaloftickinminutes == 0)
                    answerToReturn = true;
                else
                    answerToReturn = false;

                if (debugMode == true && answerToReturn == true) System.out.println("Tick Interval (" + intervaloftickinminutes + ")" + (answerToReturn ? " was triggered " : " was not triggered ") + "at the " + SimulationMinutesElapsed + " minute mark.\n");

                return answerToReturn;
            }

            private ERStats enforceScenarioEndConditions(ERStats currentERstats)
            {
                if (ERMainFloor.getScenarioTimeInMinutes() > 0)
                    if (currentERstats.getRunTimeSinceScenarioStartInMinutes() >= ERMainFloor.getScenarioTimeInMinutes())
                    {
                        if (debugMode == true) System.out.println("\nThe period of turmoil is over and the ER Simulation Scenario returns to normal conditions.\n");

                        ERMainFloor.updateFloorSimScenario(ERSettings.ERScenarioPresets.Normal);
                        currentERstats.resetRunTimeSinceScenarioStartInMinutes();
                    }

                if (ERMainFloor.getScenarioPatientArrivals() > 0)
                    if (currentERstats.getPatientArrivalsSinceScenarioStart() >= ERMainFloor.getScenarioPatientArrivals())
                    {
                        if (debugMode == true) System.out.println("\nThe increased influx of patients has stemmed and the ER Simulation Scenario returns to normal conditions.\n");

                        ERMainFloor.updateFloorSimScenario(ERSettings.ERScenarioPresets.Normal);
                        currentERstats.resetPatientArrivalsSinceScenarioStart();
                    }

                return currentERstats;
            }

            private int countPatientsStillBeingTended()
            {
                int amountToReturn = 0;

                for (int i = 0; i < ERMainFloor.ERrooms.length; i++)
                    amountToReturn += ERMainFloor.ERrooms[i].getPatientOccupantAmount();

                return amountToReturn;
            }

    public ERStats runER()
    {
        ERStats ERstatsToReturn;
        ERSettings defaultSettings = new ERSettings(ERSettings.ERScenarioPresets.Normal);

        ERstatsToReturn = runER(defaultSettings);

        return ERstatsToReturn;
    }
}
