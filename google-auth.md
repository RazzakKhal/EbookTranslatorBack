# Google Auth - Schema Hexagonal

Ce document montre le flux d'authentification Google dans l'architecture hexagonale du projet.

## 1) Schema de dependances

```mermaid
flowchart LR
    Client[Client HTTP] --> C[AuthController<br/>infrastructure.adapter.in.web]
    C --> U[GoogleLoginUseCase<br/>application.port.in]
    U --> S[GoogleLoginService<br/>application.service]

    S --> P1[VerifyGoogleTokenPort<br/>application.port.out]
    S --> P2[LoadOrCreateUserPort<br/>application.port.out]
    S --> P3[GenerateJwtPort<br/>application.port.out]

    P1 --> A1[GoogleTokenVerifierAdapter<br/>infrastructure.adapter.out.external.auth.google]
    A1 --> G[GoogleTokenVerifierHttpClient<br/>Google tokeninfo API]

    P2 --> A2[UserPersistenceAdapter<br/>infrastructure.adapter.out.persistence]
    A2 --> R[UserJpaRepository]
    R --> DB[(PostgreSQL)]

    P3 --> A3[JwtGeneratorAdapter<br/>infrastructure.adapter.out.security]

    D[Domain: User, AuthenticationFailedException] -. utilise .-> S
```

## 2) Schema de sequence

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as AuthController
    participant UseCase as GoogleLoginUseCase
    participant Service as GoogleLoginService
    participant Verify as VerifyGoogleTokenPort
    participant Google as GoogleTokenVerifierAdapter/HttpClient
    participant UserPort as LoadOrCreateUserPort
    participant Persistence as UserPersistenceAdapter/JPA
    participant JwtPort as GenerateJwtPort
    participant Jwt as JwtGeneratorAdapter

    Client->>Controller: POST /api/v1/auth/google { idToken }
    Controller->>UseCase: loginWithGoogle(command)
    UseCase->>Service: delegation

    Service->>Verify: verifyIdToken(idToken)
    Verify->>Google: call Google tokeninfo API
    Google-->>Verify: GoogleProfile (valide) ou vide
    Verify-->>Service: Optional<GoogleProfile>

    alt token invalide
        Service-->>Controller: AuthenticationFailedException
        Controller-->>Client: 401 Unauthorized
    else token valide
        Service->>UserPort: loadOrCreateGoogleUser(profile)
        UserPort->>Persistence: find/save user
        Persistence-->>UserPort: User
        UserPort-->>Service: User

        Service->>JwtPort: generateAccessToken(user)
        JwtPort->>Jwt: sign token
        Jwt-->>JwtPort: accessToken
        JwtPort-->>Service: accessToken

        Service-->>Controller: AuthResult(accessToken, user)
        Controller-->>Client: 200 OK + AuthResponse
    end
```

## 3) Mapping rapide des couches

- Domain: modeles et regles metier pures (`User`, exceptions metier).
- Application: use cases et ports (`GoogleLoginUseCase`, `...Port`).
- Infrastructure IN: entree HTTP (`AuthController`, DTO web, mapper web).
- Infrastructure OUT: Google API, persistence JPA, generation token.
- Configuration: wiring Spring (`AuthenticationBeanConfig`, properties).

