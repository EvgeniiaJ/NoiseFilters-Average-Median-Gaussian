import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NoiseFilters3Methods {

	public static void main(String[] args) {

		String dataFileName;
		String maskFileName;
		String averageFilterFileName;
		String medianFilterFileName;
		String gaussianFilterFileName;

		if (args.length != 5) {
			System.out.println("Invalid number of arguments.");
			System.out.println(
					"There should be data file, mask file, averageFilterOutput file, gaussianFilterOutputFile, amd medianFilterOutputFile.");
			System.exit(0);
		}
		try {

			dataFileName = args[0];
			maskFileName = args[1];
			averageFilterFileName = args[2];
			medianFilterFileName = args[4];
			gaussianFilterFileName = args[3];

			Scanner data = new Scanner(new File(dataFileName));
			Scanner mask = new Scanner(new File(maskFileName));
			FileWriter averageFilter = new FileWriter(new File(averageFilterFileName));
			FileWriter medianFilter = new FileWriter(new File(medianFilterFileName));
			FileWriter gaussianFilter = new FileWriter(new File(gaussianFilterFileName));

			ImageProcessing image = new ImageProcessing(data, mask);
			image.loadImage(data, image.mirrorFramedArray);
			image.loadMask(mask, image.maskArray);
			image.mirrorFraming(image.mirrorFramedArray);

			image.computeAverageFilterImage(image.averageFilterArray);
			image.outputArrayToFile(image.averageFilterArray, averageFilter);

			image.computeMedianFilterImage(image.medianFilterArray);
			image.outputArrayToFile(image.medianFilterArray, medianFilter);

			image.computeGaussianFilterImage(image.gaussianFilterArray);
			image.outputArrayToFile(image.gaussianFilterArray, gaussianFilter);

			data.close();
			mask.close();
			averageFilter.close();
			medianFilter.close();
			gaussianFilter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
