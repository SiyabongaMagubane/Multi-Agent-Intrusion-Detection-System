package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;

import fr.ign.cogit.roc4j.graphics.ColorMap;
import fr.ign.cogit.roc4j.core.ConfidenceBands;
import fr.ign.cogit.roc4j.core.ReceiverOperatingCharacteristics;
import fr.ign.cogit.roc4j.graphics.RocSpace;
import fr.ign.cogit.roc4j.graphics.RocSpaceStyle;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;
import fr.ign.cogit.roc4j.core.RocCurvesCollection;

public class EnsembleAgent extends Agent {
	@Override
	protected void setup() {
		System.out.println(" Hello Ensemble Agent is ready " + getAID().getName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("ensemble");
		sd.setName("Ensemble");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour( new EnsembleMethod());

	}
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Printout a dismissal message
		System.out.println("Ensemble-agent "+getAID().getName()+" terminating.");
	}
	private class EnsembleMethod extends Behaviour {
		private String response;
		private String [] responseClassification ;
		private ArrayList<String> EnsembleResult;
		private String [] Temporary;
		public ArrayList<ArrayList<String>> TrueValues;
		public ArrayList<ArrayList<String>> PredictionValues;
		private int step = 0;
		ConfusionMatrix confusionmatrix;
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			TrueValues = new ArrayList<ArrayList<String>>();
			PredictionValues = new ArrayList<ArrayList<String>>();
			EnsembleResult = new ArrayList<String>();
			confusionmatrix = new ConfusionMatrix();
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				//ACCEPT_PROPOSAL Message received. Process it
				String predictions = msg.getContent();
				//System.out.println("Response: " + " Size :" + predictions.length()+ predictions );
				ACLMessage reply = msg.createReply();
				System.out.println(" received the ensemble input now about to write the ensemble method");
				response = predictions;
				responseClassification = response.split("-");
				System.out.println(" Number of Agent output received " +responseClassification.length );
				String perAgent = "";
			switch (step) {
			case 0:
			
			
				for ( int i = 0 ; i < responseClassification.length; i++){
						//System.out.println(" Content index: "+ (i+1)+" : " +responseClassification[i]);
						TrueValues.add( new ArrayList<String>());
						PredictionValues.add( new ArrayList<String>());
						
						String [] temp = responseClassification[i].trim().split("]");
						System.out.println("Response from Agent number: "+(i+1));
						for (int p = 0; p < temp.length ; p++){
							//responsePerAgent.add(new ArrayList<String>());
							
							
							Temporary = temp[p].trim().split(",") ;
							for ( int x = 0; x < Temporary.length ; x++){
								perAgent = Temporary[x];
								if ((x %  2) == 0){
								
								TrueValues.get(i).add(perAgent);
								//System.out.println("Even");
								
								}
								else {
								
									PredictionValues.get(i).add(perAgent);
									//System.out.println("Odd");
								}
								
							}
							
						}
					}
			case 1:
				// Develop Ensemble results based on Majority Vote
				
				System.out.println(" Size of prediction array " + PredictionValues.size());
				
					
					int j = 0;
					
					for (j = 0; j < PredictionValues.get(0).size(); j++) {
						int maxCount = 0;
						int indexX = -1; // sentinels
						int indexY = -1;
						int x = 0;
						for (x = 0; x < PredictionValues.size(); x++) {	
							//System.out.println("Index 1:" + PredictionValues.get(i).get(j));
							int count = 0;
							for ( int i = 0; i < PredictionValues.size();i++)
								if(PredictionValues.get(i).get(j).equals(PredictionValues.get(x).get(j))){
									count++;
									//System.out.println("Index"+ (x+1) + ":" + PredictionValues.get(x).get(j));
								}
								// update maxCount if count of
								// current element is greater
								if (count > maxCount) {
									maxCount = count;	
									indexX = x;
									indexY = j;
									//System.out.println("indexX "+ indexX);
									
								}
						//System.out.println("Proposed value " + PredictionValues.get(x).get(j));
						}
					//	System.out.println("---------");
						//System.out.println("Assigned value " + PredictionValues.get(indexX).get(indexY));
					//	System.out.println("---------");
					//	System.out.println("---------");
						EnsembleResult.add(PredictionValues.get(indexX).get(indexY));	
					}
			case 2:
				// Insert ensemble in confusion matrix
				System.out.println(" Size of Ensemble Values"+ EnsembleResult.size());
			
				for ( int k = 0; k < EnsembleResult.size(); k++){
					//System.out.println(EnsembleResult.get(k));
					confusionmatrix.addTrueValue(TrueValues.get(0).get(k));
					confusionmatrix.addPrediction(EnsembleResult.get(k));
				}
				double noise = 0.1;
				int[] expected = new int[confusionmatrix.getTrueValuesNumber().size()];
				double[] score= new double[confusionmatrix.getPredictionValuesNumber().size()]; 
				for (int i=0; i<confusionmatrix.getTrueValuesNumber().size(); i++){
					
				    double rand11 = confusionmatrix.getTrueValuesNumber().get(i);
				    double rand22 =  confusionmatrix.getPredictionValuesNumber().get(i);
				    System.out.println("True Value distance"+rand11);
				    System.out.println("Prediction distance"+rand22);
				    expected[i] = (int)(rand11+0.5);
				    expected[i] = (int)Math.max(expected[i], 0);
				    expected[i] = (int)Math.min(expected[i], 1);
				    score[i] = noise*rand22 + 0.2*expected[i] + 0.4;
				    score[i] = Math.max(score[i], 0);
				    score[i] = Math.min(score[i], 1);
				    System.out.println("expected: "+expected[i]);
				    System.out.println("score: "+score[i]);
				}
				for ( int k =0; k < confusionmatrix.getTrueValues().length;k++){
					
					confusionmatrix.getTrueValues()[k] = (int)(confusionmatrix.getTrueValues()[k]+0.5);
					confusionmatrix.getPredictions()[k] = noise*confusionmatrix.getPredictions()[k] +0.2*confusionmatrix.getTrueValues()[k] +0.4;
					confusionmatrix.getPredictions()[k] = Math.max(confusionmatrix.getPredictions()[k], 0);
					confusionmatrix.getPredictions()[k] = Math.max(confusionmatrix.getPredictions()[k], 1);
					
					//System.out.println("TrueValue : " +confusionmatrix.getTrueValues()[k]);
					//System.out.println("Prediction : " + confusionmatrix.getPredictions()[k]);
				}
				RocCurvesCollection ROCS = new RocCurvesCollection(true);
				ReceiverOperatingCharacteristics roc = new ReceiverOperatingCharacteristics(confusionmatrix.getTrueValues(),
						confusionmatrix.getPredictions());
				ROCS.add(roc);

				//ConfidenceBands bands = new ConfidenceBands(roc, ConfidenceBands.METHOD_KOLMOGOROV_SMIRNOV);
				roc.setColor(ColorMap.TYPE_STANDARD);
				roc.setThickness(1.f);
				// roc.plot();
				roc.smooth(ReceiverOperatingCharacteristics.SMOOTH_KERNEL);
				// Processing generated curves
				// ROCS.smooth(ReceiverOperatingCharacteristics.SMOOTH_KERNEL);

				// Confidence bands computation at level % with FWB method
				// ConfidenceBands bands = new ConfidenceBands(ROCS,
				// ConfidenceBands.METHOD_FIXED_WIDTH_BAND, level, 1);
				//bands.getCentralROC().setThickness(2.f);
				//bands.setBordersTransparency(0.5f);
				// Creating ROC space
				RocSpace space = new RocSpace();
				space.setStyle(RocSpaceStyle.STYLE_OSCILLO);

				space.addRocCurve(roc);
				// space.setStyle(RocSpaceStyle.STYLE_DOS_BLUE)
				//space.addConfidenceBands(bands);
				space.setTitle("ROC curve diagram of Ensemble Diagram");
				space.setXLabel("FPR axis");
				space.setYLabel("TPR axis");

				// Graphical display
				JFrame fen = new JFrame();
				fen.setSize(700, 700);
				fen.setContentPane(space);
				fen.setLocationRelativeTo(null);
				fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				fen.setVisible(true);

				System.out.println("");
				System.out.println("Total number of predictions: " + confusionmatrix.TotalNumberOfPredictions());
				System.out.println("Number of correct predictions: " + confusionmatrix.Correct());
				System.out.println("Number of incorrect predictions: " + confusionmatrix.Incorrect());

				System.out.println("True positive (TP): correct positive prediction: " + confusionmatrix.TruePositive());
				System.out.println("False positive (FP): incorrect positive prediction: " + confusionmatrix.FalsePositive());
				System.out.println("True negative (TN): correct negative prediction: " + confusionmatrix.TrueNegative());
				System.out.println("False negative (FN): incorrect negative prediction: " + confusionmatrix.FalseNegative());
				System.out.println("Error Rate: " + confusionmatrix.ErrorRate());
				System.out.println("Accuracy Rate: " + confusionmatrix.AccuracyRate());
				System.out.println("True Positive Rate: " + confusionmatrix.TruePositiveRate());
				System.out.println("False Positive Rate: " + confusionmatrix.FalsePostiveRate());
				reply.setPerformative(ACLMessage.INFORM);
				String ClusteredOutput = String.valueOf(confusionmatrix.TotalNumberOfPredictions()) +"," + String.valueOf(confusionmatrix.Correct())
										+"," +String.valueOf(confusionmatrix.Incorrect()) + "," + String.valueOf(confusionmatrix.TruePositive()) +  "," +String.valueOf(confusionmatrix.FalsePositive())
										+"," + String.valueOf(confusionmatrix.TrueNegative()) +"," + String.valueOf(confusionmatrix.FalseNegative()) +"," +String.valueOf(confusionmatrix.ErrorRate())
										+","+  String.valueOf(confusionmatrix.AccuracyRate()) +","+ String.valueOf(confusionmatrix.TruePositiveRate()) +","+String.valueOf(confusionmatrix.FalsePostiveRate());
				reply.setContent(ClusteredOutput);
				myAgent.send(reply);	
			}
		}	
		else{
				block();
			}
			
		}
		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}

