/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERTSim;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Scanner;

/**
 *
 * @author thetr
 */
public final class ERPatient implements Comparable<ERPatient>
{
    private int dna = (int) (Math.random() * Integer.MAX_VALUE) | 715827882;    // Essentially a random seed. Should be the first class variable to be initialized.

    private String Name, FirstName;
    private int MedID;
    private EmergencySeverityIndex InitialESIstatus;
    private EmergencySeverityIndex ESIstatus;

    private int Arrival = -1;
    private int Admittance = -1;
    private int Departure = -1;

    private int NeededAssistanceLeftInMinutes = 0;

    private boolean leftAgainstMedicalAdvice = false;

    boolean isSimulated;

    boolean debugMode = false;

    private final String COMMON_FIRST_NAMES_FILENAME = "commonfirstnames.txt";
    private final String COMMON_LAST_NAMES_FILENAME = "commonlastnames.txt";
    private final String UNCOMMON_FIRST_NAMES_FILENAME = "firstnames.txt";
    private final String UNCOMMON_LAST_NAMES_FILENAME = "lastnames.txt";

    public ERPatient(String patientname, int patientmedid, EmergencySeverityIndex emergencyseverityindex, boolean patientissimulated)
    {
        isSimulated = patientissimulated;

        Name = patientname;
        MedID = patientmedid;
        ESIstatus = emergencyseverityindex;
        InitialESIstatus = InitialESIstatus;

        FirstName = Name.split(" ", 2)[0];
    }

    public ERPatient(String patientname, int patientmedid, int emergencyseverityindex)
    {
        this(patientname,patientmedid,EmergencySeverityIndex.values()[emergencyseverityindex],false);
    }

    public ERPatient(EmergencySeverityIndex emergencyseverityindex)
    {
        isSimulated = true;

        Name = generatePatientName();
        MedID = generatePatientMedID();
        ESIstatus = emergencyseverityindex;
        InitialESIstatus = ESIstatus;

        FirstName = Name.split(" ", 2)[0];
    }

    public ERPatient()
    {
        isSimulated = true;

        Name = generatePatientName();
        MedID = generatePatientMedID();
        ESIstatus = generatePatientESI();
        InitialESIstatus = ESIstatus;

        FirstName = Name.split(" ", 2)[0];
    }

        private String generatePatientName()
        {
            String nameOfPatientToReturn = generatePatientFirstName() + " " + generatePatientLastName();

            return nameOfPatientToReturn;
        }

            private String generatePatientFirstName()
            {
                String patientFirstNameToReturn;

                if ((dna | 60851584) % 2 == 0)
                    patientFirstNameToReturn = generateUncommonPatientFirstName();
                else
                    patientFirstNameToReturn = generateCommonPatientFirstName();

                return patientFirstNameToReturn;
            }

                private String generateCommonPatientFirstName()
                {
                    String commonFirstNameToReturn = loadRandomNameFromFile(COMMON_FIRST_NAMES_FILENAME);

                    return commonFirstNameToReturn;
                }

                private String generateUncommonPatientFirstName()
                {
                    String commonFirstNameToReturn = loadRandomNameFromFile(UNCOMMON_FIRST_NAMES_FILENAME);

                    return commonFirstNameToReturn;
                }

            private String generatePatientLastName()
            {
                String patientFirstLastToReturn;

                if ((dna | 60851584) % 3 == 0)
                    patientFirstLastToReturn = generateUncommonPatientLastName();
                else
                    patientFirstLastToReturn = generateCommonPatientLastName();

                return patientFirstLastToReturn;
            }

                private String generateCommonPatientLastName()
                {
                    String commonFirstNameToReturn = loadRandomNameFromFile(COMMON_LAST_NAMES_FILENAME);

                    return commonFirstNameToReturn;
                }

                private String generateUncommonPatientLastName()
                {
                    String commonFirstNameToReturn = loadRandomNameFromFile(UNCOMMON_LAST_NAMES_FILENAME);

                    return commonFirstNameToReturn;
                }

            private String loadRandomNameFromFile(String nameoffilecontainingrandomnames)
            {
                String randomNameToReturn = "";
                File fileContainingRandomNames = new File(nameoffilecontainingrandomnames);

                if (fileContainingRandomNames.exists() && fileContainingRandomNames.length() > 3)
                {
                    int numberOfFileEntries = countEntriesInFile(nameoffilecontainingrandomnames);
                    if (numberOfFileEntries == 0) System.out.println("\nZero file entries recorded causes divide by zero.\n");

                    int getFileEntryPosition = dna % numberOfFileEntries;

                    randomNameToReturn = fetchStringEntryFromFileAtPosition(getFileEntryPosition, nameoffilecontainingrandomnames);
                }

                return randomNameToReturn;
            }

                private int countEntriesInFile(String filenameofentriestocount)
                {
                    int countedAmountToReturn = 0;

                    try
                    {
                        File fileOfEntriesToCount = new File(filenameofentriestocount);

                        //if (debugMode == true) System.out.println("\nAccessing file to count: \"" + filenameofentriestocount + "\" Size in Bytes: " + fileOfEntriesToCount.length());

                        Scanner fileScannerOfEntriesToCount = new Scanner(fileOfEntriesToCount,"UTF-8");
                        fileScannerOfEntriesToCount.useDelimiter(",");

                        while (fileScannerOfEntriesToCount.hasNext())
                            {
                                countedAmountToReturn++;
                                fileScannerOfEntriesToCount.next();
                            }

                        fileScannerOfEntriesToCount.close();

                    }
                    catch (IOException e)
                    {
                        System.out.println("\nError: Exception occurred while counting entries in file: \"" + filenameofentriestocount + "\"\n" + e + "\n");
                    }

                    //if (debugMode == true) System.out.println("Number of entries found in file \"" + filenameofentriestocount + "\": " + countedAmountToReturn + "\n");
                    return countedAmountToReturn;
                }

                private String fetchStringEntryFromFileAtPosition(int positionofentry, String filenametofetchfrom)
                {
                    String entryToReturn = "";

                    try
                    {
                        Scanner fileScannerToFetchFrom = new Scanner(new File(filenametofetchfrom),"UTF-8");
                        fileScannerToFetchFrom.useDelimiter(",");

                        for (int i = 0; i < positionofentry - 1; i++)
                            fileScannerToFetchFrom.next();

                        entryToReturn = fileScannerToFetchFrom.next();
                        fileScannerToFetchFrom.close();
                    }
                    catch (IOException e)
                    {
                        System.out.println("Error: Exception occurred while fetching an entry from file: \"" + filenametofetchfrom + "\"\n" + e + "\n");
                    }

                    return entryToReturn;
                }

        private int generatePatientMedID()
        {
            int MedIDOfPatientToReturn = 0;

            MedIDOfPatientToReturn = (int) (Math.random() * Integer.MAX_VALUE);

            return MedIDOfPatientToReturn;
        }

        private EmergencySeverityIndex generatePatientESI()
        {
            EmergencySeverityIndex ESIOfPatientToReturn = EmergencySeverityIndex._5_Nonurgent;

            return ESIOfPatientToReturn;
        }

    public boolean updatePatientName(String newnameofpatient)
    {
        boolean success = false;

        success = setName(newnameofpatient);

        return success;
    }

        private boolean setName(String nameofpatient)
        {
            boolean success = false;

            if (nameofpatient.length() >= 4)
            {
                Name = nameofpatient;
                success = true;
            }

            return success;
        }

    public boolean updatePatientESIstatus(EmergencySeverityIndex newESIstatus)
    {
        ESIstatus = newESIstatus;

        return true;
    }

    public int moduloBaseOnDNA(int modulus)
    {
        return dna % modulus;
    }

    public String getName()
    {
        return Name;
    }

    public String getFirstName()
    {
        return FirstName;
    }

    public int getMedID()
    {
        return MedID;
    }

    public EmergencySeverityIndex getESIstatus()
    {
        return ESIstatus;
    }

    public int getESIstatusNumber()
    {
        int numberToReturn = ESIstatus.ordinal() + 1;

        return numberToReturn;
    }

    public int getArrival()
    {
        return Arrival;
    }

    public int getAdmittance()
    {
        return Admittance;
    }

    public int getDeparture()
    {
        return Departure;
    }

    public int getTotalWaitTime()
    {
        int waitTimeToReturn;

        try
        {
            if (Admittance >= Arrival && Admittance != -1)
                waitTimeToReturn = Admittance - Arrival;
            else if (Departure >= Arrival && Departure != -1)
                waitTimeToReturn = Departure - Arrival;
            else
                waitTimeToReturn = -1;
        }
        catch (Exception e)
        {
            waitTimeToReturn = -2;
        }

        return waitTimeToReturn;
    }

    public int getTotalAssistanceTime()
    {
        int assistanceTimeToReturn;

        try
        {
            if (Departure > Admittance && Admittance != -1)
                assistanceTimeToReturn = Departure - Admittance;
            else
                assistanceTimeToReturn = -1;
        }
        catch (Exception e)
        {
            assistanceTimeToReturn = -2;
        }

        return assistanceTimeToReturn;
    }

    public int getTotalTimeSpent()
    {
        int totalTimeToReturn;

        try
        {
            if (Departure > Arrival && Departure != -1)
                totalTimeToReturn = Departure - Arrival;
            else
                totalTimeToReturn = -1;
        }
        catch (Exception e)
        {
            totalTimeToReturn = -2;
        }

        return totalTimeToReturn;
    }

    public int getNeededAssistanceLeftInMinutes()
    {
        return NeededAssistanceLeftInMinutes;
    }

    public void recordArrival(int simulationtimeelapsed)
    {
        Arrival = simulationtimeelapsed;

        if (debugMode == true) System.out.println("Patient " + Name + "'s arrival recorded at: " + Arrival + "\n");
    }

    public void recordAdmittance(int simulationtimeelapsed)
    {
        Admittance = simulationtimeelapsed;

        if (debugMode == true) System.out.println("Patient " + Name + "'s admittance recorded at: " + Admittance + "\n");
    }

    public void recordDeparture(int simulationtimeelapsed)
    {
        Departure = simulationtimeelapsed;

        if (debugMode == true) System.out.println("Patient " + Name + "'s departure recorded at: " + Departure + "\n");
    }

    public void setNeededAssistanceLeftInMinutes(int minutesofneededassistanceleft)
    {
        NeededAssistanceLeftInMinutes = minutesofneededassistanceleft;
    }

    public int decreaseAssistanceLeftByATick(int tickamountinminutes)
    {
        if (patientStatusIsDed())
        {
            NeededAssistanceLeftInMinutes = -1;
            if (debugMode == true)
                System.out.println("There's nothing we can do. Patient is beyond help now. Rest in peace " + Name + ".\n");
        }
        else if (NeededAssistanceLeftInMinutes >= tickamountinminutes)
        {
            NeededAssistanceLeftInMinutes -= tickamountinminutes;
            if (ESIstatus.ordinal() < 3 && Math.random() * 10 == 1)
                updatePatientESIstatus(EmergencySeverityIndex.values()[ESIstatus.ordinal() + 1]);
            else if ( patientDeceasedDuringTick(tickamountinminutes) == true)
                updatePatientESIstatus(EmergencySeverityIndex._6_Ded);
        }
        else
        {
            NeededAssistanceLeftInMinutes = 0;
            updatePatientESIstatus(EmergencySeverityIndex._5_Nonurgent);
        }

        return NeededAssistanceLeftInMinutes;
    }

    public boolean isFinishedBeingAssisted(int tickamountinminutes)
    {
        boolean answerToReturn = false;

        if (patientStatusIsDed())
            answerToReturn = true;
        else if (NeededAssistanceLeftInMinutes <= 0)
        {
            updatePatientESIstatus(EmergencySeverityIndex._5_Nonurgent);
            answerToReturn = true;
        }
        else if (patientLeavesAgainstMedicalAdviceDuringTick(tickamountinminutes))
            answerToReturn = true;
        else
            answerToReturn = false;

        if (debugMode == true) System.out.println("Patient " + Name + (answerToReturn ? " is done " : " is not done ") + "being assisted.\n");

        return answerToReturn;
    }

    public boolean patientDeceasedDuringTick(int tickamountinminutes)
    {
        boolean answerToReturn = (ESIstatus.ordinal() < 3);
        answerToReturn &= ( (Math.random() * 100 * 60 / tickamountinminutes) <= (10 * (3 - ESIstatus.ordinal())) );

        return answerToReturn;
    }

    public boolean patientStatusIsDed()
    {
        boolean answer = false;

        if (getESIstatus() == EmergencySeverityIndex._6_Ded)
            answer = true;
        else
            answer = false;

        if (debugMode == true && answer == true) System.out.println("Patient " + Name + (answer ? " is ded" : " is alive and kicking") + ".\n");

        return answer;
    }

    public boolean patientLeavesAgainstMedicalAdviceDuringTick(int tickamountinminutes)
    {
        boolean answer = (ESIstatus != EmergencySeverityIndex._1_Resuscitation);
        answer &= (ESIstatus != EmergencySeverityIndex._6_Ded);
        answer &= ( (Math.random() * 100 * 60 / tickamountinminutes) <= (5 * (ESIstatus.ordinal() / 2)) );   //(NeededAssistanceLeftInMinutes + 1)

        if (debugMode == true && answer == true) System.out.println("Patient leaves against medical advice.\n");

        leftAgainstMedicalAdvice = answer;

        return answer;
    }

    public boolean patientLeftAgainstMedicalAdvice()
    {
        return leftAgainstMedicalAdvice;
    }

    public boolean patientLeavesBeforeBeingAdmittedDuringTick(int tickamountinminutes)
    {
        boolean answer = (ESIstatus != EmergencySeverityIndex._1_Resuscitation);
        answer &= (ESIstatus != EmergencySeverityIndex._6_Ded);
        answer &= ( (Math.random() * 100 * 60 / tickamountinminutes) <= (5 * (ESIstatus.ordinal() / 2)) );   //(NeededAssistanceLeftInMinutes + 1)

        if (debugMode == true && answer == true) System.out.println("Patient leaves before being admitted.\n");

        return answer;
    }

    public String toFullString(Clock simstarttime)
    {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
        String arrivalTime = ( getArrival() == -1 ? "" : timeFormat.format(Clock.offset(simstarttime, Duration.ofMinutes(getArrival())).instant()) );
        String admittanceTime = ( getAdmittance() == -1 ? "" : timeFormat.format(Clock.offset(simstarttime, Duration.ofMinutes(getAdmittance())).instant()) );
        String departureTime = ( getDeparture() == -1 ? "" : timeFormat.format(Clock.offset(simstarttime, Duration.ofMinutes(getDeparture())).instant()) );

        String stringToReturn = "\t⌐-----------------------------¬\n";

        stringToReturn += "\t| " + getName() + "\n";
        stringToReturn += "\t| " + "MedID: " + getMedID() + "\n";
        stringToReturn += "\t| " + "Init. ESI: " + InitialESIstatus + "\n";
        stringToReturn += "\t| " + "Leave ESI: " + ( getDeparture() == -1 ? "" : getESIstatus() ) + "\n\n\n";
        stringToReturn += "\t| " + "Arrival: " + arrivalTime + "\n";
        stringToReturn += "\t| " + "Admittance: " + admittanceTime + "\n";
        stringToReturn += "\t| " + "Departure: " + departureTime + "\n\n\n";
        stringToReturn += "\t| " + "Total Wait Time: " + ( getTotalWaitTime() == -1 ? "" : getTotalWaitTime() + " min" ) + "\n";
        stringToReturn += "\t| " + "Total Assist Time: " + ( getTotalAssistanceTime() == -1 ? "" : getTotalAssistanceTime() + " min" ) + "\n";
        stringToReturn += "\t| " + "Total Time: " + ( getTotalTimeSpent() == -1 ? "" : getTotalTimeSpent() + " min") + "\n";
        if (leftAgainstMedicalAdvice == true)
            stringToReturn += "\t| " + "Note: Patient left against\n\t| medical advice.\n";
        if (debugMode == true) stringToReturn += "\t| " + "dna: " + dna + "\n";
        if (debugMode == true && getNeededAssistanceLeftInMinutes() > 0) stringToReturn += "\t| " + "Actual Needed Assistance Left In Minutes: " + getNeededAssistanceLeftInMinutes() + "\n";
        stringToReturn += "\t└-----------------------------┘\n";
        stringToReturn += "\t╘=============================╝\n";

        return stringToReturn;
    }

    @Override
    public int compareTo(ERPatient p)
    {
        int comparableToReturn = this.ESIstatus.compareTo(p.ESIstatus);

        // If ESIstatuses are the same, then higher priority should be given
        // to earlier recorded arrival times.
        if (comparableToReturn == 0)
            if (this.Arrival < p.Arrival)
                comparableToReturn = -1;
            else if (this.Arrival > p.Arrival)
                comparableToReturn = 1;
            else
                comparableToReturn = 0;

        return comparableToReturn;
    }

    @Override
    public String toString()
    {
        String stringToReturn = getName();

        if (debugMode == true) stringToReturn += " (ESI:" + getESIstatusNumber() + "/Arr:" + getArrival() + ")";

        return stringToReturn;
    }

}
