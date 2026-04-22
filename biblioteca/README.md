# Sistema de Gestão da Biblioteca

Projeto em **Java 17 + Spring Boot + Thymeleaf + MongoDB** para gerenciar usuários, livros, empréstimos e reservas.

## Tecnologias

- Java 17+
- Spring Boot 3.2.5
- Spring Web
- Thymeleaf
- Spring Security
- Spring Data MongoDB
- MongoDB local ou Atlas

## Como executar

1. Instale e ative um JDK 17 ou superior.
2. Instale o Maven e confirme com:
   ```bash
   mvn -version
   ```
3. Configure a variável de ambiente `MONGODB_URI` com a string do MongoDB, ou deixe sem configurar para usar:
   ```text
   mongodb://localhost:27017/biblioteca
   ```
4. Execute na raiz do projeto:
   ```bash
   mvn spring-boot:run
   ```
5. Acesse:
   ```text
   http://localhost:8080/login
   ```

## Usuário padrão

- E-mail: `admin@biblioteca.com`
- Senha: `admin123`

## Recuperacao de senha por e-mail

Configure as variaveis de ambiente do servidor SMTP antes de usar o link "Esqueci minha senha":

```text
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua-senha-de-app
MAIL_FROM=seu-email@gmail.com
```

No Gmail, use uma senha de app em vez da senha normal da conta.

## Funcionalidades

- Login com Spring Security.
- Cadastro e consulta de usuários.
- Cadastro, edição e exclusão de livros.
- Registro de empréstimos e devoluções.
- Registro de reservas para livros indisponíveis.
- Dashboard com contagens do sistema.
- Frontend Thymeleaf responsivo para as telas principais.

## Observações

- Reservas são permitidas somente para livros sem exemplares disponíveis.
- O sistema cria automaticamente o usuário administrador padrão quando ele ainda não existe.
- Se aparecer `UnsupportedClassVersionError`, o Java ativo no terminal é antigo. Confirme com `java -version` e ajuste o `PATH`/`JAVA_HOME` para um JDK 17 ou superior.
