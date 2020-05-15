package classification;

import java.util.ArrayList;

public class ConfusionMatrix {
	ArrayList<String> Predictions;
	ArrayList<String>  TrueValues;

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
		 this.Predictions = new ArrayList<String>();
		 this.TrueValues = new ArrayList<String>();
	}
	
	public void addPrediction(String extracted_TrainingDataset) {
		
		System.out.println(" Prediction: " + extracted_TrainingDataset);
	
			this.Predictions.add(extracted_TrainingDataset);
		
	}
	
	public void addTrueValue(String extracted_TestDataset) {
		System.out.println(" True Value :" + extracted_TestDataset);
	
			this.TrueValues.add(extracted_TestDataset);
		
	}
	
	
	
	public int Correct() {
		Positive = 0;
		TruePositive = 0;
		TrueNegative = 0;
		//System.out.println(" Size of True Values array "+ this.TrueValues.size());
		for ( int k = 0; k < this.TrueValues.size(); k++) {
			//System.out.println("Index : " + k + "Test Value : " + this.TrueValues.get(k));
			//System.out.println("Index : " + k + "Predicted Value : " + this.Predictions.get(k));
			if (this.TrueValues.get(k).equals(this.Predictions.get(k))) {
				Positive++;
				if (this.TrueValues.get(k).equals("normal")) {
					TruePositive++;
				}else
				if (this.TrueValues.get(k).equals("anomaly")) {
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
		//System.out.println(" Size of True Values array "+ this.TrueValues.size());
		for ( int k = 0; k < this.TrueValues.size(); k++) {
			//System.out.println("Index : " + k + "Test Value : " + this.TrueValues.get(k));
			//System.out.println("Index : " + k + "Predicted Value : " + this.Predictions.get(k));
			if (!this.TrueValues.get(k).equals(this.Predictions.get(k))) {
				Negative++;
				if (this.TrueValues.get(k).equals("normal")) {
					FalsePositive++;
				}else
				if (this.TrueValues.get(k).equals("anomaly")) {
					FalseNegative++;
				}
			}
		}
		
		return Negative;	
	}
	public double ErrorRate() {
		
		//System.out.println((Double.valueOf(this.FalsePositive) + Double.valueOf(this.FalseNegative))/(Double.valueOf(this.Positive) + Double.valueOf(this.Negative)));
		return (Double.valueOf(this.FalsePositive()) + Double.valueOf(this.FalseNegative()))/(Double.valueOf(this.Positive) + Double.valueOf(this.Negative));
		
	}
	
	public double AccuracyRate() {
	
		//System.out.println((Double.valueOf(this.TruePositive) + Double.valueOf(this.TrueNegative))/(Double.valueOf(this.Positive) + Double.valueOf(this.Negative)));
		return (Double.valueOf(this.TruePositive) + Double.valueOf(this.TrueNegative))/(Double.valueOf(this.Positive) + Double.valueOf(this.Negative));
		
	}
	

}
