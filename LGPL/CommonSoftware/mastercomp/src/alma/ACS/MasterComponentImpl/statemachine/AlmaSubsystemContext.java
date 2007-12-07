package alma.ACS.MasterComponentImpl.statemachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import alma.ACSErrTypeCommon.wrappers.AcsJIllegalStateEventEx;
import alma.acs.genfw.runtime.sm.AcsState;
import alma.acs.genfw.runtime.sm.AcsStateActionException;
import alma.acs.genfw.runtime.sm.AcsStateChangeListener;
import alma.acs.logging.AcsLogger;


/**
 * The super context class for the AlmaSubsystem state machine.
 */
public class AlmaSubsystemContext
{
	private AlmaSubsystemStateAbstract m_currentState;
	private AlmaSubsystemActions m_actionDelegate;
	private List<AcsStateChangeListener> m_stateChangeListeners;
	private AcsState[] m_oldHierarchy;
	private AcsLogger m_logger;
	private boolean m_verbose = false;

	
	// state objects
	// todo: make instances private and add get methods

	public AvailableState m_stateAvailable;

	public ErrorState m_stateError;

	public OnlineState m_stateOnline;

	public OperationalState m_stateOperational;

	public OfflineState m_stateOffline;

	public ShutdownState m_stateShutdown;

	public ShuttingdownPass1State m_stateShuttingdownPass1;

	public InitializingPass2State m_stateInitializingPass2;

	public InitializingPass1State m_stateInitializingPass1;

	public ReinitializingState m_stateReinitializing;

	public PreInitializedState m_statePreInitialized;

	public PreShutdownState m_statePreShutdown;

	public ShuttingdownPass2State m_stateShuttingdownPass2;


	public AlmaSubsystemContext(AlmaSubsystemActions actions, AcsLogger logger) {
		m_actionDelegate = actions;
		m_stateChangeListeners = new ArrayList<AcsStateChangeListener>();
		m_logger = logger;

		m_stateAvailable = new AvailableState(this);

		m_stateOperational = new OperationalState(this, m_stateAvailable);

		m_stateError = new ErrorState(this, m_stateAvailable);

		m_stateOnline = new OnlineState(this, m_stateAvailable);

		m_stateOffline = new OfflineState(this, m_stateAvailable);

		m_stateShutdown = new ShutdownState(this, m_stateOffline, m_logger);

		m_stateShuttingdownPass1 = new ShuttingdownPass1State(this, m_stateOffline, m_logger);

		m_stateInitializingPass2 = new InitializingPass2State(this, m_stateOffline, m_logger);

		m_stateInitializingPass1 = new InitializingPass1State(this, m_stateOffline, m_logger);

		m_stateReinitializing = new ReinitializingState(this, m_stateOffline, m_logger);

		m_statePreInitialized = new PreInitializedState(this, m_stateOffline, m_logger);

		m_statePreShutdown = new PreShutdownState(this, m_stateOffline, m_logger);

		m_stateShuttingdownPass2 = new ShuttingdownPass2State(this, m_stateOffline, m_logger);


		// initial state
		m_stateShutdown.activate("<init>");
		
		m_oldHierarchy = getCurrentTopLevelState().getStateHierarchy();
	}

	/**
	 * Registers an object that will be notified about any state change in this state machine.
	 * @param listener
	 */
	public synchronized void addAcsStateChangeListener(AcsStateChangeListener listener) {
		m_stateChangeListeners.add(listener);
	}
	

	void setState(AlmaSubsystemStateAbstract newState, String eventName) {
		AlmaSubsystemStateAbstract oldState = m_currentState;
		if (oldState != newState) {
			logTransition(m_currentState, newState, eventName);
			m_currentState = newState;
			m_currentState.entry();
		}
		AcsState[] currentHierarchy = getCurrentTopLevelState().getStateHierarchy();
		// check if there was a state change down the nesting hierarchy		
		if (!Arrays.equals(currentHierarchy, m_oldHierarchy)) {
			// if so, notify listeners
			for (Iterator<AcsStateChangeListener> iter = m_stateChangeListeners.iterator(); iter.hasNext();) {
				AcsStateChangeListener listener = iter.next();
				listener.stateChangedNotify(m_oldHierarchy, currentHierarchy);
			}
			m_oldHierarchy = currentHierarchy;
		}
	}
	
	public synchronized AlmaSubsystemStateAbstract getCurrentTopLevelState()
	{
		return m_currentState;
	}

	//======================================================================
	// delegates incoming events to current state class
	//======================================================================

	public synchronized void initPass1() throws AcsJIllegalStateEventEx {
		m_currentState.initPass1();
	}

	public synchronized void initPass2() throws AcsJIllegalStateEventEx {
		m_currentState.initPass2();
	}

	public synchronized void reinit() throws AcsJIllegalStateEventEx {
		m_currentState.reinit();
	}

	public synchronized void start() throws AcsJIllegalStateEventEx {
		m_currentState.start();
	}

	public synchronized void stop() throws AcsJIllegalStateEventEx {
		m_currentState.stop();
	}

	public synchronized void shutdownPass1() throws AcsJIllegalStateEventEx {
		m_currentState.shutdownPass1();
	}

	public synchronized void shutdownPass2() throws AcsJIllegalStateEventEx {
		m_currentState.shutdownPass2();
	}

	public synchronized void error() throws AcsJIllegalStateEventEx {
		m_currentState.error();
	}

	

	//======================================================================
	// delegates actions to user-provided action handler
	// Note that actions of activity states are run in separate threads 
	//======================================================================

	public void initSubsysPass1() throws AcsStateActionException {
		try {
			m_actionDelegate.initSubsysPass1();
		}
		catch (AcsStateActionException ex) {
			throw ex;
		}
		catch (Throwable thr) {
			throw new AcsStateActionException(thr);
		}
	}

	public void initSubsysPass2() throws AcsStateActionException {
		try {
			m_actionDelegate.initSubsysPass2();
		}
		catch (AcsStateActionException ex) {
			throw ex;
		}
		catch (Throwable thr) {
			throw new AcsStateActionException(thr);
		}
	}

	public void reinitSubsystem() throws AcsStateActionException {
		try {
			m_actionDelegate.reinitSubsystem();
		}
		catch (AcsStateActionException ex) {
			throw ex;
		}
		catch (Throwable thr) {
			throw new AcsStateActionException(thr);
		}
	}

	public void shutDownSubsysPass1() throws AcsStateActionException {
		try {
			m_actionDelegate.shutDownSubsysPass1();
		}
		catch (AcsStateActionException ex) {
			throw ex;
		}
		catch (Throwable thr) {
			throw new AcsStateActionException(thr);
		}
	}

	public void shutDownSubsysPass2() throws AcsStateActionException {
		try {
			m_actionDelegate.shutDownSubsysPass2();
		}
		catch (AcsStateActionException ex) {
			throw ex;
		}
		catch (Throwable thr) {
			throw new AcsStateActionException(thr);
		}
	}

	
	//======================================================================
	// util methods
	//======================================================================

	void illegalEvent(String stateName, String eventName) throws AcsJIllegalStateEventEx
	{
		String msg = "illegal event '" + eventName + "' in state '" + stateName + "'.";
		if (m_verbose) {
			m_logger.warning(msg);
		}
//		for (Iterator iter = m_stateChangeListeners.iterator(); iter.hasNext();) {
//			AcsStateChangeListener listener = (AcsStateChangeListener) iter.next();
//			listener.illegalEventNotify(stateName, eventName);			
//		}
		AcsJIllegalStateEventEx ex = new AcsJIllegalStateEventEx();
		ex.setEvent(eventName);
		ex.setState(stateName);
		throw ex;
	}
	
	void logTransition(AcsState sourceState, AcsState targetState, String eventName) {
		 
		if (m_verbose && sourceState != null) {  // sourceState is null at initial state setting
			String msg = "event '" + eventName + "' causes transition from '" + 
			sourceState.stateName() + "' to '" + targetState.stateName() + "'.";
			m_logger.info(msg);
		}
	}

}
