insert into cozinha (id, nome) values (1,'Tailandesa');
insert into cozinha (id, nome) values (2,'Indiana');

insert into restaurante (nome,taxa_frete, cozinha_id) values ('Masume',10.0, 1);
insert into restaurante (nome,taxa_frete, cozinha_id) values ('Subway',15.2, 2);

insert into forma_pagamento (descricao) values ('Cartao');
insert into forma_pagamento (descricao) values ('Boleto');

insert into estado (nome) values ('Minas Gerais');
insert into estado (nome) values ('Bahia');

insert into cidade (nome,estado_id) values ('Timoteo', 1);
insert into cidade (nome, estado_id) values ('Porto Seguro', 2);
