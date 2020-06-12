package video.rental.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public class CmdUI {
	// JPA EntityManager
	private static EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

	private static Scanner scanner = new Scanner(System.in);

	public void start() {
		generateSamples();

		boolean quit = false;
		while (!quit) {
			int command = getCommand();
			switch (command) {
			case 0:
				quit = true;
				break;
			case 1:
				listCustomers();
				break;
			case 2:
				listVideos();
				break;
			case 3:
				register("customer");
				break;
			case 4:
				register("video");
				break;
			case 5:
				rentVideo();
				break;
			case 6:
				returnVideo();
				break;
			case 7:
				getCustomerReport();
				break;
			case 8:
				clearRentals();
				break;
			default:
				break;
			}
		}
		System.out.println("Bye");
	}

	public void clearRentals() {
		System.out.println("Enter customer code: ");
		int customerCode = scanner.nextInt();

		Customer foundCustomer = findCustomerById(customerCode);

		if (foundCustomer == null) {
			System.out.println("No customer found");
		} else {
			System.out.println("Id: " + foundCustomer.getCode() + "\nName: " + foundCustomer.getName() + "\tRentals: "
					+ foundCustomer.getRentals().size());
			for (Rental rental : foundCustomer.getRentals()) {
				System.out.print("\tTitle: " + rental.getVideo().getTitle() + " ");
				System.out.print("\tPrice Code: " + rental.getVideo().getPriceCode());
			}

			List<Rental> rentals = new ArrayList<Rental>();
			foundCustomer.setRentals(rentals);

			saveCustomer(foundCustomer);
		}
	}

	public void returnVideo() {
		System.out.println("Enter customer code: ");
		int customerCode = scanner.nextInt();

		Customer foundCustomer = findCustomerById(customerCode);
		if (foundCustomer == null)
			return;

		System.out.println("Enter video title to return: ");
		String videoTitle = scanner.next();

		List<Rental> customerRentals = foundCustomer.getRentals();

		for (Rental rental : customerRentals) {
			if (rental.getVideo().getTitle().equals(videoTitle) && rental.getVideo().isRented()) {
				Video video = rental.returnVideo();
				video.setRented(false);
				saveVideo(video);
				break;
			}
		}

		saveCustomer(foundCustomer);
	}

	public void listVideos() {
		System.out.println("List of videos");

		List<Video> videos = findAllVideos();

		for (Video video : videos) {
			System.out.println(
					"Video type: " + video.getVideoType() + 
					"\tPrice code: " + video.getPriceCode() + 
					"\tRating: " + video.getVideoRating() +
					"\tTitle: " + video.getTitle()
					); 
		}
		System.out.println("End of list");
	}

	public void listCustomers() {
		System.out.println("List of customers");

		List<Customer> customers = findAllCustomers();

		for (Customer customer : customers) {
			System.out.println("ID: " + customer.getCode() + "\nName: " + customer.getName() + "\tRentals: "
					+ customer.getRentals().size());
			for (Rental rental : customer.getRentals()) {
				System.out.print("\tTitle: " + rental.getVideo().getTitle() + " ");
				System.out.print("\tPrice Code: " + rental.getVideo().getPriceCode());
				System.out.println("\tReturn Status: " + rental.getStatus());
			}
		}
		System.out.println("End of list");
	}

	public void getCustomerReport() {
		System.out.println("Enter customer code: ");
		int code = scanner.nextInt();

		Customer foundCustomer = findCustomerById(code);

		if (foundCustomer == null) {
			System.out.println("No customer found");
		} else {
			String result = foundCustomer.getReport();
			System.out.println(result);
		}
	}

	public void rentVideo() {
		System.out.println("Enter customer code: ");
		int code = scanner.nextInt();

		Customer foundCustomer = findCustomerById(code);
		if (foundCustomer == null)
			return;

		System.out.println("Enter video title to rent: ");
		String videoTitle = scanner.next();

		Video foundVideo = findVideoByTitle(videoTitle);

		if (foundVideo == null)
			return;

		if (foundVideo.isRented() == true)
			return;

		Boolean status = foundVideo.rentFor(foundCustomer);
		if (status == true) {
			saveVideo(foundVideo);
			saveCustomer(foundCustomer);
		} else {
			return;
		}
	}

	public void register(String object) {
		if (object.equals("customer")) {
			System.out.println("Enter customer name: ");
			String name = scanner.next();

			System.out.println("Enter customer code: ");
			int code = scanner.nextInt();

			System.out.println("Enter customer birthday: ");
			String dateOfBirth = scanner.next();

			Customer customer = new Customer(code, name, LocalDate.parse(dateOfBirth));
			saveCustomer(customer);
		} else {
			System.out.println("Enter video title to register: ");
			String title = scanner.next();

			System.out.println("Enter video type( 1 for VHD, 2 for CD, 3 for DVD ):");
			int videoType = scanner.nextInt();

			System.out.println("Enter price code( 1 for Regular, 2 for New Release 3 for Children ):");
			int priceCode = scanner.nextInt();

			System.out.println("Enter video rating( 1 for 12, 2 for 15, 3 for 18 ):");
			int videoRating = scanner.nextInt();
			
			LocalDate registeredDate = LocalDate.now();
			Rating rating;
			if (videoRating == 1) rating = Rating.TWELVE;
			else if (videoRating == 2) rating = Rating.FIFTEEN;
			else if (videoRating == 3) rating = Rating.EIGHTEEN;
			else throw new IllegalArgumentException("No such rating " + videoRating);
			
			Video video = new Video(title, videoType, priceCode, rating, registeredDate);

			saveVideo(video);
		}
	}

	public int getCommand() {
		System.out.println("\nSelect a command !");
		System.out.println("\t 0. Quit");
		System.out.println("\t 1. List customers");
		System.out.println("\t 2. List videos");
		System.out.println("\t 3. Register customer");
		System.out.println("\t 4. Register video");
		System.out.println("\t 5. Rent video");
		System.out.println("\t 6. Return video");
		System.out.println("\t 7. Show customer report");
		System.out.println("\t 8. Show customer and clear rentals");

		int command = scanner.nextInt();

		return command;
	}

	private void generateSamples() {
		Customer james = new Customer(0, "James", LocalDate.parse("1975-05-15"));
		Customer brown = new Customer(1, "Brown", LocalDate.parse("2002-03-17"));
        Customer shawn = new Customer(2, "Shawn", LocalDate.parse("2010-11-11"));
		saveCustomer(james);
		saveCustomer(brown);
		saveCustomer(shawn);

		Video v1 = new Video("V1", Video.CD, Video.REGULAR, Rating.FIFTEEN, LocalDate.of(2018, 1, 1));
		v1.setRented(true);
		Video v2 = new Video("V2", Video.DVD, Video.NEW_RELEASE, Rating.TWELVE, LocalDate.of(2018, 3, 1));
		v2.setRented(true);
        Video v3 = new Video("V3", Video.VHS, Video.NEW_RELEASE, Rating.EIGHTEEN, LocalDate.of(2018, 3, 1));

		saveVideo(v1);
		saveVideo(v2);
		saveVideo(v3);

		Rental r1 = new Rental(v1);
		Rental r2 = new Rental(v2);

		List<Rental> rentals = james.getRentals();
		rentals.add(r1);
		rentals.add(r2);
		james.setRentals(rentals);
		saveCustomer(james);
	}

	/*
	 * Database Access private methods
	 */
	private Customer findCustomerById(int code) {
		em.getTransaction().begin();
		Customer customer = em.find(Customer.class, code);
		em.getTransaction().commit();
		return customer;
	}

	private Video findVideoByTitle(String title) {
		em.getTransaction().begin();
		Video video = em.find(Video.class, title);
		em.getTransaction().commit();
		return video;
	}

	private List<Customer> findAllCustomers() {
		TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
		return query.getResultList();
	}

	private List<Video> findAllVideos() {
		TypedQuery<Video> query = em.createQuery("SELECT c FROM Video c", Video.class);
		return query.getResultList();
	}

	private void saveCustomer(Customer customer) {
		try {
			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();
		} catch (PersistenceException e) {
			return;
		}
	}

	private void saveVideo(Video video) {
		try {
			em.getTransaction().begin();
			em.persist(video);
			em.getTransaction().commit();
		} catch (PersistenceException e) {
			return;
		}
		return;
	}
}
