#!/bin/bash
#sgen="path/to/schemagen"
if [ "x" == "x$sgen" ]; then
	echo "path to schemagen without quotes or similiar stuff?"
	read sgen
fi
if [ -d "$sgen" ]; then
	sgen="$sgen/schemagen"
fi
if [ ! -x "$sgen" ]; then
	echo "file under $sgen not executeable"
	exit
fi

if [ -e sif ]; then
	clean=false
else 
	clean=true
fi

"$sgen" -cp ../../../../bin/ -d . ../inspection/InspectionRequest.java 
mv Schema1.xsd Report.xsd
"$sgen" -cp ../../../../bin/ -d . ../policy/PolicyList.java
mv Schema1.xsd Request.xsd

if [ clean ]; then
	# doing it a bit more complicated so we don't accidently remove source files
	find sif -name "*.class" -delete
	find sif -depth -type d  -exec rmdir {} +
fi