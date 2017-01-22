#include <DDSHelper.h>
#include <ace/Service_Config.h>
#include <string.h>
#include <stdlib.h>
#include <maciHelper.h>
#include <loggingACEMACROS.h>

using namespace ddsnc;

static bool factories_init = false;
static CORBA::String_var channel_name;

DDSHelper::DDSHelper(CORBA::String_var channelName)
{
	channel_name = channelName;
	ACE_CString managerName;
	try
	{
	   managerName = maci::MACIHelper::getManagerHostname(1,NULL);
	}
	catch(CORBA::TRANSIENT &ex)
	{
	   ACS_STATIC_LOG(LM_FULL_INFO, "DDSHelper::DDSHelper", (LM_ERROR,
				 "Manager ref null."));
	   exit(1);
        }
	ACE_CString tmpRoute = "corbaloc:iiop:";
	tmpRoute += managerName;
	tmpRoute +=":3999/DCPSInfoRepo";
	char* route = tmpRoute.rep();

	init(channelName, route);
	
}

DDSHelper::DDSHelper(const char* channelName, const char *DCPSInfoRepoLoc)
{
	init(channelName, DCPSInfoRepoLoc);
}

void DDSHelper::init(const char* channelName, const char* DCPSInfoRepoLoc)
{
	int argc;
	char* argv[7];
	argc=7;

	argv[0] = strdup("Participant");
	argv[1] = strdup("-DCPSInforepo");
	argv[2] = strdup(DCPSInfoRepoLoc);
	//DCPS Debug Level
	argv[3] = strdup("-DCPSDebugLevel");
	argv[4] = strdup("0");
	//Transport Debug Level
	argv[5] = strdup("-DCPSTransportDebugLevel");
	argv[6] = strdup("0");

	ACS_STATIC_LOG(LM_FULL_INFO, "DDSHelper::init", (LM_INFO,
			 "Registering TransportImpl."));

/* Fedora 8 systems have a problem with next line of code. If the component is
 * referenced by a simple client the DDS Notiification Channel works only once
 * time, the second time the maciContainer will exit with a Segmentation Fault
 * generated by ACE library. The error cannot be replicated if it used
 * object explorer. Maybe this error can affect to all RedHat OSs similar to
 * Fedora 8 (SL 5.2). The line below works great under Ubuntu 8.04
 */
//	ACE_Service_Config::process_directive(
//			"static DCPS_SimpleTcpLoader \"-type SimpleTcp\"");

	factories_init = true;
	
	dpf=TheParticipantFactoryWithArgs(argc, (ACE_TCHAR**)argv);
	
	initialized=false;
	setPartitionName(channelName);

//	::std::string channelStr(channelName);
//	 int pos=channelStr.find_last_of("_");
//   if(pos<0){
//
//      setTopicName(channelName);
//      return;
//   }
//   setTopicName(channelStr.substr(++pos).c_str());
//	  std::cerr << "Trying to set topic name: " << topicName <<std::endl;
//   setPartitionName(channelStr.substr(0,--pos).c_str());

}

int DDSHelper::createParticipant(){
    ACS_TRACE("DDSHelper::createParticipant");
	participant = dpf->create_participant(DOMAIN_ID,
			PARTICIPANT_QOS_DEFAULT,
			DDS::DomainParticipantListener::_nil(),
			OpenDDS::DCPS::DEFAULT_STATUS_MASK);

	if (CORBA::is_nil(participant.in())){
		ACS_STATIC_LOG(LM_FULL_INFO, "DDSHelper::createParticipant", (LM_ERROR,
				 "Create Participant Failed."));
		return 1;
	}
	
	ACS_STATIC_LOG(LM_FULL_INFO, "DDSHelper::createParticipant", (LM_INFO,
			 "Created the participant."));
	
	return 0;
}

void DDSHelper::setTopicName(const char* topicName)
{
	this->topicName=strdup(topicName);
}

void DDSHelper::initializeTopic(const char* topicName, CORBA::String_var typeName)
{
    const char *typeNameChar = typeName;
	ACS_STATIC_LOG(LM_FULL_INFO, "DDSHelper::initializeTopic", (LM_INFO,
			 "Initializing topic: '%s' with type: '%s'",this->topicName,typeNameChar));
	participant->get_default_topic_qos(topicQos);
	topic=participant->create_topic(topicName, typeName.in(),
			topicQos, DDS::TopicListener::_nil(),
			OpenDDS::DCPS::DEFAULT_STATUS_MASK);

	if (CORBA::is_nil(topic.in())){
		ACS_STATIC_LOG(LM_FULL_INFO, "DDSHelper::initializeTopic", (LM_INFO,
				 "create_topic failed"));
	}
	
}

void DDSHelper::initializeTopic(CORBA::String_var typeName)
{
	std::string topicStr (typeName);
	int f = topicStr.find_first_of(":");
	int l = topicStr.find_last_of(":");
	topicName = strdup(topicStr.substr(f+1, (l-1)-f).c_str());
	initializeTopic(topicName, typeName);
}

void DDSHelper::setPartitionName(const char* partitionName){
	this->partitionName=strdup(partitionName);
}

void DDSHelper::disconnect()
{
	ACS_TRACE("DDSHelper::disconnect");
	if(initialized==true){
		participant->delete_contained_entities();
		dpf->delete_participant(participant.in());
		initialized=false;
	}
}

DDSHelper::~DDSHelper()
{
	ACS_TRACE("DDSHelper::~DDSHelper");
	disconnect();
	free(partitionName);
	free(topicName);
}

void DDSHelper::cleanUp()
{
	ACS_TRACE("DDSHelper::cleanUp");
	if (factories_init){
		TheServiceParticipant->shutdown();
	}
}

CORBA::String_var DDSHelper::getChannelName()
{
return channel_name;
}
