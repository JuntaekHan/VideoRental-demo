package video.rental.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import video.rental.demo.domain.Rating;
import video.rental.demo.domain.Rental;
import video.rental.demo.domain.Video;

class RentalTest {
	static final LocalDate UNUSED_DATE = null;
	final Video ANY_VIDEO = new Video("ANY_TITLE", Video.VHS, Video.NEW_RELEASE, Rating.EIGHTEEN, UNUSED_DATE);

	@Test
	@DisplayName("rental status should be `rented` when created")
	void test_rental_creation() {
		// Given

		// When
		Rental rental = new Rental(ANY_VIDEO);

		// Then
		assertEquals(0, rental.getStatus());
	}

	@Test
	@DisplayName("rental status should be `returned` when video returned")
	void test_video_return() {
		// Given
		Rental rental = new Rental(ANY_VIDEO);

		// When
		Video video = rental.returnVideo();

		// Then
		assertAll("Return status", 
				() -> assertEquals(1, rental.getStatus()),
		        () -> assertEquals(ANY_VIDEO.getTitle(), video.getTitle())
        );
	}

	@Test
	@DisplayName("return_date_cannot_precede_rented_date")
	void should_throw_exception_if_returned_date_is_earlier_than_rent_date() {
		// Given
		Rental rental = new Rental(ANY_VIDEO);

		// When
		// Then
		assertThrows(IllegalArgumentException.class, () -> rental.setReturnDate(LocalDateTime.now().minusDays(1)));
	}

	@Test
	void daysRented_should_return_1_if_returned_within_the_day() {
		// Given
		Rental rental = new Rental(ANY_VIDEO);

		// When
		rental.setReturnDate(rental.getRentDate().plusHours(23));

		// Then
		assertEquals(1, rental.getDaysRented());
	}

	@Test
	void daysRented_should_return_2_if_returned_within_two_days() {
		// Given
		Rental rental = new Rental(ANY_VIDEO);

		// When
		rental.setReturnDate(rental.getRentDate().plusDays(1));
		// Then
		assertEquals(2, rental.getDaysRented());
		
		// When
		rental.setReturnDate(rental.getRentDate().plusHours(24));
		
		// Then
		assertEquals(2, rental.getDaysRented());
	}

	@Test
	void daysRented_for_not_yet_returned_video() {
		// Given
		Rental rental = new Rental(ANY_VIDEO);

		// When
		rental.setRentDate(LocalDateTime.now().minusHours(23));

		// Then
		assertEquals(1, rental.getDaysRented());
		
		// When
		rental.setRentDate(LocalDateTime.now().minusDays(1));
		
		// Then
		assertEquals(2, rental.getDaysRented());
	}

}