# Feature `signUp`

## Vue d'ensemble

La feature `signUp` permet a un client HTTP de creer une identite utilisateur via l'endpoint `POST /auth/sign-up`.

Le flux actuel est le suivant :

1. `AuthController` recoit la requete HTTP.
2. `SignUpWebMapper` convertit le payload web en `SignUpCommand`.
3. `SignUpUseCase` expose le cas d'usage cote application.
4. `SignUpService` orchestre le cas d'usage.
5. `CreateIdentityPort` abstrait la dependance technique necessaire a la creation du compte.
6. `KeycloakCreateIdentityAdapter` implemente ce port en s'appuyant sur Keycloak.
7. `KeycloakAdminConfig` et `KeycloakProperties` fournissent les objets et la configuration techniques necessaires.
8. `SecurityConfig` autorise l'acces non authentifie a `/auth/sign-up`.

Cette feature respecte globalement le style hexagonal : l'entree web depend de l'application, l'application depend de
ports, et l'infrastructure fournit les implementations techniques.

## Lecture hexagonale

### Ce qui appartient a l'application

L'application porte le cas d'usage `signUp`. Elle ne connait ni HTTP, ni Spring MVC, ni Keycloak. Elle exprime
seulement :

- ce que l'on veut faire : `SignUpUseCase`
- les donnees utiles au cas d'usage : `SignUpCommand`
- la dependance externe requise : `CreateIdentityPort`
- l'orchestration : `SignUpService`

### Ce qui appartient a l'infrastructure

L'infrastructure contient les details techniques :

- exposition HTTP
- mapping entre DTO web et objet applicatif
- appel au fournisseur d'identite Keycloak

### Ce qui appartient a la configuration

Le package `configuration` ne porte pas de logique metier. Il sert a assembler les composants Spring et a brancher les
proprietes externes.

## Detail de chaque classe / record / interface

### `application.dto.SignUpCommand`

- Type : `record`
- Role : transporter les donnees necessaires au cas d'usage d'inscription (`login`, `password`).
- Pourquoi dans `application.dto` :
    - ce n'est pas un DTO HTTP, car il ne represente pas le contrat REST
    - ce n'est pas un objet d'infrastructure, car il doit rester utilisable par n'importe quel adaptateur entrant
    - il sert de format d'entree interne au cas d'usage
- Role dans l'architecture hexagonale : objet de passage entre adaptateur entrant et coeur applicatif.

### `application.port.in.SignUpUseCase`

- Type : `interface`
- Role : definir le port entrant du cas d'usage.
- Pourquoi dans `application.port.in` :
    - un port entrant decrit ce que le systeme sait faire depuis l'exterieur
    - les adaptateurs entrants, comme le web, doivent dependre de cette abstraction et non d'une implementation concrete
- Role dans l'architecture hexagonale : point d'entree du coeur applicatif.

### `application.service.SignUpService`

- Type : `class`
- Role : implementer le cas d'usage `SignUpUseCase`.
- Ce que fait concretement la classe :
    - elle recoit un `SignUpCommand`
    - elle delegue la creation d'identite a `CreateIdentityPort`
- Pourquoi dans `application.service` :
    - cette classe porte l'orchestration du cas d'usage
    - elle ne contient pas de detail HTTP ni de detail Keycloak
    - elle reste donc dans la couche application
- Role dans l'architecture hexagonale : implementation du use case, au centre du flux.

### `application.port.out.CreateIdentityPort`

- Type : `interface`
- Role : abstraire le besoin externe "creer une identite utilisateur".
- Pourquoi dans `application.port.out` :
    - l'application a besoin d'un service externe, mais ne doit pas connaitre son implementation
    - ce port formalise la sortie necessaire vers l'exterieur
- Role dans l'architecture hexagonale : contrat de sortie du coeur applicatif vers un adaptateur technique.

### `infrastructure.adapter.in.web.dto.request.SignUpRequest`

- Type : `record`
- Role : representer le corps de requete HTTP recu par l'API.
- Pourquoi dans `infrastructure.adapter.in.web.dto.request` :
    - ce type appartient clairement au contrat web
    - il depend du contexte HTTP et ne doit pas fuiter dans l'application
- Role dans l'architecture hexagonale : DTO propre a l'adaptateur entrant web.

### `infrastructure.adapter.in.web.mapper.SignUpWebMapper`

- Type : `class`
- Role : convertir `SignUpRequest` en `SignUpCommand`.
- Pourquoi dans `infrastructure.adapter.in.web.mapper` :
    - le mapping entre contrat web et contrat applicatif est une responsabilite d'adaptateur entrant
    - cela evite de polluer le controleur avec des details de transformation
- Role dans l'architecture hexagonale : anti-corruption layer tres simple entre le monde HTTP et la couche application.

### `infrastructure.adapter.in.web.controller.AuthController`

- Type : `class`
- Role : exposer l'endpoint REST `POST /auth/sign-up`.
- Ce que fait concretement la classe :
    - elle recoit `SignUpRequest`
    - elle appelle `SignUpWebMapper`
    - elle invoque `SignUpUseCase`
    - elle retourne un `201 Created`
- Pourquoi dans `infrastructure.adapter.in.web.controller` :
    - un controleur Spring est un detail d'exposition technique
    - il ne doit pas se trouver dans `application` ni `domain`
- Role dans l'architecture hexagonale : adaptateur entrant web.

### `infrastructure.adapter.out.keycloak.KeycloakCreateIdentityAdapter`

- Type : `class`
- Role : implementer `CreateIdentityPort` via l'API admin Keycloak.
- Ce que fait concretement la classe :
    - elle construit un `UserRepresentation`
    - elle construit un `CredentialRepresentation`
    - elle appelle Keycloak pour creer l'utilisateur dans le realm configure
    - elle traduit les statuts techniques en exceptions runtime
- Pourquoi dans `infrastructure.adapter.out.keycloak` :
    - cette classe depend explicitement du SDK Keycloak
    - elle parle a un systeme externe
    - elle ne doit donc pas remonter dans l'application
- Role dans l'architecture hexagonale : adaptateur sortant branche sur un fournisseur IAM externe.

## Classes de support a la feature

### `configuration.bean.ApplicationBeanConfig`

- Type : `class`
- Role : declarer le bean `SignUpUseCase`.
- Pourquoi dans `configuration.bean` :
    - cette classe ne porte pas de use case
    - elle assemble les dependances Spring entre port et implementation
- Role dans l'architecture hexagonale : wiring de l'application, en dehors du coeur metier.

### `configuration.bean.KeycloakAdminConfig`

- Type : `class`
- Role : construire le client `Keycloak` utilise par l'adaptateur sortant.
- Pourquoi dans `configuration.bean` :
    - la creation d'un client technique est une responsabilite de configuration
    - cela evite de melanger la creation d'objet technique avec la logique applicative
- Role dans l'architecture hexagonale : bootstrap technique pour l'adaptateur sortant.

### `configuration.property.KeycloakProperties`

- Type : `class`
- Role : porter les proprietes externes `keycloak.*` venant de `application.yaml` ou de variables d'environnement.
- Pourquoi dans `configuration.property` :
    - ces donnees sont de la configuration technique
    - elles ne representent ni un concept metier ni un DTO applicatif
- Role dans l'architecture hexagonale : point d'entree des parametres techniques du systeme externe.

### `configuration.bean.SecurityConfig`

- Type : `class`
- Role : definir la securite HTTP.
- Contribution a `signUp` :
    - autorise explicitement `POST /auth/sign-up` sans authentification
    - protege le reste de l'API
- Pourquoi dans `configuration.bean` :
    - la securite Spring est un detail d'infrastructure et de wiring
    - elle ne doit pas etre dans `application`
- Role dans l'architecture hexagonale : configuration transverse necessaire a l'exposition web.

### `EbookTranslatorApplication`

- Type : `class`
- Role : point d'entree Spring Boot.
- Contribution a `signUp` :
    - active `KeycloakProperties` avec `@EnableConfigurationProperties`
- Pourquoi a la racine du projet :
    - c'est le bootstrap global de l'application
    - ce n'est pas un composant du domaine ni d'un use case particulier
- Role dans l'architecture hexagonale : lancement de l'application, hors logique metier.

## Pourquoi il n'y a pas de couche `domain` dans cette feature

Dans l'etat actuel, `signUp` est essentiellement un cas d'orchestration applicative vers un fournisseur d'identite
externe.

Il n'existe pas encore de :

- entite metier `User`
- value object `Email`, `Password`, `Login`
- regles metier riches portees par le domaine

Ce n'est pas forcement faux. Tant que la feature consiste surtout a deleguer la creation du compte a Keycloak, la couche
`application` peut suffire. En revanche, si des regles metier apparaissent, par exemple :

- validation fonctionnelle du login
- politique metier de mot de passe
- verification d'unicite combinee avec d'autres contraintes
- emission d'evenements metier

alors une vraie modelisation `domain` deviendrait pertinente.

## Positionnement par package : justification rapide

### `application.*`

On y place ce qui decrit le cas d'usage sans detail technique.

### `infrastructure.adapter.in.web.*`

On y place tout ce qui parle HTTP : controleur, DTO web, mapping web.

### `infrastructure.adapter.out.keycloak`

On y place l'integration concrete avec le fournisseur d'identite externe.

### `configuration.*`

On y place le wiring Spring, la securite, les beans techniques et les proprietes.

## Points d'attention observes dans l'implementation actuelle

### Les exceptions de `KeycloakCreateIdentityAdapter` sont tres techniques

L'adaptateur jette des `RuntimeException` generiques. Dans une architecture hexagonale plus mature, il serait preferable
de :

1. definir des exceptions applicatives ou domaine explicites
2. traduire ensuite ces exceptions dans l'adaptateur web avec un handler HTTP

## Resume

La feature `signUp` est aujourd'hui bien alignee avec les principes essentiels de l'architecture hexagonale :

- le web entre par un adaptateur entrant
- le cas d'usage est porte par l'application
- l'application depend d'un port de sortie
- Keycloak est isole dans un adaptateur sortant
- Spring sert surtout au wiring et a la configuration

Le point principal a retenir est que la logique "inscrire un utilisateur" est exprimee par l'application, tandis que la
facon concrete de recevoir la demande et de creer le compte est deleguee a l'infrastructure.
