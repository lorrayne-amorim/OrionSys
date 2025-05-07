CREATE TABLE categoria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- exemplo: 'receita' ou 'despesa'
    descricao VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE meta_financeira (
    id SERIAL PRIMARY KEY,
    objetivo VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    valor_limite NUMERIC(10,2) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL
);

INSERT INTO categoria (nome, tipo, descricao, ativo) VALUES 
('Salário Mensal', 'receita', 'Renda principal proveniente do trabalho', TRUE),
('Aluguel Residencial', 'despesa', 'Pagamento mensal do imóvel', TRUE);

INSERT INTO meta_financeira (objetivo, categoria, valor_limite, data_inicio, data_fim) VALUES 
('Economizar para viagem', 'receita', 3000.00, '2025-06-01', '2025-12-01'),
('Reduzir gastos com aluguel', 'despesa', 1200.00, '2025-05-01', '2025-10-01');

