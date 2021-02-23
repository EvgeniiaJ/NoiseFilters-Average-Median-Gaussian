import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ImageProcessing {
	private int numRows;
	private int numCols;
	private int minValue;
	private int maxValue;
	
	private int maskRows;
	private int maskCols;
	private int maskMin;
	private int maskMax;
	
	public int[] neighborsArray;
	public int[] histogramArray;
	public int[][] maskArray;
	public int[][] mirrorFramedArray;
	public int[][] averageFilterArray;
	public int[][] medianFilterArray;
	public int[][] gaussianFilterArray;
	
	public ImageProcessing(Scanner input1, Scanner input2) {
		if(input1.hasNext()) {
			this.numRows = input1.nextInt();
		}
		if(input1.hasNext()) {
			this.numCols = input1.nextInt();
		}
		if(input1.hasNext()) {
			this.minValue = input1.nextInt();
		}
		if(input1.hasNext()) {
			this.maxValue = input1.nextInt();
		}
		
		if(input2.hasNext()) {
			this.maskRows = input2.nextInt();
		}
		if(input2.hasNext()) {
			this.maskCols = input2.nextInt();
		}
		if(input2.hasNext()) {
			this.maskMin = input2.nextInt();
		}
		if(input2.hasNext()) {
			this.maskMax = input2.nextInt();
		}
		
		initializeArrays();
	}	
	
	public void initializeArrays() {
		
		this.mirrorFramedArray = new int[this.numRows + 2][this.numCols + 2];
		this.averageFilterArray = new int[this.numRows + 2][this.numCols + 2];
		this.medianFilterArray = new int[this.numRows + 2][this.numCols + 2];
		this.gaussianFilterArray = new int[this.numRows + 2][this.numCols + 2];
		this.maskArray = new int[this.maskRows][this.maskCols];
		this.neighborsArray = new int[this.maskRows * this.maskCols];
		this.histogramArray = new int[this.maxValue + 1];
		
		for(int i = 0; i < this.numRows + 2; i++) {
			for(int j = 0; j < this.numCols + 2; j++) {
				
				this.mirrorFramedArray[i][j] = 0;
				this.averageFilterArray[i][j] = 0;
				this.medianFilterArray[i][j] = 0;
				this.gaussianFilterArray[i][j] = 0;
				
				if(i < this.maskRows && j < this.maskCols) {
					this.maskArray[i][j] = 0;
				}
				
			}
		}
		
		for(int i = 0; i < this.maskRows * this.maskCols; i++) {
			this.neighborsArray[i] = 0;
		}
		
		for(int i = 0; i <= this.maxValue; i++) {
			this.histogramArray[i] = 0;
		}
	}
	
	public void mirrorFraming(int[][] mirrorFramedArray) {
		for(int i = 0; i < numRows + 2; i++) {
			mirrorFramedArray[i][0] = mirrorFramedArray[i][1];
			mirrorFramedArray[i][numCols + 1] = mirrorFramedArray[i][numCols];
		}
		
		for(int i = 0; i < numCols + 2; i++) {
			mirrorFramedArray[0][i] = mirrorFramedArray[1][i];
			mirrorFramedArray[numRows + 1][i] = mirrorFramedArray[numRows][i];
		}
		
	}
	
	public void loadImage(Scanner input, int[][] mirrorFramedArray) {

		while (input.hasNext()) {
			for (int i = 1; i < numRows + 1; i++) {
				for (int j = 1; j < numCols + 1; j++) {
					mirrorFramedArray[i][j] = input.nextInt();
				}
			}
		}
	}

	public void loadMask(Scanner input, int[][] maskArray) {

		while (input.hasNext()) {
			for (int i = 0; i < maskRows; i++) {
				for (int j = 0; j < maskCols; j++) {
					maskArray[i][j] = input.nextInt();
				}
			}
		}
	}
	
	public void computeAverageFilterImage(int[][] averageFilterArray) {
		int newMin = 9999;
		int newMax = 0;
		
		for(int i = 1; i < numRows + 1; i++) {
			for(int j = 1; j < numCols + 1; j++) {
				
				averageFilterArray[i][j] = average3x3(i, j, neighborsArray);
				
				if(newMin > averageFilterArray[i][j]) {
					newMin = averageFilterArray[i][j];
				}
				
				if(newMax < averageFilterArray[i][j]) {
					newMax = averageFilterArray[i][j];
				}
			}
		}
	}
	
	public int average3x3(int i, int j, int[] neighbors) {
		
		int average = 0, sum = 0;
		loadNeighbors(i, j, neighbors);
		
		for(int k = 0; k < maskRows * maskCols; k++) {
			sum += neighbors[k];
		}
		
		average = sum / (maskRows * maskCols);
		return average;
	}
	
	public void loadNeighbors(int i, int j, int[] neighbors) {
		int k = 0;
		
		for(int r = i - 1; r <= i + 1; r++) {
			for(int c = j - 1; c <= j + 1; c++) {
				neighbors[k] = mirrorFramedArray[r][c];
				k++;
			}
		}
	}
	
	public void computeMedianFilterImage(int[][] medianFilterArray) {
		int newMin = 9999;
		int newMax = 0;
		
		for(int i = 1; i < numRows + 1; i++) {
			for(int j = 1; j < numCols + 1; j++) {
				loadNeighbors(i, j, neighborsArray);
				sort(neighborsArray);
				medianFilterArray[i][j] = neighborsArray[5];
				
				if(newMin > medianFilterArray[i][j]) {
					newMin = medianFilterArray[i][j];
				}
				
				if(newMax < medianFilterArray[i][j]) {
					newMax = medianFilterArray[i][j];
				}
			}
		}		
	}
	
	public void sort(int[] array) {
		int temp;
		for(int i = 0; i < (maskRows * maskCols) - 1; i++) {
			for(int j = i + 1; j < (maskRows * maskCols); j++) {
				if(array[i] > array[j]) {
					temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
	}
	
	public void computeGaussianFilterImage(int[][] gaussianFilterArray) {
		
		int newMin = 9999;
		int newMax = 0;
		
		for(int i = 1; i < numRows + 1; i++) {
			for(int j = 1; j < numCols + 1; j++) {
				gaussianFilterArray[i][j] = performConvolution(i, j, neighborsArray, maskArray);
				
				if(newMin > gaussianFilterArray[i][j]) {
					newMin = gaussianFilterArray[i][j];
				}
				
				if(newMax < gaussianFilterArray[i][j]) {
					newMax = gaussianFilterArray[i][j];
				}
			}
		}
		
	}
	
	public int performConvolution(int i, int j, int[] neighbors, int[][] mask) {
		int k = 0;
		int sumValues = 0;
		int total = 0;
		
		loadNeighbors(i, j, neighbors);
		
		for(int r = 0; r < maskRows; r++) {
			for(int c = 0; c < maskCols; c++) {
				sumValues += mask[r][c];
				total += (mask[r][c] * neighbors[k]);
				k++;
			}
		}
		return (total / sumValues);
	}
	
	public void outputArrayToFile(int[][] array, FileWriter output) {
		
		computeHistogram(array, histogramArray);
		printHistogram(output, histogramArray);
		
		try {
			for (int i = 1; i < numRows + 1; i++) {
				for (int j = 1; j < numCols + 1; j++) {
					if(array[i][j] >= 32) {
						output.write("1 ");
					}
					else {
						output.write("  ");
					}
				}
				output.write("\n");
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void computeHistogram(int[][] array, int[] histogram) {
		
		for (int i = 1; i < numRows + 1; i++) {
			for (int j = 1; j < numCols + 1; j++) {
				histogram[array[i][j]]++;				
			}
		}
		
	}
	
	public void printHistogram(FileWriter output, int[] histogram) {
		try {
			for (int i = 1; i <= maxValue; i++) {
				if(i < 10) {
					output.write("00");
				}
				else if(i >= 10 && i < 100) {
					output.write("0");
				}
				output.write(i + "  (");
				if(histogram[i] < 10) {
					output.write("00");
				}
				else if(histogram[i] >= 10 && histogram[i] < 100) {
					output.write("0");
				}
				output.write(histogram[i] + "):");
				if (histogram[i] != 0) {
					int max = histogram[i];
					if (max > 32) {
						max = 32;
					}
					for (int j = 0; j < max; j++) {
						output.write("+");
					}
				}
				output.write("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
