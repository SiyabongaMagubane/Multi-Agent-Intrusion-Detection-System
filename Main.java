package classification;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		KNearest classification = new KNearest();
		
		try {
			
			
			classification.LoadData("/Users/siyabonga/eclipse-workspace/Classification/src/com/classification/csv_result-KDDTrain+_20Percent.csv");
			System.out.println("Loading data");
			classification.FeatureExtractionTraining();
			classification.FeatureExtractionTest();
			classification.KNN();
			
		}
		catch ( Exception e) {
			
			System.out.println(e);
		}	
	}
}
