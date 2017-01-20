#!/bin/bash

if [ ! $# -eq 17 ];
  then
    echo "##"
    echo "-info-"
    echo "pathscript.sh <MAPSET> <LOCATION> <PATHDTM> <PATHBUILDING> <EPSG> <PATHDSM> <OFFSET_REGION> <LINKED_VALUE> <ALBEDO> <DAY> <STEP> <DIST> <COEFFBHM> <REGION_NORTH> <REGION_SOUTH> <REGION_WEST> <REGION_EAST>"
    echo "##"
    exit
fi

export GISBASE=/usr/lib/grass70
export GRASS_VERSION="7.0"
 
log="/home/tomct7/grass/output.log"
exec 3>&1 1>>${log} 2>&1

#generate GISRCRC
#MYGISDBASE=/mapset
MYGISDBASE=/home/tomct7/grass/mapset
MYLOC=$2
MYMAPSET=$1
echo "mapset $MYMAPSET"

# Set the global grassrc file to individual file name
MYGISRC="/home/install/.grassrc.$GRASS_VERSION.$$"
export HOME="home/install"
 
echo "GISDBASE: $MYGISDBASE" > "$MYGISRC"
echo "LOCATION_NAME: $MYLOC" >> "$MYGISRC"
echo "MAPSET: $MYMAPSET" >> "$MYGISRC"
echo "GRASS_GUI: text" >> "$MYGISRC"
 
# path to GRASS settings file
export GISRC=$MYGISRC
#export GRASS_PYTHON=python
#export GRASS_MESSAGE_FORMAT=plain
#export GRASS_TRUECOLOR=TRUE
#export GRASS_TRANSPARENT=TRUE
#export GRASS_PNG_AUTO_WRITE=TRUE
#export GRASS_GNUPLOT='gnuplot -persist'
#export GRASS_WIDTH=640
#export GRASS_HEIGHT=480
#export GRASS_HTML_BROWSER=firefox
#export GRASS_PAGER=cat
#export GRASS_WISH=wish
 
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
r.import input=$3 output=dtm_${timestamp}
r.import input=${6} output=dsm_${timestamp}

echo "import building"
#v.import input=$4 output=building_${timestamp} epsg=$5 >> $log
v.in.ogr input=$4 output=building_import_${timestamp} --overwrite -c
v.clean input=building_import_${timestamp} output=building_${timestamp} tool=bpol thres=0.00 type=point,line,area

g.region vect=building_import_${timestamp} align=dtm_${timestamp}

echo "calculate elevation for building"
v.rast.stats raster=dtm_${timestamp}@$1 map=building_${timestamp} column_prefix=elev method=average

echo "vector to raster for elevation"
v.to.rast --overwrite input=building_${timestamp} output=building_${timestamp}_raster_elev@$1 use=attr attribute_column=elev_average

echo "vector to raster for height"
#v.to.rast input=building_$timestamp output=building_${timestamp}_raster_height@$1 use=z >> $log
v.to.rast input=building_${timestamp}@$1 output=building_${timestamp}_raster_height use=attr attribute_column=height_height_val

echo "set region for building for buffer"
g.region raster=building_${timestamp}_raster_height@$1
outstrin=$(g.region -g)
#myarr=(${outstrin// / })

#r_n=${myarr[0]/n=/ }
#r_s=${myarr[1]/s=/ }
#r_w=${myarr[2]/w=/ }
#r_e=${myarr[3]/e=/ }

r_n=${14}
r_s=${15}
r_w=${16}
r_e=${17}

g.region n=${r_n} s=${r_s} w=${r_w} e=${r_e}

echo "mapcalc for put the sum of raster for elevation and raster for height if the building is null"
r.mapcalc --overwrite "RESULT_$timestamp =if( isnull(building_${timestamp}_raster_elev@$1) , dtm_$timestamp@$1 , building_${timestamp}_raster_elev@$1+ building_${timestamp}_raster_height@$1)"

echo "set region dsm" 
g.region raster=dsm_${timestamp}@$1
 
echo "mapcalc for put the calculated raster if is null"
r.mapcalc --overwrite "RESULT_${timestamp}_final = if( isnull( RESULT_$timestamp) , dsm_$timestamp@$1, RESULT_$timestamp)"

#r.out.gdal input=RESULT_${timestamp}_final output=/opt/geoserver_datadir/data/gsc/${timestamp}_result_prima_rsun.tiff format=GTiff

#echo "ooioioi"
#g.region raster=building_${timestamp}_raster_height@$1 >> $log
#outstrin=$(g.region -g)
#myarr=(${outstrin// / })
#echo ${myarr[*]} >> $log

#r_n=${myarr[0]/n=/ }
#r_s=${myarr[1]/s=/ }
#r_w=${myarr[2]/w=/ }
#r_e=${myarr[3]/e=/ }

#r_n=$(bc <<< "$r_n+$7")
#r_s=$(bc <<< "$r_s+$7")
#r_w=$(bc <<< "$r_w+$7")
#r_e=$(bc <<< "$r_e+$7")

#g.region $(((myarr[0]//./),+0)) $(((myarr[1]//./),+0)) $(((myarr[4]//./),+0)) $(((myarr[3]//./),+0))
echo "set region for building with buffer"
g.region n=${r_n} s=${r_s} w=${r_w} e=${r_e}

echo "copy raster for calculate bhm"
g.copy raster=dtm_${timestamp}@$1,rastercopy_${timestamp}

r.mapcalc "resultbhm_${timestamp} = if (isnull(rastercopy_${timestamp}@$1), 0, ${13})"

#r.out.gdal input=resultbhm_${timestamp} output=/opt/geoserver_datadir/data/gsc/${timestamp}_outputBHM.tiff
#r.sun elevation=dsm_${timestamp} horizon_step=1 horizon_basename=dsm_${timestamp}  coeff_bh=dsm_${timestamp} aspect=dsm_${timestamp} slope=dsm_${timestamp} linke_value=$8 albedo_value=$9  day=${10} step=${11} dist=${12} glob_rad=rsun_${timestamp} > /home/tomct7/grass/output.log
seq=${10}
seqarray=(${seq//-/ })
echo "seq $seq"

timeStartRsun="$(date -d 'today' +'%Y/%m/%d %H:%M:%S')"
echo "*****************************************"
echo "*************START RSUN******************"
echo "             $timeStartRsun              "
echo "*****************************************"

for i in `seq ${seqarray[0]} ${seqarray[1]}`;
  do
    echo r.sun elevation=RESULT_${timestamp}_final  coeff_bh=resultbhm_${timestamp} horizon_step=90 aspect=RESULT_${timestamp}_final slope=RESULT_${timestamp}_final linke_value=$8 albedo_value=$9  day=$i step=${11} dist=${12} glob_rad=rsun_${timestamp}_$i
    r.sun elevation=RESULT_${timestamp}_final coeff_bh=resultbhm_${timestamp} horizon_step=90 aspect=RESULT_${timestamp}_final slope=RESULT_${timestamp}_final linke_value=$8 albedo_value=$9  day=$i step=${11} dist=${12} glob_rad=rsun_${timestamp}_$i
    #r.out.gdal input=rsun_${timestamp}_$i output=/opt/geoserver_datadir/data/gsc/${timestamp}_rsun_$i.tiff format=GTiff  
done


timeSumProcess="$(date -d 'today' +'%Y/%m/%d %H:%M:%S')"
echo "*****************************************"
echo "*************START SUM PROCESS***********"
echo "             $timeSumProcess             "
echo "*****************************************"


r.mapcalc --overwrite "RESULT_SUM_${timestamp} = rsun_${timestamp}_${seqarray[0]}"
first=${seqarray[0]}
echo "first $first"
next=$(bc <<< "$first+1")
echo "next $next"

if [ ${seqarray[1]} -gt $next -o $next -eq ${seqarray[1]} ]; then
  for i in `seq $next ${seqarray[1]}`;
    do
      echo "sum for r.sun"
      r.mapcalc --overwrite "RESULT_SUM_${timestamp} = (rsun_${timestamp}_$i + RESULT_SUM_${timestamp})/1000"
    done; 
 fi

timeEndSum="$(date -d 'today' +'%Y/%m/%d %H:%M:%S')"
echo "*****************************************"
echo "*************END SUM PROCESS*************"
echo "             $timeEndSum                 "
echo "*****************************************"


#r.out.gdal input=rsun_${timestamp}@$1 output=/home/install/grass/${timestamp}_output.tiff format=GTiff
#r.out.gdal input=rsun_${timestamp}@$1 output=/tmp/${timestamp}_output.tiff format=GTiff > /home/tomct7/grass/output.log

#r.out.gdal input=rsun_${timestamp}_1 output=/opt/geoserver_datadir/data/gsc/${timestamp}_outputRSUN1.tiff format=GTiff
#r.out.gdal input=rsun_${timestamp}_2 output=/opt/geoserver_datadir/data/gsc/${timestamp}_outputRSUN2.tiff format=GTiff
r.out.gdal input=RESULT_SUM_${timestamp} output=/opt/geoserver_datadir/data/gsc/${timestamp}_outputRSUN.tiff format=GTiff

echo "/opt/geoserver_datadir/data/gsc/${timestamp}_outputRSUN.tiff" 1>&3

g.remove type=raster name=dtm_${timestamp}
g.remove type=raster name=dsm_${timestamp}
g.remove type=raster name=building_${timestamp}_raster_elev
g.remove type=raster name=building_${timestamp}_raster_height
g.remove type=raster name=RESULT_$timestamp,RESULT_${timestamp}_final
g.remove type=raster name=rastercopy_${timestamp}
g.remove type=raster name=RESULT_SUM_${timestamp}
g.remove type=vector name=building_import_${timestamp}

timeEnd="$(date -d 'today' +'%Y/%m/%d %H:%M:%S')"
echo "*****************************************"
echo "*************END PROCESS*****************"
echo "             $timeEnd                    "
echo "*****************************************"

