#!/bin/bash


if [ ! $# -eq 6 ]&& [ ! $# -eq 7 ];
  then
    echo "##"
    echo "-info-"
    echo "pathscript.sh <MAPSET> <LOCATION> <PATHDTM> <PATHBUILDING> <EPSG> <YEAR>"
    echo "##"
    exit
fi

export GISBASE=/usr/lib/grass70
export GRASS_VERSION="7.0"
 
log="/home/tomct7/grass/output_zero_balance.log"
exec 3>&1 1>>${log} 2>&1

#generate GISRCRC
#MYGISDBASE=/mapset
MYGISDBASE=/home/tomct7/grass/mapset
MYLOC=$2
MYMAPSET=$1
echo "mapset $MYMAPSET"

# Set the global grassrc file to individual file name
MYGISRC="$HOME/.grassrc.$GRASS_VERSION.$$"
 
echo "GISDBASE: $MYGISDBASE" > "$MYGISRC"
echo "LOCATION_NAME: $MYLOC" >> "$MYGISRC"
echo "MAPSET: $MYMAPSET" >> "$MYGISRC"
echo "GRASS_GUI: text" >> "$MYGISRC"
 
# path to GRASS settings file
export GISRC=$MYGISRC
 
export PATH="$GISBASE/bin:$GISBASE/scripts:$PATH"
export LD_LIBRARY_PATH="$GISBASE/lib"
export GRASS_LD_LIBRARY_PATH="$LD_LIBRARY_PATH"
export PYTHONPATH="$GISBASE/etc/python:$PYTHONPATH"
export MANPATH=$MANPATH:$GISBASE/man
#g.mapsets addmapset=Vmodel
#g.mapset -c epsg:3763 $1
#exit

#echo "dopo export"
g.proj -d epsg=$5
timestamp="$(date -d 'today' +'%Y%m%d%H%M%S')"
timeStart="$(date -d 'today' +'%Y/%m/%d %H:%M:%S')"
echo "*****************************************"
echo "***************START*********************"
echo "             $timeStart                  "
echo "*****************************************"

if [ $3 != "null" ]
    then
	r.import input=$3 output=solar_${timestamp}
fi

echo "import building"
v.in.ogr input=$4 output=zero_balance_building_${timestamp} --overwrite -c
#v.clean input=building_import_${timestamp} output=building_${timestamp} tool=bpol thres=0.00 type=point,line,area

g.region vect=zero_balance_building_${timestamp} align=solar_${timestamp}

if [ $3 != "null" ]
    then
	echo "calculate avg for building"
	v.rast.stats raster=solar_${timestamp} map=zero_balance_building_${timestamp} column_prefix=z method=average

	echo "rename column"
	v.db.renamecolumn map=zero_balance_building_${timestamp} column=z_average,sp
fi

echo "add column at building"
v.db.addcolumn map=zero_balance_building_${timestamp} columns="zb double"
v.info -c zero_balance_building_${timestamp}

echo "update column zero_balance with difference"
v.db.update map=zero_balance_building_${timestamp} layer=1 column=zb query_column=sp-$6

timeEnd="$(date -d 'today' +'%Y/%m/%d %H:%M:%S')"

echo "*****************************************"
echo "*************END ZERO_BALANCE PROCESS****"
echo "             $timeEnd                    "
echo "*****************************************"


v.out.ogr input=zero_balance_building_${timestamp} output=/opt/geoserver_datadir/data/gsc/zero_balance_building_${timestamp} format=ESRI_Shapefile

echo "/opt/geoserver_datadir/data/gsc/zero_balance_building_${timestamp}.zip" 1>&3

g.remove -f type=raster name=solar_${timestamp}
cd /opt/geoserver_datadir/data/gsc/zero_balance_building_${timestamp}
zip zero_balance_building_${timestamp}.zip *
mv zero_balance_building_${timestamp}.zip ..
cd ..

rm -rf zero_balance_building_${timestamp}


timeEnd="$(date -d 'today' +'%Y/%m/%d %H:%M:%S')"
echo "*****************************************"
echo "*************END PROCESS*****************"
echo "             $timeEnd                    "
echo "*****************************************"
