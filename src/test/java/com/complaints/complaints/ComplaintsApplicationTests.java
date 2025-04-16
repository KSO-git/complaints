package com.complaints.complaints;

import com.complaints.complaints.model.Complaint;
import com.complaints.complaints.repository.ComplaintsRepository;
import com.complaints.complaints.service.ComplaintsService;
import com.complaints.complaints.service.GeoLocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ComplaintsApplicationTests {

    private static Long defaultID = 1L;

    @Autowired
    private GeoLocationService geoLocationService;
    private ComplaintsRepository repository;
    private ComplaintsService service;

    @BeforeEach
    public void setUp() {
        repository = mock(ComplaintsRepository.class);
        geoLocationService = mock(GeoLocationService.class);
        service = new ComplaintsService(repository, geoLocationService);
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testAddNewComplaint() {
        Complaint complaint = new Complaint();
        complaint.setProductId("P1");
        complaint.setCustomer("Jan Kowalski");
        complaint.setDescription("Damaged");

        when(repository.findByProductIdAndCustomer("P1", "Jan Kowalski")).thenReturn(Optional.empty());
        when(geoLocationService.getCountryByIp(Mockito.anyString())).thenReturn("Africa");
        when(repository.save(Mockito.any(Complaint.class))).thenAnswer(i -> i.getArguments()[0]);

        Complaint result = service.addComplaint(complaint, "127.0.0.1");

        assertNotNull(result.getDateOfCreation());
        assertEquals(1, result.getCounter());
        assertEquals("P1", result.getProductId());
        assertEquals("Jan Kowalski", result.getCustomer());
        assertNotNull(result.getCountry());
    }

    @Test
    public void testComplaintCounter_addingOne() {
        Complaint complaint = new Complaint();
        complaint.setProductId("P2");
        complaint.setCustomer("Jan Kowalski");
        complaint.setDescription("Damaged2");
        complaint.setCounter(2);

        when(repository.findByProductIdAndCustomer("P2", "Jan Kowalski")).thenReturn(Optional.of(complaint));
        when(geoLocationService.getCountryByIp(Mockito.anyString())).thenReturn("Africa");
        when(repository.save(Mockito.any(Complaint.class))).thenAnswer(i -> i.getArguments()[0]);

        Complaint newComplaint = new Complaint();
        newComplaint.setProductId("P2");
        newComplaint.setCustomer("Jan Kowalski");
        newComplaint.setDescription("Damaged3");

        Complaint result = service.addComplaint(newComplaint, "127.0.0.1");

        assertEquals("P2", result.getProductId());
        assertEquals("Jan Kowalski", result.getCustomer());
        assertEquals(3, result.getCounter());
        assertEquals(complaint.getDescription(), "Damaged2");
    }

    @Test
    public void testEditComplaint() {
        Complaint complaint = new Complaint();
        complaint.setId(defaultID);
        complaint.setProductId("P2");
        complaint.setCustomer("Jan Kowalski");
        complaint.setDescription("Damaged2");
        complaint.setCounter(2);

        when(repository.findByProductIdAndCustomer("P2", "Jan Kowalski")).thenReturn(Optional.of(complaint));
        when(geoLocationService.getCountryByIp(Mockito.anyString())).thenReturn("Africa");
        when(repository.save(Mockito.any(Complaint.class))).thenAnswer(i -> i.getArguments()[0]);
        when(repository.findById(defaultID)).thenReturn(Optional.of(complaint));

        Complaint result = service.editComplaint(1L, "Damaged3");

        assertEquals("P2", result.getProductId());
        assertEquals("Jan Kowalski", result.getCustomer());
        assertEquals("Damaged3", complaint.getDescription());
    }

}
