SELECT
	pg_terminate_backend (pid)
FROM
	pg_stat_activity
WHERE
	datname = 'db_currency_converter'
	AND pid <> pg_backend_pid ();

drop database db_currency_converter;

create database db_currency_converter;

ALTER SCHEMA public
RENAME TO finance;

CREATE TABLE
	finance.cadastro_tipo (
		id_cadastro_tipo serial4 NOT NULL,
		no_cadastro_tipo varchar(60) NOT NULL,
		CONSTRAINT pk_cadastro_tipo PRIMARY KEY (id_cadastro_tipo),
		CONSTRAINT un_cadastro_tipo UNIQUE (no_cadastro_tipo)
	);

INSERT INTO
	finance.cadastro_tipo (id_cadastro_tipo, no_cadastro_tipo)
VALUES
	(1, 'converter'),
	(2, 'Google'),
	(3, 'Facebook');

CREATE TABLE
	finance.usuario (
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

CREATE UNIQUE INDEX un_email_usuario ON finance.usuario (ds_email);

CREATE TABLE
	finance.empresa (
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

CREATE UNIQUE INDEX un_empresa ON finance.empresa (nu_cnpj_cpf);

CREATE TABLE
	finance.empresa_situacao (
		co_empresa_situacao CHAR(1) NOT NULL,
		no_empresa_situacao varchar(30) NOT NULL,
		CONSTRAINT pk_empresa_situacao PRIMARY KEY (co_empresa_situacao)
	);

INSERT INTO
	finance.empresa_situacao (co_empresa_situacao, no_empresa_situacao)
VALUES
	('A', 'Ativa'),
	('B', 'Bloqueada'),
	('E', 'Em Análise');

CREATE TABLE
	finance.perfil (
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
	finance.perfil_tipo (
		id_tipo_perfil serial4 NOT NULL,
		ds_tipo_perfil varchar(60) NOT NULL,
		CONSTRAINT pk_perfil_tipo PRIMARY KEY (id_tipo_perfil)
	);

CREATE TABLE
	finance.usuario_perfil (
		id_usuario serial4 NOT NULL,
		id_empresa int NOT NULL,
		id_perfil int NOT NULL,
		dt_ultimo_acesso TIMESTAMP NULL,
		in_ativo bool DEFAULT true NOT NULL,
		CONSTRAINT pk_usuario_perfil PRIMARY KEY (id_perfil, id_usuario, id_empresa)
	);

INSERT INTO
	finance.perfil_tipo (id_tipo_perfil, ds_tipo_perfil)
VALUES
	(1, 'Interno/Privado, acesso com validação'),
	(2, 'Externo/Publico, acesso sem validação');

CREATE TABLE
	finance.banco (
		id_banco serial4 NOT NULL,
		no_banco VARCHAR(100) NOT NULL,
		ds_banco VARCHAR(255) NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		in_excluido BOOL DEFAULT false NOT NULL,
		CONSTRAINT pk_banco PRIMARY KEY (id_banco)
	);

INSERT INTO
	finance.banco (no_banco, ds_banco, in_ativo, in_excluido)
VALUES
	(
		'Banco do Brasil',
		'Banco do Brasil S.A.',
		true,
		false
	),
	(
		'Caixa Econômica Federal',
		'Caixa Econômica Federal S.A.',
		true,
		false
	),
	('Itaú', 'Itaú Unibanco S.A.', true, false),
	('Bradesco', 'Banco Bradesco S.A.', true, false),
	(
		'Santander',
		'Banco Santander (Brasil) S.A.',
		true,
		false
	),
	('Inter', 'Banco Inter S.A.', true, false),
	('Safra', 'Banco Safra S.A.', true, false),
	('Original', 'Banco Original S.A.', true, false),
	('Pan', 'Banco Pan S.A.', true, false),
	(
		'BTG Pactual',
		'BTG Pactual Banco Múltiplo S.A.',
		true,
		false
	),
	('Modal', 'Banco Modal S.A.', true, false),
	('C6 Bank', 'C6 Bank S.A.', true, false),
	('Daycoval', 'Banco Daycoval S.A.', true, false),
	(
		'Votorantim',
		'Banco Votorantim S.A.',
		true,
		false
	),
	('Fibra', 'Banco Fibra S.A.', true, false),
	('BMG', 'Banco BMG S.A.', true, false),
	(
		'Mercantil do Brasil',
		'Banco Mercantil do Brasil S.A.',
		true,
		false
	),
	(
		'Rendimento',
		'Banco Rendimento S.A.',
		true,
		false
	),
	(
		'Modalmais',
		'Modalmais Banco Digital S.A.',
		true,
		false
	),
	('BS2', 'Banco BS2 S.A.', true, false),
	('Agibank', 'Agibank S.A.', true, false),
	(
		'Digimais',
		'Digimais Banco Digital S.A.',
		true,
		false
	),
	('Neon', 'Neon Pagamentos S.A.', true, false),
	('Cora', 'Cora S.A.', true, false),
	(
		'Intermedium',
		'Intermedium Banco Múltiplo S.A.',
		true,
		false
	),
	(
		'Sofisa Direto',
		'Sofisa Direto S.A.',
		true,
		false
	),
	(
		'B3',
		'B3 S.A. – Brasil, Bolsa, Balcão',
		true,
		false
	),
	('Nubank', 'Nubank S.A.', true, false),
	(
		'Outros',
		'Outros Bancos e Instituições Financeiras',
		true,
		false
	);

CREATE TABLE
	finance.conta_financeira (
		id_conta serial4 NOT NULL,
		id_empresa INT NOT NULL,
		no_conta VARCHAR(50) NOT NULL,
		id_tipo_conta INT NOT NULL,
		id_banco INT NOT NULL,
		saldo_inicial DECIMAL(14, 2) DEFAULT 0,
		id_usuario_criacao INT NOT NULL,
		id_usuario_alteracao INT NULL,
		dt_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
		dt_alteracao TIMESTAMP NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		CONSTRAINT pk_conta_financeira PRIMARY KEY (id_conta)
	);

CREATE TABLE
	finance.tipo_conta_financeira (
		id_tipo_conta serial4 NOT NULL,
		no_tipo_conta VARCHAR(100) NOT NULL,
		ds_tipo_conta VARCHAR(50) NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		in_excluido BOOL DEFAULT false NOT NULL,
		CONSTRAINT pk_tipo_conta_financeira PRIMARY KEY (id_tipo_conta)
	);

INSERT INTO
	finance.tipo_conta_financeira (
		no_tipo_conta,
		ds_tipo_conta,
		in_ativo,
		in_excluido
	)
VALUES
	(
		'Conta Corrente',
		'Conta Corrente Bancária',
		true,
		false
	),
	('Carteira', 'Carteira de Dinheiro', true, false),
	(
		'Cartão de Crédito',
		'Cartão de Crédito Utilizado',
		true,
		false
	),
	('Poupança', 'Conta Poupança', true, false),
	(
		'Investimento',
		'Conta de Investimentos',
		true,
		false
	),
	('Conta Digital', 'Conta Digital', true, false),
	('Conta Salário', 'Conta Salário', true, false),
	('Conta Conjunta', 'Conta Conjunta', true, false),
	(
		'Conta Empresarial',
		'Conta Empresarial',
		true,
		false
	),
	(
		'Conta de Pagamento',
		'Conta de Pagamento Pré-Paga',
		true,
		false
	),
	(
		'Conta Internacional',
		'Conta Internacional',
		true,
		false
	),
	(
		'Conta de Criptomoedas',
		'Conta de Criptomoedas',
		true,
		false
	),
	(
		'Conta de Investimentos',
		'Conta de Investimentos',
		true,
		false
	),
	(
		'Conta de Previdência',
		'Conta de Previdência Privada',
		true,
		false
	),
	('Conta de Câmbio', 'Conta de Câmbio', true, false),
	(
		'Conta de Financiamento',
		'Conta de Financiamento',
		true,
		false
	),
	(
		'Conta de Consórcio',
		'Conta de Consórcio',
		true,
		false
	),
	(
		'Conta de Empréstimo',
		'Conta de Empréstimo Pessoal',
		true,
		false
	),
	(
		'Conta de Cartão Pré-Pago',
		'Conta de Cartão Pré-Pago',
		true,
		false
	),
	(
		'Conta de Pagamento Digital',
		'Conta de Pagamento Digital',
		true,
		false
	),
	(
		'Conta de Microcrédito',
		'Conta de Microcrédito',
		true,
		false
	),
	(
		'Conta de Cooperativa de Crédito',
		'Conta de Cooperativa de Crédito',
		true,
		false
	),
	(
		'Conta de Financiamento Imobiliário',
		'Conta de Financiamento Imobiliário',
		true,
		false
	),
	(
		'Conta de Leasing',
		'Conta de Leasing Financeiro',
		true,
		false
	),
	(
		'Conta de Arrendamento Mercantil',
		'Conta de Arrendamento Mercantil',
		true,
		false
	),
	(
		'Conta de Cartão de Benefício',
		'Conta de Cartão de Benefício',
		true,
		false
	),
	(
		'Conta de Pagamento Instantâneo',
		'Conta de Pagamento Instantâneo',
		true,
		false
	),
	(
		'Conta de Pagamento Recorrente',
		'Conta de Pagamento Recorrente',
		true,
		false
	),
	(
		'Conta de Pagamento Internacional',
		'Conta de Pagamento Internacional',
		true,
		false
	),
	(
		'Conta de Pagamento Corporativo',
		'Conta de Pagamento Corporativo',
		true,
		false
	),
	(
		'Conta de Pagamento para E-commerce',
		'Conta de Pagamento para E-commerce',
		true,
		false
	),
	(
		'Conta de Pagamento para Marketplace',
		'Conta de Pagamento para Marketplace',
		true,
		false
	),
	(
		'Conta de Pagamento para Aplicativos',
		'Conta de Pagamento para Aplicativos',
		true,
		false
	),
	(
		'Conta de Pagamento para Negócios',
		'Conta de Pagamento para Negócios',
		true,
		false
	),
	(
		'Conta de Pagamento para Freelancers',
		'Conta de Pagamento para Freelancers',
		true,
		false
	),
	(
		'Conta de Pagamento para Autônomos',
		'Conta de Pagamento para Autônomos',
		true,
		false
	),
	(
		'Conta de Pagamento para Profissionais Liberais',
		'Conta de Pagamento para Profissionais Liberais',
		true,
		false
	),
	(
		'Conta de Pagamento para Microempreendedores Individuais (MEI)',
		'Conta de Pagamento para MEI',
		true,
		false
	),
	(
		'Conta de Pagamento para Pequenas Empresas',
		'Conta de Pagamento para Pequenas Empresas',
		true,
		false
	),
	(
		'Conta de Pagamento para Médias Empresas',
		'Conta de Pagamento para Médias Empresas',
		true,
		false
	),
	(
		'Conta de Pagamento para Grandes Empresas',
		'Conta de Pagamento para Grandes Empresas',
		true,
		false
	),
	(
		'Conta de Pagamento para Startups',
		'Conta de Pagamento para Startups',
		true,
		false
	),
	(
		'Conta de Pagamento para Organizações Sem Fins Lucrativos',
		'Conta de Pagamento para ONGs',
		true,
		false
	),
	(
		'Conta de Pagamento para Associações e Clubes',
		'Conta de Pagamento para Associações e Clubes',
		true,
		false
	),
	(
		'Outros',
		'Outras Contas Financeiras',
		true,
		false
	);

CREATE TABLE
	finance.forma_pagamento (
		id_forma_pagamento serial4 NOT NULL,
		id_empresa INT NULL,
		no_forma_pagamento VARCHAR(50),
		ds_forma_pagamento VARCHAR(100),
		dt_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
		dt_alteracao TIMESTAMP NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		in_excluido BOOL DEFAULT false NOT NULL,
		CONSTRAINT pk_forma_pagamento PRIMARY KEY (id_forma_pagamento)
	);

INSERT INTO
	finance.forma_pagamento (
		no_forma_pagamento,
		ds_forma_pagamento,
		in_ativo,
		in_excluido
	)
VALUES
	('Dinheiro', 'Pagamento em Dinheiro', true, false),
	(
		'Cartão de Crédito',
		'Pagamento com Cartão de Crédito',
		true,
		false
	),
	(
		'Cartão de Débito',
		'Pagamento com Cartão de Débito',
		true,
		false
	),
	(
		'Transferência Bancária',
		'Pagamento via Transferência Bancária',
		true,
		false
	),
	('PIX', 'Pagamento via PIX', true, false),
	(
		'Boleto Bancário',
		'Pagamento via Boleto Bancário',
		true,
		false
	),
	('Cheque', 'Pagamento com Cheque', true, false),
	(
		'Criptomoedas',
		'Pagamento com Criptomoedas',
		true,
		false
	),
	(
		'Vale Alimentação',
		'Pagamento com Vale Alimentação',
		true,
		false
	),
	(
		'Vale Refeição',
		'Pagamento com Vale Refeição',
		true,
		false
	),
	(
		'Vale Transporte',
		'Pagamento com Vale Transporte',
		true,
		false
	),
	(
		'Outros',
		'Outros Meios de Pagamento',
		true,
		false
	);

CREATE TABLE
	finance.categoria_financeira (
		id_categoria serial4 NOT NULL,
		id_empresa INT NULL,
		no_categoria VARCHAR(100),
		no_tipo VARCHAR(10) CHECK (no_tipo IN ('RECEITA', 'DESPESA')),
		in_ativo BOOL DEFAULT true NOT NULL,
		in_excluido BOOL DEFAULT false NOT NULL,
		CONSTRAINT pk_categoria_financeira PRIMARY KEY (id_categoria)
	);

INSERT INTO
	finance.categoria_financeira (
		no_categoria,
		no_tipo,
		id_empresa,
		in_ativo,
		in_excluido
	)
VALUES
	('Alimentação', 'DESPESA', null, true, false),
	('Transporte', 'DESPESA', null, true, false),
	('Saúde', 'DESPESA', null, true, false),
	('Educação', 'DESPESA', null, true, false),
	('Lazer', 'DESPESA', null, true, false),
	('Moradia', 'DESPESA', null, true, false),
	('Serviços Públicos', 'DESPESA', null, true, false),
	('Impostos e Taxas', 'DESPESA', null, true, false),
	('Renda Variável', 'RECEITA', null, true, false),
	('Renda Fixa', 'RECEITA', null, true, false),
	(
		'Investimentos Diversos',
		'RECEITA',
		null,
		true,
		false
	),
	('Salário', 'RECEITA', null, true, false),
	('Freelance', 'RECEITA', null, true, false),
	('Aluguel', 'RECEITA', null, true, false),
	('Dividendos', 'RECEITA', null, true, false),
	(
		'Juros e Multas Recebidas',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Doações e Contribuições',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Prêmios e Sorteios',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Reembolso de Despesas',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Vendas de Produtos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Vendas de Serviços',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Comissões Recebidas',
		'RECEITA',
		null,
		true,
		false
	),
	('Consultoria', 'RECEITA', null, true, false),
	('Honorários', 'RECEITA', null, true, false),
	('Royalties', 'RECEITA', null, true, false),
	(
		'Licenciamento de Software',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas Financeiras',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Aluguel',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Franquia',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Publicidade',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Assinatura',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Parcerias',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Eventos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Cursos e Treinamentos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Consultoria',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Licenciamento',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Venda de Ativos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços Financeiros',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços Jurídicos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços Contábeis',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços Administrativos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Marketing',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de TI',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Design',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria Empresarial',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria Financeira',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria Jurídica',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria Contábil',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em TI',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Marketing',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Design',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Recursos Humanos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Vendas',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Logística',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Produção',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Qualidade',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Segurança do Trabalho',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Meio Ambiente',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Sustentabilidade',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Inovação',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Tecnologia',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Transformação Digital',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão Empresarial',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão Financeira',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão Jurídica',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão Contábil',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão Tributária',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão Comercial',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão Operacional',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão de Pessoas',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão de Projetos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão de Processos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão da Qualidade',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão da Segurança',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão do Meio Ambiente',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão da Sustentabilidade',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão da Inovação',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão da Tecnologia',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Receitas de Serviços de Consultoria em Gestão da Transformação Digital',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Aluguel de Imóveis',
		'RECEITA',
		null,
		true,
		false
	),
	('Venda de Imóveis', 'RECEITA', null, true, false),
	('Venda de Veículos', 'RECEITA', null, true, false),
	(
		'Venda de Equipamentos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Mercadorias',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Digitais',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços Digitais',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Licenças de Software',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Assinaturas de Software',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Cursos Online',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Consultorias Online',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Físicos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços Físicos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Importados',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Nacionais',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Personalizados',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Artesanais',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Industriais',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Agrícolas',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Pecuários',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Florestais',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Pesqueiros',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Minerais',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Químicos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Farmacêuticos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Cosméticos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos de Limpeza',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos de Higiene Pessoal',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos de Beleza',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos de Saúde',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos de Bem-Estar',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Esportivos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Eletrônicos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Eletrodomésticos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos Automotivos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos para Casa e Jardim',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos para Animais',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos para Bebês e Crianças',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos para Moda e Acessórios',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Produtos para Tecnologia e Informática',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços Financeiros',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços Jurídicos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços Contábeis',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços Administrativos',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços de Marketing Digital',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços de Design Gráfico',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços de Desenvolvimento Web',
		'RECEITA',
		null,
		true,
		false
	),
	(
		'Venda de Serviços de Consultoria Empresarial',
		'RECEITA',
		null,
		true,
		false
	),
	('Outros', 'DESPESA', null, true, false),
	('Outros', 'RECEITA', null, true, false);

CREATE TABLE
	finance.centro_custo (
		id_centro serial4 NOT NULL,
		id_empresa INT,
		no_centro VARCHAR(100),
		CONSTRAINT pk_centro_custo PRIMARY KEY (id_centro),
		FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa)
	);

INSERT INTO
	finance.centro_custo (no_centro, id_empresa)
VALUES
	('Marketing', NULL),
	('Vendas', NULL),
	('Recursos Humanos', NULL),
	('Financeiro', NULL),
	('Operações', NULL),
	('TI', NULL),
	('Jurídico', NULL),
	('Logística', NULL),
	('Pesquisa e Desenvolvimento', NULL),
	('Atendimento ao Cliente', NULL),
	('Suporte Técnico', NULL),
	('Administração Geral', NULL),
	('Compras e Suprimentos', NULL),
	('Produção e Fabricação', NULL),
	('Qualidade e Controle', NULL),
	('Manutenção e Reparos', NULL),
	('Projetos Especiais', NULL),
	('Treinamento e Capacitação', NULL),
	('Inovação e Tecnologia', NULL),
	(
		'Sustentabilidade e Responsabilidade Social',
		NULL
	),
	('Expansão e Crescimento', NULL),
	('Estratégia e Planejamento', NULL),
	('Comunicação e Relações Públicas', NULL),
	('Desenvolvimento de Produtos', NULL),
	('Gestão de Riscos', NULL),
	('Compliance e Conformidade', NULL),
	('Segurança da Informação', NULL),
	('Gestão de Dados e Análise', NULL),
	('Gestão de Processos', NULL),
	('Gestão de Mudanças', NULL),
	('Gestão de Crises', NULL),
	('Gestão de Stakeholders', NULL),
	('Gestão de Parcerias Estratégicas', NULL),
	('Gestão de Fornecedores', NULL),
	('Gestão de Clientes', NULL),
	('Gestão de Projetos e Portfólios', NULL),
	('Carro', NULL),
	('Casa', NULL),
	('Saúde', NULL),
	('Educação', NULL),
	('Entretenimento', NULL),
	('Viagens', NULL),
	('Faculdade', NULL),
	('Cursos e Treinamentos', NULL),
	('Assinaturas e Serviços Online', NULL),
	('Internet e Telefonia', NULL),
	('Seguros', NULL),
	('Impostos e Taxas', NULL),
	('Manutenção de Veículos', NULL),
	('Reformas e Manutenção Residencial', NULL),
	('Aluguel e Financiamento Imobiliário', NULL),
	('Compras de Móveis e Eletrodomésticos', NULL),
	('Compras de Roupas e Acessórios', NULL),
	(
		'Compras de Produtos de Beleza e Cuidados Pessoais',
		NULL
	),
	('Compras de Alimentos e Bebidas', NULL),
	('Compras de Produtos Eletrônicos', NULL),
	('Tecnologia', NULL),
	('Alimentação', NULL),
	('Vestuário', NULL),
	('Beleza e Cuidados Pessoais', NULL),
	('Esportes e Lazer', NULL),
	('Animais de Estimação', NULL),
	('Serviços Públicos', NULL),
	('Impostos e Taxas', NULL),
	('Outros Custos Variáveis', NULL);

CREATE TABLE
	finance.cartao (
		id_cartao serial4 NOT NULL,
		id_empresa INT NOT NULL,
		id_banco INT NOT NULL,
		id_bandeira INT NOT NULL,
		id_tipo_conta INT NOT NULL,
		id_tipo_cartao INT NOT NULL,
		no_cartao VARCHAR(50) NOT NULL,
		ds_cartao VARCHAR(100) NULL,
		id_usuario_criacao INT NOT NULL,
		id_usuario_alteracao INT NULL,
		dt_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
		dt_alteracao TIMESTAMP NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		in_excluido BOOL DEFAULT false NOT NULL,
		CONSTRAINT pk_cartao PRIMARY KEY (id_cartao)
	);

CREATE TABLE
	finance.bandeira_cartao (
		id_bandeira serial4 NOT NULL,
		no_bandeira VARCHAR(50) NOT NULL,
		ds_bandeira VARCHAR(100) NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		in_excluido BOOL DEFAULT false NOT NULL,
		CONSTRAINT pk_bandeira_cartao PRIMARY KEY (id_bandeira)
	);

INSERT INTO
	finance.bandeira_cartao (no_bandeira, ds_bandeira)
VALUES
	('Visa', 'Cartão Visa'),
	('Mastercard', 'Cartão Mastercard'),
	('American Express', 'Cartão American Express'),
	('Elo', 'Cartão Elo'),
	('Hipercard', 'Cartão Hipercard'),
	('Diners Club', 'Cartão Diners Club'),
	('Discover', 'Cartão Discover'),
	('JCB', 'Cartão JCB'),
	('Aura', 'Cartão Aura'),
	('Outros', 'Outras bandeiras de cartões');

CREATE TABLE
	finance.tipo_cartao (
		id_tipo_cartao serial4 NOT NULL,
		no_tipo_cartao VARCHAR(50) NOT NULL,
		ds_tipo_cartao VARCHAR(100) NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		in_excluido BOOL DEFAULT false NOT NULL,
		CONSTRAINT pk_tipo_cartao PRIMARY KEY (id_tipo_cartao)
	);

INSERT INTO
	finance.tipo_cartao (no_tipo_cartao, ds_tipo_cartao)
VALUES
	(
		'Crédito',
		'Cartão de crédito para compras parceladas'
	),
	('Débito', 'Cartão de débito para compras à vista'),
	(
		'Pré-pago',
		'Cartão recarregável utilizado em viagens ou apps'
	),
	(
		'Corporativo',
		'Cartão corporativo para despesas empresariais'
	),
	(
		'Benefício',
		'Cartão de benefício para vale-alimentação ou refeição'
	),
	('Virtual', 'Cartão virtual para compras online'),
	(
		'Fidelidade',
		'Cartão de fidelidade para acumular pontos e recompensas'
	),
	('Outros', 'Outros tipos de cartões financeiros');

CREATE TABLE
	finance.movimentacao (
		id_movimentacao serial4 NOT NULL,
		id_empresa INT,
		id_conta INT,
		id_forma_pagamento INT,
		id_categoria INT,
		id_centro INT,
		ds_movimentacao VARCHAR(100) NULL,
		no_tipo VARCHAR(10) CHECK (no_tipo IN ('RECEITA', 'DESPESA')),
		vl_total DECIMAL(14, 2) NOT NULL,
		dt_vencimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
		dt_pagamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
		in_pago BOOL DEFAULT false NOT NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		id_usuario_criacao INT NOT NULL,
		id_usuario_alteracao INT NULL,
		dt_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
		dt_alteracao TIMESTAMP NULL,
		CONSTRAINT pk_movimentacao PRIMARY KEY (id_movimentacao)
	);

CREATE TABLE
	finance.detalhe_movimentacao (
		id_detalhe_movimentacao serial4 NOT NULL,
		id_movimentacao INT NOT NULL,
		ds_detalhe_movimentacao VARCHAR(100) NULL,
		vl_detalhe_movimentacao DECIMAL(14, 2) NOT NULL,
		dt_detalhe_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
		in_ativo BOOL DEFAULT true NOT NULL,
		id_usuario_criacao INT NOT NULL,
		id_usuario_alteracao INT NULL,
		dt_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
		dt_alteracao TIMESTAMP NULL,
		CONSTRAINT pk_detalhe_movimentacao PRIMARY KEY (id_detalhe_movimentacao),
		FOREIGN KEY (id_movimentacao) REFERENCES finance.movimentacao (id_movimentacao)
	);

CREATE TABLE
	finance.parcelamento (
		id_parcela serial4 NOT NULL,
		id_movimentacao INT,
		nu_parcela INT,
		vl_parcela DECIMAL(14, 2),
		dt_vencimento DATE,
		dt_pagamento DATE,
		in_pago BOOL DEFAULT false NOT NULL,
		CONSTRAINT pk_parcelamento PRIMARY KEY (id_parcela)
	);

CREATE TABLE
	finance.recorrencia (
		id_recorrencia serial4 NOT NULL,
		id_empresa INT,
		ds_recorrencia VARCHAR(100),
		no_tipo VARCHAR(10) CHECK (no_tipo IN ('RECEITA', 'DESPESA')),
		ds_frequencia VARCHAR(20) NOT NULL CHECK (ds_frequencia IN ('MENSAL', 'SEMESTRAL', 'ANUAL')),
		dt_vencimento DATE,
		vl_recorrencia DECIMAL(14, 2),
		in_ativo BOOLEAN DEFAULT TRUE,
		in_excluido BOOLEAN DEFAULT FALSE,
		id_usuario_criacao INT NOT NULL,
		id_usuario_alteracao INT NULL,
		CONSTRAINT pk_recorrencia PRIMARY KEY (id_recorrencia),
		FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa)
	);

ALTER TABLE finance.usuario ADD CONSTRAINT fk_ultima_empresa FOREIGN KEY (id_ultima_empresa) REFERENCES finance.empresa (id_empresa);

ALTER TABLE finance.usuario ADD CONSTRAINT fk_usuario_ref_cadastro_tipo FOREIGN KEY (id_cadastro_tipo) REFERENCES finance.cadastro_tipo (id_cadastro_tipo) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.usuario ADD CONSTRAINT fk_usuario_ref_usuario_alt FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.usuario ADD CONSTRAINT fk_usuario_ref_usuario_cri FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.perfil ADD CONSTRAINT fk_perfil_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.perfil ADD CONSTRAINT fk_perfil_ref_perfil_tipo FOREIGN KEY (id_tipo_perfil) REFERENCES finance.perfil_tipo (id_tipo_perfil) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.perfil ADD CONSTRAINT fk_perfil_ref_usuario_alt FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.perfil ADD CONSTRAINT fk_perfil_ref_usuario_cri FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.usuario_perfil ADD CONSTRAINT fk_usuario_perfil_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.usuario_perfil ADD CONSTRAINT fk_usuario_perfil_ref_perfil FOREIGN KEY (id_perfil) REFERENCES finance.perfil (id_perfil) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.usuario_perfil ADD CONSTRAINT fk_usuario_perfil_ref_usuario FOREIGN KEY (id_usuario) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.empresa ADD CONSTRAINT fk_empresa_ref_usuario_alt FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.empresa ADD CONSTRAINT fk_empresa_sit_ref_empres FOREIGN KEY (co_empresa_situacao) REFERENCES finance.empresa_situacao (co_empresa_situacao) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.empresa ADD CONSTRAINT fk_usuario_criacao FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario);

ALTER TABLE finance.categoria_financeira ADD CONSTRAINT fk_categoria_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.centro_custo ADD CONSTRAINT fk_centro_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.movimentacao ADD CONSTRAINT fk_movimentacao_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa);

ALTER TABLE finance.movimentacao ADD CONSTRAINT fk_movimentacao_ref_conta FOREIGN KEY (id_conta) REFERENCES finance.conta_financeira (id_conta);

ALTER TABLE finance.movimentacao ADD CONSTRAINT fk_movimentacao_ref_categoria FOREIGN KEY (id_categoria) REFERENCES finance.categoria_financeira (id_categoria);

ALTER TABLE finance.movimentacao ADD CONSTRAINT fk_movimentacao_ref_centro FOREIGN KEY (id_centro) REFERENCES finance.centro_custo (id_centro);

ALTER TABLE finance.movimentacao ADD CONSTRAINT fk_movimentacao_ref_forma_pagamento FOREIGN KEY (id_forma_pagamento) REFERENCES finance.forma_pagamento (id_forma_pagamento);

ALTER TABLE finance.movimentacao ADD CONSTRAINT fk_movimentacao_ref_usuario_criacao FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario);

ALTER TABLE finance.movimentacao ADD CONSTRAINT fk_movimentacao_ref_usuario_alteracao FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario);

ALTER TABLE finance.detalhe_movimentacao ADD CONSTRAINT fk_detalhe_movimentacao_ref_movimentacao FOREIGN KEY (id_movimentacao) REFERENCES finance.movimentacao (id_movimentacao) ON DELETE CASCADE ON UPDATE RESTRICT;

ALTER TABLE finance.detalhe_movimentacao ADD CONSTRAINT fk_detalhe_movimentacao_ref_usuario_criacao FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.detalhe_movimentacao ADD CONSTRAINT fk_detalhe_movimentacao_ref_usuario_alteracao FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.parcelamento ADD CONSTRAINT fk_parcelamento_ref_movimentacao FOREIGN KEY (id_movimentacao) REFERENCES finance.movimentacao (id_movimentacao) ON DELETE CASCADE ON UPDATE RESTRICT;

ALTER TABLE finance.recorrencia ADD CONSTRAINT fk_recorrencia_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.recorrencia ADD CONSTRAINT fk_recorrencia_ref_usuario_criacao FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.recorrencia ADD CONSTRAINT fk_recorrencia_ref_usuario_alteracao FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.conta_financeira ADD CONSTRAINT fk_conta_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.conta_financeira ADD CONSTRAINT fk_conta_ref_tipo_conta FOREIGN KEY (id_tipo_conta) REFERENCES finance.tipo_conta_financeira (id_tipo_conta) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.conta_financeira ADD CONSTRAINT fk_conta_ref_banco FOREIGN KEY (id_banco) REFERENCES finance.banco (id_banco) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.conta_financeira ADD CONSTRAINT fk_conta_ref_usuario_criacao FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario);

ALTER TABLE finance.conta_financeira ADD CONSTRAINT fk_conta_ref_usuario_alteracao FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario);

ALTER TABLE finance.forma_pagamento ADD CONSTRAINT fk_forma_pagamento_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.cartao ADD CONSTRAINT fk_cartao_ref_empresa FOREIGN KEY (id_empresa) REFERENCES finance.empresa (id_empresa) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.cartao ADD CONSTRAINT fk_cartao_ref_banco FOREIGN KEY (id_banco) REFERENCES finance.banco (id_banco) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.cartao ADD CONSTRAINT fk_cartao_ref_bandeira FOREIGN KEY (id_bandeira) REFERENCES finance.bandeira_cartao (id_bandeira) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.cartao ADD CONSTRAINT fk_cartao_ref_tipo_conta FOREIGN KEY (id_tipo_conta) REFERENCES finance.tipo_conta_financeira (id_tipo_conta) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.cartao ADD CONSTRAINT fk_cartao_ref_tipo_cartao FOREIGN KEY (id_tipo_cartao) REFERENCES finance.tipo_cartao (id_tipo_cartao) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.cartao ADD CONSTRAINT fk_cartao_ref_usuario_criacao FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE finance.cartao ADD CONSTRAINT fk_cartao_ref_usuario_alteracao FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

INSERT INTO
	finance.usuario (
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
		'sistema@finance.com.br',
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
	finance.empresa (
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
	finance.empresa_situacao (co_empresa_situacao, no_empresa_situacao)
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

ALTER TABLE public.esqueleto ADD CONSTRAINT fk_esqueleto_ref_usuario_alt FOREIGN KEY (id_usuario_alteracao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE public.esqueleto ADD CONSTRAINT fk_esqueleto_ref_usuario_cri FOREIGN KEY (id_usuario_criacao) REFERENCES finance.usuario (id_usuario) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE
OR REPLACE VIEW finance.vw_acessos AS
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
	finance.usuario u
	JOIN finance.usuario_perfil up ON u.id_usuario = up.id_usuario
	JOIN finance.perfil p ON up.id_perfil = p.id_perfil
	JOIN finance.empresa e ON up.id_empresa = e.id_empresa
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
	finance.perfil (
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
	finance.perfil (
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
	finance.perfil (
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
	finance.perfil (
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
		'Usuário encarregado das operações financeiras, com acesso a relatórios, despesas, receitas e fluxo de caixa.',
		'Financeiro',
		true,
		true,
		3,
		3,
		'2024-03-22 09:26:31.497',
		NULL,
		NULL
	);

INSERT INTO
	finance.perfil (
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

ALTER TABLE finance.conta_financeira
ALTER COLUMN id_empresa
DROP NOT NULL;

ALTER TABLE finance.conta_financeira
ADD COLUMN id_usuario INT NOT NULL;

ALTER TABLE finance.conta_financeira ADD CONSTRAINT fk_conta_financeira_usuario FOREIGN KEY (id_usuario) REFERENCES finance.usuario (id_usuario);

ALTER TABLE finance.movimentacao
ADD COLUMN id_usuario INT NOT NULL;

ALTER TABLE finance.movimentacao ADD CONSTRAINT fk_movimentacao_usuario FOREIGN KEY (id_usuario) REFERENCES finance.usuario (id_usuario);

-- 23/07/25
DROP VIEW IF EXISTS finance.vw_acessos;

CREATE
OR REPLACE VIEW finance.vw_acessos AS
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
	finance.usuario u
	JOIN finance.usuario_perfil up ON u.id_usuario = up.id_usuario
	JOIN finance.perfil p ON up.id_perfil = p.id_perfil
	LEFT JOIN finance.empresa e ON up.id_empresa = e.id_empresa
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

ALTER TABLE finance.usuario_perfil
DROP CONSTRAINT pk_usuario_perfil;

ALTER TABLE finance.usuario_perfil ADD CONSTRAINT pk_usuario_perfil PRIMARY KEY (id_usuario, id_perfil);

ALTER TABLE finance.usuario_perfil
ALTER COLUMN id_empresa
DROP NOT NULL;

ALTER TABLE finance.movimentacao
ALTER COLUMN no_tipo
SET NOT NULL;

ALTER TABLE finance.movimentacao
ADD COLUMN in_excluido BOOL DEFAULT false NOT NULL;

ALTER TABLE finance.movimentacao
ADD COLUMN ds_observacao TEXT NULL;

ALTER TABLE finance.parcelamento
ADD COLUMN in_ativo BOOL DEFAULT true NOT NULL;

ALTER TABLE finance.parcelamento
ADD COLUMN in_excluido BOOL DEFAULT false NOT NULL;

ALTER TABLE finance.centro_custo
ADD COLUMN id_usuario INT;

ALTER TABLE finance.centro_custo
ADD CONSTRAINT fk_centro_custo_usuario
FOREIGN KEY (id_usuario) REFERENCES finance.usuario(id_usuario)
ON DELETE RESTRICT ON UPDATE RESTRICT;