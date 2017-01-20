package org.hibernatespatial.postgis;
import java.sql.Types;

import org.hibernatespatial.postgis.PostgisDialect;

/**
 * Wrap default PostgisDialect with 'json' type.
 *
 */
public class JsonPostgisDialect extends PostgisDialect {

    public JsonPostgisDialect() {

        super();

        this.registerColumnType(Types.JAVA_OBJECT, "json");
    }
}