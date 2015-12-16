CREATE SEQUENCE rp_t_preferences_id_seq;
ALTER SEQUENCE routingpreferences.rp_t_preferences_id_seq
  OWNER TO routingpreferences;
CREATE SEQUENCE routingpreferences.rp_t_itineraries_id_seq;
ALTER SEQUENCE routingpreferences.rp_t_itineraries_id_seq
  OWNER TO routingpreferences;


/**
CREATE TABLE rp_t_preferences
(
  id integer NOT NULL default nextval('rp_t_preferences_id_seq'),
  userID character varying(15) NOT NULL,  
  maxWalkDistance integer NOT NULL,
  walkingSpeed integer NOT NULL,
  maxBikeDistance integer NOT NULL,
  bikingSpeed integer NOT NULL,
  CONSTRAINT rp_t_preferences_pk PRIMARY KEY (id)
);*/

CREATE TABLE routingpreferences.rp_t_preferences2
(
  data json
)

CREATE TABLE routingpreferences.rp_t_itineraries2
(
  id integer NOT NULL DEFAULT nextval('routingpreferences.rp_t_itineraries_id_seq'::regclass),
  data  json NOT NULL,
  CONSTRAINT rp_t_itineraries_pk PRIMARY KEY (id)
);
ALTER TABLE routingpreferences.rp_t_itineraries2
  OWNER TO routingpreferences;