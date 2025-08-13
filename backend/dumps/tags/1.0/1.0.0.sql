SELECT
	pg_terminate_backend (pid)
FROM
	pg_stat_activity
WHERE
	datname = 'db_currency_converter'
	AND pid <> pg_backend_pid ();

drop database db_currency_converter;

create database db_currency_converter;

CREATE SCHEMA IF NOT EXISTS seguranca;

CREATE TABLE
	seguranca.cadastro_tipo (
		id_cadastro_tipo serial4 NOT NULL,
		no_cadastro_tipo varchar(60) NOT NULL,
		CONSTRAINT pk_cadastro_tipo PRIMARY KEY (id_cadastro_tipo),
		CONSTRAINT un_cadastro_tipo UNIQUE (no_cadastro_tipo)
	);

INSERT INTO
	seguranca.cadastro_tipo (id_cadastro_tipo, no_cadastro_tipo)
VALUES
	(1, 'Converter'),
	(2, 'Google'),
	(3, 'Facebook');

CREATE TABLE
	seguranca.usuario (
		id_usuario serial4 NOT NULL,
		no_usuario varchar(45) NOT NULL,
		ds_email varchar(100) NOT NULL,
		ds_senha varchar(100) NULL,
		ds_foto varchar(255) NULL,
		in_ativo bool DEFAULT true NOT NULL,
		id_usuario_criacao int NOT NULL,
		id_usuario_alteracao int NULL,
		dt_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
		dt_alteracao TIMESTAMP NULL,
		in_email_valido bool DEFAULT false NOT NULL,
		dt_excluir date NULL,
		id_cadastro_tipo int DEFAULT 1 NOT NULL,
		id_ultima_empresa int NULL,
		dt_ultimo_acesso TIMESTAMP NULL,
		ds_celular varchar(11) NULL,
		ds_foto_externa varchar(1000) NULL,
		CONSTRAINT pk_usuario PRIMARY KEY (id_usuario)
	);

CREATE UNIQUE INDEX un_email_usuario ON seguranca.usuario (ds_email);

CREATE TABLE
	seguranca.empresa (
		id_empresa serial4 NOT NULL,
		co_empresa_situacao CHAR(1) NOT NULL,
		nu_cnpj_cpf CHAR(14) NOT NULL,
		no_razao_social varchar(500) NOT NULL,
		no_fantasia varchar(60) NOT NULL,
		ds_caminho_logo varchar(200) NULL,
		dt_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
		nu_inscricao_estadual varchar(14) NULL,
		id_usuario_alteracao int NULL,
		dt_alteracao TIMESTAMP NULL,
		co_empresa_tipo CHAR(2) NOT NULL,
		id_usuario_criacao int NOT NULL,
		dt_excluir date NULL,
		ds_email_cobranca varchar(50) NULL,
		nu_tel_cobranca varchar(14) NULL,
		ds_horario_atendimento varchar(255) NULL,
		CONSTRAINT pk_empresa PRIMARY KEY (id_empresa)
	);

CREATE UNIQUE INDEX un_empresa ON seguranca.empresa (nu_cnpj_cpf);

CREATE TABLE
	seguranca.empresa_situacao (
		co_empresa_situacao CHAR(1) NOT NULL,
		no_empresa_situacao varchar(30) NOT NULL,
		CONSTRAINT pk_empresa_situacao PRIMARY KEY (co_empresa_situacao)
	);

INSERT INTO
	seguranca.empresa_situacao (co_empresa_situacao, no_empresa_situacao)
VALUES
	('A', 'Ativa'),
	('B', 'Bloqueada'),
	('E', 'Em Análise');

CREATE TABLE
	seguranca.perfil (
		id_perfil serial4 NOT NULL,
		id_tipo_perfil int NOT NULL,
		id_empresa int NULL,
		ds_perfil varchar(1024) NOT NULL,
		ds_perfil_curto varchar(30) NOT NULL,
		in_agrupar bool DEFAULT true NOT NULL,
		in_ativo bool DEFAULT true NOT NULL,
		id_usuario_criacao int NOT NULL,
		id_usuario_alteracao int NULL,
		dt_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
		dt_alteracao TIMESTAMP NULL,
		id_perfil_referencia int NULL,
		CONSTRAINT pk_perfil PRIMARY KEY (id_perfil)
	);

CREATE TABLE
	seguranca.perfil_tipo (
		id_tipo_perfil serial4 NOT NULL,
		ds_tipo_perfil varchar(60) NOT NULL,
		CONSTRAINT pk_perfil_tipo PRIMARY KEY (id_tipo_perfil)
	);

CREATE TABLE
	seguranca.usuario_perfil (
		id_usuario serial4 NOT NULL,
		id_empresa int NOT NULL,
		id_perfil int NOT NULL,
		dt_ultimo_acesso TIMESTAMP NULL,
		in_ativo bool DEFAULT true NOT NULL,
		CONSTRAINT pk_usuario_perfil PRIMARY KEY (id_perfil, id_usuario, id_empresa)
	);

INSERT INTO
	seguranca.perfil_tipo (id_tipo_perfil, ds_tipo_perfil)
VALUES
	(1, 'Interno/Privado, acesso com validação'),
	(2, 'Externo/Publico, acesso sem validação');

ALTER TABLE seguranca.usuario ADD CONSTRAINT fk_ultima_empresa FOREIGN KEY (id_ultima_empresa) REFERENCES seguranca.empresa (id_empresa);

ALTER TABLE seguranca.usuario ADD CONSTRAINT fk_usuario_ref_cadastro_tipo FOREIGN KEY (id_cadastro_tipo) REFERENCES seguranca.cadastro_tipo (id_cadastro_tipo) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.usuario ADD CONSTRAINT fk_usuario_ref_usuario_alt FOREIGN KEY (id_usuario_alteracao) REFERENCES seguranca.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.usuario ADD CONSTRAINT fk_usuario_ref_usuario_cri FOREIGN KEY (id_usuario_criacao) REFERENCES seguranca.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.perfil ADD CONSTRAINT fk_perfil_ref_empresa FOREIGN KEY (id_empresa) REFERENCES seguranca.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.perfil ADD CONSTRAINT fk_perfil_ref_perfil_tipo FOREIGN KEY (id_tipo_perfil) REFERENCES seguranca.perfil_tipo (id_tipo_perfil) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.perfil ADD CONSTRAINT fk_perfil_ref_usuario_alt FOREIGN KEY (id_usuario_alteracao) REFERENCES seguranca.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.perfil ADD CONSTRAINT fk_perfil_ref_usuario_cri FOREIGN KEY (id_usuario_criacao) REFERENCES seguranca.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.usuario_perfil ADD CONSTRAINT fk_usuario_perfil_ref_empresa FOREIGN KEY (id_empresa) REFERENCES seguranca.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.usuario_perfil ADD CONSTRAINT fk_usuario_perfil_ref_perfil FOREIGN KEY (id_perfil) REFERENCES seguranca.perfil (id_perfil) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.usuario_perfil ADD CONSTRAINT fk_usuario_perfil_ref_usuario FOREIGN KEY (id_usuario) REFERENCES seguranca.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.empresa ADD CONSTRAINT fk_empresa_ref_usuario_alt FOREIGN KEY (id_usuario_alteracao) REFERENCES seguranca.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.empresa ADD CONSTRAINT fk_empresa_sit_ref_empres FOREIGN KEY (co_empresa_situacao) REFERENCES seguranca.empresa_situacao (co_empresa_situacao) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE seguranca.empresa ADD CONSTRAINT fk_usuario_criacao FOREIGN KEY (id_usuario_criacao) REFERENCES seguranca.usuario (id_usuario);

INSERT INTO
	seguranca.usuario (
		id_usuario,
		no_usuario,
		ds_email,
		ds_senha,
		ds_foto,
		in_ativo,
		id_usuario_criacao,
		id_usuario_alteracao,
		dt_criacao,
		dt_alteracao,
		in_email_valido,
		dt_excluir,
		id_cadastro_tipo,
		id_ultima_empresa,
		dt_ultimo_acesso,
		ds_celular,
		ds_foto_externa
	)
VALUES
	(
		3,
		'Sistema',
		'sistema@seguranca.com.br',
		'e10adc3949ba59abbe56e057f20f883e',
		NULL,
		true,
		3,
		NULL,
		'2023-11-01 15:26:30.069',
		NULL,
		true,
		NULL,
		1,
		NULL,
		NULL,
		NULL,
		NULL
	);

INSERT INTO
	seguranca.empresa (
		id_empresa,
		co_empresa_situacao,
		nu_cnpj_cpf,
		no_razao_social,
		no_fantasia,
		ds_caminho_logo,
		dt_criacao,
		nu_inscricao_estadual,
		id_usuario_alteracao,
		dt_alteracao,
		co_empresa_tipo,
		id_usuario_criacao,
		dt_excluir,
		ds_email_cobranca,
		nu_tel_cobranca,
		ds_horario_atendimento
	)
VALUES
	(
		12500,
		'A',
		'20733448000121',
		'Empresa Core',
		'Empresa Core',
		'images/default/padrao-empresa.jpg',
		'2023-11-01 15:23:29.000',
		null,
		null,
		null,
		'PJ',
		3,
		null,
		'sistema@uishop.com.br',
		'6133224455',
		'<p><strong>Horários de Atendimento</strong></p><p>Seg. à Sex.</p><p>8h às 19h</p><p>Sáb. e Dom.</p><p>Fechados</p>'
	);

INSERT INTO
	seguranca.empresa_situacao (co_empresa_situacao, no_empresa_situacao)
VALUES
	('I', 'Inativa');

CREATE SCHEMA IF NOT EXISTS public AUTHORIZATION postgres;

CREATE TABLE
	public.esqueleto (
		id_esqueleto serial4 NOT NULL,
		no_esqueleto varchar(250) NOT NULL,
		id_usuario_criacao int4 NOT NULL,
		id_usuario_alteracao int4 NULL,
		dt_criacao timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
		dt_alteracao timestamptz NULL,
		CONSTRAINT pk_esqueleto PRIMARY KEY (id_esqueleto)
	);

ALTER TABLE public.esqueleto ADD CONSTRAINT fk_esqueleto_ref_usuario_alt FOREIGN KEY (id_usuario_alteracao) REFERENCES seguranca.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE public.esqueleto ADD CONSTRAINT fk_esqueleto_ref_usuario_cri FOREIGN KEY (id_usuario_criacao) REFERENCES seguranca.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE
OR REPLACE VIEW seguranca.vw_acessos AS
SELECT
	e.id_empresa,
	e.no_fantasia,
	e.no_razao_social,
	e.dt_criacao,
	u.no_usuario,
	u.id_usuario,
	u.ds_foto,
	p.id_perfil,
	p.ds_perfil_curto,
	up.in_ativo
FROM
	seguranca.usuario u
	JOIN seguranca.usuario_perfil up ON u.id_usuario = up.id_usuario
	JOIN seguranca.perfil p ON up.id_perfil = p.id_perfil
	JOIN seguranca.empresa e ON up.id_empresa = e.id_empresa
GROUP BY
	e.id_empresa,
	e.no_fantasia,
	u.no_usuario,
	u.id_usuario,
	u.ds_foto,
	p.id_perfil,
	p.ds_perfil_curto,
	up.in_ativo;

INSERT INTO
	seguranca.perfil (
		id_perfil,
		id_tipo_perfil,
		id_empresa,
		ds_perfil,
		ds_perfil_curto,
		in_agrupar,
		in_ativo,
		id_usuario_criacao,
		id_usuario_alteracao,
		dt_criacao,
		dt_alteracao,
		id_perfil_referencia
	)
VALUES
	(
		1,
		1,
		NULL,
		'Perfil administrador sistema Segurança',
		'Administrador',
		true,
		true,
		3,
		NULL,
		'2024-03-22 08:58:51.675',
		NULL,
		NULL
	);

INSERT INTO
	seguranca.perfil (
		id_perfil,
		id_tipo_perfil,
		id_empresa,
		ds_perfil,
		ds_perfil_curto,
		in_agrupar,
		in_ativo,
		id_usuario_criacao,
		id_usuario_alteracao,
		dt_criacao,
		dt_alteracao,
		id_perfil_referencia
	)
VALUES
	(
		2,
		2,
		NULL,
		'Usuário com acesso a todas as informações, exceto ações administrativas a remoção da empresa.',
		'Administrador',
		true,
		true,
		3,
		NULL,
		'2024-03-22 08:58:51.675',
		NULL,
		NULL
	);

INSERT INTO
	seguranca.perfil (
		id_perfil,
		id_tipo_perfil,
		id_empresa,
		ds_perfil,
		ds_perfil_curto,
		in_agrupar,
		in_ativo,
		id_usuario_criacao,
		id_usuario_alteracao,
		dt_criacao,
		dt_alteracao,
		id_perfil_referencia
	)
VALUES
	(
		9,
		2,
		NULL,
		'Usuário com acesso a todas as informações, exceto ações administrativas a remoção da empresa.',
		'Colaborador',
		true,
		true,
		3,
		3,
		'2024-03-22 09:26:25.814',
		NULL,
		NULL
	);

INSERT INTO
	seguranca.perfil (
		id_perfil,
		id_tipo_perfil,
		id_empresa,
		ds_perfil,
		ds_perfil_curto,
		in_agrupar,
		in_ativo,
		id_usuario_criacao,
		id_usuario_alteracao,
		dt_criacao,
		dt_alteracao,
		id_perfil_referencia
	)
VALUES
	(
		11,
		2,
		NULL,
		'Usuário encarregado das operações segurancairas, com acesso a relatórios, despesas, receitas e fluxo de caixa.',
		'segurancairo',
		true,
		true,
		3,
		3,
		'2024-03-22 09:26:31.497',
		NULL,
		NULL
	);

INSERT INTO
	seguranca.perfil (
		id_perfil,
		id_tipo_perfil,
		id_empresa,
		ds_perfil,
		ds_perfil_curto,
		in_agrupar,
		in_ativo,
		id_usuario_criacao,
		id_usuario_alteracao,
		dt_criacao,
		dt_alteracao,
		id_perfil_referencia
	)
VALUES
	(
		13,
		2,
		NULL,
		'Usuário encarregado da criação da loja online, com acesso ao construtor, banners, páginas e produtos.',
		'Web Designer',
		true,
		true,
		3,
		3,
		'2024-03-22 09:26:37.165',
		NULL,
		NULL
	);

-- 23/07/25
DROP VIEW IF EXISTS seguranca.vw_acessos;

CREATE
OR REPLACE VIEW seguranca.vw_acessos AS
SELECT
	e.id_empresa,
	e.no_fantasia,
	e.no_razao_social,
	e.dt_criacao,
	u.no_usuario,
	u.id_usuario,
	u.ds_foto,
	p.id_perfil,
	p.ds_perfil_curto,
	up.in_ativo
FROM
	seguranca.usuario u
	JOIN seguranca.usuario_perfil up ON u.id_usuario = up.id_usuario
	JOIN seguranca.perfil p ON up.id_perfil = p.id_perfil
	LEFT JOIN seguranca.empresa e ON up.id_empresa = e.id_empresa
GROUP BY
	e.id_empresa,
	e.no_fantasia,
	e.no_razao_social,
	e.dt_criacao,
	u.no_usuario,
	u.id_usuario,
	u.ds_foto,
	p.id_perfil,
	p.ds_perfil_curto,
	up.in_ativo;

ALTER TABLE seguranca.usuario_perfil
DROP CONSTRAINT pk_usuario_perfil;

ALTER TABLE seguranca.usuario_perfil ADD CONSTRAINT pk_usuario_perfil PRIMARY KEY (id_usuario, id_perfil);

ALTER TABLE seguranca.usuario_perfil
ALTER COLUMN id_empresa
DROP NOT NULL;

CREATE SCHEMA IF NOT EXISTS fx;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_type t
    JOIN pg_namespace n ON n.oid = t.typnamespace
    WHERE t.typname = 'co_moeda' AND n.nspname = 'fx'
  ) THEN
    CREATE DOMAIN fx.co_moeda AS CHAR(3)
      CHECK (VALUE ~ '^[A-Z]{3}$');
  END IF;
END
$$ LANGUAGE plpgsql;

SET search_path = fx, public;

CREATE TABLE IF NOT EXISTS moeda (
  co_moeda           fx.co_moeda PRIMARY KEY,           
  no_moeda           TEXT NOT NULL,                      
  nu_codigo_numerico SMALLINT,                           
  nu_casas_decimais  SMALLINT NOT NULL DEFAULT 2,       
  in_ativo           BOOLEAN  NOT NULL DEFAULT TRUE,
  dt_criacao         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS fonte_cotacao (
  id_fonte_cotacao SERIAL PRIMARY KEY,
  no_fonte         TEXT NOT NULL UNIQUE,                
  ds_base_url      TEXT,
  ds_descricao     TEXT,
  dt_criacao       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS cotacao (
  id_cotacao         BIGSERIAL PRIMARY KEY,
  id_fonte_cotacao   INTEGER NOT NULL
                      REFERENCES fonte_cotacao(id_fonte_cotacao) ON DELETE RESTRICT,
  co_moeda_base      fx.co_moeda NOT NULL,              
  co_moeda_cotada    fx.co_moeda NOT NULL,               
  vl_taxa            NUMERIC(20,10) NOT NULL CHECK (vl_taxa > 0),
  dt_referencia      DATE NOT NULL,                      
  dt_coleta          TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT cotacao_uniq UNIQUE (id_fonte_cotacao, co_moeda_base, co_moeda_cotada, dt_referencia),
  CONSTRAINT cotacao_base_ne_cotada CHECK (co_moeda_base <> co_moeda_cotada)
);

CREATE TABLE IF NOT EXISTS conversao_historico (
  id_conversao     BIGSERIAL PRIMARY KEY,
  co_moeda_origem  fx.co_moeda NOT NULL,
  co_moeda_destino fx.co_moeda NOT NULL,
  vl_montante      NUMERIC(20,10) NOT NULL CHECK (vl_montante >= 0),
  vl_taxa          NUMERIC(20,10) NOT NULL CHECK (vl_taxa > 0),
  vl_resultado     NUMERIC(20,10) GENERATED ALWAYS AS (vl_montante * vl_taxa) STORED,
  dt_conversao     TIMESTAMPTZ NOT NULL DEFAULT now(),
  ds_ip_cliente    INET,
  ds_user_agent    TEXT,
  id_usuario       INTEGER,
  CONSTRAINT conversao_moedas_diferentes CHECK (co_moeda_origem <> co_moeda_destino)
);

SET search_path = fx, public;

CREATE INDEX IF NOT EXISTS idx_cotacao_base_cotada_data
  ON cotacao (co_moeda_base, co_moeda_cotada, dt_referencia DESC);

CREATE INDEX IF NOT EXISTS idx_cotacao_fonte_data
  ON cotacao (id_fonte_cotacao, dt_referencia DESC);

CREATE INDEX IF NOT EXISTS idx_conversao_dt
  ON conversao_historico (dt_conversao DESC);

SET search_path = fx, public;

CREATE OR REPLACE FUNCTION upsert_cotacao(
  p_id_fonte_cotacao INTEGER,
  p_co_moeda_base    fx.co_moeda,
  p_co_moeda_cotada  fx.co_moeda,
  p_vl_taxa          NUMERIC,
  p_dt_referencia    DATE
) RETURNS VOID AS $$
BEGIN
  INSERT INTO cotacao (id_fonte_cotacao, co_moeda_base, co_moeda_cotada, vl_taxa, dt_referencia)
  VALUES (p_id_fonte_cotacao, p_co_moeda_base, p_co_moeda_cotada, p_vl_taxa, p_dt_referencia)
  ON CONFLICT (id_fonte_cotacao, co_moeda_base, co_moeda_cotada, p_dt_referencia)
  DO UPDATE SET
    vl_taxa   = EXCLUDED.vl_taxa,
    dt_coleta = now();
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_conversao(
  p_co_moeda_origem  fx.co_moeda,
  p_co_moeda_destino fx.co_moeda,
  p_vl_montante      NUMERIC,
  p_vl_taxa          NUMERIC,
  p_ds_ip_cliente    INET DEFAULT NULL,
  p_ds_user_agent    TEXT DEFAULT NULL,
  p_id_usuario       INTEGER DEFAULT NULL
) RETURNS BIGINT AS $$
DECLARE
  v_id BIGINT;
BEGIN
  INSERT INTO conversao_historico (
    co_moeda_origem, co_moeda_destino, vl_montante, vl_taxa,
    ds_ip_cliente, ds_user_agent, id_usuario
  ) VALUES (
    p_co_moeda_origem, p_co_moeda_destino, p_vl_montante, p_vl_taxa,
    p_ds_ip_cliente, p_ds_user_agent, p_id_usuario
  )
  RETURNING id_conversao INTO v_id;
  RETURN v_id;
END;
$$ LANGUAGE plpgsql;

SET search_path = fx, public;

CREATE OR REPLACE VIEW vw_cotacao_ultima AS
SELECT *
FROM (
  SELECT
    c.*,
    ROW_NUMBER() OVER (
      PARTITION BY c.id_fonte_cotacao, c.co_moeda_base, c.co_moeda_cotada
      ORDER BY c.dt_referencia DESC, c.dt_coleta DESC, c.id_cotacao DESC
    ) AS rn
  FROM cotacao c
) t
WHERE t.rn = 1;

CREATE OR REPLACE VIEW vw_cotacao_ultima_simple AS
SELECT id_fonte_cotacao, co_moeda_base, co_moeda_cotada, vl_taxa, dt_referencia, dt_coleta
FROM vw_cotacao_ultima;

SET search_path = fx, public;

INSERT INTO moeda (co_moeda, no_moeda, nu_codigo_numerico, nu_casas_decimais) VALUES
  ('USD','Dólar Americano',840,2),
  ('BRL','Real Brasileiro',986,2),
  ('EUR','Euro',978,2),
  ('GBP','Libra Esterlina',826,2),
  ('JPY','Iene Japonês',392,0)
ON CONFLICT (co_moeda) DO NOTHING;

INSERT INTO fonte_cotacao (no_fonte, ds_base_url, ds_descricao)
VALUES ('Frankfurter', 'https://api.frankfurter.app', 'API pública de câmbio baseada no ECB')
ON CONFLICT (no_fonte) DO NOTHING;

WITH s AS (
  SELECT id_fonte_cotacao FROM fonte_cotacao WHERE no_fonte = 'Frankfurter' LIMIT 1
)
INSERT INTO cotacao (id_fonte_cotacao, co_moeda_base, co_moeda_cotada, vl_taxa, dt_referencia)
SELECT s.id_fonte_cotacao, v.co_moeda_base, v.co_moeda_cotada, v.vl_taxa, v.dt_referencia
FROM s,
LATERAL (VALUES
  ('USD','BRL',5.2000000000, CURRENT_DATE),
  ('USD','EUR',0.9000000000, CURRENT_DATE),
  ('USD','GBP',0.7800000000, CURRENT_DATE),
  ('USD','JPY',155.0000000000, CURRENT_DATE),
  ('EUR','BRL',5.7777777778, CURRENT_DATE),
  ('BRL','USD',0.1923076923, CURRENT_DATE)
) AS v(co_moeda_base, co_moeda_cotada, vl_taxa, dt_referencia)
ON CONFLICT (id_fonte_cotacao, co_moeda_base, co_moeda_cotada, dt_referencia) DO NOTHING;

SET search_path = fx, public;

SELECT v.id_fonte_cotacao, v.co_moeda_base, v.co_moeda_cotada, v.vl_taxa, v.dt_referencia
FROM vw_cotacao_ultima v
JOIN fonte_cotacao f ON f.id_fonte_cotacao = v.id_fonte_cotacao
WHERE f.no_fonte = 'Frankfurter'
  AND v.co_moeda_base = 'USD'
  AND v.co_moeda_cotada = 'BRL';

WITH taxa AS (
  SELECT v.vl_taxa
  FROM vw_cotacao_ultima v
  JOIN fonte_cotacao f ON f.id_fonte_cotacao = v.id_fonte_cotacao
  WHERE f.no_fonte = 'Frankfurter'
    AND v.co_moeda_base = 'USD'
    AND v.co_moeda_cotada = 'BRL'
  LIMIT 1
)
SELECT 100.00::NUMERIC(20,10) AS vl_montante_usd,
       (SELECT vl_taxa FROM taxa) AS vl_taxa_usd_brl,
       100.00 * (SELECT vl_taxa FROM taxa) AS vl_resultado_brl;

SELECT fx.log_conversao('USD','BRL', 250.00, 5.20, '192.0.2.123', 'MyApp/1.0', NULL);

SELECT id_conversao, co_moeda_origem, co_moeda_destino, vl_montante, vl_taxa, vl_resultado, dt_conversao
FROM conversao_historico
ORDER BY dt_conversao DESC
LIMIT 20;


