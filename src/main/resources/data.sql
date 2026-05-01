CREATE EXTENSION if not exists pg_trgm;

create table if not exists constructor_users (
    user_id bigserial not null,
    email varchar(255) not null,
    user_name varchar(255),
    user_password varchar(255) not null,
    authority varchar(16) not null,
    primary key (user_id)
);


create table if not exists projects (
    project_id bigserial not null,
    description varchar(255),
    project_name varchar(255) not null,
    primary key (project_id)
);


create table if not exists services (
    service_id bigserial not null,
    project_id bigint not null,
    description varchar(255),
    service_name varchar(255) not null,
    service_path varchar(255) not null,
    primary key (service_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE
);
CREATE INDEX if not exists services_name_gin ON services USING gin (service_name gin_trgm_ops);


create table if not exists ctypes (
    type_id bigserial not null,
    type_name varchar(255) not null,
    project_id bigint not null,
    description varchar(255),
    primary key (type_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE
);
CREATE INDEX if not exists idx_ñtype_name_gin ON ctypes USING gin (type_name gin_trgm_ops);


create table if not exists ctype_fields (
    field_id bigserial not null,
    owner_ctype_id bigint not null,
    field_name varchar(255) not null,
    field_ctype_id bigint,
    description varchar(255),
    multiplicity varchar(255) check (multiplicity in ('SINGLE','COLLECTION')) not null,
    primary key (field_id),
    FOREIGN KEY (owner_ctype_id) REFERENCES ctypes(type_id) ON DELETE CASCADE,
    FOREIGN KEY (field_ctype_id) REFERENCES ctypes(type_id) ON DELETE SET NULL
);
CREATE INDEX if not exists idx_owner_ctype_id_type_id ON ctype_fields (owner_ctype_id);


create table if not exists blocks (
    block_id bigserial not null,
    project_id bigint not null,
    service_id bigint not null,
    block_name varchar(255) not null,
    block_type varchar(255) not null check (block_type in ('WORKFLOW','ACTION','SOURCE')),
    description varchar(255),
    primary key (block_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE CASCADE
);
CREATE INDEX if not exists idx_blocks_name_gin ON blocks USING gin (block_name gin_trgm_ops);

create table if not exists scenario_blocks (
    scenario_block_id bigserial not null,
    block_id bigint not null,
    next_scenario_block bigint unique,
    parent_workflow_id bigint not null,
    previous_scenario_block bigint unique,
    x bigint,
    y bigint,
    var_version BIGINT NOT NULL DEFAULT 0,
    primary key (scenario_block_id),
    FOREIGN KEY (block_id) REFERENCES blocks(block_id) ON DELETE CASCADE,
    FOREIGN KEY (next_scenario_block) REFERENCES scenario_blocks(scenario_block_id) ON DELETE SET NULL,
    FOREIGN KEY (parent_workflow_id) REFERENCES blocks(block_id) ON DELETE CASCADE
);
CREATE INDEX if not exists idx_scenario_blocks_fk_parent_workflow_id ON scenario_blocks (parent_workflow_id);
CREATE INDEX if not exists idx_scenario_version ON scenario_blocks(var_version);

create table if not exists variables (
    variable_id bigserial not null,
    block_id bigint not null,
    var_type_id bigint,
    description varchar(255),
    multiplicity varchar(255) check (multiplicity in ('SINGLE','COLLECTION')) not null,
    var_pin_type varchar(255) check (var_pin_type in ('IN','OUT')) not null,
    variable_name varchar(255) not null,
    scenario_block_id bigint,
    primary key (variable_id),
    FOREIGN KEY (block_id) REFERENCES blocks(block_id) ON DELETE CASCADE,
    FOREIGN KEY (scenario_block_id) REFERENCES scenario_blocks(scenario_block_id) ON DELETE CASCADE,
    FOREIGN KEY (var_type_id) REFERENCES ctypes(type_id) ON DELETE SET NULL
);
CREATE INDEX if not exists idx_variables_fk_block_id ON variables (block_id);





create table if not exists start_stop_points (
    point_id bigserial not null,
    connected_scenario_block bigint unique,
    workflow_id bigint not null,
    x bigint,
    y bigint,
    point_type varchar(255) check (point_type in ('START','END')) not null,
    primary key (point_id),
    FOREIGN KEY (connected_scenario_block) REFERENCES scenario_blocks(scenario_block_id) ON DELETE SET NULL,
    FOREIGN KEY (workflow_id) REFERENCES blocks(block_id) ON DELETE CASCADE
);
CREATE INDEX if not exists idx_start_stop_points_fk_workflow_id ON start_stop_points (workflow_id);


create table if not exists scenario_variables (
    scenario_variable_id bigserial not null,
    consumer_variable_id bigint not null,
    scenario_block_id bigint not null,
    owner_variable bigint not null,
    producer_variable_id bigint,
    producer_source_id bigint,
    producer_script text,
    type_var varchar(255) not null check (type_var in ('SIMPLE','SOURCE','SCRIPT')),
    function_variable_type varchar(255) not null check (function_variable_type in ('CONSUMER','FUNCTION','PARAMETER')),
    primary key (scenario_variable_id),
    FOREIGN KEY (consumer_variable_id) REFERENCES variables(variable_id) ON DELETE CASCADE,
    FOREIGN KEY (producer_variable_id) REFERENCES variables(variable_id) ON DELETE CASCADE,
    FOREIGN KEY (scenario_block_id) REFERENCES scenario_blocks(scenario_block_id) ON DELETE CASCADE,
    FOREIGN KEY (producer_source_id) REFERENCES blocks(block_id) ON DELETE CASCADE,
    FOREIGN KEY (owner_variable) REFERENCES variables(variable_id) ON DELETE CASCADE
);
CREATE INDEX if not exists idx_scenario_variables_fk_scenario_block_id ON scenario_variables (scenario_block_id);


--create table if not exists scenario_variable_parents (
--    parent_local_id bigint not null,
--    parent_id bigint not null,
--    child_id bigint not null,
--    child_local_id bigint not null,
--    FOREIGN KEY (child_id, child_local_id) REFERENCES scenario_variables(scenario_variable_id, local_id) ON DELETE CASCADE,
--    FOREIGN KEY (parent_id, parent_local_id) REFERENCES scenario_variables(scenario_variable_id, local_id) ON DELETE CASCADE,
--    primary key (parent_id, parent_local_id, child_id, child_local_id)
--);


create table if not exists type_dependencies (
    type_dep_id bigserial not null,
    dep_count integer not null,
    current_ctype_field bigint not null,
--    parent_type bigint not null,
    scenario_variable bigint not null,
    primary key (type_dep_id),
    FOREIGN KEY (current_ctype_field) REFERENCES ctype_fields(field_id) ON DELETE CASCADE,
--    FOREIGN KEY (parent_type) REFERENCES variable_types(type_id) ON DELETE CASCADE,
    FOREIGN KEY (scenario_variable) REFERENCES scenario_variables(scenario_variable_id) ON DELETE CASCADE
);
CREATE INDEX if not exists idx_type_dependencies_fk_scenario_variable ON type_dependencies (scenario_variable);


--create table if not exists scripts (
--    script_id bigserial not null,
--    scenario_variable bigint not null unique,
--    script varchar(255) not null,
--    primary key (script_id),
--    FOREIGN KEY (scenario_variable) REFERENCES scenario_variables(scenario_variable_id) ON DELETE CASCADE
--);
--CREATE INDEX if not exists idx_scripts_fk_scenario_variable ON scripts (scenario_variable);












