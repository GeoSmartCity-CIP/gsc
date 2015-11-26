package it.sinergis.routingpreferences.common;

/**
 * Classe che contiene tutte le costanti del progetto.
 * 
 * @author Andrea Di Nora
 */
public final class Constants {
    /** Nome della persistence unit usata nelle classi JPA. */
    public static final String PERSISTENCE_UNIT = "routing-preferences";
    
    /** CODICI DI ERRORE **/
    public static final String RP001_ERR_CODE = "RP001";
    public static final String RP002_ERR_CODE = "RP002";
    public static final String RP003_ERR_CODE = "RP003";
    public static final String RP004_ERR_CODE = "RP004";
    public static final String RP005_ERR_CODE = "RP005";
    public static final String RP006_ERR_CODE = "RP006";
    
        
    /** MESSAGGI DI ERRORE **/
    public static final String RP001_ERR_MSG ="Routing preference insert error.";    
    public static final String RP002_ERR_MSG ="XML not valid.";
    public static final String RP003_ERR_MSG ="Routing preference search error.";
    public static final String RP004_ERR_MSG ="Routing preference delete error.";
    public static final String RP005_ERR_MSG ="No routing preference founded";
    public static final String RP006_ERR_MSG ="File di properties non esistente.";
}
