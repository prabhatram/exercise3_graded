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
    public void testExportTripDetails() throws IOException {
        
        String savedTestFile = "/tmp/testTripExport.txt";  // Hardcoded for CI compatibility

    
        ExportTripDetails exportTripDetails = new ExportTripDetails();
        exportTripDetails.export(passengers, vipPassengers, bookedTrip, savedTestFile);
    
        StringBuilder testString = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(savedTestFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                testString.append(line).append(System.lineSeparator());
            }
        }
    
        String output = testString.toString();
    
        assertTrue(output.contains("Booking ID: " + bookedTrip.getBookingID()));
        assertTrue(output.contains("Total Passengers: " + bookedTrip.getTotalPassengers()));
        assertTrue(output.contains("Number of Children: " + bookedTrip.getNumberOfChildren()));
        assertTrue(output.contains("Date of Departure: " + bookedTrip.getDateOfDeparture()));
        assertTrue(output.contains("Date of Return: " + bookedTrip.getDateOfReturn()));
        assertTrue(output.contains(Double.toString(bookedTrip.getTotalTicketPrice())));
        assertTrue(output.contains("Passenger Name: " + passengers.get(0).getPassengerName()));
        assertTrue(output.contains("Passenger Name: " + passengers.get(1).getPassengerName()));
        assertTrue(output.contains("VIP Card Number: " + vipPassengers.get(0).getVipCardNumber()));
        assertTrue(output.contains("VIP Card Number: " + vipPassengers.get(1).getVipCardNumber()));
        
    }
    
}
    
    
    