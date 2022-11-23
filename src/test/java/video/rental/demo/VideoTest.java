package video.rental.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import video.rental.demo.domain.Customer;
import video.rental.demo.domain.Rating;
import video.rental.demo.domain.Video;

class VideoTest {

    static final LocalDate UNUSED_DATE = null;

    @Test
    void should_not_allow_rent_if_customer_under_age() {
        // Given
        Video video = new Video("ANY_TITLE", Video.CD, Video.REGULAR, Rating.FIFTEEN, UNUSED_DATE);
        Customer shawn = new Customer(2, "Shawn", LocalDate.now().minusYears(13));

        // When
        boolean result = video.rentFor(shawn);

        // Then
        assertFalse(result);
    }

    @Test
    void should_create_rental_if_customer_of_legal_age() {
        // Given
        Video video = new Video("ANY_TITLE", Video.VHS, Video.NEW_RELEASE, Rating.EIGHTEEN, UNUSED_DATE);
        Customer james = new Customer(0, "James", LocalDate.now().minusYears(20));

        // When
        boolean result = video.rentFor(james);

        // Then
        assertAll("Valid rent", 
        		() -> assertTrue(result),
        		() -> assertEquals(1, james.getRentals().size()),
        		() -> assertTrue(video.isRented())
        );
    }

}