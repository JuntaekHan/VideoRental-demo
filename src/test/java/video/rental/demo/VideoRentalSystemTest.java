package video.rental.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class VideoRentalSystemTest {

	private GoldenMaster goldenMaster = new GoldenMaster();
	
	@Test
	@Disabled
	void generateGoldenMaster() {
		// Given (Arrange)
		
		// When (Act)
		goldenMaster.generate();

		// Then (Assert)
		
	}
	
	@Test
//	@Disabled
	void check_runResult_against_GoldenMaster() {
		// Given (Arrange)
		String expected = goldenMaster.getGoldenMaster();
		
		// When (Act)
		String acutal = goldenMaster.getRunResult();

		// Then (Assert)
		assertEquals(expected, acutal);
	}
}
