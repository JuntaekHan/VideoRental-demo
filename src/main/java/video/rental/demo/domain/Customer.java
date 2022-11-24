package video.rental.demo.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Customer {
	@Id
	private int code;
	private String name;
	private LocalDate dateOfBirth;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Rental> rentals = new ArrayList<>();

	public Customer() {	// for hibernate
	}

	public Customer(int code, String name, LocalDate dateOfBirth) {
		this.code = code;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
	}

	// copy ctor
	public Customer(Customer another) {
		this.code = another.code;
		this.name = another.name;
		this.dateOfBirth = another.dateOfBirth;
		this.rentals = new ArrayList<>(another.rentals);
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public List<Rental> getRentals() {
		return rentals;
	}

	public void setRentals(List<Rental> rentals) {
		this.rentals = rentals;
	}

	public String getReport() {
		String result = "Customer Report for " + getName() + "\n";

		List<Rental> rentals = getRentals();
		double totalCharge = 0;
		int totalPoint = 0;

		for (Rental each : rentals) {
			double eachCharge = each.getCharge();
			int eachPoint = each.getPoint();

			result += "\tTitle: " + each.getVideo().getTitle() + "\tDays rented: " + each.getDaysRented() + "\tCharge: " + eachCharge
					+ "\tPoint: " + eachPoint + "\n";

			totalCharge += eachCharge;
			totalPoint += eachPoint;
		}
		
		result += "Total charge: " + totalCharge + "\tTotal Point: " + totalPoint + "\n";

		if (totalPoint >= 10) {
			System.out.println("Congratulations! You earned two free coupons");
		} else if (totalPoint >= 5) {
			System.out.println("Congratulations! You earned one free coupon");
		}

		return result;
	}

	int getCustomerAge() {
		Calendar calDateOfBirth = Calendar.getInstance();
	    try {
	        calDateOfBirth.setTime(new SimpleDateFormat("yyyy-MM-dd")
	        		.parse(getDateOfBirth().toString()));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	
	    // get current date
	    Calendar calNow = Calendar.getInstance();
	    calNow.setTime(new java.util.Date());
	
	    // calculate age different in years and months
	    int ageYr = (calNow.get(Calendar.YEAR) - calDateOfBirth.get(Calendar.YEAR));
	    int ageMo = (calNow.get(Calendar.MONTH) - calDateOfBirth.get(Calendar.MONTH));
	
	    // decrement age in years if month difference is negative
	    if (ageMo < 0) {
	        ageYr--;
	    }
		return ageYr;
	}

}
