package exercise3_graded;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class MarsXTest {
    private List<Passenger> passengers;
    private List<VIPPassenger> vipPassengers;
    private BookedTrip bookedTrip;
    VIPPassenger vipPassenger1;
    VIPPassenger vipPassenger2;
    private static final Random random = new Random();

    @Before
    public void setUp() {
        passengers = new ArrayList<>();
        vipPassengers = new ArrayList<>();        
        


        // Generate randomized passenger details
        String[] names = {"Balthazar", "Oedipus", "Zaphod", "Trillian", "Arthur", "Ford", "Marvin"};
        String name = names[random.nextInt(names.length)];
        
        LocalDate dob1 = LocalDate.of(random.nextInt(50) + 1970, random.nextInt(12) + 1, random.nextInt(28) + 1);
        LocalDate dob2 = LocalDate.of(random.nextInt(12) + 2010, random.nextInt(12) + 1, random.nextInt(28) + 1);
        
        String[] medicalConditions = {"Asthma", "None", "Stress", "Diabetes", "Insomnia", "Anxiety"};
        String medicalCondition = medicalConditions[random.nextInt(medicalConditions.length)];
        int insuranceNumber = random.nextInt(99999);
        String insuranceCompany = random.nextBoolean() ? "RedDOT" : "Far&Beyond";
        String insuranceContact = "555-" + (1000 + random.nextInt(9000));
        String vipCardNumber = "A" + (10000 + random.nextInt(9999)) + "MRSX";

        Passenger passenger1 = new Passenger("MX" + name.substring(0, 3).toUpperCase() + dob1.getYear(),
                name, dob1, medicalCondition, insuranceNumber, insuranceCompany, insuranceContact);

        Passenger passenger2 = new Passenger("MX" + name.substring(0, 3).toUpperCase() + dob2.getYear(),
                name, dob2, medicalCondition, insuranceNumber, insuranceCompany, insuranceContact);
        passengers.add(passenger1);
        passengers.add(passenger2);

        vipPassenger1 = new VIPPassenger("MX" + name.substring(0, 3).toUpperCase() + dob1.getYear(), name,
        dob1, medicalCondition, insuranceNumber, insuranceCompany, insuranceContact, vipCardNumber);

        vipPassenger2 = new VIPPassenger("MX" + name.substring(0, 3).toUpperCase() + dob1.getYear(), name,
        dob1, medicalCondition, insuranceNumber, insuranceCompany, insuranceContact, vipCardNumber);

        vipPassengers.add(vipPassenger1);
        vipPassengers.add(vipPassenger2);

        // Generate random trip details
        TripType[] tripTypes = TripType.values();
        TripType randomTrip = tripTypes[random.nextInt(tripTypes.length)];
        LocalDate departure = LocalDate.of(2025, random.nextInt(12) + 1, random.nextInt(28) + 1);
        LocalDate returnDate = departure.plusDays(7 + random.nextInt(15)); // Ensuring at least 7 days stay

        bookedTrip = new BookedTrip("MX" + random.nextInt(99999), randomTrip, passengers, vipPassengers,(passengers.size() + vipPassengers.size()), 2, departure, returnDate);
    }

    
    @Test
    public void testInheritance() {
        
        assertTrue("VIPPassenger should inherit from Passenger", vipPassenger1 instanceof Passenger);
        assertTrue("VIPPassenger should inherit from Passenger", vipPassenger2 instanceof Passenger);
    }


    @Test
    public void testImportVIPPassengerDetails() {
        
        
        String path = "TestList.txt";
        RegisterVIPPassenger registerVIPPassenger = new RegisterVIPPassenger();
        registerVIPPassenger.importVIPPassengerDetails(path);

        assertTrue(registerVIPPassenger.getVipPassengers().get(1).getVipCardNumber().contains("A0255MRSX"));
        assertTrue(registerVIPPassenger.getVipPassengers().get(2).getPassengerName().contains("Ripley"));

        LocalDate dob = LocalDate.of(2018, 07, 12);
        Period agePeriod = Period.between(dob, LocalDate.now());
        int expectedAge = agePeriod.getYears();
        assertEquals(expectedAge, registerVIPPassenger.getVipPassengers().get(0).getAge());            

    }

    @Test
    public void testExportTripDetails() {
        String savedTestFile = "SavedTestTrip.txt";
        ExportTripDetails exportTripDetails = new ExportTripDetails();
        exportTripDetails.export(passengers, vipPassengers, bookedTrip, savedTestFile);

        try{
            FileReader fileReader = new FileReader(savedTestFile);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            

            StringBuilder testString = new StringBuilder();


            while(line != null){
                testString.append(line);
                testString.append(System.lineSeparator());
                line = reader.readLine();
            }

            assertTrue(testString.toString().contains("Booking ID: " + bookedTrip.getBookingID()));
            assertTrue(testString.toString().contains("Total Passengers: " + bookedTrip.getTotalPassengers()));
            assertTrue(testString.toString().contains("Number of Children: " + bookedTrip.getNumberOfChildren()));
            assertTrue(testString.toString().contains("Date of Departure: " + bookedTrip.getDateOfDeparture()));
            assertTrue(testString.toString().contains("Date of Return: " + bookedTrip.getDateOfReturn()));
            assertTrue(testString.toString().contains("Total Ticket Price: €" + bookedTrip.getTotalTicketPrice()));
            assertTrue(testString.toString().contains("Passenger Name: " + passengers.get(0).getPassengerName()));
            assertTrue(testString.toString().contains("Passenger Name: " + passengers.get(1).getPassengerName()));
            assertTrue(testString.toString().contains("Passenger Name: " + vipPassengers.get(0).getPassengerName()));
            assertTrue(testString.toString().contains("Passenger Name: " + vipPassengers.get(1).getPassengerName()));
            assertTrue(testString.toString().contains("VIP Card Number: " + vipPassengers.get(0).getVipCardNumber()));
            assertTrue(testString.toString().contains("VIP Card Number: " + vipPassengers.get(1).getVipCardNumber()));
            

            reader.close();

        }catch(Exception e){
            System.out.println("An error occurred while reading the file." + e.getMessage());
        }
        
    }
}
    
    
    