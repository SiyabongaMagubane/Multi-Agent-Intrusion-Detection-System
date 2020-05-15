package classification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CommunicationAgent extends Agent {
	int classes;
	private JTextField numberOfClasses;
	private JFrame frame;
	private JPanel pane;

	private AID[] classifyAgents;
	private AID[] ensembleAgent;
	private ArrayList<AID> responseAgents ;

	@Override
	protected void setup() {
		responseAgents = new ArrayList<AID>();
		System.out.println(" Hello Communication Agent is ready " + getAID().getName());
		addBehaviour(new ReadInput());
		System.out.println("About to begin classification");
		addBehaviour(new WakerBehaviour(this, 5000) {
			@Override
			protected void handleElapsedTimeout() {
				System.out.println(" Sending out comms to classification Agents");
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("classification");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template);
					System.out.println("Found the following seller agents:");
					classifyAgents = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						classifyAgents[i] = result[i].getName();
						System.out.println(classifyAgents[i].getName());
					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
				sd.setType("ensemble");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template);
					System.out.println("Found the following ensemble agents:");
					ensembleAgent = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						ensembleAgent[i] = result[i].getName();
						System.out.println(ensembleAgent[i].getName());
					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
				myAgent.addBehaviour( new ReceiveClassResults());

			}

		});
	}

	@Override
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("CommunicationAgent " + getAID().getName() + " terminating.");
	}

	// Display Welcome screen and main menu.
	private class ReadInput extends OneShotBehaviour {
		// The list of known seller agents
		private MessageTemplate mt;
		private Vector classifyAgents = new Vector();

		@Override
		public void action() {
			pane = new JPanel();
			pane.setLayout(new GridLayout(0, 2, 2, 2));
			pane.setSize(40, 100);
			numberOfClasses = new JTextField(5);

			pane.add(new JLabel("Welcome to the multi agent system wizard."));
			int option = JOptionPane.showConfirmDialog(frame, pane, "Please fill all the fields",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

			if (option == JOptionPane.YES_OPTION) {

				try {
					pane.setBackground(Color.blue);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}

			}
			// Create the classification Agents
			/*
			 * 
			 * 
			 * 
			 * ACLMessage msg = new ACLMessage(ACLMessage.CFP); for ( int i = 0;
			 * i < classes ; i++){ AID classifyAgent = new AID((String)
			 * ("ClassifyAgent" +Integer.toString(i+1)), AID.ISLOCALNAME);
			 * System.out.println("Agent:"+classifyAgent.getName());
			 * msg.addReceiver(classifyAgent);
			 * System.out.println("Sending classification request : " +
			 * msg.getContent() + " to receiver: "+classifyAgent.getName());
			 * 
			 * } msg.setContent("Perform classification");
			 * msg.setConversationId("Classification_ID");
			 * msg.setReplyWith("msg"+System.currentTimeMillis());
			 * myAgent.send(msg); mt =
			 * MessageTemplate.and(MessageTemplate.MatchConversationId(
			 * "Classification_ID"),
			 * MessageTemplate.MatchInReplyTo(msg.getReplyWith()));
			 * 
			 * 
			 */
		}
	}

	private class ReceiveClassResults extends Behaviour {
		private int repliesCount = 0; // The counter of replies from seller agents
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;
		private String response;
		private String [] responseClassification;
		private String EnsembleContent="";
		private ArrayList<String> states = new ArrayList();
		
		private JFrame f; 
	    // Table 
	    private JTable j;
		
		public void action() {
			switch (step) {
			case 0:
				// Send the cfp to all sellers
				
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < classifyAgents.length; ++i) {
					cfp.addReceiver(classifyAgents[i]);
				} 
				cfp.setContent("classify");
				cfp.setConversationId("classification");
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				myAgent.send(cfp);
				// Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("classification"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				//receive the classifications from Classify Agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null){
					if ( reply.getPerformative() == ACLMessage.PROPOSE){
						response = reply.getContent();
						System.out.println("Received this feedback from agent "+ reply.getSender().getName());
						System.out.println(response);
						System.out.println("..........");
						String[] fullcontent = response.trim().split("&");
						response = fullcontent[0];
						states.add(fullcontent[1]);
						System.out.println(states.get(repliesCount));
						if (EnsembleContent.equals("")){
							EnsembleContent = response;
							System.out.println("number of Agents: "+repliesCount);
						}
						else{
							EnsembleContent = EnsembleContent + "-"+ response;
							responseAgents.add(reply.getSender());
							System.out.println("number of Agents: "+repliesCount);
							}
						}
					repliesCount++;
					if (repliesCount >= classifyAgents.length ){
						System.out.println(" Content: "+EnsembleContent);
						System.out.println("number of Agents: "+repliesCount);
						step = 2;
					}
				}
				else {
					block();
				}
				break;
			case 2:
				// send to ensemble agent
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				for (int i = 0; i < ensembleAgent.length; ++i) {
					//ensembleAgent[i] = result[i].getName();
					System.out.println(ensembleAgent[i].getName());
					order.addReceiver(ensembleAgent[i]);
				}
				order.setContent(EnsembleContent);
				String [] temp = EnsembleContent.trim().split("-");
				System.out.println(" There ensmble content contains "+temp.length + " content from the Agents");
				order.setConversationId("ensemble");
				order.setReplyWith("order"+System.currentTimeMillis());
				myAgent.send(order);
				// Prepare the template to get the purchase order reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("ensemble"),
						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 3;
				break;
				
			case 3:
				// receive reply from ensemble Agent and Visualize output
				reply = myAgent.receive(mt);
				if (reply != null) {
					// Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// Purchase successful. We can terminate
						states.add(reply.getContent() +","+reply.getSender().getName());
						String [] temp1;
						f = new JFrame(); 
						  
				        // Frame Title 
				        f.setTitle("Classification Results"); 
				        String [] count = states.get(0).trim().split(",");
				        // Data to be displayed in the JTable 
				        String[][] data = new String [states.size()][count.length];
						for ( int k = 0; k < states.size(); k++){
							System.out.println(states.get(k));
							temp1 = states.get(k).trim().split(",");
							for ( int i = 0; i < temp1.length ; i++){
								data[k][i] = temp1[i];
							}
							
						}
				  
				        // Column Names 
				        String[] columnNames = { "TotalNumberOfPredictions", "Correct", "Incorrect","TruePositive","FalsePositive","TrueNegative","FalseNegative","ErrorRate","AccuracyRate","TruePositiveRate","FalsePostiveRate","Agent Name" }; 
				  
				        // Initializing the JTable 
				        j = new JTable(data, columnNames); 
				        j.setBounds(30, 40, 1000, 500); 
				  
				        // adding it to JScrollPane 
				        JScrollPane sp = new JScrollPane(j); 
				        f.getContentPane().add(sp,BorderLayout.CENTER ); 
				        // Frame Size 
				        f.setSize(1000, 500); 
				        // Frame Visible = true 
				        f.setVisible(true); 
				        final Color VERY_LIGHT_BLUE = new Color(51,153,255);
				        j.setBackground(VERY_LIGHT_BLUE);
				        
				      
						System.out.println("successfully ensembled from agent "+reply.getSender().getName());
						step = 4;
						
						myAgent.doDelete();
					}	
				}
				else {
					block();
				}
				System.out.println("Before breaking statement");
				break;
			case 4:
				System.out.println("After breaking statement");
			
			}
		}

		@Override
		public boolean done() {
			if (step == 2 && ensembleAgent == null) {
				System.out.println("Attempt failed: "+" not available for sale");
			}
			return ((step == 2 && ensembleAgent == null) || step == 4);
		}
	}
}

