create table oauth2_authorization (
    id varchar(100) not null,
    registered_client_id varchar(100) not null,
    principal_name varchar(200) not null,
    authorization_grant_type varchar(100) not null,
    authorized_scopes varchar(1000) default null,
    attributes blob default null,
    state varchar(500) default null,

    authorization_code_value blob default null,
    authorization_code_issued_at timestamp null default null,
    authorization_code_expires_at timestamp null default null,
    authorization_code_metadata blob default null,

    access_token_value blob default null,
    access_token_issued_at timestamp null default null,
    access_token_expires_at timestamp null default null,
    access_token_metadata blob default null,
    access_token_type varchar(100) default null,
    access_token_scopes varchar(1000) default null,

    oidc_id_token_value blob default null,
    oidc_id_token_issued_at timestamp null default null,
    oidc_id_token_expires_at timestamp null default null,
    oidc_id_token_metadata blob default null,
    oidc_id_token_claims blob default null,

    refresh_token_value blob default null,
    refresh_token_issued_at timestamp null default null,
    refresh_token_expires_at timestamp null default null,
    refresh_token_metadata blob default null,

    user_code_value blob default null,
    user_code_issued_at timestamp null default null,
    user_code_expires_at timestamp null default null,
    user_code_metadata blob default null,

    device_code_value blob default null,
    device_code_issued_at timestamp null default null,
    device_code_expires_at timestamp null default null,
    device_code_metadata blob default null,

    primary key (id)
);

create index idx_oauth2_authorization_registered_client
    on oauth2_authorization (registered_client_id);

create index idx_oauth2_authorization_principal
    on oauth2_authorization (principal_name);

create table oauth2_authorization_consent (
    registered_client_id varchar(100) not null,
    principal_name varchar(200) not null,
    authorities varchar(1000) not null,
    primary key (registered_client_id, principal_name)
);