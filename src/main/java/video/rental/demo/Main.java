package video.rental.demo;

import video.rental.demo.application.Interactor;
import video.rental.demo.domain.Repository;
import video.rental.demo.infrastructure.RepositoryMemImpl;
import video.rental.demo.presentation.GraphicUI;
import video.rental.demo.util.SampleGenerator;

public class Main {

	public static void main(String[] args) {
		Repository repository = new RepositoryMemImpl();
		SampleGenerator sample = new SampleGenerator(repository);
		sample.generateSamples();
		Interactor interactor = new Interactor(repository);
		GraphicUI graphicUI = new GraphicUI(interactor);
		graphicUI.start();
	}
}
