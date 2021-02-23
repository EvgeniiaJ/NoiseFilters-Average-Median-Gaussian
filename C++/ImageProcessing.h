#pragma once
#include <iostream>
#include <fstream>
#include <string>
using namespace std;

class ImageProcessing{

private:

	int numRows;
	int numCols;
	int minValue;
	int maxValue;

	int maskRows;
	int maskCols;
	int maskMin;
	int maskMax;

public:

	int* neighborsArray;
	int* histogramArray;
	int** maskArray;
	int** mirrorFramedArray;
	int** averageFilterArray;
	int** medianFilterArray;
	int** gaussianFilterArray;

	ImageProcessing(ifstream&, ifstream&);

	void initializeArrays();

	void mirrorFraming(int**);

	void loadImage(ifstream&, int**);

	void loadMask(ifstream&, int**);

	void computeAverageFilterImage(int**);

	int average3x3(int, int, int*);

	void loadNeighbors(int, int, int*);

	void computeMedianFilterImage(int**);

	void sort(int*);

	void computeGaussianFilterImage(int**);

	int performConvolution(int, int, int*, int**);

	void outputArrayToFile(int**, ofstream&);

	void computeHistogram(int**, int*);

	void printHistogram(ofstream&, int*);
};

