//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.03 at 12:13:34 PM CET 
//


package it.sinergis.routingpreferences.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.sinergis.routingpreferences.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RoutingPreferencesDeleteResponse_QNAME = new QName("", "routingPreferencesDeleteResponse");
    private final static QName _RoutingResponseSearchResponse_QNAME = new QName("", "routingResponseSearchResponse");
    private final static QName _RoutingPreferencesInsertResponse_QNAME = new QName("", "routingPreferencesInsertResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.sinergis.routingpreferences.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RoutingPreferencesDeleteResponseType }
     * 
     */
    public RoutingPreferencesDeleteResponseType createRoutingPreferencesDeleteResponseType() {
        return new RoutingPreferencesDeleteResponseType();
    }

    /**
     * Create an instance of {@link RoutingPreferencesSearchRequest }
     * 
     */
    public RoutingPreferencesSearchRequest createRoutingPreferencesSearchRequest() {
        return new RoutingPreferencesSearchRequest();
    }

    /**
     * Create an instance of {@link RoutingPreferencesSearchResponseType }
     * 
     */
    public RoutingPreferencesSearchResponseType createRoutingPreferencesSearchResponseType() {
        return new RoutingPreferencesSearchResponseType();
    }

    /**
     * Create an instance of {@link RoutingPreferencesInsertRequest }
     * 
     */
    public RoutingPreferencesInsertRequest createRoutingPreferencesInsertRequest() {
        return new RoutingPreferencesInsertRequest();
    }

    /**
     * Create an instance of {@link RoutingPreferenceType }
     * 
     */
    public RoutingPreferenceType createRoutingPreferenceType() {
        return new RoutingPreferenceType();
    }

    /**
     * Create an instance of {@link RoutingPreferencesInsertResponseType }
     * 
     */
    public RoutingPreferencesInsertResponseType createRoutingPreferencesInsertResponseType() {
        return new RoutingPreferencesInsertResponseType();
    }

    /**
     * Create an instance of {@link RoutingPreferencesDeleteRequest }
     * 
     */
    public RoutingPreferencesDeleteRequest createRoutingPreferencesDeleteRequest() {
        return new RoutingPreferencesDeleteRequest();
    }

    /**
     * Create an instance of {@link ErrorType }
     * 
     */
    public ErrorType createErrorType() {
        return new ErrorType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoutingPreferencesDeleteResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "routingPreferencesDeleteResponse")
    public JAXBElement<RoutingPreferencesDeleteResponseType> createRoutingPreferencesDeleteResponse(RoutingPreferencesDeleteResponseType value) {
        return new JAXBElement<RoutingPreferencesDeleteResponseType>(_RoutingPreferencesDeleteResponse_QNAME, RoutingPreferencesDeleteResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoutingPreferencesSearchResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "routingResponseSearchResponse")
    public JAXBElement<RoutingPreferencesSearchResponseType> createRoutingResponseSearchResponse(RoutingPreferencesSearchResponseType value) {
        return new JAXBElement<RoutingPreferencesSearchResponseType>(_RoutingResponseSearchResponse_QNAME, RoutingPreferencesSearchResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoutingPreferencesInsertResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "routingPreferencesInsertResponse")
    public JAXBElement<RoutingPreferencesInsertResponseType> createRoutingPreferencesInsertResponse(RoutingPreferencesInsertResponseType value) {
        return new JAXBElement<RoutingPreferencesInsertResponseType>(_RoutingPreferencesInsertResponse_QNAME, RoutingPreferencesInsertResponseType.class, null, value);
    }

}
