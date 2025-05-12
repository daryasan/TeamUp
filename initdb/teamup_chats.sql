--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2025-05-12 14:54:02

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
-- TOC entry 221 (class 1259 OID 42106)
-- Name: attachments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attachments (
    id bigint NOT NULL,
    message_id bigint,
    file_url character varying(255) NOT NULL,
    file_type character varying(255) NOT NULL
);


ALTER TABLE public.attachments OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 42105)
-- Name: attachments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.attachments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attachments_id_seq OWNER TO postgres;

--
-- TOC entry 3359 (class 0 OID 0)
-- Dependencies: 220
-- Name: attachments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.attachments_id_seq OWNED BY public.attachments.id;


--
-- TOC entry 217 (class 1259 OID 42080)
-- Name: chat_participants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat_participants (
    id bigint NOT NULL,
    chat_id bigint,
    user_id bigint NOT NULL,
    joined_at timestamp without time zone NOT NULL
);


ALTER TABLE public.chat_participants OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 42079)
-- Name: chat_participants_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.chat_participants_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.chat_participants_id_seq OWNER TO postgres;

--
-- TOC entry 3360 (class 0 OID 0)
-- Dependencies: 216
-- Name: chat_participants_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.chat_participants_id_seq OWNED BY public.chat_participants.id;


--
-- TOC entry 215 (class 1259 OID 42073)
-- Name: chats; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chats (
    id bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    is_group boolean DEFAULT false NOT NULL
);


ALTER TABLE public.chats OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 42072)
-- Name: chats_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.chats_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.chats_id_seq OWNER TO postgres;

--
-- TOC entry 3361 (class 0 OID 0)
-- Dependencies: 214
-- Name: chats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.chats_id_seq OWNED BY public.chats.id;


--
-- TOC entry 219 (class 1259 OID 42092)
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    id bigint NOT NULL,
    chat_id bigint,
    sender_id bigint NOT NULL,
    content text,
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 42091)
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.messages_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.messages_id_seq OWNER TO postgres;

--
-- TOC entry 3362 (class 0 OID 0)
-- Dependencies: 218
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.messages_id_seq OWNED BY public.messages.id;


--
-- TOC entry 3192 (class 2604 OID 42806)
-- Name: attachments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attachments ALTER COLUMN id SET DEFAULT nextval('public.attachments_id_seq'::regclass);


--
-- TOC entry 3190 (class 2604 OID 42821)
-- Name: chat_participants id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants ALTER COLUMN id SET DEFAULT nextval('public.chat_participants_id_seq'::regclass);


--
-- TOC entry 3188 (class 2604 OID 42828)
-- Name: chats id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats ALTER COLUMN id SET DEFAULT nextval('public.chats_id_seq'::regclass);


--
-- TOC entry 3191 (class 2604 OID 42845)
-- Name: messages id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages ALTER COLUMN id SET DEFAULT nextval('public.messages_id_seq'::regclass);


--
-- TOC entry 3353 (class 0 OID 42106)
-- Dependencies: 221
-- Data for Name: attachments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.attachments (id, message_id, file_url, file_type) FROM stdin;
\.


--
-- TOC entry 3349 (class 0 OID 42080)
-- Dependencies: 217
-- Data for Name: chat_participants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat_participants (id, chat_id, user_id, joined_at) FROM stdin;
3	2	12	2025-05-06 17:00:18.175671
4	2	15	2025-05-06 17:00:18.176675
5	3	12	2025-05-06 17:01:53.0346
6	3	15	2025-05-06 17:01:53.036598
7	4	12	2025-05-06 17:07:39.095355
8	4	15	2025-05-06 17:07:39.10068
9	5	12	2025-05-06 17:50:27.72279
10	5	15	2025-05-06 17:50:27.728484
\.


--
-- TOC entry 3347 (class 0 OID 42073)
-- Dependencies: 215
-- Data for Name: chats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chats (id, created_at, is_group) FROM stdin;
2	2025-05-06 17:00:18.173676	f
3	2025-05-06 17:01:53.03248	f
4	2025-05-06 17:07:39.026158	f
5	2025-05-06 17:50:27.653242	f
\.


--
-- TOC entry 3351 (class 0 OID 42092)
-- Dependencies: 219
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, chat_id, sender_id, content, "timestamp") FROM stdin;
1	2	12	Привет!	2025-05-08 11:57:21.065168
2	2	12	Привет!	2025-05-08 12:08:29.630488
3	2	12	Привет!	2025-05-08 12:09:08.774404
4	2	12	Привет!	2025-05-08 12:09:29.982678
5	2	12	Привет!	2025-05-08 12:10:39.409268
6	2	12	Привет!	2025-05-08 12:12:15.926211
\.


--
-- TOC entry 3363 (class 0 OID 0)
-- Dependencies: 220
-- Name: attachments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.attachments_id_seq', 1, false);


--
-- TOC entry 3364 (class 0 OID 0)
-- Dependencies: 216
-- Name: chat_participants_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chat_participants_id_seq', 10, true);


--
-- TOC entry 3365 (class 0 OID 0)
-- Dependencies: 214
-- Name: chats_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chats_id_seq', 5, true);


--
-- TOC entry 3366 (class 0 OID 0)
-- Dependencies: 218
-- Name: messages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.messages_id_seq', 6, true);


--
-- TOC entry 3200 (class 2606 OID 42808)
-- Name: attachments attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT attachments_pkey PRIMARY KEY (id);


--
-- TOC entry 3196 (class 2606 OID 42823)
-- Name: chat_participants chat_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_pkey PRIMARY KEY (id);


--
-- TOC entry 3194 (class 2606 OID 42830)
-- Name: chats chats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_pkey PRIMARY KEY (id);


--
-- TOC entry 3198 (class 2606 OID 42847)
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- TOC entry 3203 (class 2606 OID 42848)
-- Name: attachments attachments_message_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT attachments_message_id_fkey FOREIGN KEY (message_id) REFERENCES public.messages(id);


--
-- TOC entry 3201 (class 2606 OID 42831)
-- Name: chat_participants chat_participants_chat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_participants
    ADD CONSTRAINT chat_participants_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id);


--
-- TOC entry 3202 (class 2606 OID 42836)
-- Name: messages messages_chat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_chat_id_fkey FOREIGN KEY (chat_id) REFERENCES public.chats(id);


-- Completed on 2025-05-12 14:54:03

--
-- PostgreSQL database dump complete
--

