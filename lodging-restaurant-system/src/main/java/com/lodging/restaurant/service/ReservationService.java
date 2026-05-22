package com.lodging.restaurant.service;

import com.lodging.restaurant.model.Folio;
import com.lodging.restaurant.model.Reservation;
import com.lodging.restaurant.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private FolioService folioService;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public Reservation saveReservation(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);
        if (savedReservation.getId() != null) {
            Folio folio = new Folio();
            folio.setReservation(savedReservation);
            folio.setTotalBill(0.0);
            folioService.saveFolio(folio);
        }
        return savedReservation;
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
