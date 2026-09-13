# FIFA Match API

Uma API REST moderna para gerenciar partidas de futebol (FIFA) e montar seleções aleatórias com base em equipes da base de dados.

## 📋 Sobre o Projeto

O FIFA Match é uma aplicação backend desenvolvida em **Spring Boot** que permite:
- 🎲 Gerar partidas aleatórias com equipes selecionadas
- 🔍 Filtrar equipes por diferentes critérios (rating, país, tipo)
- 📊 Recuperar informações sobre equipes cadastradas
- ⚙️ Importar dados de equipes via arquivo CSV
- 🌐 Suporte CORS para integração com frontends

## 🛠️ Tecnologias

- **Java 26** - Linguagem de programação
- **Spring Boot 4.1.1** - Framework web
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **Flyway** - Migração de banco de dados
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização
- **Lombok** - Redução de boilerplate

## ✨ Funcionalidades

### Gerenciar Partidas
- Criar partidas aleatórias com número específico de times
- Filtrar equipes por rating, país e tipo
- Gerar seleções balanceadas automaticamente

### Equipes
- Importar equipes via CSV
- Listar todas as equipes
- Filtrar por múltiplos critérios
- Obter opções de filtro disponíveis

### Configuração
- CORS configurável por properties
- Suporte a múltiplos ambientes

## 🚀 Começando

### Pré-requisitos

- **Java 26+**
- **Maven 3.8+**
- **Docker & Docker Compose** (opcional)
- **PostgreSQL 14+** (ou utilize Docker)

### Instalação Local

1. **Clone o repositório**
   ```bash
   git clone https://github.com/vinicius2343/fifa-match.git
   cd fifa-match
   ```

2. **Configure o banco de dados** (PostgreSQL rodando localmente)
   
   Edite `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/fifa_match
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

3. **Compile o projeto**
   ```bash
   ./mvnw clean package
   ```

4. **Execute a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```

   A API estará disponível em: `http://localhost:8080`

### Instalação com Docker

1. **Build e execute com Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **A API estará disponível em:** `http://localhost:8080`

   O banco de dados PostgreSQL será iniciado automaticamente.

## 📚 Endpoints

### Partidas

#### Gerar Partida Aleatória
```
POST /api/matches/randomize
Content-Type: application/json

{
  "matchSize": "ELEVEN_VS_ELEVEN",
  "filters": [
    {
      "type": "RATING",
      "operator": "GREATER_THAN_EQUAL",
      "value": "85"
    }
  ]
}
```

**Resposta:**
```json
{
  "teamA": [
    {
      "id": 1,
      "name": "Manchester United",
      "rating": 92,
      "country": "England",
      "teamType": "OFFICIAL"
    }
  ],
  "teamB": [...]
}
```

### Equipes

#### Listar Todas as Equipes
```
GET /api/teams
```

#### Obter Filtros Disponíveis
```
GET /api/teams/filters
```

**Resposta:**
```json
{
  "countries": ["England", "Spain", "Germany", ...],
  "ratings": [75, 80, 85, 90, 95],
  "teamTypes": ["OFFICIAL", "CUSTOM"]
}
```

#### Filtrar Equipes
```
POST /api/teams/filter
Content-Type: application/json

{
  "filters": [
    {
      "type": "COUNTRY",
      "value": "England"
    }
  ]
}
```

## ⚙️ Configuração

### Variáveis de Ambiente

Create um arquivo `.env` na raiz do projeto:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/fifa_match
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
SPRING_JPA_HIBERNATE_DDL_AUTO=validate

CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
```

### application.properties

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/fifa_match
spring.datasource.username=postgres
spring.datasource.password=password

# Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway
spring.flyway.enabled=true

# CORS
cors.allowed-origins=http://localhost:3000
```

## 🗄️ Banco de Dados

### Schema

As migrações são gerenciadas automaticamente pelo Flyway. A tabela principal é:

```sql
CREATE TABLE teams (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  rating INTEGER NOT NULL,
  country VARCHAR(100) NOT NULL,
  team_type VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 📦 Importar Dados

O arquivo `teams.csv` na pasta `resources` contém dados de exemplo. Para importar:

1. A importação pode ser feita via a classe `TeamCsvImporter`
2. Ou através de um endpoint específico (se implementado)

Formato esperado do CSV:
```csv
name,rating,country,teamType
Manchester United,92,England,OFFICIAL
Real Madrid,90,Spain,OFFICIAL
```

## 🧪 Testes

Execute os testes com:

```bash
./mvnw test
```

Para cobertura de testes:

```bash
./mvnw jacoco:report
```

## 📝 Estrutura do Projeto

```
src/main/java/io/github/vinicius2343/fifa_match/
├── config/              # Configurações (CORS, etc)
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
├── enums/               # Enumerações
├── model/               # Entidades JPA
├── repository/          # Repositórios Spring Data
├── service/             # Lógica de negócio
└── FifaMatchApplication.java

src/main/resources/
├── application.properties
├── teams.csv
└── db/migration/        # Scripts Flyway
```

## 🔒 CORS

A aplicação está configurada com suporte a CORS. Os origins permitidos podem ser configurados via `application.properties` ou variáveis de ambiente.

## 📖 Documentação Adicional

- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/4.1.1/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)
- [Flyway Documentation](https://flywaydb.org/documentation/)

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Vinicius2343**
- GitHub: [@vinicius2343](https://github.com/vinicius2343)

## 📞 Suporte

Para reportar issues ou sugerir melhorias, abra uma [issue no GitHub](https://github.com/vinicius2343/fifa-match/issues).

---

**Desenvolvido com ❤️ por Vinicius2343**
