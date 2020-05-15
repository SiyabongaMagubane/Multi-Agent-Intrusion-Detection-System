package classification;

import java.util.ArrayList;

public class ConfusionMatrix {
	ArrayList<String> Predictions;
	ArrayList<String> TrueValues;
	ArrayList<Double> PredictionsNumber;
	ArrayList<Double> TrueValuesNumber;
	double[] BinaryPredictions;
	int[] BinaryTrueValues;
	int size;

	int TruePositive;
	int TrueNegative;
	int FalsePositive;
	int FalseNegative;
	int Positive;
	int Negative;

	int ClassIndex;

	public ConfusionMatrix() {
		TruePositive = 0;
		TrueNegative = 0;
		FalsePositive = 0;
		FalseNegative = 0;
		int Positive = 0;
		int Negative = 0;
		int size = 0;
		this.Predictions = new ArrayList<String>();
		this.TrueValues = new ArrayList<String>();
		this.PredictionsNumber = new ArrayList<Double>();
		this.TrueValuesNumber = new ArrayList<Double>();

	}

	public void addPrediction(String extracted_TrainingDataset) {

		//System.out.println(" Prediction: " + extracted_TrainingDataset);

		this.Predictions.add(extracted_TrainingDataset);
		size++;

	}
	public void addPredictionNumber(double extracted_TrainingDataset) {

		//System.out.println(" Prediction: " + extracted_TrainingDataset);

		this.PredictionsNumber.add(extracted_TrainingDataset);
		size++;

	}

	public void addTrueValue(String extracted_TestDataset) {
		//System.out.println(" True Value :" + extracted_TestDataset);

		this.TrueValues.add(extracted_TestDataset);

	}
	public void addTrueValueNumber(double arrayList) {
		//System.out.println(" True Value :" + extracted_TestDataset);

		this.TrueValuesNumber.add(arrayList);

	}
	
	public ArrayList<String> TrueValues(){
		return this.TrueValues;
		
	}
	public ArrayList<String> PredictionValues(){
		return this.Predictions;
		
	}
	public ArrayList<Double> getTrueValuesNumber(){
		return this.TrueValuesNumber;
		
	}
	public ArrayList<Double> getPredictionValuesNumber(){
		return this.PredictionsNumber;
		
	}

	public int[] getTrueValues() {
		//System.out.println("In TrueVales  value conversion to binary");
		this.BinaryTrueValues = new int[this.TrueValues.size()];
		//System.out.println("In TrueVales  value conversion to binary");
		for (int i = 0; i < this.TrueValues.size(); i++) {
			if (this.TrueValues.get(i).equals("normal")) {
				this.BinaryTrueValues[i] = 1;
			} else if (this.TrueValues.get(i).equals("anomaly")) {
				this.BinaryTrueValues[i] = 0;
			}

			/*
			 * else if(this.TrueValues.get(i).equals("anomaly")){
			 * this.BinaryTrueValues[i]=0; }
			 */
			//System.out.println("Count:" + i + " TrueValuesArray value: " + this.BinaryTrueValues[i]);
		}
		return this.BinaryTrueValues;

	}

	public double[] getPredictions() {
		//System.out.println("In prediction value conversion to binary");
		this.BinaryPredictions = new double[this.Predictions.size()];
		for (int i = 0; i < this.TrueValues.size(); i++) {
			if (this.Predictions.get(i).equals("normal")) {
				this.BinaryPredictions[i] = 1.0;
			} else if (this.Predictions.get(i).equals("anomaly")) {
				this.BinaryPredictions[i] = 0.0;
			}
			/*
			 * else if (this.Predictions.get(i).equals("anomaly")){
			 * this.BinaryPredictions[i] = 0.0 ; }
			 */
			//System.out.println("Count:" + i + " Prediction value: " + this.BinaryPredictions[i]);
		}

		return this.BinaryPredictions;

	}

	public int Correct() {
		Positive = 0;
		TruePositive = 0;
		TrueNegative = 0;
		// System.out.println(" Size of True Values array "+
		// this.TrueValues.size());
		for (int k = 0; k < this.TrueValues.size(); k++) {
			// System.out.println("Index : " + k + "Test Value : " +
			// this.TrueValues.get(k));
			// System.out.println("Index : " + k + "Predicted Value : " +
			// this.Predictions.get(k));
			if (this.TrueValues.get(k).equals(this.Predictions.get(k))) {
				Positive++;
				if (this.TrueValues.get(k).equals("normal")) {
					TruePositive++;
				} else if (this.TrueValues.get(k).equals("anomaly")) {
					TrueNegative++;
				}

			}
		}
		return Positive;

	}

	public int TotalNumberOfPredictions() {
		return this.TrueValues.size();
	}

	public int TruePositive() {

		return this.TruePositive;
	}

	public int FalsePositive() {

		return this.FalsePositive;
	}

	public int TrueNegative() {

		return this.TrueNegative;
	}

	public int FalseNegative() {

		return this.FalseNegative;
	}

	public int Incorrect() {
		Negative = 0;
		FalsePositive = 0;
		FalseNegative = 0;
		// System.out.println(" Size of True Values array "+
		// this.TrueValues.size());
		for (int k = 0; k < this.TrueValues.size(); k++) {
			// System.out.println("Index : " + k + "Test Value : " +
			// this.TrueValues.get(k));
			// System.out.println("Index : " + k + "Predicted Value : " +
			// this.Predictions.get(k));
			if (!this.TrueValues.get(k).equals(this.Predictions.get(k))) {
				Negative++;
				if (this.TrueValues.get(k).equals("normal")) {
					FalsePositive++;
				} else if (this.TrueValues.get(k).equals("anomaly")) {
					FalseNegative++;
				}
			}
		}

		return Negative;
	}

	public double ErrorRate() {

		// System.out.println((Double.valueOf(this.FalsePositive) +
		// Double.valueOf(this.FalseNegative))/(Double.valueOf(this.Positive) +
		// Double.valueOf(this.Negative)));
		return (Double.valueOf(this.FalsePositive()) + Double.valueOf(this.FalseNegative()))
				/ (Double.valueOf(this.Positive) + Double.valueOf(this.Negative));

	}

	public double AccuracyRate() {

		// System.out.println((Double.valueOf(this.TruePositive) +
		// Double.valueOf(this.TrueNegative))/(Double.valueOf(this.Positive) +
		// Double.valueOf(this.Negative)));
		return (Double.valueOf(this.TruePositive) + Double.valueOf(this.TrueNegative))
				/ (Double.valueOf(this.Positive) + Double.valueOf(this.Negative));

	}

public double TruePositiveRate() {
		
		//System.out.println((Double.valueOf(this.TruePositive) + Double.valueOf(this.TrueNegative))/(Double.valueOf(this.Positive) + Double.valueOf(this.Negative)));
		return Double.valueOf(this.TruePositive) /( Double.valueOf(this.TruePositive) + Double.valueOf(this.FalseNegative));
		
	}


	public double FalsePostiveRate() {

		// System.out.println((Double.valueOf(this.TruePositive) +
		// Double.valueOf(this.TrueNegative))/(Double.valueOf(this.Positive) +
		// Double.valueOf(this.Negative)));
		return Double.valueOf(this.FalsePositive)
				/ (Double.valueOf(this.TrueNegative) + Double.valueOf(this.FalsePositive));

	}

}
