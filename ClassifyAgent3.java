package classification;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;;

public class ClassifyAgent3 extends Agent {

	public ClassifyAgent3() {

	}

	@Override
	protected void setup() {
		System.out.println(" Hello classify Agent 3 is ready " + getAID().getName());
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
		sd.setName("ClassifyAgent3");
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
		System.out.println("ClassifyAgent 3 " + getAID().getName() + " terminating");
	}

	private class ExecuteClassification extends CyclicBehaviour {
		private String ClusteredOutput;
		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				System.out.println("Received classification request: " + msg.getContent());
				ACLMessage reply = msg.createReply();
				KNearest classification = new KNearest();
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
					
					
					
					String content = "";
					for ( int k = 0 ; k < classification.trueValues().size() ; k++){
						content = content + classification.trueValues().get(k)+ ",";
					}
					content = content + "]";
					for ( int k = 0 ; k < classification.predictionValues().size(); k++){
						content = content+ classification.predictionValues().get(k) + ",";
					}
				
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(content+"&"+ClusteredOutput);
					//System.out.println(" Content sent through " + reply.getContent());

					
					System.out.println();
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

		}

	
	}

}
