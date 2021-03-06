# -*- Makefile -*-

# platform_sunos5_g++.GNU,v 4.58 2004/03/03 00:17:37 dhinton Exp

# SunOS 5.x (Solaris 2.x) with g++

# NOTE: with g++ 2.8.0, you'll need to disable optimization in order to
#       instantiate ACE_Map_Manager (ace/Map_Manager.cpp).  The easiest
#       way to do that is to set "optimize = 0", either below or on
#       your make command line.

# NOTE: On Solaris86, you'll need to use GNU as instead of /usr/ccs/bin/as,
#       if you want -gstabs+ and -pipe support.
exceptions = 1

ifeq (,$(debug))
  debug = 1
endif
ifeq (,$(optimize))
  optimize = 1
endif
ifeq (,$(threads))
  threads = 1
endif

ifeq (,$(exceptions))
exceptions = 0
endif
# if we are not working with old gcc ...
ifeq ($(ACE_HAS_GNUG_PRE_2_8),0)
ifeq ($(exceptions),0)
CFLAGS += -fno-exceptions
endif # exceptions
endif # ! ACE_HAS_GNUG_PRE_2_8

ifeq ($(shell /bin/uname -m),i86pc)
  #### gcc on Solaris86 doesn't use -g
  DCFLAGS         += -gstabs+
else  # ! i86pc
  DCFLAGS         += -g
endif # ! i86pc

pipes ?= 1

CC              = gcc
CXX             = g++
CFLAGS          += -W -Wall -Wpointer-arith #### -Winline

ifeq ($(threads),1)
  CPPFLAGS      += -D_REENTRANT
endif

DLD             = $(CXX)
LD              = $(CXX)
LDFLAGS         +=
LIBS            += -lsocket -ldl -lgen -lnsl -lposix4 -lthread
OCFLAGS         += -O3
PIC             = -fPIC
AR              = ar
ARFLAGS         = ruv
RANLIB          = @true
SOFLAGS         += -G $(CPPFLAGS)
SOBUILD         = $(COMPILE.cc) $(PIC) -o $(VSHDIR)$*.so $<
PRELIB          = @true

# Test for template instantiation, add to SOFLAGS if SONAME set, 
# add -E to LDFLAGS if using GNU ld
#
include $(ACE_ROOT)/include/makeinclude/platform_g++_common.GNU

CCFLAGS         += $(CFLAGS) $(TEMPLATES_FLAG)

PLATFORM_X11_CPPFLAGS= -I/usr/openwin/include -I/usr/dt/include
PLATFORM_X11_LIBS =-lX11
PLATFORM_X11_LDFLAGS=

## Unfortunately, gcc 3.0 fails to many cpp files with
## optimization enabled.  Even -O causes the compiler
## to core dump on some files.
ifeq ($(shell $(CXX) --version),3.0)
  override optimize = 0
endif
