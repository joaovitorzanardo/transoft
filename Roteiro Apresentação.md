# Roteiro da Apresentação do Software

## Aplicação Web

O fluxo de cadastro irá seguir a seguinte ordem na maioria dos casos:

- **1. Criar Conta:** O responsável pela empresa entra no site da transoft e faz o cadastro da sua empresa;
- **2. Cadastrar Veículo:** O responsável realiza os cadastros dos veículos da frota;
- **3. Cadastrar Motorista:** O responsável cadastra os motoristas que fazem parte da empresa;
- **4. Cadastrar Rota:** O responsável faz o cadastro das rotas que executa diariamente;
- **5. Cadastrar Passageiros:** O responsável cadastra os passageiros que já contrataram o serviço e a medida que novos passageiros vão contratando esse também vão sendo cadastrados;
- **6. Gerar Itinerários:** O responsável faz a geração dos itinerários baseado em uma rota e em um intervalo de datas.

### 1. Criar Conta

- **Nome:** Zanardo Escolar Ltda.
- **Email:** zanardojoaovitor7@gmail.com
- **CNPJ:** 01.907.100/0001-69
- **Senha:** a1b2c3

### 2. Cadastrar Veículo

#### Veículo 1

- **Placa:** HSF-3R68
- **Montadora:** Mercedes-Benz
- **Modelo:** Sprinter
- **Quantidade:** 15

#### Veículo 2

- **Placa:** ZGA-1J74
- **Montadora:** Renault
- **Modelo:** Master Minibus
- **Quantidade:** 20

### 3. Cadastrar Motorista

- **Nome:** Valdir Morais
- **Email:** valdirmoraistcc@hotmail.com
- **Telefone:** (54) 9 3965-0569
- **CNH:** 35073262448
- **Validade:** 06/12/2029

### 4. Cadastrar Rota

#### Rota 1

- **Nome:** Rota URI - Campus II - NOTURNO

#### Rota 2

- **Nome:** Rota URI - Campus I - NOTURNO

### 5. Cadastrar Passageiro

#### Passageiro 1

- **Nome:** Eduardo Ferreira
- **Email:** eduardoferreiratcc@hotmail.com
- **Telefone:** (54) 9 3484-9591
- **Rota:** Rota URI - Campus II - NOTURNO
- **CEP:** 99700-036
- **Número:** 155

#### Passageiro 2

- **Nome:** Adriano de Freitas
- **Email:** adrianofreitastcc@hotmail.com
- **Telefone:** (54) 9 3123-2056
- **Rota:** Rota URI - Campus II - NOTURNO
- **CEP:** 99700-066
- **Número:** 226

### 6. Gerar Itinerários

- **Rota** Rota URI - Campus II - NOTURNO
- **Data Inicial:** 08/12/2025
- **Data Final:** 31/12/2025

#### Casos de Teste Específicos (Itinerários)

- Mostrar os filtros na tela.
- Mostrar a funcionalidade de editar um itinerário e de cancelá-lo.

### Casos de Teste Específicos (Geral)

- Editar as informações da rota e clicar na checkbox para regerar os itinerários.
- Habilitar/Desabilitar recursos.
- Sair da conta e logar na outra para mostrar a separação lógica dos dados por empresa.

## Aplicação Mobile

### Motorista

- **Email:** valdirmoraistcc@hotmail.com
- **Senha:** a1b2c3

#### Tela Inicial

- Itinerário em Execução
- Próximo Itinerário

#### Tela de Itinerário

- Próximo Itinerários
    - Visualizar informações do itinerário
    - Visualizar passageiros Confirmados
    - Iniciar Itinerário
    - Finalizar Itinerário
- Histórico de Itinerários

#### Tela de Perfil

- Alterar Email, Nome e Telefone

### Passageiro

- **Email:** eduardoferreiratcc@hotmail.com
- **Senha:** a1b2c3

#### Tela Inicial

- Itinerário em Execução
- Próximo Itinerário

#### Tela de Itinerário

- Próximo Itinerários
    - Visualizar informações do itinerário
    - Cancelar Presença
    - Confirmar Presença
- Histórico de Itinerários

#### Tela de Perfil

- Alterar Email, Nome e Telefone
- Visualizar o endereço

### Casos de Teste Específicos

- Como **passageiro**, cancelar presença no itinerário, e como **motorista**, mostrar a atualização no mapa.
- Ao iniciar um itinerário como **motorista**, o itinerário sendo executado deve aparecer na tela inicial.