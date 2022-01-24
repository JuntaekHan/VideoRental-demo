package video.rental.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public class CmdUI {
    // JPA EntityManager
    private static final EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

    private static final Scanner scanner = new Scanner(System.in);

    //@formatter:off
	public void start() {
        scanner.useDelimiter("\\r\\n|\\n");
		generateSamples();

		boolean quit = false;
		while (!quit) {
            try {
			    switch (getCommand()) {
				    case 0: quit = true; 			break;
				    case 1: listCustomers(); 		break;
				    case 2: listVideos(); 			break;
				    case 3: register("customer"); 	break;
				    case 4: register("video"); 		break;
				    case 5: rentVideo(); 			break;
				    case 6: returnVideo(); 			break;
				    case 7: getCustomerReport(); 	break;
				    case 8: clearRentals(); 		break;
				    default: 						break;
			    }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
		}
		System.out.println("Bye");
	}
	//@formatter:on

    public void clearRentals() {
        System.out.println("Enter customer code: ");
        int customerCode = scanner.nextInt();

        Customer foundCustomer = findCustomerById(customerCode);
        if (foundCustomer == null) {
            throw new IllegalArgumentException("No such customer exists");
        }

        System.out.println("Id: " + foundCustomer.getCode() + ", "
                + "Name: " + foundCustomer.getName() + ", "
                + "Rentals: " + foundCustomer.getRentals().size());
        for (Rental rental : foundCustomer.getRentals()) {
            System.out.print("\tTitle: " + rental.getVideo().getTitle() + ", "
                    + "Price Code: " + rental.getVideo().getPriceCode());
        }

        foundCustomer.setRentals(new ArrayList<Rental>());
        saveCustomer(foundCustomer);
    }

    public void returnVideo() {
        System.out.println("Enter customer code: ");
        int customerCode = scanner.nextInt();
        Customer foundCustomer = findCustomerById(customerCode);
        if (foundCustomer == null) {
            throw new IllegalArgumentException("No such customer exists");
        }

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

        for (Video video : findAllVideos()) {
            System.out.println("Video type: " + video.getVideoType() + ", "
                    + "Price code: " + video.getPriceCode() + ", "
                    + "Rating: " + video.getVideoRating() + ", "
                    + "Title: " + video.getTitle());
        }
        System.out.println("End of list");
    }

    public void listCustomers() {
        System.out.println("List of customers");

        for (Customer customer : findAllCustomers()) {
            System.out.println("ID: " + customer.getCode() + ", "
                    + "Name: " + customer.getName() + ", "
                    + "Rentals: " + customer.getRentals().size());
            for (Rental rental : customer.getRentals()) {
                System.out.println("\tTitle: " + rental.getVideo().getTitle() + ", "
                        + "Price Code: " + rental.getVideo().getPriceCode() + ", "
                        + "Return Status: " + rental.getStatus());
            }
        }
        System.out.println("End of list");
    }

    public void getCustomerReport() {
        System.out.println("Enter customer code: ");
        int code = scanner.nextInt();

        Customer foundCustomer = findCustomerById(code);
        if (foundCustomer == null) {
            throw new IllegalArgumentException("No such customer exists");
        }

        System.out.println(foundCustomer.getReport());
    }

    public void rentVideo() {
        System.out.println("Enter customer code: ");
        int code = scanner.nextInt();

        Customer foundCustomer = findCustomerById(code);
        if (foundCustomer == null)
            throw new IllegalArgumentException("No such customer exists");

        System.out.println("Enter video title to rent: ");
        String videoTitle = scanner.next();

        Video foundVideo = findVideoByTitle(videoTitle);
        if (foundVideo == null)
            throw new IllegalArgumentException("Cannot find the video " + videoTitle);
        if (foundVideo.isRented())
            throw new IllegalStateException("The video " + videoTitle + " is already rented");

        if (foundVideo.rentFor(foundCustomer)) {
            saveVideo(foundVideo);
            saveCustomer(foundCustomer);
        } else {
            throw new IllegalStateException("Customer " + foundCustomer.getName()
                    + " cannot rent this video because he/she is under age.");
        }
    }

    public void register(String object) {
        if (object.equals("customer")) {
            System.out.println("Enter customer name: ");
            String name = scanner.next();

            System.out.println("Enter customer code: ");
            int code = scanner.nextInt();

            System.out.println("Enter customer birthday (YYYY-MM-DD): ");
            String dateOfBirth = scanner.next();

            // dirty hack for the moment
            if (findAllCustomers().stream().mapToInt(Customer::getCode).anyMatch(c -> c == code)) {
                throw new IllegalArgumentException("Customer code " + code + " already exists");
            }

            saveCustomer(new Customer(code, name, LocalDate.parse(dateOfBirth)));
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

            // dirty hack for the moment
            if (findAllVideos().stream().map(Video::getTitle).anyMatch(t -> t.equals(title))) {
                throw new IllegalArgumentException("Video " + title + " already exists");
            }

            saveVideo(new Video(title, videoType, priceCode, rating, registeredDate));
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

        return scanner.nextInt();
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

    private void saveCustomer(Customer customer) {
        doIt(customer, em::persist);
    }

    private void saveVideo(Video video) {
        doIt(video, em::persist);
    }

    /*
     * Database Access private methods
     */

    private List<Customer> findAllCustomers() {
        TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
        return query.getResultList();
    }

    private List<Video> findAllVideos() {
        TypedQuery<Video> query = em.createQuery("SELECT c FROM Video c", Video.class);
        return query.getResultList();
    }

    private <T> T find(Supplier<T> action) {
        T value = null;
        try {
            em.getTransaction().begin();
            value = action.get();
            em.getTransaction().commit();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    private Customer findCustomerById(int code) {
        return find(() -> em.find(Customer.class, code));
    }

    private Video findVideoByTitle(String title) {
        return find(() -> em.find(Video.class, title));
    }

    private <T> void doIt(T value, Consumer<T> action) {
        try {
            em.getTransaction().begin();
            action.accept(value);
            em.getTransaction().commit();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }
    }

}
