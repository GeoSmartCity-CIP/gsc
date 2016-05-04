package eu.geosmartcity.hub.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.CityGMLBuilder;
import org.citygml4j.model.citygml.CityGML;
import org.citygml4j.model.citygml.CityGMLClass;
import org.citygml4j.model.citygml.building.AbstractBoundarySurface;
import org.citygml4j.model.citygml.core.AbstractCityObject;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.citygml.core.CityObjectMember;
import org.citygml4j.model.gml.geometry.aggregates.MultiSurface;
import org.citygml4j.model.gml.geometry.aggregates.MultiSurfaceProperty;
import org.citygml4j.model.gml.geometry.primitives.AbstractRingProperty;
import org.citygml4j.model.gml.geometry.primitives.AbstractSurface;
import org.citygml4j.model.gml.geometry.primitives.LinearRing;
import org.citygml4j.model.gml.geometry.primitives.Polygon;
import org.citygml4j.model.gml.geometry.primitives.PosOrPointPropertyOrPointRep;
import org.citygml4j.model.gml.geometry.primitives.SurfaceProperty;
import org.citygml4j.util.walker.FeatureWalker;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReadException;
import org.citygml4j.xml.io.reader.CityGMLReader;
import org.hibernate.Session;
import org.hibernate.Transaction;

import eu.geosmartcity.hub.delegate.PostgisDelegate;
import eu.geosmartcity.hub.utils.Functions;
import eu.geosmartcity.hub.utils.HibernateUtil;
import eu.geosmartcity.hub.velocity.SetupInsertSensorTemplate;

public class CityGmlParser {
	final static Logger LOGGER = Logger.getLogger(CityGmlParser.class);
	
	public void doParse(String path, String epsg, String timestampParam) throws JAXBException, CityGMLReadException {
		CityGMLContext ctx = new CityGMLContext();
		CityGMLBuilder builder = ctx.createCityGMLBuilder();
		
		CityGMLInputFactory in = builder.createCityGMLInputFactory();
		CityGMLReader readerCGML = in.createCityGMLReader(new File(path));
		final PostgisDelegate pg = new PostgisDelegate();
		final SetupInsertSensorTemplate template = new SetupInsertSensorTemplate();
		final String timestamp = timestampParam;
		final String epsgCityGml = epsg;
		
		while (readerCGML.hasNext()) {
			CityGML citygml = readerCGML.nextFeature();
			LOGGER.debug("Found class:" + citygml.getCityGMLClass() + "\nVersion"
					+ citygml.getCityGMLModule().getVersion());
			
			//Counting the no of buildings
			CityModel citymodel = new CityModel();
			if (citygml.getCityGMLClass() == CityGMLClass.CITY_MODEL) {
				citymodel = (CityModel) citygml;
				// Counting the no of buildings
				int count = 0;
				for (CityObjectMember cityObjectMember : citymodel.getCityObjectMember()) {
					AbstractCityObject cityobject = cityObjectMember.getCityObject();
					if (cityobject.getCityGMLClass() == CityGMLClass.BUILDING) {
						++count;
					}
				}
				LOGGER.debug("Building count" + count);
			}
			
			FeatureWalker walker = new FeatureWalker() {
				@Override
				public void visit(AbstractBoundarySurface boundarySurface) {
					Session session = HibernateUtil.getSessionFactory().getCurrentSession();
					Transaction tx = session.beginTransaction();
					MultiSurfaceProperty lod2MultiSurface = boundarySurface.getLod2MultiSurface();
					if (lod2MultiSurface != null) {
						MultiSurface multiSurface = lod2MultiSurface.getMultiSurface();
						if (multiSurface == null) {
							// Do something!
							
						}
						List<SurfaceProperty> surfaceMember = multiSurface.getSurfaceMember();
						for (SurfaceProperty surfaceProperty : surfaceMember) {
							AbstractSurface abstractSurface = surfaceProperty.getObject();
							if (abstractSurface instanceof Polygon) {
								Polygon polygon = (Polygon) abstractSurface;
								
								LOGGER.info("ESTERNO");
								List<String> coord = new ArrayList<String>();
								AbstractRingProperty pp = polygon.getExterior();
								LinearRing linearRing = (LinearRing) pp.getObject();
								List<PosOrPointPropertyOrPointRep> pppList = null;
								if (linearRing.isSetPosOrPointPropertyOrPointRep()) {
									pppList = linearRing.getPosOrPointPropertyOrPointRep();
									for (PosOrPointPropertyOrPointRep elemInt : pppList) {
										List<Double> values = elemInt.getPos().getValue();
										String tupla = "";
										for (int i = 0; i < values.size(); i++) {
											if (i == values.size() - 1) {
												tupla = tupla + values.get(i);
											}
											else {
												tupla = tupla + values.get(i) + " ";
												
											}
										}
										coord.add(tupla);
										LOGGER.info(elemInt.getPos().getValue());
										
									}
									
								}
								
								String polygonz = Functions.createPolygonZFromCoordinates(coord);
								String sql = template.getTemplateInsertMultiPolygonFromPolygon(polygonz, epsgCityGml,
										timestamp);
								pg.insertGeometry(sql, tx, session);
								LOGGER.info(polygonz);
								
							}
							// Check for other subtypes of AbstractSurface
						}
					}
					tx.commit();
					// Process LOD3 and LOD4
					super.visit(boundarySurface);
				}
			};
			citymodel.accept(walker);
			
		}
	}
}
