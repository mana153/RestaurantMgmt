package com.lodgingrestaurant.config;

import com.lodgingrestaurant.models.*;
import com.lodgingrestaurant.repositories.*;
import com.lodgingrestaurant.services.BookingService;
import com.lodgingrestaurant.services.PartnerApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingService bookingService;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final PartnerApiService partnerApiService;

    @Autowired
    public DataInitializer(RoomRepository roomRepository,
                           GuestRepository guestRepository,
                           BookingService bookingService,
                           MenuItemRepository menuItemRepository,
                           OrderRepository orderRepository,
                           PartnerApiService partnerApiService) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.bookingService = bookingService;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.partnerApiService = partnerApiService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only run initialization if database is empty
        if (roomRepository.count() > 0) {
            return;
        }

        System.out.println(">>> Starting Database Initialization for Luxury Lodging & Restaurant System...");

        // 1. POPULATE LUXURY SUITES & ROOMS
        Room r101 = new Room("101", "STANDARD", 3500.0, true, "Elegant standard room with luxurious bedding, high-speed Wi-Fi, and garden view.");
        Room r102 = new Room("102", "STANDARD", 3800.0, true, "Charming courtyard-view room featuring standard premium comforts and classic furnishings.");
        Room r201 = new Room("201", "DELUXE", 6500.0, true, "Spacious deluxe room with high ceilings, private balcony, marble vanity, and modern minibar.");
        Room r202 = new Room("202", "DELUXE", 6800.0, true, "Executive deluxe room with premium views, comfortable workspace, and a plush king-size bed.");
        Room r301 = new Room("301", "SUITE", 12500.0, true, "Opulent suite with separate living parlor, luxury spa tub, complementary butler service, and skyline view.");
        Room r302 = new Room("302", "SUITE", 14000.0, true, "Imperial corner suite featuring magnificent panoramic views and elite luxury design details.");
        Room r500 = new Room("500", "ROYAL", 32000.0, true, "The Royal Penthouse. Pure luxury featuring private terrace, heated dip pool, grand fireplace, and personal concierge.");
        
        List<Room> rooms = Arrays.asList(r101, r102, r201, r202, r301, r302, r500);
        roomRepository.saveAll(rooms);
        System.out.println("Saved " + rooms.size() + " Luxury Lodging Rooms.");

        // 2. POPULATE VIP GUEST RECOGNITIONS
        Guest g1 = new Guest("Vikram", "Malhotra", "vikram.malhotra@gmail.com", "+91 98765 43210", "PASSPORT-A39281");
        Guest g2 = new Guest("Ananya", "Sharma", "ananya.sharma@yahoo.com", "+91 87654 32109", "AADHAAR-890283");
        Guest g3 = new Guest("Elena", "Rostova", "elena.ro@luxurytravel.com", "+1 312 555 0199", "PASSPORT-F29183");
        Guest g4 = new Guest("Rahul", "Verma", "rahul.verma@outlook.com", "+91 76543 21098", "VOTER-293812");

        List<Guest> guests = Arrays.asList(g1, g2, g3, g4);
        guestRepository.saveAll(guests);
        System.out.println("Registered " + guests.size() + " VIP Guest Profiles.");

        // 3. POPULATE REPRESENTATIVE BOOKINGS (Updates room availability)
        Booking b1 = new Booking(g1, r301, LocalDate.now(), LocalDate.now().plusDays(3), 0.0, "CONFIRMED");
        Booking b2 = new Booking(g3, r500, LocalDate.now().plusDays(2), LocalDate.now().plusDays(7), 0.0, "CONFIRMED");
        Booking b3 = new Booking(g2, r101, LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), 0.0, "CHECKED_IN");
        
        bookingService.saveBooking(b1);
        bookingService.saveBooking(b2);
        bookingService.saveBooking(b3);
        System.out.println("Created initial active bookings. Rooms 301, 500, and 101 are now marked reserved.");

        // 4. POPULATE LUXURY FINE-DINING MENU
        List<MenuItem> menu = Arrays.asList(
                // Starters
                new MenuItem("Truffle Arancini", "Crispy wild mushroom rice croquettes centered with liquid fontina cheese and grated black truffle oil.", 450.0, "STARTER", true, "arancini.jpg"),
                new MenuItem("Gold-Edged Bruschetta", "Herbed heirloom tomatoes on grilled sourdough topped with organic olive oil and micro-herbs.", 380.0, "STARTER", true, "bruschetta.jpg"),
                new MenuItem("Smoked Salmon Cone", "Dill cream cheese infused with citrus, wrapped in cold-smoked Scottish salmon with lumpfish caviar garnish.", 550.0, "STARTER", true, "salmon.jpg"),

                // Main Courses
                new MenuItem("Royal Wagyu Ribeye", "Grade A5 dry-aged Wagyu ribeye, pan-seared with rosemary butter, served with truffle mash and gold leaf garnish.", 2800.0, "MAIN_COURSE", true, "ribeye.jpg"),
                new MenuItem("Seared Chilean Seabass", "Pan-seared sea bass over creamy saffron cauliflower puree, toasted pine nuts, and lemon caper drizzle.", 1850.0, "MAIN_COURSE", true, "seabass.jpg"),
                new MenuItem("Saffron Forest Risotto", "Vibrant Carnaroli rice cooked in rich saffron vegetable broth, loaded with wild porcini and parmesan crisps.", 950.0, "MAIN_COURSE", true, "risotto.jpg"),

                // Desserts
                new MenuItem("Deconstructed Tiramisu", "Espresso-soaked ladyfingers layered with fresh whipped mascarpone clouds and dark cocoa dusting.", 500.0, "DESSERT", true, "tiramisu.jpg"),
                new MenuItem("Saffron Honey Panna Cotta", "Silky cream cooked with Persian saffron, organic wild honey reduction, and pistachio brittle.", 480.0, "DESSERT", true, "pannacotta.jpg"),
                new MenuItem("Lava Fondant & Gold Dust", "Decadent dark chocolate molten lava cake topped with edible gold flakes and vanilla bean gelato.", 600.0, "DESSERT", true, "lava.jpg"),

                // Beverages
                new MenuItem("Merlot Sangria Mocktail", "Rich pomegranate and berry juices infused with orange peel and sparkling rosemary water.", 350.0, "BEVERAGE", true, "mocktail.jpg"),
                new MenuItem("Lavender Blueberry Elixir", "Organic blueberry puree muddled with dried lavender flower syrup and freshly squeezed lemon soda.", 320.0, "BEVERAGE", true, "elixir.jpg"),
                new MenuItem("Imperial Matcha Latte", "Finest ceremonial Uji matcha whisked with warm oat milk and organic agave drizzle.", 280.0, "BEVERAGE", true, "matcha.jpg")
        );
        menuItemRepository.saveAll(menu);
        System.out.println("Imported " + menu.size() + " Royal Fine-Dining Dishes into the database.");

        // 5. POPULATE HISTORICAL RECENT REST ORDERS (Simulated and Direct)
        Order o1 = new Order("101", LocalDateTime.now().minusHours(2), 730.0, "DELIVERED", "IN-HOUSE");
        Order o2 = new Order(null, LocalDateTime.now().minusHours(1), 1430.0, "DELIVERED", "ZOMATO");
        Order o3 = new Order("201", LocalDateTime.now().minusMinutes(30), 2200.0, "PREPARING", "IN-HOUSE");
        Order o4 = new Order(null, LocalDateTime.now().minusMinutes(10), 1250.0, "PENDING", "SWIGGY");
        orderRepository.saveAll(Arrays.asList(o1, o2, o3, o4));

        // 6. INITIALIZE MOCK THIRD-PARTY API COUNTERS (Zomato/Swiggy stats)
        partnerApiService.initStats(
                54,   // Total API Requests
                24,   // Listing Checks
                18,   // Create Successes
                3,    // Create Failures
                6,    // Update Successes
                2,    // Update Failures
                1,    // Cancel Successes
                0     // Cancel Failures
        );

        System.out.println(">>> Database Initialization Completed Successfully. All records ready for run target!");
    }
}
