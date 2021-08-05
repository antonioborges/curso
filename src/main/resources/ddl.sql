create table formas_pagamento (
	id bigint not null auto_increment, 
	descricao varchar(255) not null, 
	
	primary key (id)
) engine=InnoDB default charset=utf8; 


create table grupo (
	id bigint not null auto_increment, 
	nome varchar(255) not null, 
	
	primary key (id)
) engine=InnoDB default charset=utf8;


create table grupo_permissao (
	grupo_id bigint not null, 
	permissao_id bigint not null,
	
	primary key (grupo_id, permissao_id)
) engine=InnoDB default charset=utf8;


create table permissao (
	id bigint not null auto_increment, 
	nome varchar(255) not null, 
	descricao varchar(255) not null, 
	
	primary key(id)
) engine=InnoDB default charset=utf8;
 
 
create table produto (
	id bigint not null auto_increment, 
	restaurante_id bigint not null, 
	nome varchar(255) not null,
	descricao text not null,  
	preco decimal(10,2) not null, 
	ativo tinyint(1) not null,
	
	primary key (id)
) engine=InnoDB default charset=utf8;


create table restaurante (
	id bigint not null auto_increment, 
	cozinha_id bigint not null, 
	nome varchar(255) not null, 
	taxa_frete decimal(10,2) not null, 
	data_atualizacao datetime not null, 
	data_cadastro datetime not null, 
	
	endereco_cidade_id bigint, 
	endereco_cep varchar(255), 
	endereco_logradouro varchar(255), 
	endereco_numero varchar(255), 
	endereco_complemento varchar(255), 
	endereco_bairro varchar(255), 
	
	primary key (id)
) engine=InnoDB default charset=utf8;


create table restaurante_forma_pagamento (
	restaurante_id bigint not null, 
	forma_pagamento_id bigint not null,
	
	primary key (restaurante_id, forma_pagamento_id)
) engine=InnoDB default charset=utf8;


create table usuario (
	id bigint not null auto_increment, 
	nome varchar(255) not null, 
	senha varchar(255) not null, 
	data_cadastro datetime not null, 
	email varchar(255) not null, 
	
	
	primary key (id)
) engine=InnoDB default charset=utf8;


create table usuario_grupo (
	usuario_id bigint not null, 
	grupo_id bigint not null,
	
	primary key (usuario_id, grupo_id)
) engine=InnoDB default charset=utf8;


alter table grupo_permissao add constraint fk_grupo_permissao_permissao	 
foreign key (permissao_id) references permissao (id);

alter table grupo_permissao add constraint fk_grupo_permissao_grupo 
foreign key (grupo_id) references grupo (id);

alter table produto add constraint fk_produto_restaurante 
foreign key (restaurante_id) references restaurante (id);

alter table restaurante add constraint fk_restaurante_cozinha 
foreign key (cozinha_id) references cozinha (id);

alter table restaurante add constraint fk_restaurante_cidade 
foreign key (endereco_cidade_id) references cidade (id);

alter table restaurante_forma_pagamento add constraint fk_rest_forma_pagto_forma_pagto 
foreign key (forma_pagamento_id) references formas_pagamento (id);

alter table restaurante_forma_pagamento add constraint fk_rest_forma_pagto_restaurante 
foreign key (restaurante_id) references restaurante (id);

alter table usuario_grupo add constraint fk_usuario_grupo_grupo 
foreign key (grupo_id) references grupo (id);	

alter table usuario_grupo add constraint fk_usuario_grupo_usuario 
foreign key (usuario_id) references usuario (id);







create table pedido (
	id bigint not null auto_increment, 
	data_cancelamento datetime, 
	data_confirmacao datetime, 
	data_criacao datetime not null, 
	data_entrega datetime, 
	
	endereco_cidade_id bigint, 
	endereco_bairro varchar(255), 
	endereco_cep varchar(255), 
	endereco_complemento varchar(255), 
	endereco_logradouro varchar(255), 
	endereco_numero varchar(255), 
	
	status varchar(10) not null, 
	subtotal decimal(19,2) not null, 
	taxa_frete decimal(19,2) not null, 
	valor_total decimal(19,2) not null, 
	usuario_cliente_id bigint not null, 
	
	forma_pagamento_id bigint not null, 
	restaurante_id bigint not null, 
	
	primary key (id),
	) engine=InnoDB default charset=utf8;






	alter table pedido add constraint fk_pedido_endereco_cidade foreign key (endereco_cidade_id) references cidade (id),
    	alter table pedido add constraint fk_pedido_restaurante foreign key (restaurante_id) references restaurante (id),
    	alter table pedido add constraint fk_pedido_usuario_cliente foreign key (usuario_cliente_id) references usuario (id),
    	alter table pedido add constraint fk_pedido_forma_pagamento foreign key (forma_pagamento_id) references forma_pagamento (id)



create table item_pedido (
    id bigint not null auto_increment,
    quantidade smallint(6) not null,
    preco_unitario decimal(10,2) not null,
    preco_total decimal(10,2) not null,
    observacao varchar(255) null,
    pedido_id bigint not null,
    produto_id bigint not null,
    
    primary key (id),
    unique key uk_item_pedido_produto (pedido_id, produto_id),

    constraint fk_item_pedido_pedido foreign key (pedido_id) references pedido (id),
    constraint fk_item_pedido_produto foreign key (produto_id) references produto (id)
) engine=InnoDB default charset=utf8;

create table item_pedido (id bigint not null auto_increment, observacao varchar(255), preco_total decimal(19,2), preco_unitario decimal(19,2), quantidade integer, pedido_id bigint not null, produto_id bigint not null, primary key (id)) engine=MyISAM













alter table item_pedido add constraint FK60ym08cfoysa17wrn1swyiuda foreign key (pedido_id) references pedido (id)
alter table item_pedido add constraint FKtk55mn6d6bvl5h0no5uagi3sf foreign key (produto_id) references produto (id)





