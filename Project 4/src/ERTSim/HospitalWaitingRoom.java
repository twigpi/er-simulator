/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERTSim;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author thetr
 */
public final class HospitalWaitingRoom extends HospitalRoom
{
    PriorityQueue<ERPatient> ERqueue;

    private boolean debugMode = false;

    public HospitalWaitingRoom(String nameofroom, int maximumcapacityofpatients, int amountofpatientoccupants)
    {
        super(nameofroom,maximumcapacityofpatients,amountofpatientoccupants);

        ERqueue = new PriorityQueue(maximumcapacityofpatients, new WaitingRoomPatientComparator());
    }

    public HospitalWaitingRoom()
    {
        this("Waiting Room",10,0);
    }

    public HospitalWaitingRoom(int maximumcapacityofpatients)
    {
        this("Waiting Room with Capacity: " + maximumcapacityofpatients,maximumcapacityofpatients,0);
    }

    public HospitalWaitingRoom(int maximumcapacityofpatients, int amountofpatientoccupants)
    {
        this("Waiting Room with Capacity: " + maximumcapacityofpatients,maximumcapacityofpatients,amountofpatientoccupants);
    }

    public HospitalWaitingRoom(String nameofroom)
    {
        this(nameofroom,10,0);
    }

    public HospitalWaitingRoom(String nameofroom, int maximumcapacityofpatients)
    {
        this(nameofroom,maximumcapacityofpatients,0);
    }


    public boolean addPatientToRoomList(int simulationtimeelapsed, ERPatient patienttoadd)
    {
        boolean success = super.addPatientToRoomList(patienttoadd);

        if (success == true)
        {
            patienttoadd.recordArrival(simulationtimeelapsed);
            success = ERqueue.add(patienttoadd);
            if (debugMode == true) System.out.println("Waiting Room Queue: " + ERqueue + "\n");
        }

        if (debugMode == true) System.out.println("Patient " + patienttoadd + (success ? " was successfully added " : " was not successfully added ") + "to the waiting queue at the " + simulationtimeelapsed + " minute mark.\n");

        return success;
    }

    public boolean addNewPatientToRoomList(int simulationtimeelapsed)
    {
        ERPatient patientToAdd = new ERPatient();

        if (debugMode == true) System.out.println("New Patient received to Waiting Room: " + patientToAdd + "\n");

        boolean success = addPatientToRoomList(simulationtimeelapsed, patientToAdd);

        return success;
    }

    public ERPatient fetchNextPatient(int simulationtimeelapsed)
    {
        ERPatient nextPatientToReturn = ERqueue.poll();
        super.removePatientFromRoomList(nextPatientToReturn);

        nextPatientToReturn.recordAdmittance(simulationtimeelapsed);

        if (debugMode == true) System.out.println("Bringing Patient from Waiting Room: " + nextPatientToReturn + "\n");

        return nextPatientToReturn;
    }

    public boolean patientIsWaiting()
    {
        boolean answerToReturn;

        if (ERqueue.isEmpty())
            answerToReturn = false;
        else
            answerToReturn = true;

        if (debugMode == true) System.out.println("======> The waiting room" + (answerToReturn ? " has a patient waiting" : " is empty") + ".\n");

        return answerToReturn;
    }

    public PriorityQueue<ERPatient> getERqueue()
    {
        return ERqueue;
    }

    @Override
    public boolean removePatientFromRoomList(ERPatient patienttoremove)
    {
        boolean success = super.removePatientFromRoomList(patienttoremove);

        if (success == true)
            success = ERqueue.remove(patienttoremove);

        if (debugMode == true) System.out.println("Updated Waiting Queue: " + ERqueue + "\n");

        return success;
    }

    @Override
    public boolean removePatientsFromRoomList(LinkedList<ERPatient> patientstoremove)
    {
        boolean success;

        success = super.removePatientsFromRoomList(patientstoremove);

        if (success == true)
            success = ERqueue.removeAll(patientstoremove);

        if (debugMode == true) System.out.println("Updated Waiting Queue: " + ERqueue);

        return success;
    }



    /*
        public boolean removePatient(int MedIDofpatienttoremove)
        {
            boolean success = false;

            success = ERqueue.removeIf(p -> (p.getMedID() == MedIDofpatienttoremove));

            return success;
        }

        public boolean removePatient(String nameofpatienttoremove)
        {
            boolean success = false;
            ERPatient patientToRemove = getPatient(nameofpatienttoremove);

            if (patientToRemove != null)
            {
                removePatientFromRoomList(patientToRemove);
                success = true;
            }
            else
                success = false;

            return success;
        }
    */

    private ERPatient getPatient(String nameofpatient)
    {
        ERPatient ERpatientToReturn = null;
        Iterator<ERPatient> i = ERqueue.iterator();

        while (i.hasNext() && ERpatientToReturn != null)
            if (nameofpatient.equals(i.next().getName()))
                ERpatientToReturn = i.next();

        return ERpatientToReturn;
    }

    /*
    public class ERPriorityQueue<E> extends PriorityQueue<E>
    {
        public String toString()
        {

        }
    }
    */
}

    class WaitingRoomPatientComparator implements Comparator<ERPatient>
    {
        @Override
        public int compare(ERPatient p1, ERPatient p2)
        {
            int answerToReturn = p1.getESIstatus().compareTo(p2.getESIstatus());

            if (answerToReturn == 0)
                if (p1.getArrival() < p2.getArrival())
                    answerToReturn = -1;
                else if (p1.getArrival() > p2.getArrival())
                    answerToReturn = 1;
                else
                    answerToReturn = 0;

            return answerToReturn;
        }
    }
