/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERTSim;

import java.util.LinkedList;

/**
 *
 * @author thetr
 */
public class HospitalRoom
{
    protected String RoomName;

    protected int MaxPatientCapacity;
    protected int PatientOccupantAmount;

    protected boolean EnforceMaxPatientCapacity = true;
    // OccupantList contains the patient roster of the room in the order that they were added.
    private LinkedList<ERPatient> OccupantList = new LinkedList<>();

    private boolean debugMode = false;

    public HospitalRoom(String nameofroom, int maximumcapacityofpatients, int amountofpatientoccupants)
    {
        RoomName = nameofroom;

        if (maximumcapacityofpatients >= 0)
            MaxPatientCapacity = maximumcapacityofpatients;
        else
            MaxPatientCapacity = 0;

        if (amountofpatientoccupants <= MaxPatientCapacity ||
                EnforceMaxPatientCapacity == false)
            PatientOccupantAmount = amountofpatientoccupants;
        else
            PatientOccupantAmount = MaxPatientCapacity;
    }

    public HospitalRoom()
    {
        this("Hospital Room",1,0);
    }

    public HospitalRoom(String nameofroom)
    {
        this(nameofroom,1,0);
    }

    public HospitalRoom(int maximumcapacityofpatients)
    {
        this("Hospital Room with Capacity: " + maximumcapacityofpatients,maximumcapacityofpatients,0);
    }

    public HospitalRoom(String nameofroom, int maximumcapacityofpatients)
    {
        this(nameofroom,maximumcapacityofpatients,0);
    }

    public HospitalRoom(int maximumcapacityofpatients, int amountofpatientoccupants)
    {
        this("Hospital Room with Capacity: " + maximumcapacityofpatients,maximumcapacityofpatients,amountofpatientoccupants);
    }



    public boolean addPatientToRoomList(ERPatient patienttoadd)
    {
        boolean success = false;

        if ( PatientOccupantAmount < MaxPatientCapacity ||
                EnforceMaxPatientCapacity == false )
        {
            success = OccupantList.add(patienttoadd);
            PatientOccupantAmount++;
        }
        else
            success = false;

        if (debugMode == true) System.out.println("Patient" + (success ? " was " : " was not ") + "added to the roster of " + getRoomName() + ".\n");

        return success;
    }

    public boolean removePatientFromRoomList(ERPatient patienttoremove)
    {
        boolean success = OccupantList.remove(patienttoremove);

        if (success == true)
            PatientOccupantAmount--;

        if (debugMode == true) System.out.println("Patient " + patienttoremove.getName() + (success ? " was " : " was not ") + "removed from " + RoomName + " roster.\n");

        return success;
    }

    public boolean removePatientsFromRoomList(LinkedList<ERPatient> patientstoremove)
    {
        boolean success;

        try
        {
            success = OccupantList.removeAll(patientstoremove);
            PatientOccupantAmount = OccupantList.size();
        }
        catch (Exception e)
        {
            success = false;
        }

        if (debugMode == true) System.out.println("Patient" + (patientstoremove.size() == 1 ? "" : "s") + (success ? " that were " : " were not ") + "removed from the roster of " + RoomName + ": " + patientstoremove + "\n");

        return success;
    }

    public boolean updateRoomRoster(LinkedList<ERPatient> updatedRoomRoster)
    {
        boolean success = false;

        OccupantList = updatedRoomRoster;
        PatientOccupantAmount = OccupantList.size();

        return success;
    }

    public LinkedList<ERPatient> getRoomRoster()
    {
        if (debugMode == true) System.out.println("Checking Room Roster of " + RoomName + ": \n" + OccupantList + "\n");

        return OccupantList;
    }

    /*
    public boolean releasePatient(ERPatient patienttorelease)
    {
        boolean success = OccupantList.remove(patienttorelease);

        return success;
    }
    */

    public boolean isReadyForAPatient()
    {
        boolean answerToReturn = false;

        if (EnforceMaxPatientCapacity == true)
            answerToReturn = (PatientOccupantAmount < MaxPatientCapacity);
        else if (EnforceMaxPatientCapacity == false)
            answerToReturn = true;

        if (debugMode == true) System.out.println("------> " + RoomName + (answerToReturn ? " is ready for a patient" : " is fully occupied") + ".");

        return answerToReturn;
    }

    public String getRoomName()
    {
        return RoomName;
    }

    public int getPatientOccupantAmount()
    {
        return PatientOccupantAmount;
    }
}
