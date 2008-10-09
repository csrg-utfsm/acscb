#! /usr/bin/env python
#*******************************************************************************
# ALMA - Atacama Large Millimiter Array
# (c) National Research Council of Canada, 2008 
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
# 
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
#
# "@(#) $Id: test_Acsalarmpy_ASI.py,v 1.1 2008/10/09 16:11:10 agrimstrup Exp $"
#
# who       when      what
# --------  --------  ----------------------------------------------
# agrimstrup  2008-10-02  created
#

import unittest
import Acsalarmpy.ASI as ASI
import time

def mocktime():
    return 1222887968.813309

time.time = mocktime

import Acsalarmpy.Timestamp as Timestamp
import Acsalarmpy.FaultState as FaultState

class TestASIConfiguration(unittest.TestCase):
    def test_object_initialization(self):
        """ASIConfiguration default initializer"""
        asiconfig = ASI.ASIConfiguration()
        self.assertEquals(ASI.ASI_VERSION, asiconfig.asiVersion)
        self.assertEquals(ASI.ALARMS_TOPIC, asiconfig.alarmsTopic)
        self.assertEquals(ASI.BACKUP_DELIVERY_MODE, asiconfig.backupDeliveryMode)
        self.assertEquals(ASI.BACKUP_PRIORITY, asiconfig.backupPriority)
        self.assertEquals(ASI.BACKUP_TIME_TO_LIVE, asiconfig.backupTimeToLive)
        self.assertEquals(ASI.CHANGES_DELIVERY_MODE, asiconfig.changesDeliveryMode)
        self.assertEquals(ASI.CHANGES_PRIORITY, asiconfig.changesPriority)
        self.assertEquals(ASI.CHANGES_TIME_TO_LIVE, asiconfig.changesTimeToLive)


class TestASIMessage(unittest.TestCase):
    def test_object_initialization(self):
        """ASIMessage default initializer"""
        msg = ASI.ASIMessage()
        self.assertEquals(True, msg.faultStates is None)
        self.assertEquals(False, msg.backup)
        self.assertEquals(ASI.ASI_VERSION, msg.version)
        self.assertEquals(ASI.ALARM_SOURCE_NAME, msg.sourceName)
        self.assertEquals(True, msg.sourceHostname is None)
        self.assertEquals(True, msg.sourceTimestamp is None)

    def test_toXML(self):
        """ASIMessage XML output for defaults"""
        msg = ASI.ASIMessage()
        msg.faultStates = [FaultState.FaultState("Family","Member",1)]
        msg.sourceHostname = 'foo'
        msg.sourceTimestamp = Timestamp.Timestamp()
        self.assertEqual('<ASI-message xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" backup="false" version="0.9" xsi:type="ASI-message">\n   <source-name>ALARM_SYSTEM_SOURCES</source_name>\n   <source-hostname>foo</source_hostname>\n   <source-timestamp seconds="1222887968" microseconds="813308"/>\n   <fault-states>\n   <fault-state family="Family" member="Member" code="1">\n</fault-state>\n   </fault-states>\n</ASI-message>\n',msg.toXML())

if __name__ == '__main__':
    unittest.main()
