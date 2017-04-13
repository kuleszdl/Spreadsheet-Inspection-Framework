Spreadsheet Inspection Framework SIFCore
========================================
Build Status: [![Build Status](https://travis-ci.org/kuleszdl/Spreadsheet-Inspection-Framework.svg?branch=master)](https://travis-ci.org/kuleszdl/Spreadsheet-Inspection-Framework)

SIFCore is a RESTful web service implemented in Java 8 for checking spreadsheets against definable policy rules.
It is meant to be used in combination with [SIFEI](https://github.com/kuleszdl/SIFEI) which is a Microsoft Excel AddIn.

It was initially created by Sebastian Zitzelsberger and later extended by Manuel Lemcke, Ehssan Doust, Jonas Scheurich, Wolfgang Kraus, Sebastian Beck and Fabian Toth and reengineered by Frieder Schüler as part of diploma and bachelor theses. See the "Theses" section below for more details.

License
-------
Spreadsheet Inspection Framework is avaible under the GNU General Public License Version 3.
For details see file *LICENSE.txt.*

Prerequisites
-------------
* Java 8 JVM (tested on Linux and Windows 7,8, 10)
* Internet connection (to fetch dependencies with gradle)

Download
--------
```Shell
git clone https://github.com/kuleszdl/SIFEI.git
```

Build
-----
Windows:
```Shell
gradlew.bat build
```

Linux, MacOS
```Shell
./gradlew build
```

Gradle will download all needed dependencies, build SIFCore and run all included tests.
Be sure that you set JAVA_HOME environment variable to a valid JDK.

Run
---

Windows:
```Shell
gradlew.bat run
```

Linux, MacOS
```Shell
./gradlew run
```

Alternatively you can run the "build/libs/Spreadsheet-Inspection-Framework-all.jar" jar which contains all dependencies.

```Shell
java -jar build/libs/SIF-experimental-all.jar
```

Development quickstart guide
----------------------------

You can find a [development quickstart guide](https://github.com/kuleszdl/Spreadsheet-Inspection-Framework/tree/master/doc/README.md) written in German in the "/doc" folder.

Theses
---------------------------
You can find the diploma and bachelor theses (all in German) connected to this project here:

<a href="http://elib.uni-stuttgart.de/opus/volltexte/2012/7056/">Sebastian Zitzelsberger: Error Detection in Spreadsheets</a>

<a href="http://elib.uni-stuttgart.de/opus/volltexte/2013/8722/">Manuel Lemcke: Dynamic spreadsheet inspection</a>

<a href="http://elib.uni-stuttgart.de/opus/volltexte/2014/9209/">Ehssan Doust: Visualisation of Spreadsheet Errors</a>

<a href="http://elib.uni-stuttgart.de/opus/volltexte/2014/9207/">Jonas Scheurich: User Interface for a Spreadsheet Test Facility</a>

<a href="http://elib.uni-stuttgart.de/opus/volltexte/2014/9340/">Sebastian Beck: Spreadsheet Error Patterns</a>

<a href="http://elib.uni-stuttgart.de/opus/volltexte/2014/9463/">Wolfgang Kraus: Plausibility check of implicitely coupled spreadsheet data</a>

Fabian Toth: Live inspection of spreadsheets during use (not available yet)

Frieder Schüler: Architecture analysis and re-engineering of an inspection environment for spreadsheets (not available yet)

Bugs and Suggestions
---------------------------
SIFCore and its Microsoft Excel integration component SIFEI use a combined Issue tracker which can be found here: https://github.com/kuleszdl/SIFEI/issues