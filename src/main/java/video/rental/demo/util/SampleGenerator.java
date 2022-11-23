package video.rental.demo.util;

import java.time.LocalDate;
import java.util.List;

import video.rental.demo.domain.Customer;
import video.rental.demo.domain.Rating;
import video.rental.demo.domain.Rental;
import video.rental.demo.domain.Repository;
import video.rental.demo.domain.Video;

public class SampleGenerator {
	private Repository repository;

	public SampleGenerator(Repository repository) {
		this.repository = repository;
	}

	public void generateSamples() {
		Customer james = new Customer(0, "James", LocalDate.parse("1975-05-15"));
		Customer brown = new Customer(1, "Brown", LocalDate.parse("2002-03-17"));
		Customer shawn = new Customer(2, "Shawn", LocalDate.parse("2010-11-11"));
		repository.saveCustomer(james);
		repository.saveCustomer(brown);
		repository.saveCustomer(shawn);

		Video v1 = new Video("V1", Video.CD, Video.REGULAR, Rating.FIFTEEN, LocalDate.of(2018, 1, 1));
		v1.setRented(true);
		Video v2 = new Video("V2", Video.DVD, Video.NEW_RELEASE, Rating.TWELVE, LocalDate.of(2018, 3, 1));
		v2.setRented(true);
		Video v3 = new Video("V3", Video.VHS, Video.NEW_RELEASE, Rating.EIGHTEEN, LocalDate.of(2018, 3, 1));

		repository.saveVideo(v1);
		repository.saveVideo(v2);
		repository.saveVideo(v3);

		Rental r1 = new Rental(v1);
		Rental r2 = new Rental(v2);

		List<Rental> rentals = james.getRentals();
		rentals.add(r1);
		rentals.add(r2);
		james.setRentals(rentals);
		repository.saveCustomer(james);
	}

}
