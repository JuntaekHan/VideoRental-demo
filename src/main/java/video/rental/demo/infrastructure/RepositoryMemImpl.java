package video.rental.demo.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import video.rental.demo.domain.Customer;
import video.rental.demo.domain.Repository;
import video.rental.demo.domain.Video;

public class RepositoryMemImpl implements Repository {

	private HashMap<Integer, Customer> customers = new LinkedHashMap<>();
	private HashMap<String, Video> videos = new LinkedHashMap<>();
	
	@Override
	public List<Customer> findAllCustomers() {
		return new ArrayList<>(customers.values());
	}

	@Override
	public List<Video> findAllVideos() {
		return new ArrayList<>(videos.values());
	}

	@Override
	public Customer findCustomerById(int code) {
		return customers.get(code);
	}

	@Override
	public Video findVideoByTitle(String title) {
		return videos.get(title);
	}

	@Override
	public void saveCustomer(Customer customer) {
		customers.put(customer.getCode(), new Customer(customer));
	}

	@Override
	public void saveVideo(Video video) {
		videos.put(video.getTitle(), new Video(video));
	}

}
