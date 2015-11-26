CREATE SEQUENCE rp_t_preferences_id_seq;

CREATE TABLE rp_t_preferences
(
  id integer NOT NULL default nextval('rp_t_preferences_id_seq'),
  userID character varying(15) NOT NULL,  
  maxWalkDistance integer NOT NULL,
  walkingSpeed integer NOT NULL,
  maxBikeDistance integer NOT NULL,
  bikingSpeed integer NOT NULL,
  CONSTRAINT rp_t_preferences_pk PRIMARY KEY (id)
);