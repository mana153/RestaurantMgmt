package com.lodgingrestaurant.services;

import com.lodgingrestaurant.models.Booking;
import com.lodgingrestaurant.models.Room;
import com.lodgingrestaurant.repositories.BookingRepository;
import com.lodgingrestaurant.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Transactional
    public Booking saveBooking(Booking booking) {
        // Fetch the associated room
        if (booking.getRoom() != null && booking.getRoom().getId() != null) {
            Room room = roomRepository.findById(booking.getRoom().getId()).orElse(null);
            if (room != null) {
                booking.setRoom(room);
                
                // Calculate stay duration in nights
                long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
                if (nights <= 0) {
                    nights = 1; // Minimum 1 night
                }
                
                // Calculate and set total price
                booking.setTotalPrice(room.getPricePerNight() * nights);

                // If confirming, mark room as unavailable
                if ("CONFIRMED".equalsIgnoreCase(booking.getStatus()) || "CHECKED_IN".equalsIgnoreCase(booking.getStatus())) {
                    room.setAvailable(false);
                    roomRepository.save(room);
                }
            }
        }
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            String oldStatus = booking.getStatus();
            booking.setStatus(status);

            Room room = booking.getRoom();
            if (room != null) {
                // If status changes to CANCELLED or CHECKED_OUT, make the room available again
                if ("CANCELLED".equalsIgnoreCase(status) || "CHECKED_OUT".equalsIgnoreCase(status)) {
                    room.setAvailable(true);
                    roomRepository.save(room);
                } else if (("CONFIRMED".equalsIgnoreCase(status) || "CHECKED_IN".equalsIgnoreCase(status))
                           && !("CONFIRMED".equalsIgnoreCase(oldStatus) || "CHECKED_IN".equalsIgnoreCase(oldStatus))) {
                    // If moving back to confirmed status, make room unavailable
                    room.setAvailable(false);
                    roomRepository.save(room);
                }
            }
            return bookingRepository.save(booking);
        }
        return null;
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            Room room = booking.getRoom();
            if (room != null && !room.isAvailable()) {
                room.setAvailable(true);
                roomRepository.save(room);
            }
            bookingRepository.deleteById(id);
        }
    }
}
