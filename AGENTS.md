# AGENTS.md

## Objectif

Ce projet Spring Boot suit une **architecture hexagonale** (Ports & Adapters), avec une séparation claire entre :

- **Domain** (métier pur)
- **Application** (cas d’usage)
- **Infrastructure** (adaptateurs techniques)
- **Configuration** (wiring Spring)
- **Shared** (utilitaires transverses)

L’objectif est de garder le **métier indépendant des frameworks** (Spring, JPA, clients HTTP, stockage, etc.).

---

## Structure cible du projet

```text
src/main/java/com/ebooktranslator
├─ EbookTranslatorApplication.java
├─ configuration
│  ├─ bean
│  └─ properties
├─ shared
│  ├─ utils
│  ├─ constants
│  └─ mapper              (optionnel, si vraiment générique)
├─ domain
│  ├─ model
│  ├─ exception
│  ├─ service             (optionnel : logique métier pure)
│  ├─ event               (optionnel)
│  └─ valueobject         (optionnel)
├─ application
│  ├─ port
│  │  ├─ in               (use cases exposés)
│  │  └─ out              (dépendances externes requises)
│  ├─ service             (implémentation des use cases)
│  ├─ mapper              (optionnel : mapping applicatif)
│  └─ dto                 (optionnel : DTO internes applicatifs)
└─ infrastructure
   └─ adapter
      ├─ in
      │  ├─ web
      │  │  ├─ controller
      │  │  ├─ dto
      │  │  │  ├─ request
      │  │  │  └─ response
      │  │  ├─ mapper
      │  │  └─ exception
      │  ├─ messaging      (optionnel)
      │  └─ batch          (optionnel)
      └─ out
         ├─ persistence
         │  ├─ entity
         │  ├─ repository   (Spring Data)
         │  ├─ mapper
         │  └─ adapter
         ├─ external 
            ├─translation     (clients DeepL/OpenAI/etc.)
         │      ├─ client
         │      ├─ dto
         │      ├─ mapper
         │      └─ adapter
         ├─ storage         (filesystem/S3/etc.)
         │  └─ adapter
