--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2025-04-24 13:02:41

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 41976)
-- Name: reviews_; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reviews_ (
    id bigint NOT NULL,
    sender_id bigint NOT NULL,
    receiver_id bigint NOT NULL,
    review_text character varying(255),
    rate integer NOT NULL
);


ALTER TABLE public.reviews_ OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 41944)
-- Name: role_; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role_ (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.role_ OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 41947)
-- Name: role__id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.role_ ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.role__id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 216 (class 1259 OID 41939)
-- Name: session_; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.session_ (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    token text NOT NULL,
    expires timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.session_ OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 41962)
-- Name: session__id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.session_ ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.session__id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 214 (class 1259 OID 41932)
-- Name: user_; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_ (
    id bigint NOT NULL,
    nickname character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    contacts character varying(255),
    github character varying(255),
    experience character varying(255),
    description character varying(255),
    role_id bigint,
    first_name character varying(255),
    image character varying(255),
    last_name character varying(255),
    middle_name character varying(255),
    role smallint,
    tags bigint[],
    CONSTRAINT user__role_check CHECK (((role >= 0) AND (role <= 2)))
);


ALTER TABLE public.user_ OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 41935)
-- Name: user__id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.user_ ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user__id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3346 (class 0 OID 41976)
-- Dependencies: 220
-- Data for Name: reviews_; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reviews_ (id, sender_id, receiver_id, review_text, rate) FROM stdin;
\.


--
-- TOC entry 3343 (class 0 OID 41944)
-- Dependencies: 217
-- Data for Name: role_; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role_ (id, name) FROM stdin;
1	participant
2	mentor
3	organazer
\.


--
-- TOC entry 3342 (class 0 OID 41939)
-- Dependencies: 216
-- Data for Name: session_; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.session_ (id, user_id, token, expires) FROM stdin;
3	3	eyJhbGciOiJSUzI1NiJ9.eyJlbWFpbCI6ImFiYzJAZ21haWwuY29tIiwiaWQiOjMsIm5pY2tuYW1lIjoiZGFzc2hhc2FuIiwicm9sZSI6InBhcnRpY2lwYW50IiwiaWF0IjoxNzQ1NDAwODg1LCJleHAiOjE3NDU0MDQ0ODV9.cZS_QVfQ_q3wZRbOPGRta_I4nkZl5Gx0-5a9RdOzkH1PvBIoLxn-ZP4Wkk5P0_SxG9u1tcgtG-FqWGUH6FoiUcaAui7bdZfy4A1BCQcIwVtNoAmcDmGGF2xoy-n_flOYeevryWowPJfbt0obKEHGQYVBBQT_U6PWkY7UPJFGMluC-PDKXOQT0peBcXrfhir9uwzvtur2CRiBwSxWXKXCCGschTL3Icb4HK9NjB4XZ_6iX6z7N221wvVJTJCtLX64Ohluod26yJIUmwDjTg7JNphzxasCtQ-mYhBpa3Vgc7Qx6GK4BI9chW1LIruzys3ZXhgm2affug6v5VYkTK5r3w	2025-04-23 13:34:45
4	4	eyJhbGciOiJSUzI1NiJ9.eyJlbWFpbCI6ImRhcnlhX3NtaWxlMTdAZ21haWwuY29tIiwiaWQiOjQsIm5pY2tuYW1lIjoiZGFzc2hhc2FuMSIsInJvbGUiOiJtZW50b3IiLCJpYXQiOjE3NDU0MDA5OTksImV4cCI6MTc0NTQwNDU5OX0.vvZsBxE9rDFx5VUdbBS2Z0aQONuUL_TLhtHZQg4v7L-mfxUhHWBVIF5cbJpJYf9ppRNHA1F2VL33AAeE_X8XxUPPj6QupJrHGLHILj0uKsMVGa8hr_33dcYN5qjUpq1irBgD6LeHtYFA4clPRm3JcN_xd36f7gAfDTqZJFv-NeVAzAXTFtsAK2LuRMkgVJkyplmxHOfv_mr-aCDTaSgoZ1-hz4jOLhqlrnm3MFXwQqE7l7PAzTWilJq8I9KxWi5qdo-_9uJcWXV-hq-l6Mazwf8dzrE0F1hzxRFYpCO6xv2kpfm_veeVtrj0umacRL57-7Vt8_yH50M1riYlB2W-xw	2025-04-23 13:36:39
\.


--
-- TOC entry 3340 (class 0 OID 41932)
-- Dependencies: 214
-- Data for Name: user_; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_ (id, nickname, email, password, contacts, github, experience, description, role_id, first_name, image, last_name, middle_name, role, tags) FROM stdin;
3	dasshasan	abc2@gmail.com	$2a$10$WNDn7ifdtbctA/gzVQho.e4AOreS7yvuSKJCfokWs2P0pgiU/XA4O	\N	\N	\N	\N	\N	Дарья	\N	Судакова	Евгеньевна	0	\N
4	dasshasan1	darya_smile17@gmail.com	$2a$10$hCh4vGCo61g.yQ1MN3lZKuFzfdq0/kPN2vRiZ7gIpZZBxc0hXNuUu	\N	\N	\N	\N	\N	Елена	\N	Кузьмина	Евгеньевна	1	\N
\.


--
-- TOC entry 3352 (class 0 OID 0)
-- Dependencies: 218
-- Name: role__id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role__id_seq', 3, true);


--
-- TOC entry 3353 (class 0 OID 0)
-- Dependencies: 219
-- Name: session__id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.session__id_seq', 4, true);


--
-- TOC entry 3354 (class 0 OID 0)
-- Dependencies: 215
-- Name: user__id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user__id_seq', 4, true);


--
-- TOC entry 3193 (class 2606 OID 41954)
-- Name: role_ id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_
    ADD CONSTRAINT id PRIMARY KEY (id);


--
-- TOC entry 3195 (class 2606 OID 41982)
-- Name: reviews_ reviews__pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews_
    ADD CONSTRAINT reviews__pkey PRIMARY KEY (id);


--
-- TOC entry 3191 (class 2606 OID 41969)
-- Name: session_ session_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.session_
    ADD CONSTRAINT session_id PRIMARY KEY (id);


--
-- TOC entry 3189 (class 2606 OID 41956)
-- Name: user_ user_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_
    ADD CONSTRAINT user_id PRIMARY KEY (id);


--
-- TOC entry 3196 (class 2606 OID 41957)
-- Name: user_ role; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_
    ADD CONSTRAINT role FOREIGN KEY (role_id) REFERENCES public.role_(id) NOT VALID;


--
-- TOC entry 3197 (class 2606 OID 41970)
-- Name: session_ user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.session_
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.user_(id) NOT VALID;


-- Completed on 2025-04-24 13:02:42

--
-- PostgreSQL database dump complete
--

