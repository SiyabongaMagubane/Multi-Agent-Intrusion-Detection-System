package classification;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;




public class KNearest implements classificationInterface{
	private JFrame frame;
    private JPanel pane;
    private JTextField sizeOfK;
    String ratio = "";
    int ValueofK = 0;
    String[][] Extracted_TrainingDataset;
	String[][] Extracted_TestDataset;
	String[][] Extracted_Dataset;
	HashMap<Integer, Integer> feature_extraction;
	double[] EucledianDistance ;
	HashMap<Integer, String []> TrainingDataset;
	HashMap<Integer, String []> TestDataset;
	HashMap<Integer, String []> Dataset;
	int trainingSize = 0;
	int testSize = 0;
	ConfusionMatrix confusionmatrix ;
    
    public KNearest() {
    	
    	this.TrainingDataset = new HashMap<>(); 
		this.TestDataset = new HashMap<>(); 
		this.Dataset = new HashMap<>(); 
		this.feature_extraction = new HashMap();
		this.feature_extraction.put(DURATION,DURATION);
		this.feature_extraction.put(SOURCE_BYTES, SOURCE_BYTES);
		this.feature_extraction.put(DESTINATION_BYTES, DESTINATION_BYTES);
		this.feature_extraction.put(WRONG_FRAGMENT , WRONG_FRAGMENT);
		this.feature_extraction.put(URGENT,URGENT);
		this.feature_extraction.put(HOT,HOT);
		this.feature_extraction.put(NUMBER_FAILED_LOGINS, NUMBER_FAILED_LOGINS);
		this.feature_extraction.put(NUMBER_COMPROMISED, NUMBER_COMPROMISED);
		this.feature_extraction.put(ROOT_SHELL , ROOT_SHELL);
		this.feature_extraction.put(SU_ATTEMPTED , SU_ATTEMPTED);
		this.feature_extraction.put(NUM_ROOT, NUM_ROOT);
		this.feature_extraction.put(NUMBER_FILES_CREATION, NUMBER_FILES_CREATION);
		this.feature_extraction.put(NUMBER_OF_SHELL_PROMPTS, NUMBER_OF_SHELL_PROMPTS);
		this.feature_extraction.put(NUM_ACCESS_FILES , NUM_ACCESS_FILES);
		this.feature_extraction.put(NUM_OUTBOUNDS_CMDB, NUM_OUTBOUNDS_CMDB);
		this.feature_extraction.put(COUNT ,COUNT );
		this.feature_extraction.put(SRV_COUNT, SRV_COUNT);
		this.feature_extraction.put(SERROR_RATE , SERROR_RATE);
		this.feature_extraction.put(SRV_SERROR_RATE, SRV_SERROR_RATE);
		this.feature_extraction.put(RERROR_RATE , RERROR_RATE);
		this.feature_extraction.put(SRV_ERROR_RATE, SRV_ERROR_RATE);
		this.feature_extraction.put(SAME_SRV_RATE , SAME_SRV_RATE);
		this.feature_extraction.put(DIFF_SRV_RATE,DIFF_SRV_RATE);
		this.feature_extraction.put(SRV_DIFF_HOST_RATE, SRV_DIFF_HOST_RATE);
		this.feature_extraction.put(DST_HOST_COUNT, DST_HOST_COUNT);
		this.feature_extraction.put(DST_HOST_SRV_COUNT , DST_HOST_SRV_COUNT);
		this.feature_extraction.put(DST_HOST_SAME_SRV_RATE,DST_HOST_SAME_SRV_RATE);
		this.feature_extraction.put(DST_HOST_DIFF_SRV_RATE,DST_HOST_DIFF_SRV_RATE);
		this.feature_extraction.put(DST_HOST_SAME_SRV_PORT_RATE, DST_HOST_SAME_SRV_PORT_RATE);
		this.feature_extraction.put(DST_HOST_SRV_DIFF_HOST_RATE, DST_HOST_SRV_DIFF_HOST_RATE);
		this.feature_extraction.put(DST_HOST_SERROR_RATE, DST_HOST_SERROR_RATE);
		this.feature_extraction.put(DST_HOST_SRV_SERROR_RATE,DST_HOST_SRV_SERROR_RATE);
		this.feature_extraction.put(DST_HOST_RERROR_RATE,DST_HOST_RERROR_RATE);
		this.feature_extraction.put(DST_HOST_SRV_RERROR_RATE,DST_HOST_SRV_RERROR_RATE);
		this.feature_extraction.put(CLASS,CLASS);
		confusionmatrix = new ConfusionMatrix();
    }

	@Override
	public void LoadData(String NameOfFile) throws FileNotFoundException {
		   
			this.ReadInput();
			
	        Scanner sc = new Scanner(new BufferedReader(new FileReader(NameOfFile)));
		      System.out.println("Found the file");
		      System.out.println(sc);

		      int count = 0;
		      while(sc.hasNextLine()) {
		            String[] line = sc.nextLine().trim().split(" ");
		            String temp = line[0];
		            String[] line2 = temp.split(",");
		            this.Dataset.put(count, line2);
		           //System.out.println(this.Dataset.get(count));
		            count++;
		          // System.out.println(" ");  
		         }
		      
		      
		      
		      this.SplitData();
	       
	}
	
	private void ReadInput() {
		pane = new JPanel();
        pane.setLayout(new GridLayout(0, 2, 2, 2));

        this.sizeOfK = new JTextField(5);


        pane.add(new JLabel("Enter the size of K"));
        sizeOfK.setBounds(20, 30, 20, 30); 
        pane.add(this.sizeOfK);
        

      
        pane.add(new JLabel("Enter training/testing dataset ratio"));
        JRadioButton ratio1 = new JRadioButton("90/10", true);
        JRadioButton ratio2 = new JRadioButton("85/15");
        JRadioButton ratio3 = new JRadioButton("80/20");
        JRadioButton ratio4 = new JRadioButton("70/30");

        //... Create a button group and add the buttons.
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(ratio1);
        bgroup.add(ratio2);
        bgroup.add(ratio3);
        bgroup.add(ratio4);
        
        //... Arrange buttons vertically in a panel
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(4, 1));
        radioPanel.add(ratio1);
        radioPanel.add(ratio2);
        radioPanel.add(ratio3);
        radioPanel.add(ratio4);
        
        //... Add a titled border to the button panel.
        radioPanel.setBorder(BorderFactory.createTitledBorder(
                   BorderFactory.createEtchedBorder(), "Ratio"));
        
        pane.add(radioPanel);

        int option = JOptionPane.showConfirmDialog(frame, pane, "Please fill all the fields", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {

            try {
            	this.ValueofK  = Integer.parseInt(this.sizeOfK.getText());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            
            if (ratio1.isSelected()) { 
            	        
                ratio = ratio1.getText();
            } 

            else if (ratio2.isSelected()) { 

            	ratio = ratio2.getText();
            } 
            else  if (ratio3.isSelected()) { 

            	ratio = ratio3.getText();
            }  if (ratio4.isSelected()) { 

            	ratio = ratio4.getText();
            }  
           
            pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

            pane.add(new JLabel("Value of chosen k: " + ValueofK));
            pane.add(new JLabel("selected ratio : " + ratio));
            
            JOptionPane.showMessageDialog(frame, pane);
        }
		
	}

	@Override
	public String[][] FeatureExtraction(HashMap<Integer, String[]> dataset) {
		this.Extracted_Dataset = new String [this.getSize(dataset)][this.feature_extraction.size()] ;
		
		
		for (int count = 0 ; count < getSize(dataset) ; count++) {
			int newCount = 0;
					for ( int i = 0; i < dataset.get(count).length; i++) {
						//System.out.println(i);
						switch(i) {
						
						case DURATION :
							//System.out.print("assignment : DURATION ");				
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
							
							
						case SOURCE_BYTES:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case DESTINATION_BYTES:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case DST_HOST_DIFF_SRV_RATE:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_SRV_DIFF_HOST_RATE:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_SERROR_RATE:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_SRV_SERROR_RATE:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_RERROR_RATE:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_SRV_RERROR_RATE:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_SAME_SRV_PORT_RATE:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_SAME_SRV_RATE:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case WRONG_FRAGMENT:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case URGENT:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case HOT:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case NUMBER_FAILED_LOGINS:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case ROOT_SHELL:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case SU_ATTEMPTED:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case NUM_ROOT:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case NUMBER_COMPROMISED:
							//System.out.print("assignment : SOURCE_BYTES ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case NUMBER_FILES_CREATION:
							//System.out.print("assignment : NUMBER_FILES_CREATION ");
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case NUM_ACCESS_FILES:
							//System.out.print("assignment : NUMBER_OF_SHELL_PROMPTS ");
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case NUMBER_OF_SHELL_PROMPTS:
							//System.out.print("assignment : NUMBER_OF_SHELL_PROMPTS ");
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
							
						case SERROR_RATE:
							//System.out.print("assignment : SERROR_RATE ");
							
							this.Extracted_Dataset[count][newCount] = dataset.get(count)[i];
							//this.Extracted_Dataset[count][newCount] = (int) (Double.parseDouble(this.TrainingDataset.get(count)[i]));

							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
							
						case SAME_SRV_RATE:
							//System.out.print("assignment : SRV_SERROR_RATE ");
							
							this.Extracted_Dataset[count][newCount] =(dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DIFF_SRV_RATE:
							//System.out.print("assignment : SRV_SERROR_RATE ");
							
							this.Extracted_Dataset[count][newCount] =(dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						
						case SRV_DIFF_HOST_RATE:
							//System.out.print("assignment : SRV_SERROR_RATE ");
							
							this.Extracted_Dataset[count][newCount] =(dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_COUNT:
							//System.out.print("assignment : SRV_SERROR_RATE ");
							
							this.Extracted_Dataset[count][newCount] =(dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case DST_HOST_SRV_COUNT:
							//System.out.print("assignment : SRV_SERROR_RATE ");
							
							this.Extracted_Dataset[count][newCount] =(dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
						case RERROR_RATE:
							//System.out.print("assignment : RERROR_RATE ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
							
						case SRV_ERROR_RATE:
							//System.out.print("assignment : SRV_ERROR_RATE ");
							
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							break;
							
						case NUM_OUTBOUNDS_CMDB:
							//System.out.print("assignment : NUMBER_OF_FAILED_LOGIN_ATTEMPTS : ");
						
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							
							break;
						
						case COUNT:
							//System.out.print("assignment : NUMBER_OF_FAILED_LOGIN_ATTEMPTS : ");
						
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							
							break;
						
						case SRV_COUNT:
							//System.out.print("assignment : NUMBER_OF_FAILED_LOGIN_ATTEMPTS : ");
						
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							
							break;
						
						case SRV_SERROR_RATE:
							//System.out.print("assignment : NUMBER_OF_FAILED_LOGIN_ATTEMPTS : ");
						
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							
							break;
						case CLASS:
							//System.out.print("assignment : CLASS : ");
						
							this.Extracted_Dataset[count][newCount] = (dataset.get(count)[i]);
							//System.out.print(this.Extracted_Dataset[count][newCount]);
							//System.out.println(" ");
							newCount++;
							
							break;	
							
						 default :
							 
							
						}
						
					}
			
			}

return this.Extracted_Dataset;
	}

	@Override
	public void FeatureExtractionTraining() {
		this.Extracted_TrainingDataset = new String [getSize(this.TrainingDataset)][this.feature_extraction.size()];
		
		String [][] arr = FeatureExtraction(this.TrainingDataset);
		//System.out.println(" Performing Feature Extraction on training dataset");
		//System.out.print(" total number of rows :");
		//System.out.print(getSize(this.TrainingDataset));
		for (int k = 0; k < getSize(this.TrainingDataset) ; k++) {
			//System.out.print("row index :  ");
			//System.out.print(k);
			for ( int i = 0; i < this.feature_extraction.size(); i++) {
				this.Extracted_TrainingDataset[k][i] = arr[k][i];
				//System.out.print("  :  ");
				//System.out.print(this.Extracted_TrainingDataset[k][i]);
				
			}
			//System.out.println(" ");
		}
	}

	@Override
	public void FeatureExtractionTest() {
		this.Extracted_TestDataset = new String [getSize(this.TestDataset)][this.feature_extraction.size()];
		String [][] arr= FeatureExtraction(this.TestDataset);
		System.out.println(" Performing Feature Extraction on test dataset");
		System.out.print(" total number of rows :");
		System.out.print(getSize(this.TestDataset));
		System.out.println(" ");
		for (int k = 0; k < (getSize(this.TestDataset)-1) ; k++) {
			//System.out.print("row index :  ");
			//System.out.print(k);
			for ( int i = 0; i < this.feature_extraction.size(); i++) {
				this.Extracted_TestDataset[k][i] = arr[k][i];
				//System.out.print("  :  ");
				//System.out.print(this.Extracted_TestDataset[k][i]);
			}
			//System.out.println(" ");
		}
		
	}

	/*
	 * Load the training and test data
	 * choose the value of k
	 * for each point in test data:
	 * 		- find the Eucledian distance to all training data points
	 * 		- store the Eucledian distances in a list and sort it choose the first k points
	 * 		- assign a class to the test point based on the majority of the classes present in the chosen points
	 * 		- end 
	 *
	 * 
	 */
	public void KNN() {

		
		// for each point in test data
		double x = 0;
		double y = 0;
		double xy = 0;
		double sum = 0;
		double m = this.Extracted_TestDataset.length;
		ArrayList<ArrayList<Double>> EucledianDistanceUnsorted = new  ArrayList<>(this.Extracted_TestDataset.length);
		ArrayList<ArrayList<Double>> EucledianDistanceSorted = new  ArrayList<>(this.Extracted_TestDataset.length);
		//EucledianDistance
		
		// Initialize EucledianDistance List
		for(int i=0; i < this.Extracted_TestDataset.length; i++) {
			EucledianDistanceUnsorted.add(new ArrayList());
		}
		
		for(int i=0; i < this.Extracted_TestDataset.length; i++) {
			EucledianDistanceSorted.add(new ArrayList());
		}
		
		// Testing sorting 
		/*
		for(int i=0; i < this.Extracted_TestDataset.length; i++) {
			for(int k=0; k < this.Extracted_TestDataset[i].length; k++) {
				double min = 0.0; //  Set To Your Desired Min Value
		        double max = 10.0; //    Set To Your Desired Max Value
		        double ran = (Math.random() * ((max - min) + 1)) + min; //    This Will Create 
		  
		        double xrounded = Math.round(ran * 100.0) / 100.0; 
		        System.out.print(xrounded); 
		        //System.out.print(" , "); 
		        EucledianDistanceUnsorted.get(i).add(xrounded);
				
			}
			//System.out.println(""); 
			
		}
		*/

		
		
		//start at index 1 discard the attribute name row
		for ( int p = 1; p < (this.Extracted_TestDataset.length-1) ; p++) {
		//start at index 1 discard the attribute name row
		for ( int i = 1; i < this.Extracted_TrainingDataset.length; i++) {
			sum = 0;
			int k = 0;
			//System.out.println("first loop");
			for (  ; k < (this.Extracted_TestDataset[0].length-1) ; k++) {			
				//System.out.println("second loop");
				
				x = (Double.parseDouble(this.Extracted_TestDataset[p][k]));
					//System.out.print(" x value : ");
					//System.out.print(x);
				
				
				y = (Double.parseDouble(this.Extracted_TrainingDataset[i][k]));
			//	System.out.print(" y value : ");
			//	System.out.println(y);
				
					
				xy = x - y;
			
				xy = (Math.pow(xy, 2));
				sum += xy;
						
				//System.out.print(" sum : ");
				//System.out.println(sum);
				}

			EucledianDistanceUnsorted.get(p).add((Math.sqrt(sum/k)));
				//System.out.printf(" distance value of row %i in col %i : %s %n", i, k ,EucledianDistanceUnsorted.get(i).get(k));		
			}
		
			//start at index 1 discard the attribute name row
			/*for ( int i = 0; i < (this.Extracted_TrainingDataset.length-1) ; i++) {		
				System.out.print(" distance value of row ");
				System.out.print(p);
				System.out.print(" in col ");
				System.out.print(i);
				System.out.print(" = ");
				System.out.print(EucledianDistanceUnsorted.get(p).get(i));
				System.out.println("");	
			}*/
			
		 System.out.print("Count : ");
		 System.out.println(p);
		}
	
		System.out.println("About to sort distance");
		
		for(  int i = 1; i < (EucledianDistanceUnsorted.size()-1); i++){
			ArrayList<Double> temp = EucledianDistanceUnsorted.get(i);
			Collections.sort(temp);
			for(int k=0; k < (this.Extracted_TrainingDataset.length- 1); k++) {
				EucledianDistanceSorted.get(i).add(temp.get(k));
			}
		}
		System.out.println(" Sorted eucledian distance of first k values ");
		for(int i=1; i < EucledianDistanceSorted.size()-1; i++) {
			for(int k=0; k < (this.ValueofK); k++) {
				System.out.print(EucledianDistanceSorted.get(i).get(k));
				System.out.print(" , ");
				
			}
			System.out.println("");
			
		}
		
		System.out.println(" finding class of first k points ");
		// find majority distance and find index of distance in unsorted distance   
		double majority = 0.0;
		int IndexofDistance = 0;
		for(int i=1; i < (EucledianDistanceSorted.size()-1); i++) {
			
				majority = findMajority(EucledianDistanceSorted.get(i), this.ValueofK);
				System.out.print(" index : ");
				System.out.print(i);
				System.out.print(" majority : ");
				System.out.println(majority);
				IndexofDistance = findIndexofMajority(majority,EucledianDistanceUnsorted.get(i));

				//System.out.print(" test row  original class :");
				//System.out.println(this.Extracted_TestDataset[i][this.feature_extraction.size()-1]);
				confusionmatrix.addTrueValue(Extracted_TestDataset[i][this.feature_extraction.size()-1]);
				//System.out.print(" predectived training row  classifier : ");
				//System.out.println(this.Extracted_TrainingDataset[IndexofDistance][this.feature_extraction.size()-1]);	
				confusionmatrix.addPrediction(Extracted_TrainingDataset[IndexofDistance][this.feature_extraction.size()-1]);
				
				
				
			}
		
			System.out.println("");
			System.out.println("Total number of predictions" + confusionmatrix.TotalNumberOfPredictions());
			System.out.println("Number of correct predictions: "+confusionmatrix.Correct());
			System.out.println("Number of incorrect predictions: "+confusionmatrix.Incorrect());
			System.out.println("Error Rate: "+confusionmatrix.ErrorRate());
			System.out.println("Accuracy Rate: "+confusionmatrix.AccuracyRate());
		
	}
	static int findIndexofMajority( double majority, ArrayList<Double> arrayList) {
		int i = 0 ;
		while ( i < arrayList.size()) {
			if (arrayList.get(i).equals(majority)) {
				if (i == 0) {
					i++;
					continue;
				}
				break;
			}
			i++;
		}
		
		return i;
	}
	
	
	
	static double findMajority(ArrayList<Double> arrayList, int n)  
	{  
		

	    int maxCount = 0;  
	    int index = -1; // sentinels  
	    for(int i = 0; i < n; i++)  
	    {  
	        int count = 0;  
	        for(int j = 0; j < n; j++)  
	        {  
	            if(arrayList.get(i).equals(arrayList.get(j))) {       
	            count++; 
	            }
	         
	        } 

 
	        // update maxCount if count of  
	        // current element is greater  
	        if(count > maxCount)  
	        {  
	            maxCount = count;  
	            index = i;  
	          //  System.out.print(" found: ");
		      //  System.out.print(count);
	        }  
	    }

	    return (arrayList.get(index));  
	}
	public int getSize(HashMap<Integer, String []> dataset) {
		int count = 0;
		while (dataset.get(count)!= null) {
			count++;
		}
		
		return count;
	}
	

	@Override
	public void SplitData() {
		int totalSize = getSize(this.Dataset);
		if (this.ratio == "90/10") {
			this.trainingSize = (int) (totalSize * .9);
			this.testSize = (int) (totalSize * .1);
		}
		else if (this.ratio == "85/15") {
			this.trainingSize = (int) (totalSize * .85);
			this.testSize = (int) (totalSize * .15);
		}
		else if (this.ratio == "80/20") {
			this.trainingSize = (int) (totalSize * .8);
			this.testSize = (int) (totalSize * .2);
		}
		else if (this.ratio == "70/30") {
			this.trainingSize = (int) (totalSize * .7);
			this.testSize = (int) (totalSize * .3);
		}
		
		int count = 0;
		int TestCount = 0;
		while (count < totalSize) {
			if ( count < this.trainingSize) {
				this.TrainingDataset.put(count, this.Dataset.get(count));
			}
			else if (TestCount < this.trainingSize) {
					this.TestDataset.put(TestCount, this.Dataset.get(count));
					TestCount++;
				}
			count++;
		}
	}

	

}
