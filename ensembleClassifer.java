package classification;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ensembleClassifer implements ensemble {

	int classes;
	private JTextField numberOfClasses;
	private JFrame frame;
	private JPanel pane;

	public ensembleClassifer() {

	}

	private void ReadInput() {

		pane = new JPanel();
		pane.setLayout(new GridLayout(0, 2, 2, 2));

		this.numberOfClasses = new JTextField(5);

		pane.add(new JLabel("Enter the number of classes"));
		numberOfClasses.setBounds(20, 30, 20, 30);
		pane.add(this.numberOfClasses);

		int option = JOptionPane.showConfirmDialog(frame, pane, "Please fill all the fields", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);

		if (option == JOptionPane.YES_OPTION) {

			try {
				this.classes = Integer.parseInt(this.numberOfClasses.getText());
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}

		}

	}

	@Override
	public void ensemble() {
		ReadInput();

	}

	@Override
	public void addBase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeBase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void RoundsOfBagging() {
		// TODO Auto-generated method stub

	}

	@Override
	public void voting() {
		// TODO Auto-generated method stub

	}

}
