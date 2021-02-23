#include <iostream>
#include <fstream>
#include <string>

#include "ImageProcessing.h"

using namespace std;

int main(int argc, char** argv) {
	string dataFileName;
	string maskFileName;
	string averageFilterFileName;
	string medianFilterFileName;
	string gaussianFilterFileName;

	if (argc != 6) {
		cout << "Invalid number of arguments."<< endl;
		cout << "There should be data file, mask file, averageFilterOutput file, gaussianFilterOutputFile, amd medianFilterOutputFile." << endl;
		exit(0);
	}
	try {

		dataFileName = argv[1];
		maskFileName = argv[2];
		averageFilterFileName = argv[3];
		medianFilterFileName = argv[5];
		gaussianFilterFileName = argv[4];

		ifstream data;
		ifstream mask;
		ofstream averageFilter;
		ofstream medianFilter;
		ofstream gaussianFilter;

		data.open(dataFileName);
		mask.open(maskFileName);
		averageFilter.open(averageFilterFileName);
		medianFilter.open(medianFilterFileName);
		gaussianFilter.open(gaussianFilterFileName);

		ImageProcessing* image = new ImageProcessing(data, mask);
		image->loadImage(data, image->mirrorFramedArray);
		image->loadMask(mask, image->maskArray);
		image->mirrorFraming(image->mirrorFramedArray);

		image->computeAverageFilterImage(image->averageFilterArray);
		image->outputArrayToFile(image->averageFilterArray, averageFilter);

		image->computeMedianFilterImage(image->medianFilterArray);
		image->outputArrayToFile(image->medianFilterArray, medianFilter);

		image->computeGaussianFilterImage(image->gaussianFilterArray);
		image->outputArrayToFile(image->gaussianFilterArray, gaussianFilter);


		data.close();
		mask.close();
		averageFilter.close();
		medianFilter.close();
		gaussianFilter.close();
	}
	catch (exception e) {
		cout << "There is an error: " << e.what();
	}
	return 0;
}