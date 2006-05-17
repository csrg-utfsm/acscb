package si.ijs.acs.objectexplorer;

import java.util.Hashtable;


import si.ijs.acs.objectexplorer.engine.Converter;
import si.ijs.acs.objectexplorer.engine.Invocation;
import si.ijs.acs.objectexplorer.engine.RemoteCall;
import si.ijs.acs.objectexplorer.engine.RemoteResponse;

import com.cosylab.gui.components.r2.DataFormatter;
import com.cosylab.gui.components.r2.SmartTextPane;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * Insert the type's description here.
 * Creation date: (11/10/00 4:13:13 PM)
 * @author: Miha Kadunc
 */
 
public class ReporterBean {

  
  private SmartTextPane resultArea=null;
  private NotificationBean notifier=null;
  private boolean expand=false;
  private boolean window=true;
  private Hashtable responseWindows=new Hashtable(10);
  static int raID=0;

  private static final SimpleDateFormat df = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS");

  private Style redStyle = null;
  private Style blackStyle = null;
  
/**
 * ReporterBean constructor comment.
 */
public ReporterBean() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (3/27/2001 5:25:40 PM)
 */
public void clearResponseWindows() {
	notifier.reportDebug("ReporterBean::clearResponseWindows","started");
	java.util.Enumeration windows=responseWindows.elements();
	while (windows.hasMoreElements()) {
	  Object one=windows.nextElement();
	  if (one instanceof RemoteResponseWindow) {
		  ((RemoteResponseWindow)one).setDisposeOnDestroy(true);
	      notifier.reportDebug("ReporterBean::clearResponseWindows","disposed "+one);
	  }
	}
	notifier.reportDebug("ReporterBean::clearResponseWindows","finished");
}
/**
 * Insert the method's description here.
 * Creation date: (11/4/00 3:07:23 PM)
 * @return javax.swing.JTextArea
 */
public SmartTextPane getResultArea() {
	return resultArea;
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/00 12:44:55 PM)
 */
public synchronized void invocationDestroyed(Invocation inv) {
	
	Integer windowSerial = new Integer(inv.getInvocationRequest().getSN());
	Object tempWindow = responseWindows.get(windowSerial);
	
	if (tempWindow instanceof RemoteResponseWindow) {
	  ((RemoteResponseWindow)tempWindow).disable();
	}
	else {
  	    responseWindows.remove(windowSerial);
		resultArea.append("\n["+windowSerial+"] INVOCATION "+inv+" WAS DESTROYED \n");
		resultArea.setCaretPosition(resultArea.getText().length() - 1);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (9/12/2002 2:03:42 PM)
 * @return boolean
 */
public boolean isExpand() {
	return expand;
}
/**
 * Insert the method's description here.
 * Creation date: (3/27/2001 5:25:40 PM)
 */
public void killResponseWindows() {
	notifier.reportDebug("ReporterBean::killResponseWindows","started");
	java.util.Enumeration windows=responseWindows.elements();
	while (windows.hasMoreElements()) {
	  Object one=windows.nextElement();
	  if (one instanceof RemoteResponseWindow) {
		  ((RemoteResponseWindow)one).dispose();
	  }
	}
	notifier.reportDebug("ReporterBean::killResponseWindows","finished");
}
/**
 * Insert the method's description here.
 * Creation date: (11/10/00 4:13:52 PM)
 */
public synchronized void reportRemoteCall(RemoteCall call) {
	try
	{
		boolean exceptionThrown = (call.getThrowable()!=null); 
		if (exceptionThrown)
		{
			// needed since carent does not point always to the end
			resultArea.setCaretPosition(resultArea.getText().length());
			resultArea.setLogicalStyle(redStyle);
		}
		
		resultArea.append(toString(call, expand));

		if (exceptionThrown)
			resultArea.append("\n");
	}
	finally
	{
	    resultArea.setLogicalStyle(blackStyle);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/00 12:44:55 PM)
 */
public synchronized void reportRemoteResponse(RemoteResponse response) {
	Integer windowSerial = new Integer(response.getInvocation().getInvocationRequest().getSN());
	Object tempWindow = responseWindows.get(windowSerial);
	
	if ((window) && (response.getSequenceNumber() > 0) && !(tempWindow instanceof String)) {
		RemoteResponseWindow rWindow = null;
		if (tempWindow instanceof RemoteResponseWindow) {
			rWindow = (RemoteResponseWindow)tempWindow;
		} else {
			rWindow = new RemoteResponseWindow(response, notifier, this);
			responseWindows.put(windowSerial, rWindow);
			rWindow.show();
		}
		if (!rWindow.isDestroyed()) rWindow.reportRemoteResponse(response);
	} else {
		resultArea.append(toString(response, expand) + "\n");
		resultArea.setCaretPosition(resultArea.getText().length() - 1);
		if (response.getSequenceNumber() == 0) responseWindows.put(windowSerial,response);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2001 5:08:12 PM)
 * @param newExpand boolean
 */
public void setExpand() {
	if (expand) expand=false;
	else expand=true;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2001 5:08:12 PM)
 * @param newExpand boolean
 */
public void setNotifier(NotificationBean notifier) {
	this.notifier=notifier;
}
/**
 * Insert the method's description here.
 * Creation date: (11/4/00 3:07:23 PM)
 * @param newResultArea javax.swing.JTextArea
 */
public void setResultArea(SmartTextPane newResultArea) {
	resultArea = newResultArea;

	// default style 
	blackStyle = resultArea.getLogicalStyle();
	
	// Makes text red
    redStyle = resultArea.addStyle("Red", null);
    StyleConstants.setForeground(redStyle, Color.red);
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2001 5:08:12 PM)
 * @param newExpand boolean
 */
public void setWindow() {
	if (window) window=false;
	else window=true;
}
/**
 */
public static String toString(RemoteCall call) {
  return toString(call, true);
}
/**
 */
public static String toString(RemoteCall call, boolean expand) {
	
  StringBuffer result = new StringBuffer(500);
  result.append('[');
  result.append(call.getSN());
  result.append("] : ");
  //result.append(System.currentTimeMillis());
  result.append(df.format(new Date(System.currentTimeMillis())));
  result.append(" |--------------------------------------------------------\n");
  result.append(call.getIntrospectable().getName());

  String name = null;
  if (call.getAttribute()==null) {
	if (call.getOperation()!=null) name=call.getOperation().getName();
	else throw(new NullPointerException("RemoteCall -- both Operation and Attribute are null"));
  }
  else
  	name=call.getAttribute().toString();
  
  // do the conversion
  Converter converter;
  if (call.isAttributeAccess())
  	converter = ObjectExplorer.getConverter(call.getAttribute().getIntrospectable());
  else
  	converter = ObjectExplorer.getConverter(call.getOperation().getIntrospectable());

  Object returnValue = call.getSyncReturnValue();
  Object[] auxs = call.getAuxReturnValues();
  if (converter != null && converter.acceptConvert(name))
  		returnValue = converter.convert(name, auxs, returnValue);
  
  result.append('.');
  result.append(name);
  result.append('\n');
  result.append(" --> Return value: ");
  if ((call.getOperation() != null) && (call.getOperation().getReturnType()==Void.TYPE))
	result.append("void");
  else {
  	result.append(DataFormatter.unpackReturnValue(returnValue,"      ",0,expand));
  }
  result.append('\n');
  if (auxs!=null) {  
		for (int i = 0; i < auxs.length; i++)
			if (auxs[i] != null) result.append(" --> Auxiliary return value '" + call.getOperation().getParameterNames()[i] + "' = " + DataFormatter.unpackReturnValue(auxs[i],"      ",0,expand) + "\n");
  }


  if (call.getThrowable()!=null) {
	 result.append(" --> Exception: " + call.getThrowable() + "\n");
	 // always expand exception
	 result.append(DataFormatter.unpackReturnValue(call.getThrowable(),"/      ",0,true));
	 
  }

  if (call.isTimeout()) {
	 result.append(" --> Timeout raised by the engine while waiting for response.\n");
  }
  
  return result.toString();
}
/**
 */
public static String toString(RemoteResponse response) {
	return toString(response, false);
}
/**
 */
public static String toString(RemoteResponse response, boolean expand) {

	StringBuffer result = new StringBuffer(500);
	result.append("[");
	result.append(response.getInvocation().getInvocationRequest().getSN());
	result.append(":");
	result.append(response.getSequenceNumber());
	result.append("] : ");
	//result.append(System.currentTimeMillis());
	result.append(df.format(new Date(System.currentTimeMillis())));
	result.append(" |-----------------------------------------------------\n");
	result.append(" --> Response for: ");
	result.append(response.getInvocation().getName());
	result.append("\n --> Message: ");
	result.append(response.getName());
	result.append("\n --> Parameters: ");

	Object[] data = response.getData();
	for (int i = 0; i < data.length; i++) {
		result.append("\n    ");
		result.append(response.getDataNames()[i]);
		result.append(": ");
		result.append(DataFormatter.unpackReturnValue(data[i], "      ", 0,expand));
	}
	return result.toString();
}
}
