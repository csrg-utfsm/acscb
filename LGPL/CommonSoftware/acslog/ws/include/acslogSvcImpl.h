#ifndef _ACS_LOG_SVC_IMPL_H_
#define _ACS_LOG_SVC_IMPL_H_
/*******************************************************************************
*    ALMA - Atacama Large Millimiter Array
*    (c) European Southern Observatory, 2002
*    Copyright by ESO (in the framework of the ALMA collaboration)
*    and Cosylab 2002, All rights reserved
*
*    This library is free software; you can redistribute it and/or
*    modify it under the terms of the GNU Lesser General Public
*    License as published by the Free Software Foundation; either
*    version 2.1 of the License, or (at your option) any later version.
*
*    This library is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*    Lesser General Public License for more details.
*
*    You should have received a copy of the GNU Lesser General Public
*    License along with this library; if not, write to the Free Software
*    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
*
* "@(#) $Id: acslogSvcImpl.h,v 1.13 2006/12/13 16:38:11 bjeram Exp $"
*
* who       when      what
* --------  --------  ----------------------------------------------
* bjeram 2001-09-11 created 
*/

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <acscommonC.h>

#include "acslogS.h"
#include "logging.h"

#include <acsutilPorts.h>

typedef unsigned long PriortyFlag;

class ACSLogImpl : public POA_ACSLog::LogSvc {
public:
  ACSLogImpl(LoggingProxy &logProxy) : m_logProxy(logProxy){}
  
  void logTrace (acscommon::TimeStamp time,
		 const char * msg,
		 const ACSLog::RTContext & rtCont,
		 const ACSLog::SourceInfo & srcInfo,
		 const ACSLog::NVPairSeq & data
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );

  void logDebug (acscommon::TimeStamp time,
		 const char * msg,
		 const ACSLog::RTContext & rtCont,
		 const ACSLog::SourceInfo & srcInfo,
		 const ACSLog::NVPairSeq & data
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );

  void logInfo (acscommon::TimeStamp time,
		 const char * msg,
		 const ACSLog::RTContext & rtCont,
		 const ACSLog::SourceInfo & srcInfo,
		 const ACSLog::NVPairSeq & data
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );

  void logNotice (acscommon::TimeStamp time,
		 const char * msg,
		 const ACSLog::RTContext & rtCont,
		 const ACSLog::SourceInfo & srcInfo,
		 const ACSLog::NVPairSeq & data
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );

  void logWarning (acscommon::TimeStamp time,
		 const char * msg,
		 const ACSLog::RTContext & rtCont,
		 const ACSLog::SourceInfo & srcInfo,
		 const ACSLog::NVPairSeq & data
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );

  void logError (const ACSErr::ErrorTrace &c
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );

  void logErrorWithPriority (const ACSErr::ErrorTrace &c,
			    ACSLog::Priorities p
      ) throw ( CORBA::SystemException, ACSErr::ACSException );
  
  void logCritical (acscommon::TimeStamp time,
		 const char * msg,
		 const ACSLog::RTContext & rtCont,
		 const ACSLog::SourceInfo & srcInfo,
		 const ACSLog::NVPairSeq & data
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );

  void logAlert (acscommon::TimeStamp time,
		 const char * msg,
		 const ACSLog::RTContext & rtCont,
		 const ACSLog::SourceInfo & srcInfo,
		 const ACSLog::NVPairSeq & data
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );

  void logEmergency (acscommon::TimeStamp time,
		 const char * msg,
		 const ACSLog::RTContext & rtCont,
		 const ACSLog::SourceInfo & srcInfo,
		 const ACSLog::NVPairSeq & data
		 
		 ) throw ( CORBA::SystemException, ACSErr::ACSException );


  void logXML (const char * xml
	       
	       ) throw (CORBA::SystemException, ACSErr::ACSException );
    
  void logXMLTimed (acscommon::TimeStamp time,
		    const char * xml
		    
		    ) throw (CORBA::SystemException, ACSErr::ACSException );
private:
  bool checkRTContext (const ACSLog::RTContext & rtCont);
  bool checkSourceInfo (const ACSLog::SourceInfo & srcInfo);
  PriortyFlag writeRTContext (const ACSLog::RTContext & rtCont);
  PriortyFlag writeSourceInfo (const ACSLog::SourceInfo & srcInfo);
  unsigned int writeData (const ACSLog::NVPairSeq & data);
  PriortyFlag write(const ACSLog::RTContext & rtCont,
		    const ACSLog::SourceInfo & srcInfo,
		    const ACSLog::NVPairSeq & data);
  LoggingProxy &m_logProxy;
};



#endif





