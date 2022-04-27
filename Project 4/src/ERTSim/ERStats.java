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
public final class ERStats
{
    private int LongestWaitTimeInMinutes = 0;
    private int PatientWaitTimeTotalInMinutes = 0;
    private int TotalSimulationRunTimeInMinutes = 0;

    private int RunTimeSinceScenarioStartInMinutes = 0;
    private int PatientArrivalsSinceScenarioStart = 0;

    private int PatientArrivalsTotal = 0;
    private int PatientAdmittancesTotal = 0;
    private int InterredFreezerResidentsTotal = 0;
    private int PatientsDeceasedWhileInCare = 0;
    private int PatientDischargesTotal = 0;
    private int PatientsStillWaiting = 0;
    private int PatientsStillBeingTended = 0;
    private int PatientsLeftAgainstMedicalAdvice = 0;

    public ERStats()
    {

    }


    public void addToPatientWaitTimeTotalInMinutes(int waittimeamounttoadd)
    {
        if (waittimeamounttoadd > 0)
            PatientWaitTimeTotalInMinutes += waittimeamounttoadd;
    }

    public void addPatientArrivalToTotal()
    {
        PatientArrivalsTotal++;
    }

    public void addPatientAdmittanceToTotal()
    {
        PatientAdmittancesTotal++;
    }

    public void addInterredFreezerResidentToTotal()
    {
        InterredFreezerResidentsTotal++;
    }

    public void addDeceasedPatientToTotal()
    {
        PatientsDeceasedWhileInCare++;
    }

    public void addDischargedPatientToTotal()
    {
        PatientDischargesTotal++;
    }

    public void setPatientsStillWaiting(int numberofpatientswaitinginqueue)
    {
        PatientsStillWaiting = numberofpatientswaitinginqueue;
    }

    public void setPatientsStillBeingTended(int numberofpatientsstillbeingtended)
    {
        PatientsStillBeingTended = numberofpatientsstillbeingtended;
    }

    public void addPatientsLeftAgainstMedicalAdvice()
    {
        PatientsLeftAgainstMedicalAdvice++;
    }

    public int getLongestWaitTimeInMinutes()
    {
        return LongestWaitTimeInMinutes;
    }

    public double getAverageWaitTimeInMinutes()
    {
        double averageWaitTimeToReturn = 0;

        if (PatientAdmittancesTotal > 0)
            averageWaitTimeToReturn = (double) PatientWaitTimeTotalInMinutes / PatientAdmittancesTotal;
        else
            averageWaitTimeToReturn = 0;

        return averageWaitTimeToReturn;
    }

    public int getTotalSimulationRunTimeInMinutes()
    {
        return TotalSimulationRunTimeInMinutes;
    }

    public int getRunTimeSinceScenarioStartInMinutes()
    {
        return RunTimeSinceScenarioStartInMinutes;
    }

    public int getPatientArrivalsSinceScenarioStart()
    {
        return PatientArrivalsSinceScenarioStart;
    }

    public int getPatientArrivalsTotal()
    {
        return PatientArrivalsTotal;
    }

    public int getPatientAdmittancesTotal()
    {
        return PatientAdmittancesTotal;
    }

    public int getInterredFreezerResidentsTotal()
    {
        return InterredFreezerResidentsTotal;
    }

    public int getPatientsDeceasedWhileInCare()
    {
        return PatientsDeceasedWhileInCare;
    }

    public int getPatientDischargesTotal()
    {
        return PatientDischargesTotal;
    }

    public boolean setLongestWaitTimeInMinutes(int timeoflongestwaitinminutes)
    {
        boolean success = false;

        if (timeoflongestwaitinminutes >= 0 && timeoflongestwaitinminutes > LongestWaitTimeInMinutes)
        {
            LongestWaitTimeInMinutes = timeoflongestwaitinminutes;
            success = true;
        }
        else if (timeoflongestwaitinminutes <= LongestWaitTimeInMinutes)
        {
            success = false;
            System.out.println("\n\tLogical Error: Tried to set Patient LongestWaitTimeInMinutes (" +
                    LongestWaitTimeInMinutes + ") to a lower value of " + timeoflongestwaitinminutes + ".");
        }
        else
            success = false;

        return success;
    }

    public boolean setPatientWaitTimeTotalInMinutes(int patientwaittimetotaltosetinminutes)
    {
        boolean success = false;

        if (patientwaittimetotaltosetinminutes >= 0)
        {
            PatientWaitTimeTotalInMinutes = patientwaittimetotaltosetinminutes;
            success = true;
        }
        else
            success = false;

        return success;
    }

    public boolean setTotalSimulationRunTimeInMinutes(int totalruntimeofsimulationinminutes)
    {
        boolean success = false;

        if (totalruntimeofsimulationinminutes >= 0)
        {
            TotalSimulationRunTimeInMinutes = totalruntimeofsimulationinminutes;
            success = true;
        }
        else
            success = false;

        return success;
    }

    public void setRunTimeSinceScenarioStartInMinutes(int runtimesincescenariostartinminutes)
    {
        if (runtimesincescenariostartinminutes >= -1)
            RunTimeSinceScenarioStartInMinutes = runtimesincescenariostartinminutes;
    }

    public void setPatientArrivalsSinceScenarioStart(int patientarrivalssincescenariostartinminutes)
    {
        if (patientarrivalssincescenariostartinminutes >= -1)
            PatientArrivalsSinceScenarioStart = patientarrivalssincescenariostartinminutes;
    }

    public boolean setPatientArrivalsTotal(int totalnumberofpatientarrivals)
    {
        boolean success = false;

        if (totalnumberofpatientarrivals >= 0)
        {
            PatientArrivalsTotal = totalnumberofpatientarrivals;
            success = true;
        }
        else
            success = false;

        return success;
    }

    public void incrementRunTimeSinceScenarioStartByTickAmount(int tickamounttoincreaseby)
    {
        RunTimeSinceScenarioStartInMinutes += tickamounttoincreaseby;
    }

    public void incrementByOnePatientArrivalsSinceScenarioStart()
    {
        PatientArrivalsSinceScenarioStart++;
    }

    public void resetRunTimeSinceScenarioStartInMinutes()
    {
        RunTimeSinceScenarioStartInMinutes = 0;
    }

    public void resetPatientArrivalsSinceScenarioStart()
    {
        PatientArrivalsSinceScenarioStart = 0;
    }

    @Override
    public String toString()
    {
        String stringToReturn = "\t⌐------------------------------------------------------------------------------¬\n";
        stringToReturn += "\t| " + "\tTotal Simulation Runtime In Minutes: " + getTotalSimulationRunTimeInMinutes() + "\n\n\n";
        stringToReturn += "\t| " + "\tLongest Wait Time In Minutes: " + getLongestWaitTimeInMinutes() + "\n";
        stringToReturn += "\t| " + "\tCombined Time In Minutes Waited By Patients: " + PatientWaitTimeTotalInMinutes + "\n";
        stringToReturn += "\t| " + "\tPatient Wait Time Average In Minutes: " + getAverageWaitTimeInMinutes() + "\n\n\n";
        stringToReturn += "\t| " + "\tPatient Arrivals: " + getPatientArrivalsTotal() + "\n";
        stringToReturn += "\t| " + "\tPatient Admittances: " + getPatientAdmittancesTotal() + "\n";
        stringToReturn += "\t| " + "\tPatients Deceased While In Care: " + getPatientsDeceasedWhileInCare() + "\n";
        stringToReturn += "\t| " + "\tInterred Freezer Residents: " + getInterredFreezerResidentsTotal() + "\n";
        stringToReturn += "\t| " + "\tPatient Discharges: " + getPatientDischargesTotal() + "\n";
        stringToReturn += "\t| " + "\tPatients Still Waiting: " + PatientsStillWaiting + "\n";
        stringToReturn += "\t| " + "\tPatients Still Being Tended: " + PatientsStillBeingTended + "\n";
//      stringToReturn += "\t| " + "\tPatients Left Against Medical Advice: " + PatientsLeftAgainstMedicalAdvice + "\n";
        stringToReturn += "\t└------------------------------------------------------------------------------┘\n";
        stringToReturn += "\t╘==============================================================================╝\n";


        return stringToReturn;
    }
}
