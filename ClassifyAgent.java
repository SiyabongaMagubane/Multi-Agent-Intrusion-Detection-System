package classification;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;;

public class ClassifyAgent extends Agent {

	public ClassifyAgent() {

	}

	@Override
	protected void setup() {
		System.out.println(" Hello classify Agent is ready " + getAID().getName());
		/*
		 * ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		 * msg.addReceiver(new AID("EnsembleAgent", AID.ISLOCALNAME));
		 * msg.setLanguage("English");
		 * msg.setOntology("Todays-Game-forecast-ontology");
		 * msg.setContent("SpringBoks win"); send(msg);
		 */
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("classification");
		sd.setName("ClassifyAgent1");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new ExecuteClassification());
	}
	protected void takeDown() {
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ClassifyAgent 1 " + getAID().getName() + " terminating");
	}

	private class ExecuteClassification extends CyclicBehaviour {
		private int step = 0;
		private String ClusteredOutput;
		@Override
		public void action() {
			step = 0;
			switch (step) {
			case 0:
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			KNearest classification = new KNearest();
				if (msg != null) {
					System.out.println("Received classification request: " + msg.getContent());
					ACLMessage reply = msg.createReply();
					
					try {
						classification.LoadData(
								"/Users/siyabonga/Documents/workspace/Classification/src/classification/KDDTrain.csv");
						System.out.println("Loading data");
						classification.FeatureExtractionTraining();
						classification.FeatureExtractionTest();
						classification.KNN();
						classification.VisualizeClassification();
						ClusteredOutput = String.valueOf(classification.TotalNumberOfPredictions()) +"," + String.valueOf(classification.Correct())
						+"," +String.valueOf(classification.Incorrect()) + "," + String.valueOf(classification.TruePositive()) +  "," +String.valueOf(classification.FalsePositive())
						+"," + String.valueOf(classification.TrueNegative()) +"," + String.valueOf(classification.FalseNegative()) +"," +String.valueOf(classification.ErrorRate())
						+","+  String.valueOf(classification.AccuracyRate()) +","+ String.valueOf(classification.TruePositiveRate()) +","+String.valueOf(classification.FalsePostiveRate())
						+ "," + myAgent.getName();
						
						
						
						// Build string to send through to back to communicationAgent which consists of True Values Array and Predictions array
						String content = "";
						for ( int k = 0 ; k < classification.trueValues().size() ; k++){
							content = content + classification.trueValues().get(k)+ ",";
						}
						content = content + "]";
						for ( int k = 0 ; k < classification.predictionValues().size(); k++){
							content = content+ classification.predictionValues().get(k) + ",";
						}
					
						//System.out.println("Content concantenated: " +content);
						
						// rebuild String in ensemble logic
	
						reply.setPerformative(ACLMessage.PROPOSE);
						reply.setContent(content+"&"+ClusteredOutput);
						myAgent.send(reply);
					} catch (Exception e) {
						reply.setPerformative(ACLMessage.REFUSE);
						reply.setContent("not-available");
						block();
						System.out.println(e);
					}
	
				}
	
				else {
					block();
				}
				step = 1;
				break;
			case 1:
			
			MessageTemplate mt2 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage order = myAgent.receive(mt2);
			if (order != null) {
				if ( order.getPerformative() == ACLMessage.PROPOSE){
				System.out.println("Received classification request: " + order.getContent());
				ACLMessage reply = order.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setContent(ClusteredOutput);
				myAgent.send(reply);
				}
				
			}
			else {
				block();
			}
				step = 3;
				break;
			case 3:
				myAgent.doDelete();
			}
		}

	
	}

}
