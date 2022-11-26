package video.rental.demo.application;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import video.rental.demo.domain.Customer;
import video.rental.demo.domain.Rating;
import video.rental.demo.domain.Rental;
import video.rental.demo.domain.Repository;
import video.rental.demo.domain.Video;

public class Interactor {
	private Repository repository; 
	
	public Interactor(Repository repository) {
		super();
		this.repository = repository;
	}

	public String clearRentals(int customerCode) {
		StringBuilder builder = new StringBuilder();
		
		Customer foundCustomer = getRepository().findCustomerById(customerCode);
	    if (foundCustomer == null) {
	        throw new IllegalArgumentException("No such customer exists");
	    }
	
	    builder.append(foundCustomer.getInfo(false));
	
	    foundCustomer.setRentals(new ArrayList<Rental>());
	    getRepository().saveCustomer(foundCustomer);
	    
	    return builder.toString();
	}

	public void returnVideo(int customerCode, String videoTitle) {
		Customer foundCustomer = getRepository().findCustomerById(customerCode);
	    if (foundCustomer == null) {
	        throw new IllegalArgumentException("No such customer exists");
	    }
	
	    List<Rental> customerRentals = foundCustomer.getRentals();
	
	    for (Rental rental : customerRentals) {
	        if (rental.getVideo().getTitle().equals(videoTitle) && rental.getVideo().isRented()) {
	            Video video = rental.returnVideo();
	            video.setRented(false);
	            getRepository().saveVideo(video);
	            break;
	        }
	    }
	
	    getRepository().saveCustomer(foundCustomer);
	}

	public String listVideos() {
		StringBuilder builder = new StringBuilder();
		
		for (Video video : getRepository().findAllVideos()) {
	        builder.append("Video type: " + video.getVideoType() + ", "
	                + "Price code: " + video.getPriceCode() + ", "
	                + "Rating: " + video.getVideoRating() + ", "
	                + "Title: " + video.getTitle() + "\n");
	    }
		
		return builder.toString();
	}

	public String listCustomers() {
		StringBuilder builder = new StringBuilder();
		for (Customer customer : getRepository().findAllCustomers()) {
		    builder.append(customer.getInfo(true));
	    }
		return builder.toString();
	}

	public String getCustomerReport(int code) {
		Customer foundCustomer = getRepository().findCustomerById(code);
	    if (foundCustomer == null) {
	        throw new IllegalArgumentException("No such customer exists");
	    }
	
	    return foundCustomer.getReport();
	}

	public void rentVideo(int code, String videoTitle) {
		Customer foundCustomer = getRepository().findCustomerById(code);
	    if (foundCustomer == null)
	        throw new IllegalArgumentException("No such customer exists");
	
	    Video foundVideo = getRepository().findVideoByTitle(videoTitle);
	    if (foundVideo == null)
	        throw new IllegalArgumentException("Cannot find the video " + videoTitle);
	    if (foundVideo.isRented())
	        throw new IllegalStateException("The video " + videoTitle + " is already rented");
	
	    if (foundVideo.rentFor(foundCustomer)) {
	        getRepository().saveVideo(foundVideo);
	        getRepository().saveCustomer(foundCustomer);
	    } else {
	        throw new IllegalStateException("Customer " + foundCustomer.getName()
	                + " cannot rent this video because he/she is under age.");
	    }
	}

	public void registerCustomer(String name, int code, String dateOfBirth) {
		// dirty hack for the moment
		if (getRepository().findAllCustomers().stream().mapToInt(Customer::getCode).anyMatch(c -> c == code)) {
		    throw new IllegalArgumentException("Customer code " + code + " already exists");
		}
	
		try {
		    new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
		} catch (Exception ignored) {
		}
	
		getRepository().saveCustomer(new Customer(code, name, LocalDate.parse(dateOfBirth)));
	}

	public void registerVideo(String title, int videoType, int priceCode, int videoRating) {
		LocalDate registeredDate = LocalDate.now();
		Rating rating;
		if (videoRating == 1) rating = Rating.TWELVE;
		else if (videoRating == 2) rating = Rating.FIFTEEN;
		else if (videoRating == 3) rating = Rating.EIGHTEEN;
		else throw new IllegalArgumentException("No such rating " + videoRating);
	
		// dirty hack for the moment
		if (getRepository().findAllVideos().stream().map(Video::getTitle).anyMatch(t -> t.equals(title))) {
		    throw new IllegalArgumentException("Video " + title + " already exists");
		}
	
		getRepository().saveVideo(new Video(title, videoType, priceCode, rating, registeredDate));
	}

	private Repository getRepository() {
		return repository;
	}

}
