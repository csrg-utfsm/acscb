
package alma.acs.logging.archive;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import com.cosylab.logging.engine.log.LogTypeHelper;
import com.cosylab.logging.settings.LogTypeRenderer;

/**
 * A class to setup a query to submit to the DB
 * 
 * @author acaproni
 *
 */
public class QueryDlg extends JDialog implements ActionListener {
	
	private JButton submitBtn;
	private JButton doneBtn;
	
	// The archive
	private ArchiveConnectionManager archive;
	
	// The time limits for the query
	private JTextField fromYY, fromMM, fromDD, fromHr, fromMin, fromSec;
	private JTextField toYY, toMM, toDD, toHr, toMin, toSec;
	
	// The min and max log type
	private JComboBox minLogLevelCB, maxLogLevelCB;
	
	// The process name for the query
	private JTextField procName;
	
	// The name of the routine
	private JTextField sourceName;
	
	private JTextField routineName;
	
	// The max number of log to get from the DB
	private JTextField rowLimit;

	/**
	 * Empty constructor
	 */
	public QueryDlg(ArchiveConnectionManager archiveConn) {
		super();
		setTitle("Load from database");
		archive = archiveConn;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
		setBounds(50,50,50,50);
		pack();
		setVisible(true);
	}
	
	/**
	 * @see java.awt.event.ActionListener
	 * @see java.awt.event.ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==submitBtn) {
			submitQuery();
		} else if (e.getSource()==doneBtn) {
			setVisible(false);
			dispose();
		} else {
			System.err.println("Unknown event "+e);
		}
	}
	
	/**
	 * Build the GUI
	 *
	 */
	private void initGUI() {
		// The actual time/date used to fill the time fields 
		Calendar calendar = Calendar.getInstance();
		
		JRootPane mainPnl = this.getRootPane();
		mainPnl.setLayout(new BorderLayout());
		
		// The panel with the option of the query
		JPanel optionsPnl = new JPanel();
		GridBagLayout prefsLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		optionsPnl.setLayout(prefsLayout);
		// Add all the labels
		JLabel fromLbl = new JLabel("From:");
		c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(fromLbl,c);
		JLabel toLbl = new JLabel("To");
		c.gridx=0; c.gridy=1; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(toLbl,c);
		JLabel minLogType = new JLabel("From type");
		c.gridx=0; c.gridy=2; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(minLogType,c);
		JLabel maxLogType = new JLabel("To type");
		c.gridx=0; c.gridy=3; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(maxLogType,c);
		JLabel procNameLbl = new JLabel("Process name");
		c.gridx=0; c.gridy=4; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(procNameLbl,c);
		JLabel srcNameLbl = new JLabel("Source object");
		c.gridx=0; c.gridy=5; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(srcNameLbl,c);
		JLabel routinNameLbl = new JLabel("Routine name");
		c.gridx=0; c.gridy=6; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(routinNameLbl,c);
		JLabel maxLogs = new JLabel("Max num of logs to load");
		c.gridx=0; c.gridy=7; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(maxLogs,c);
		
		// Add the input widgets
		fromYY = new JTextField(Integer.toString(calendar.get(Calendar.YEAR)),4);
		c.gridx=1; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(fromYY,c);
		JLabel separatorF1 = new JLabel("-");
		c.gridx=2; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(separatorF1,c);
		fromMM = new JTextField(Integer.toString(calendar.get(Calendar.MONTH)+1),2);
		c.gridx=3; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(fromMM,c);
		JLabel separatorF2 = new JLabel("-");
		c.gridx=4; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(separatorF2,c);
		fromDD= new JTextField(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)),2);
		c.gridx=5; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(fromDD,c);
		JLabel tlbl = new JLabel("T");
		c.gridx=6; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(tlbl,c);
		fromHr= new JTextField(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)),2);
		c.gridx=7; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(fromHr,c);
		JLabel comaF1Lbl = new JLabel(":");
		c.gridx=8; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(comaF1Lbl,c);
		fromMin = new JTextField(Integer.toString(calendar.get(Calendar.MINUTE)),2);
		c.gridx=9; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(fromMin,c);
		JLabel comaF2Lbl = new JLabel(":");
		c.gridx=10; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(comaF2Lbl,c);
		fromSec= new JTextField(Integer.toString(calendar.get(Calendar.SECOND)),2);
		c.gridx=11; c.gridy=0; c.anchor=GridBagConstraints.LAST_LINE_START; c.gridwidth=GridBagConstraints.REMAINDER; c.insets = new Insets(0,0,0,0);
		optionsPnl.add(fromSec,c);
		
		toYY = new JTextField(Integer.toString(calendar.get(Calendar.YEAR)),4);
		c.gridx=1; c.gridy=1; c.anchor=GridBagConstraints.LAST_LINE_START; 
		optionsPnl.add(toYY,c);
		JLabel separatorTo1 = new JLabel("-");
		c.gridx=2; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(separatorTo1,c);
		toMM = new JTextField(Integer.toString(calendar.get(Calendar.MONTH)+1),2);
		c.gridx=3; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(toMM,c);
		JLabel separatorTo2 = new JLabel("-");
		c.gridx=4; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(separatorTo2,c);
		toDD= new JTextField(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)),2);
		c.gridx=5; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(toDD,c);
		JLabel t2lbl = new JLabel("T");
		c.gridx=6; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(t2lbl,c);
		toHr= new JTextField(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)),2);
		c.gridx=7; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(toHr,c);
		JLabel comaTo1Lbl = new JLabel(":");
		c.gridx=8; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(comaTo1Lbl,c);
		toMin = new JTextField(Integer.toString(calendar.get(Calendar.MINUTE)),2);
		c.gridx=9; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(toMin,c);
		JLabel comaTo2Lbl = new JLabel(":");
		c.gridx=10; c.gridy=1; c.gridwidth=GridBagConstraints.RELATIVE; c.anchor=GridBagConstraints.LAST_LINE_START;
		optionsPnl.add(comaTo2Lbl,c);
		toSec= new JTextField(Integer.toString(calendar.get(Calendar.SECOND)),2);
		c.gridx=11; c.gridy=1; c.anchor=GridBagConstraints.LAST_LINE_START; c.gridwidth=GridBagConstraints.REMAINDER;
		optionsPnl.add(toSec,c);
		
		
		
		// Build the renderer for the combo boxex
		minLogLevelCB = new JComboBox();
		setupTypeCB(minLogLevelCB);
		minLogLevelCB.setSelectedIndex(LogTypeHelper.ENTRYTYPE_INFO);
		c.gridx=1; c.gridy=2; c.gridwidth=GridBagConstraints.REMAINDER; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(minLogLevelCB,c);
		maxLogLevelCB= new JComboBox();
		setupTypeCB(maxLogLevelCB);
		maxLogLevelCB.setSelectedIndex(LogTypeHelper.ENTRYTYPE_EMERGENCY);
		c.gridx=1; c.gridy=3; c.gridwidth=GridBagConstraints.REMAINDER; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(maxLogLevelCB,c);
		procName = new JTextField("*",20);
		c.gridx=1; c.gridy=4; c.gridwidth=GridBagConstraints.REMAINDER; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(procName,c);
		sourceName = new JTextField("*",20);
		c.gridx=1; c.gridy=5; c.gridwidth=GridBagConstraints.REMAINDER; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(sourceName,c);
		routineName = new JTextField("*",20);
		c.gridx=1; c.gridy=6; c.gridwidth=GridBagConstraints.REMAINDER; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(routineName,c);
		rowLimit = new JTextField("10000",20);
		c.gridx=1; c.gridy=7; c.gridwidth=GridBagConstraints.REMAINDER; c.insets = new Insets(5,5,5,5);
		optionsPnl.add(rowLimit,c);
		
		// Add the OK, CANCEL buttons
		JPanel btnPnl = new JPanel();
		btnPnl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		BoxLayout boxLayout = new BoxLayout(btnPnl,BoxLayout.LINE_AXIS);
		btnPnl.setLayout(boxLayout);
		btnPnl.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(this);
		submitBtn.setEnabled(archive.getDBStatus()==ArchiveConnectionManager.DATABASE_OK);
		doneBtn = new JButton("Cancel");
		doneBtn.addActionListener(this);
		btnPnl.add(submitBtn,BorderLayout.WEST);
		btnPnl.add(Box.createRigidArea(new Dimension(10, 0)));
		btnPnl.add(doneBtn,BorderLayout.EAST);
		// Add the subpanels
		mainPnl.add(optionsPnl,BorderLayout.CENTER);
		mainPnl.add(btnPnl,BorderLayout.SOUTH);
	}
	
	private void setupTypeCB(JComboBox box) {
		//	Add the ComboBox for the log level
        LogTypeRenderer discardRendererCB = new LogTypeRenderer();
        String[] levelStr = new String[LogTypeHelper.getAllTypesDescriptions().length];
        for (int t=0; t<LogTypeHelper.getAllTypesDescriptions().length; t++) {
        	levelStr[t]=LogTypeHelper.getAllTypesDescriptions()[t];
        	box.insertItemAt(levelStr[t],t);
        }
        box.setMaximumRowCount(levelStr.length);
        box.setEditable(false);
        box.setRenderer(discardRendererCB);
	}
	
	/**
	 * Submit a query to the archive and insert the logs in the main window
	 *
	 */
	private void submitQuery() {
		if (!checkFields()) {
			JOptionPane.showMessageDialog(this,"Error getting values from the form","Input error!",JOptionPane.ERROR_MESSAGE);
			return;
		}
		System.out.println("Submitting a query");
		StringBuilder from=new StringBuilder(fromYY.getText());
		from.append('-');
		from.append(fromMM.getText());
		from.append('-');
		from.append(fromDD.getText());
		from.append('T');
		from.append(fromHr.getText());
		from.append(':');
		from.append(fromMin.getText());
		from.append(':');
		from.append(fromSec.getText());
		
		StringBuilder to=new StringBuilder(toYY.getText());
		to.append('-');
		to.append(toMM.getText());
		to.append('-');
		to.append(toDD.getText());
		to.append('T');
		to.append(toHr.getText());
		to.append(':');
		to.append(toMin.getText());
		to.append(':');
		to.append(toSec.getText());
		
		short minType = (short)minLogLevelCB.getSelectedIndex();
		short maxType = (short)maxLogLevelCB.getSelectedIndex();
		
		String routine = routineName.getText();
		if (routine.length()==0) {
			routine ="*";
		}
		String source= sourceName.getText();
		if (source.length()==0) {
			source ="*";
		}
		String process = procName.getText();
		if (process.length()==0) {
			process ="*";
		}
		int maxRows = Integer.parseInt(rowLimit.getText());
		
		Collection logs = null;
		try {
			logs = archive.getLogs(from.toString(),to.toString(),minType,maxType,routine,source,process,maxRows);
		} catch (Throwable t) {
			System.out.println("Error executing the query: "+t.getMessage());
			System.out.println("Database unavailable");
			JOptionPane.showMessageDialog(this,"Error executing the query:\n"+t.getMessage(),"Database error!",JOptionPane.ERROR_MESSAGE);
		}
		System.out.println("Num. of logs read from DB: "+logs.size());
	}
	
	/**
	 * Check the fields in the GUI before executing a query
	 * 
	 * @return true if the vaules in the fields are ok
	 */
	private boolean checkFields() {
		boolean ret = 
			Pattern.matches("[0-9]+",fromYY.getText()) &&
			Pattern.matches("[0-9]+",fromMM.getText()) &&
			Pattern.matches("[0-9]+",fromMM.getText()) &&
			Pattern.matches("[0-9]+",fromHr.getText()) &&
			Pattern.matches("[0-9]+",fromMin.getText()) &&
			Pattern.matches("[0-9]+",fromSec.getText()) &&
			Pattern.matches("[0-9]+",toYY.getText()) &&
			Pattern.matches("[0-9]+",toMM.getText()) &&
			Pattern.matches("[0-9]+",toDD.getText()) &&
			Pattern.matches("[0-9]+",toHr.getText()) &&
			Pattern.matches("[0-9]+",toMin.getText()) &&
			Pattern.matches("[0-9]+",toSec.getText()) &&
			Pattern.matches("[0-9]+",rowLimit.getText());
		ret = ret && Integer.parseInt(fromYY.getText())>0 &&
			Integer.parseInt(fromMM.getText())>0 &&
			Integer.parseInt(fromDD.getText())>0 &&
			Integer.parseInt(fromHr.getText())>=0 &&
			Integer.parseInt(fromMin.getText())>=0 &&
			Integer.parseInt(fromSec.getText())>=0 &&
			Integer.parseInt(toYY.getText())>0 &&
			Integer.parseInt(toMM.getText())>0 &&
			Integer.parseInt(toDD.getText())>0 &&
			Integer.parseInt(toHr.getText())>=0 &&
			Integer.parseInt(toMin.getText())>=0 &&
			Integer.parseInt(toSec.getText())>=0 &&
			Integer.parseInt(rowLimit.getText())>=0;
        return ret;
	}
	
}
