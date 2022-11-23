package video.rental.demo.domain;

import java.util.List;

public interface Repository {

	List<Customer> findAllCustomers();

	List<Video> findAllVideos();

	Customer findCustomerById(int code);

	Video findVideoByTitle(String title);

	void saveCustomer(Customer customer);

	void saveVideo(Video video);

}