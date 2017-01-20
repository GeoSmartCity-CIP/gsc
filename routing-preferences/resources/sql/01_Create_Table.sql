CREATE SEQUENCE rp_t_preferences_id_seq;
ALTER SEQUENCE routingpreferences.rp_t_preferences_id_seq
  OWNER TO routingpreferences;
CREATE SEQUENCE routingpreferences.rp_t_itineraries_id_seq;
ALTER SEQUENCE routingpreferences.rp_t_itineraries_id_seq
  OWNER TO routingpreferences;

CREATE TABLE routingpreferences.rp_t_preferences
(
  data json
);
ALTER TABLE routingpreferences.rp_t_preferences
  OWNER TO routingpreferences;
  
CREATE TABLE routingpreferences.rp_t_itineraries
(
  id integer NOT NULL DEFAULT nextval('routingpreferences.rp_t_itineraries_id_seq'::regclass),
  data  json NOT NULL,
  CONSTRAINT rp_t_itineraries_pk PRIMARY KEY (id)
);
ALTER TABLE routingpreferences.rp_t_itineraries
  OWNER TO routingpreferences;