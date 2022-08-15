create table if not exists GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(20) not null,
    constraint GENRE_ID_KEY
        primary key (GENRE_ID)
);

create unique index if not exists GENRE_NAME_INDEX
    on GENRES (GENRE_NAME);

create table if not exists MPAA
(
    MPAA_ID   INTEGER auto_increment,
    MPAA_NAME CHARACTER VARYING(5) not null,
    constraint KEY_NAME
        primary key (MPAA_ID)
);

create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(100) not null,
    DESCRIPTION  CHARACTER VARYING(200),
    DURATION     CHARACTER VARYING(5),
    RELEASE_DATE DATE,
    MPAA_ID      INTEGER,
    constraint ID_KEY
        primary key (FILM_ID),
    constraint MPPA_F_KEY
        foreign key (MPAA_ID) references MPAA
);

create table if not exists FILMS_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILMS_GENRES_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES
);

create table if not exists USERS
(
    USER_ID   INTEGER auto_increment,
    USER_NAME CHARACTER VARYING(100) not null,
    LOGIN     CHARACTER VARYING(50)  not null,
    EMAIL     CHARACTER VARYING(50)  not null,
    BIRTHDAY  DATE,
    constraint USER_ID
        primary key (USER_ID)
);

create table if not exists FILMS_LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILMS_LIKES_PK
        primary key (FILM_ID, USER_ID),
    constraint FILM_ID_FKEY
        foreign key (FILM_ID) references FILMS,
    constraint USER_ID_FKEY
        foreign key (USER_ID) references USERS
);

create unique index if not exists USERS_EMAIL_UNQ
    on USERS (EMAIL);

create unique index if not exists USERS_LOGIN_UNQ
    on USERS (LOGIN);

create table if not exists USER_FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint USER_FRIEND_KEY
        primary key (USER_ID, FRIEND_ID),
    constraint FOREIGN_KEY_FRIEND
        foreign key (FRIEND_ID) references USERS,
    constraint FOREIGN_KEY_USER
        foreign key (USER_ID) references USERS
);


