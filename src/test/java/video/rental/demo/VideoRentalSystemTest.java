package video.rental.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class VideoRentalSystemTest {

	private GoldenMasterGraphic goldenMaster = new GoldenMasterGraphic();
	
	@Test
//	@Disabled
	void generateGoldenMaster() {
		// Given (Arrange)
		
		// When (Act)
		goldenMaster.generate();

		// Then (Assert)
		
	}
	
	@Test
	@EnabledOnOs(OS.WINDOWS)
	@Disabled
	void check_runResult_against_GoldenMaster_on_Windows() {
		// Given (Arrange)
		String expected = goldenMaster.getGoldenMaster();
		
		// When (Act)
		String acutal = goldenMaster.getRunResult();

		// Then (Assert)
		assertEquals(expected, acutal.replaceAll("\r\n", "\n"));
	}
	
	@Test
	@EnabledOnOs({OS.MAC, OS.LINUX})
//	@Disabled
	void check_runResult_against_GoldenMaster_on_Mac() {
		// Given (Arrange)
		String expected = goldenMaster.getGoldenMaster();
		
		// When (Act)
		String acutal = goldenMaster.getRunResult();

		// Then (Assert)
		assertEquals(expected, acutal);
	}
}
