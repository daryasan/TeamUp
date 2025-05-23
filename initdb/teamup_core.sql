--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2025-05-11 18:53:45

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 42119)
-- Name: core; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA core;


ALTER SCHEMA core OWNER TO postgres;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: users; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA users;


ALTER SCHEMA users OWNER TO pg_database_owner;

--
-- TOC entry 3454 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA users; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA users IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 227 (class 1259 OID 42436)
-- Name: event_participants_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.event_participants_ (
    id integer NOT NULL,
    event_id bigint NOT NULL,
    participant_id bigint NOT NULL
);


ALTER TABLE core.event_participants_ OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 42435)
-- Name: event_participants__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.event_participants__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.event_participants__id_seq OWNER TO postgres;

--
-- TOC entry 3455 (class 0 OID 0)
-- Dependencies: 226
-- Name: event_participants__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.event_participants__id_seq OWNED BY core.event_participants_.id;


--
-- TOC entry 233 (class 1259 OID 42472)
-- Name: event_step_winner_teams_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.event_step_winner_teams_ (
    id integer NOT NULL,
    event_step_id bigint NOT NULL,
    winner_team_id bigint NOT NULL
);


ALTER TABLE core.event_step_winner_teams_ OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 42471)
-- Name: event_step_winner_teams__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.event_step_winner_teams__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.event_step_winner_teams__id_seq OWNER TO postgres;

--
-- TOC entry 3456 (class 0 OID 0)
-- Dependencies: 232
-- Name: event_step_winner_teams__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.event_step_winner_teams__id_seq OWNED BY core.event_step_winner_teams_.id;


--
-- TOC entry 231 (class 1259 OID 42460)
-- Name: event_steps_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.event_steps_ (
    id bigint NOT NULL,
    event_id bigint NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    step_number integer,
    winner_teams bigint[]
);


ALTER TABLE core.event_steps_ OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 42459)
-- Name: event_steps__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.event_steps__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.event_steps__id_seq OWNER TO postgres;

--
-- TOC entry 3457 (class 0 OID 0)
-- Dependencies: 230
-- Name: event_steps__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.event_steps__id_seq OWNED BY core.event_steps_.id;


--
-- TOC entry 225 (class 1259 OID 42420)
-- Name: event_tags; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.event_tags (
    event_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE core.event_tags OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 42448)
-- Name: event_winners_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.event_winners_ (
    id integer NOT NULL,
    event_id bigint NOT NULL,
    winner_id bigint NOT NULL
);


ALTER TABLE core.event_winners_ OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 42447)
-- Name: event_winners__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.event_winners__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.event_winners__id_seq OWNER TO postgres;

--
-- TOC entry 3458 (class 0 OID 0)
-- Dependencies: 228
-- Name: event_winners__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.event_winners__id_seq OWNED BY core.event_winners_.id;


--
-- TOC entry 224 (class 1259 OID 42412)
-- Name: events_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.events_ (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    photo_path character varying(255),
    web_link character varying(255),
    prize_description character varying(255),
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    participants bigint[],
    winners bigint[]
);


ALTER TABLE core.events_ OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 42411)
-- Name: events__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.events__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.events__id_seq OWNER TO postgres;

--
-- TOC entry 3459 (class 0 OID 0)
-- Dependencies: 223
-- Name: events__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.events__id_seq OWNED BY core.events_.id;


--
-- TOC entry 240 (class 1259 OID 42535)
-- Name: meetings_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.meetings_ (
    id bigint NOT NULL,
    team_id bigint,
    link character varying(255),
    start_time timestamp without time zone,
    end_time timestamp without time zone
);


ALTER TABLE core.meetings_ OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 42534)
-- Name: meetings__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.meetings__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.meetings__id_seq OWNER TO postgres;

--
-- TOC entry 3460 (class 0 OID 0)
-- Dependencies: 239
-- Name: meetings__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.meetings__id_seq OWNED BY core.meetings_.id;


--
-- TOC entry 242 (class 1259 OID 42549)
-- Name: queries_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.queries_ (
    id bigint NOT NULL,
    team_id bigint,
    sender_id bigint,
    receiver_id bigint,
    query_status character varying(255)
);


ALTER TABLE core.queries_ OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 42548)
-- Name: queries__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.queries__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.queries__id_seq OWNER TO postgres;

--
-- TOC entry 3461 (class 0 OID 0)
-- Dependencies: 241
-- Name: queries__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.queries__id_seq OWNED BY core.queries_.id;


--
-- TOC entry 222 (class 1259 OID 42403)
-- Name: tags_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.tags_ (
    id bigint NOT NULL,
    author_id bigint,
    name character varying(255)
);


ALTER TABLE core.tags_ OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 42402)
-- Name: tags__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.tags__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.tags__id_seq OWNER TO postgres;

--
-- TOC entry 3462 (class 0 OID 0)
-- Dependencies: 221
-- Name: tags__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.tags__id_seq OWNED BY core.tags_.id;


--
-- TOC entry 238 (class 1259 OID 42523)
-- Name: team_participants_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.team_participants_ (
    id integer NOT NULL,
    team_id bigint NOT NULL,
    participant_id bigint NOT NULL
);


ALTER TABLE core.team_participants_ OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 42522)
-- Name: team_participants__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.team_participants__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.team_participants__id_seq OWNER TO postgres;

--
-- TOC entry 3463 (class 0 OID 0)
-- Dependencies: 237
-- Name: team_participants__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.team_participants__id_seq OWNED BY core.team_participants_.id;


--
-- TOC entry 236 (class 1259 OID 42507)
-- Name: team_tags; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.team_tags (
    team_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE core.team_tags OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 42484)
-- Name: teams_; Type: TABLE; Schema: core; Owner: postgres
--

CREATE TABLE core.teams_ (
    id bigint NOT NULL,
    name character varying(255),
    description character varying(255),
    event_id bigint,
    project_name character varying(255),
    mentor_id bigint,
    leader_id bigint,
    formed boolean,
    has_mentor boolean,
    participants bigint[]
);


ALTER TABLE core.teams_ OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 42483)
-- Name: teams__id_seq; Type: SEQUENCE; Schema: core; Owner: postgres
--

CREATE SEQUENCE core.teams__id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE core.teams__id_seq OWNER TO postgres;

--
-- TOC entry 3464 (class 0 OID 0)
-- Dependencies: 234
-- Name: teams__id_seq; Type: SEQUENCE OWNED BY; Schema: core; Owner: postgres
--

ALTER SEQUENCE core.teams__id_seq OWNED BY core.teams_.id;


--
-- TOC entry 220 (class 1259 OID 41976)
-- Name: reviews_; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.reviews_ (
    sender_id bigint NOT NULL,
    receiver_id bigint NOT NULL,
    review_text character varying(255),
    rate integer NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE users.reviews_ OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 42795)
-- Name: reviews__id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

ALTER TABLE users.reviews_ ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME users.reviews__id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 218 (class 1259 OID 41944)
-- Name: role_; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.role_ (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE users.role_ OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 41947)
-- Name: role__id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

ALTER TABLE users.role_ ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME users.role__id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 217 (class 1259 OID 41939)
-- Name: session_; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.session_ (
    user_id bigint NOT NULL,
    token text NOT NULL,
    expires timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE users.session_ OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 42799)
-- Name: session__id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

ALTER TABLE users.session_ ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME users.session__id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 215 (class 1259 OID 41932)
-- Name: user_; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.user_ (
    id bigint NOT NULL,
    nickname character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    contacts character varying(255),
    github character varying(255),
    experience character varying(255),
    description character varying(255),
    role_id integer,
    first_name character varying(255),
    image character varying(255),
    last_name character varying(255),
    middle_name character varying(255),
    tags bigint[]
);


ALTER TABLE users.user_ OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 41935)
-- Name: user__id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

ALTER TABLE users.user_ ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME users.user__id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 243 (class 1259 OID 42782)
-- Name: users_tags_; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.users_tags_ (
    user_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE users.users_tags_ OWNER TO postgres;

--
-- TOC entry 3253 (class 2604 OID 42439)
-- Name: event_participants_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_participants_ ALTER COLUMN id SET DEFAULT nextval('core.event_participants__id_seq'::regclass);


--
-- TOC entry 3256 (class 2604 OID 42475)
-- Name: event_step_winner_teams_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_step_winner_teams_ ALTER COLUMN id SET DEFAULT nextval('core.event_step_winner_teams__id_seq'::regclass);


--
-- TOC entry 3255 (class 2604 OID 42563)
-- Name: event_steps_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_steps_ ALTER COLUMN id SET DEFAULT nextval('core.event_steps__id_seq'::regclass);


--
-- TOC entry 3254 (class 2604 OID 42451)
-- Name: event_winners_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_winners_ ALTER COLUMN id SET DEFAULT nextval('core.event_winners__id_seq'::regclass);


--
-- TOC entry 3252 (class 2604 OID 42577)
-- Name: events_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.events_ ALTER COLUMN id SET DEFAULT nextval('core.events__id_seq'::regclass);


--
-- TOC entry 3259 (class 2604 OID 42641)
-- Name: meetings_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.meetings_ ALTER COLUMN id SET DEFAULT nextval('core.meetings__id_seq'::regclass);


--
-- TOC entry 3260 (class 2604 OID 42654)
-- Name: queries_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.queries_ ALTER COLUMN id SET DEFAULT nextval('core.queries__id_seq'::regclass);


--
-- TOC entry 3251 (class 2604 OID 42667)
-- Name: tags_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.tags_ ALTER COLUMN id SET DEFAULT nextval('core.tags__id_seq'::regclass);


--
-- TOC entry 3258 (class 2604 OID 42526)
-- Name: team_participants_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.team_participants_ ALTER COLUMN id SET DEFAULT nextval('core.team_participants__id_seq'::regclass);


--
-- TOC entry 3257 (class 2604 OID 42690)
-- Name: teams_ id; Type: DEFAULT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.teams_ ALTER COLUMN id SET DEFAULT nextval('core.teams__id_seq'::regclass);


--
-- TOC entry 3272 (class 2606 OID 42441)
-- Name: event_participants_ event_participants__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_participants_
    ADD CONSTRAINT event_participants__pkey PRIMARY KEY (id);


--
-- TOC entry 3278 (class 2606 OID 42477)
-- Name: event_step_winner_teams_ event_step_winner_teams__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_step_winner_teams_
    ADD CONSTRAINT event_step_winner_teams__pkey PRIMARY KEY (id);


--
-- TOC entry 3276 (class 2606 OID 42565)
-- Name: event_steps_ event_steps__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_steps_
    ADD CONSTRAINT event_steps__pkey PRIMARY KEY (id);


--
-- TOC entry 3270 (class 2606 OID 42424)
-- Name: event_tags event_tags_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_tags
    ADD CONSTRAINT event_tags_pkey PRIMARY KEY (event_id, tag_id);


--
-- TOC entry 3274 (class 2606 OID 42453)
-- Name: event_winners_ event_winners__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_winners_
    ADD CONSTRAINT event_winners__pkey PRIMARY KEY (id);


--
-- TOC entry 3268 (class 2606 OID 42579)
-- Name: events_ events__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.events_
    ADD CONSTRAINT events__pkey PRIMARY KEY (id);


--
-- TOC entry 3286 (class 2606 OID 42643)
-- Name: meetings_ meetings__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.meetings_
    ADD CONSTRAINT meetings__pkey PRIMARY KEY (id);


--
-- TOC entry 3288 (class 2606 OID 42656)
-- Name: queries_ queries__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.queries_
    ADD CONSTRAINT queries__pkey PRIMARY KEY (id);


--
-- TOC entry 3266 (class 2606 OID 42669)
-- Name: tags_ tags__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.tags_
    ADD CONSTRAINT tags__pkey PRIMARY KEY (id);


--
-- TOC entry 3284 (class 2606 OID 42528)
-- Name: team_participants_ team_participants__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.team_participants_
    ADD CONSTRAINT team_participants__pkey PRIMARY KEY (id);


--
-- TOC entry 3282 (class 2606 OID 42511)
-- Name: team_tags team_tags_pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.team_tags
    ADD CONSTRAINT team_tags_pkey PRIMARY KEY (team_id, tag_id);


--
-- TOC entry 3280 (class 2606 OID 42692)
-- Name: teams_ teams__pkey; Type: CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.teams_
    ADD CONSTRAINT teams__pkey PRIMARY KEY (id);


--
-- TOC entry 3264 (class 2606 OID 41954)
-- Name: role_ id; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.role_
    ADD CONSTRAINT id PRIMARY KEY (id);


--
-- TOC entry 3262 (class 2606 OID 41956)
-- Name: user_ user_id; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.user_
    ADD CONSTRAINT user_id PRIMARY KEY (id);


--
-- TOC entry 3291 (class 2606 OID 42580)
-- Name: event_tags fk_event; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_tags
    ADD CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES core.events_(id);


--
-- TOC entry 3293 (class 2606 OID 42585)
-- Name: event_participants_ fk_event_participant; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_participants_
    ADD CONSTRAINT fk_event_participant FOREIGN KEY (event_id) REFERENCES core.events_(id);


--
-- TOC entry 3296 (class 2606 OID 42566)
-- Name: event_step_winner_teams_ fk_event_step; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_step_winner_teams_
    ADD CONSTRAINT fk_event_step FOREIGN KEY (event_step_id) REFERENCES core.event_steps_(id);


--
-- TOC entry 3295 (class 2606 OID 42595)
-- Name: event_steps_ fk_event_step_event; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_steps_
    ADD CONSTRAINT fk_event_step_event FOREIGN KEY (event_id) REFERENCES core.events_(id);


--
-- TOC entry 3294 (class 2606 OID 42590)
-- Name: event_winners_ fk_event_winner; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_winners_
    ADD CONSTRAINT fk_event_winner FOREIGN KEY (event_id) REFERENCES core.events_(id);


--
-- TOC entry 3303 (class 2606 OID 42703)
-- Name: meetings_ fk_meeting_team; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.meetings_
    ADD CONSTRAINT fk_meeting_team FOREIGN KEY (team_id) REFERENCES core.teams_(id);


--
-- TOC entry 3304 (class 2606 OID 42708)
-- Name: queries_ fk_query_team; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.queries_
    ADD CONSTRAINT fk_query_team FOREIGN KEY (team_id) REFERENCES core.teams_(id);


--
-- TOC entry 3292 (class 2606 OID 42670)
-- Name: event_tags fk_tag; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.event_tags
    ADD CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES core.tags_(id);


--
-- TOC entry 3300 (class 2606 OID 42675)
-- Name: team_tags fk_tag_team; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.team_tags
    ADD CONSTRAINT fk_tag_team FOREIGN KEY (tag_id) REFERENCES core.tags_(id);


--
-- TOC entry 3301 (class 2606 OID 42693)
-- Name: team_tags fk_team; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.team_tags
    ADD CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES core.teams_(id);


--
-- TOC entry 3297 (class 2606 OID 42600)
-- Name: teams_ fk_team_event; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.teams_
    ADD CONSTRAINT fk_team_event FOREIGN KEY (event_id) REFERENCES core.events_(id);


--
-- TOC entry 3298 (class 2606 OID 42502)
-- Name: teams_ fk_team_leader; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.teams_
    ADD CONSTRAINT fk_team_leader FOREIGN KEY (leader_id) REFERENCES users.user_(id);


--
-- TOC entry 3299 (class 2606 OID 42497)
-- Name: teams_ fk_team_mentor; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.teams_
    ADD CONSTRAINT fk_team_mentor FOREIGN KEY (mentor_id) REFERENCES users.user_(id);


--
-- TOC entry 3302 (class 2606 OID 42698)
-- Name: team_participants_ fk_team_participant; Type: FK CONSTRAINT; Schema: core; Owner: postgres
--

ALTER TABLE ONLY core.team_participants_
    ADD CONSTRAINT fk_team_participant FOREIGN KEY (team_id) REFERENCES core.teams_(id);


--
-- TOC entry 3289 (class 2606 OID 42771)
-- Name: user_ role_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.user_
    ADD CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES users.role_(id) NOT VALID;


--
-- TOC entry 3305 (class 2606 OID 42790)
-- Name: users_tags_ tag_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.users_tags_
    ADD CONSTRAINT tag_id FOREIGN KEY (tag_id) REFERENCES core.tags_(id);


--
-- TOC entry 3290 (class 2606 OID 41970)
-- Name: session_ user_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.session_
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES users.user_(id) NOT VALID;


--
-- TOC entry 3306 (class 2606 OID 42785)
-- Name: users_tags_ user_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.users_tags_
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES users.user_(id);


--
-- TOC entry 3452 (class 0 OID 41944)
-- Dependencies: 218
-- Data for Name: role_; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.role_ (id, name) FROM stdin;
1	participant
2	mentor
3	organizer
\.

-- Completed on 2025-05-11 18:53:45

--
-- PostgreSQL database dump complete
--

