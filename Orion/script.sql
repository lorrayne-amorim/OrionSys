CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    saldo NUMERIC(12,2) DEFAULT 0,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE categoria (
    id_categoria SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('receita', 'despesa')),
    descricao TEXT,
    prioridade VARCHAR(10) DEFAULT 'Média' CHECK (prioridade IN ('Alta', 'Média', 'Baixa')),
    recorrente BOOLEAN DEFAULT FALSE
);


CREATE TABLE local_transacao (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

<<<<<<< HEAD
CREATE TABLE meta_financeira (
    id SERIAL PRIMARY KEY,
    objetivo VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    valor_limite NUMERIC(10,2) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL
=======
CREATE TABLE orcamento (
    id_orcamento SERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    id_categoria INT NOT NULL REFERENCES categoria(id_categoria) ON DELETE CASCADE,
    valor_limite NUMERIC(12,2) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    id_forma_pagamento INTEGER REFERENCES forma_pagamento(id),
    id_tipo_orcamento INTEGER REFERENCES tipo_orcamento(id),
    id_status INTEGER REFERENCES status_orcamento(id)
>>>>>>> ab2689a (Implementação dos 5 processos em Orçamento - não consegui testar pra validar)
);

CREATE TABLE transacao (
    id_transacao SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    id_categoria INT NOT NULL REFERENCES categoria(id_categoria),
    id_local INT NOT NULL REFERENCES local_transacao(id),
    valor NUMERIC(12,2) NOT NULL,
    data TIMESTAMP NOT NULL,
    forma_pagamento VARCHAR(20)
);

<<<<<<< HEAD
INSERT INTO meta_financeira (objetivo, categoria, valor_limite, data_inicio, data_fim) VALUES 
('Economizar para viagem', 'receita', 3000.00, '2025-06-01', '2025-12-01'),
('Reduzir gastos com aluguel', 'despesa', 1200.00, '2025-05-01', '2025-10-01');
=======
CREATE TABLE forma_pagamento (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL
);
>>>>>>> ab2689a (Implementação dos 5 processos em Orçamento - não consegui testar pra validar)

CREATE TABLE cartao_credito (
    id_cartao SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    nome_cartao VARCHAR(100) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    bandeira VARCHAR(20) NOT NULL,
    limite NUMERIC(12,2) NOT NULL,
    fatura_atual NUMERIC(12,2) DEFAULT 0,
    data_fechamento INT NOT NULL, 
    data_vencimento INT NOT NULL
);

CREATE TABLE tipo_orcamento (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL
);

CREATE TABLE status_orcamento (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(20) NOT NULL
);

INSERT INTO tipo_orcamento (nome) VALUES ('Fixo'), ('Flexível'), ('Emergencial');


INSERT INTO status_orcamento (nome) VALUES ('Ativo'), ('Suspenso'), ('Expirado');


INSERT INTO forma_pagamento (nome) VALUES ('Crédito'), ('Débito'), ('PIX'), ('Dinheiro');

-- Inserts padrão
INSERT INTO forma_pagamento (nome) VALUES 
('avista'), ('cartão'), ('pix'), ('transferencia'), ('outros');

-- Inserir usuário
INSERT INTO usuario (nome, email, data_nascimento, senha)
VALUES ('Lorrayne Amorim', 'lorrayne@example.com', '2002-08-15', '123');

-- Inserir categorias
INSERT INTO categoria (nome, tipo, descricao, prioridade, recorrente)
VALUES 
('Salário', 'receita', 'Recebimento mensal do trabalho', 'Alta', TRUE),
('Aluguel', 'despesa', 'Pagamento mensal do imóvel', 'Alta', TRUE),
('Supermercado', 'despesa', 'Compras do mês', 'Média', TRUE),
('Lazer', 'despesa', 'Cinema, bares, viagens', 'Baixa', FALSE);

-- Inserir locais
INSERT INTO local_transacao (nome)
VALUES 
('Supermercado Perim'),
('Shopping Sul'),
('Posto Ipiranga'),
('Restaurante Tradição Mineira'),
('Farmácia Santa Lúcia');

-- Inserir orçamento para a categoria "Supermercado"
-- Supondo que o ID da categoria "Supermercado" seja 3
INSERT INTO orcamento (titulo, id_categoria, valor_limite, data_inicio, data_fim)
VALUES ('Orçamento Mercado Mensal', 3, 1500.00, '2025-05-01', '2025-05-31');