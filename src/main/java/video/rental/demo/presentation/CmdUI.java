package video.rental.demo.presentation;

import java.util.Scanner;

import video.rental.demo.application.Interactor;

public class CmdUI {
    private static final Scanner scanner = new Scanner(System.in);

    private Interactor interactor;
    
	public CmdUI(Interactor interactor) {
		super();
		this.interactor = interactor;
	}

	public void start() {
        scanner.useDelimiter("\\r\\n|\\n");

        boolean quit = false;
        while (!quit) {
            //@formatter:off
            try {
                switch (getCommand()) {
                    case 0: quit = true;            break;
                    case 1: listCustomers();        break;
                    case 2: listVideos();           break;
                    case 3: register("customer");   break;
                    case 4: register("video");      break;
                    case 5: rentVideo();            break;
                    case 6: returnVideo();          break;
                    case 7: getCustomerReport();    break;
                    case 8: clearRentals();         break;
                    default:                        break;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            //@formatter:on
        }
        System.out.println("Bye");
    }

    public void clearRentals() {
        System.out.println("Enter customer code: ");
        int customerCode = scanner.nextInt();

        System.out.print(interactor.clearRentals(customerCode));
    }

	public void returnVideo() {
        System.out.println("Enter customer code: ");
        int customerCode = scanner.nextInt();
        
        System.out.println("Enter video title to return: ");
        String videoTitle = scanner.next();
        
        interactor.returnVideo(customerCode, videoTitle);
    }

	public void listVideos() {
        System.out.println("List of videos");

        System.out.print(interactor.listVideos());
        
        System.out.println("End of list");
    }

	public void listCustomers() {
        System.out.println("List of customers");

        System.out.print(interactor.listCustomers());
        
        System.out.println("End of list");
    }

	public void getCustomerReport() {
        System.out.println("Enter customer code: ");
        int code = scanner.nextInt();

        System.out.println(interactor.getCustomerReport(code));
    }

	public void rentVideo() {
        System.out.println("Enter customer code: ");
        int code = scanner.nextInt();

        System.out.println("Enter video title to rent: ");
        String videoTitle = scanner.next();
        
        interactor.rentVideo(code, videoTitle);
    }

	// Control Coupling Smell
	public void register(String object) {
        if (object.equals("customer")) {
            System.out.println("Enter customer name: ");
            String name = scanner.next();

            System.out.println("Enter customer code: ");
            int code = scanner.nextInt();
            
            System.out.println("Enter customer birthday (YYYY-MM-DD): ");
            String dateOfBirth = scanner.next();
            
            interactor.registerCustomer(name, code, dateOfBirth);
        } else {
            System.out.println("Enter video title to register: ");
            String title = scanner.next();

            System.out.println("Enter video type( 1 for VHD, 2 for CD, 3 for DVD ):");
            int videoType = scanner.nextInt();

            System.out.println("Enter price code( 1 for Regular, 2 for New Release 3 for Children ):");
            int priceCode = scanner.nextInt();

            System.out.println("Enter video rating( 1 for 12, 2 for 15, 3 for 18 ):");
            int videoRating = scanner.nextInt();

            interactor.registerVideo(title, videoType, priceCode, videoRating);
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

}
