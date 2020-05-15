package classification;

import java.util.ArrayList;

public class ConfusionMatrix {
	ArrayList<String> Predictions;
	ArrayList<String>  TrueValues;

	int TruePositive;
	int TrueNegative;
	int FalsePositive;
	int FalseNegative;
	
	int ClassIndex;
	
	public ConfusionMatrix() {
		 TruePositive = 0;
		 TrueNegative = 0;
		 FalsePositive = 0;
		 FalseNegative = 0;
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
	
	public double ErrorRate() {
		return this.Incorrect()/this.TotalNumberOfPredictions();
		
	}
	
	public double AccuracyRate() {
		return this.Correct()/this.TotalNumberOfPredictions();
		
	}
	
	public int Correct() {
		int count = 0;
		//System.out.println(" Size of True Values array "+ this.TrueValues.size());
		for ( int k = 0; k < this.TrueValues.size(); k++) {
			//System.out.println("Index : " + k + "Test Value : " + this.TrueValues.get(k));
			//System.out.println("Index : " + k + "Predicted Value : " + this.Predictions.get(k));
			if (this.TrueValues.get(k).equals(this.Predictions.get(k))) {
				count++;
				if (this.Predictions.get(k).equals("normal")) {
					TruePositive++;
				}
				if (this.Predictions.get(k).equals("anomaly")) {
					TrueNegative++;
				}
				
				
			} 
		}
		return count;
		
	}
	
	public int TotalNumberOfPredictions() {
		return this.TrueValues.size();
	}
	public int Incorrect() {
		int count = 0;
		//System.out.println(" Size of True Values array "+ this.TrueValues.size());
		for ( int k = 0; k < this.TrueValues.size(); k++) {
			//System.out.println("Index : " + k + "Test Value : " + this.TrueValues.get(k));
			//System.out.println("Index : " + k + "Predicted Value : " + this.Predictions.get(k));
			if (!this.TrueValues.get(k).equals(this.Predictions.get(k))) {
				count++;
				if (this.Predictions.get(k).equals("normal")) {
					TruePositive++;
				}
				if (this.Predictions.get(k).equals("anomaly")) {
					TrueNegative++;
				}
			}
		}
		return count;	
	}
	

}
