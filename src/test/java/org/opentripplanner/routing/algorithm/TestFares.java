/* This program is free software: you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License
 as published by the Free Software Foundation, either version 3 of
 the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package org.opentripplanner.routing.algorithm;

import junit.framework.TestCase;
import org.onebusaway.gtfs.model.calendar.CalendarServiceData;
import org.opentripplanner.ConstantsForTests;
import org.opentripplanner.gtfs.GtfsContext;
import org.opentripplanner.gtfs.GtfsLibrary;
import org.opentripplanner.routing.core.Fare;
import org.opentripplanner.routing.core.Fare.FareType;
import org.opentripplanner.routing.core.Money;
import org.opentripplanner.routing.core.RoutingRequest;
import org.opentripplanner.routing.core.WrappedCurrency;
import org.opentripplanner.routing.edgetype.factory.GTFSPatternHopFactory;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.services.FareService;
import org.opentripplanner.routing.spt.GraphPath;
import org.opentripplanner.routing.spt.ShortestPathTree;
import org.opentripplanner.util.TestUtils;

import java.io.File;

public class TestFares extends TestCase {

    private AStar aStar = new AStar();
    
    public void testBasic() throws Exception {

        Graph gg = new Graph();
        GtfsContext context = GtfsLibrary.readGtfs(new File(ConstantsForTests.CALTRAIN_GTFS));
        GTFSPatternHopFactory factory = new GTFSPatternHopFactory(context);
        factory.run(gg);
        gg.putService(CalendarServiceData.class, GtfsLibrary.createCalendarServiceData(context.getDao()));
        RoutingRequest options = new RoutingRequest();
        long startTime = TestUtils.dateInSeconds("America/Los_Angeles", 2009, 8, 7, 12, 0, 0);
        options.dateTime = startTime;
        options.setRoutingContext(gg, "Caltrain:Millbrae Caltrain", "Caltrain:Mountain View Caltrain");
        ShortestPathTree spt;
        GraphPath path = null;
        spt = aStar.getShortestPathTree(options);

        path = spt.getPath(gg.getVertex("Caltrain:Mountain View Caltrain"), true);

        FareService fareService = gg.getService(FareService.class);
        
        Fare cost = fareService.getCost(path);
        assertEquals(cost.getFare(FareType.regular), new Money(new WrappedCurrency("USD"), 425));
    }

    public void testPortland() throws Exception {

        Graph gg = ConstantsForTests.getInstance().getPortlandGraph();
        RoutingRequest options = new RoutingRequest();
        ShortestPathTree spt;
        GraphPath path = null;
        long startTime = TestUtils.dateInSeconds("America/Los_Angeles", 2009, 11, 1, 12, 0, 0);
        options.dateTime = startTime;
        options.setRoutingContext(gg, "TriMet:10579", "TriMet:8371");
        // from zone 3 to zone 2
        spt = aStar.getShortestPathTree(options);

        path = spt.getPath(gg.getVertex("TriMet:8371"), true);
        assertNotNull(path);

        FareService fareService = gg.getService(FareService.class);
        Fare cost = fareService.getCost(path);
        assertEquals(new Money(new WrappedCurrency("USD"), 200), cost.getFare(FareType.regular));

        // long trip

        startTime = TestUtils.dateInSeconds("America/Los_Angeles", 2009, 11, 1, 14, 0, 0);
        options.dateTime = startTime;
        options.setRoutingContext(gg, "TriMet:8389", "TriMet:1252");
        spt = aStar.getShortestPathTree(options);

        path = spt.getPath(gg.getVertex("TriMet:1252"), true);
        assertNotNull(path);
        cost = fareService.getCost(path);
        
        //assertEquals(cost.getFare(FareType.regular), new Money(new WrappedCurrency("USD"), 460));
        
        // complex trip
        options.maxTransfers = 5;
        startTime = TestUtils.dateInSeconds("America/Los_Angeles", 2009, 11, 1, 14, 0, 0);
        options.dateTime = startTime;
        options.setRoutingContext(gg, "TriMet:10428", "TriMet:4231");
        spt = aStar.getShortestPathTree(options);

        path = spt.getPath(gg.getVertex("TriMet:4231"), true);
        assertNotNull(path);
        cost = fareService.getCost(path);
        //
        // this is commented out because portland's fares are, I think, broken in the gtfs. see
        // thread on gtfs-changes.
        // assertEquals(cost.getFare(FareType.regular), new Money(new WrappedCurrency("USD"), 430));
    }
}
