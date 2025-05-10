CREATE TABLE categoria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- exemplo: 'receita' ou 'despesa'
    descricao VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE orcamento (
    id SERIAL PRIMARY KEY,
    id_categoria INTEGER NOT NULL,
    valor_limite NUMERIC(10,2) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);

INSERT INTO categoria (nome, tipo, descricao, ativo) VALUES 
('Salário Mensal', 'receita', 'Renda principal proveniente do trabalho', TRUE),
('Aluguel Residencial', 'despesa', 'Pagamento mensal do imóvel', TRUE);

INSERT INTO orcamento (id_categoria, valor_limite, data_inicio, data_fim) VALUES 
(2, 1200.00, '2025-05-01', '2025-10-01');

