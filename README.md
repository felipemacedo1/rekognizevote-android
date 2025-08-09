# RekognizeVote - Android App

Aplicativo Android para votação segura com reconhecimento facial usando AWS Rekognition.

## 🚀 Funcionalidades

- **Autenticação Segura**: Login/registro com tokens JWT
- **Votação com Reconhecimento Facial**: Verificação biométrica para garantir autenticidade
- **Enquetes Públicas e Privadas**: Suporte a códigos de acesso
- **Resultados em Tempo Real**: Visualização de estatísticas e gráficos
- **Interface Moderna**: Design seguindo Material Design 3

## 🏗️ Arquitetura

- **Clean Architecture**: Separação em camadas (data, domain, ui)
- **MVVM**: Pattern com ViewModels e StateFlow
- **Jetpack Compose**: UI moderna e declarativa
- **DI Manual**: Injeção de dependência com AppModule
- **Retrofit**: Cliente HTTP para APIs REST
- **CameraX**: Captura de imagens para reconhecimento facial

## 📱 Tecnologias

- **Kotlin**: Linguagem principal (1.9.24)
- **Jetpack Compose**: Framework de UI
- **Material Design 3**: Sistema de design
- **DI Manual**: AppModule + ViewModelFactory
- **Retrofit + OkHttp**: Networking
- **Kotlinx Serialization**: Serialização JSON (1.6.3)
- **CameraX**: Captura de câmera
- **Coil**: Carregamento de imagens
- **Security Crypto**: Armazenamento seguro
- **Timber**: Logging

## 🎨 Design System

### Cores
- **Primária**: `#0A2540` (azul escuro)
- **Ação**: `#1E90FF` (azul)
- **Sucesso**: `#28C76F` (verde)
- **Neutro**: `#F5F7FA` (cinza claro)
- **Texto Secundário**: `#7B8898` (cinza)

### Tipografia
- **Títulos**: Montserrat (Bold/SemiBold)
- **Corpo**: Roboto (Regular/Medium)
- **Código**: Fira Code (para logs técnicos)

## 🛠️ Configuração do Projeto

### Pré-requisitos
- Android Studio Flamingo ou superior
- JDK 21 (recomendado)
- Android SDK 24+ (compileSdk 34)
- Gradle 8.11.1

### Instalação

1. Clone o repositório:
```bash
git clone https://github.com/felipemacedo1/rekognizevote-android.git
cd rekognizevote-android
```

2. Abra o projeto no Android Studio

3. Configure as variáveis de ambiente:
```bash
# Crie o arquivo local.properties na raiz do projeto
BASE_URL_DEV="https://dev-api.rekognizevote.com/"
BASE_URL_PROD="https://api.rekognizevote.com/"
```

4. Sincronize o projeto e execute

### Versões Estáveis

- **AGP**: 8.4.2
- **Kotlin**: 1.9.24
- **Compose**: 2024.12.01
- **Target SDK**: 34

### Build Variants

- **Debug**: Ambiente de desenvolvimento
- **Release**: Ambiente de produção

## 📡 API Integration

### Endpoints Principais

```kotlin
// Autenticação
POST /auth/login
POST /auth/register
POST /auth/refresh

// Enquetes
GET /polls?status={active|upcoming|closed}
GET /polls/{id}
GET /polls/{id}/results
POST /polls/{id}/access?code={code}

// Votação
POST /polls/{id}/vote
GET /upload/presigned-url?type=face_evidence
```

### Fluxo de Votação

1. **Seleção do Candidato**: Usuário escolhe candidato na tela de votação
2. **Captura Facial**: CameraX captura selfie do usuário
3. **Upload Seguro**: Imagem enviada via pre-signed URL para S3
4. **Verificação**: Backend usa AWS Rekognition para comparar faces
5. **Registro**: Voto gravado no DynamoDB com auditoria

## 🔒 Segurança

- **Tokens JWT**: Autenticação stateless
- **EncryptedSharedPreferences**: Armazenamento seguro local
- **HTTPS**: Comunicação criptografada
- **Pre-signed URLs**: Upload direto para S3 sem credenciais
- **Biometria**: Reconhecimento facial para validação

## 🧪 Testes

### Executar Testes Unitários
```bash
./gradlew testDebugUnitTest
```

### Executar Testes Instrumentados
```bash
./gradlew connectedDebugAndroidTest
```

## 📦 Build e Deploy

### Build de Desenvolvimento
```bash
./gradlew assembleDebug
```

### Build de Produção
```bash
./gradlew assembleRelease
```

### Gerar AAB (Android App Bundle)
```bash
./gradlew bundleRelease
```

### Resolução de Problemas

#### JavaPoet/Hilt Conflicts (Resolvido)
Este projeto **removeu completamente o Hilt** para eliminar conflitos de dependência:
- ✅ Sem erros `ClassName.canonicalName()`
- ✅ Sem `hiltAggregateDepsDebug` failures
- ✅ DI manual com `AppModule` e `ViewModelFactory`

#### Build Limpo
```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
./gradlew --stop
rm -rf .gradle/
./gradlew clean assembleDebug --no-configuration-cache
```

## 🔧 Configuração do Backend

O app requer um backend compatível com os seguintes serviços AWS:

- **API Gateway**: Endpoints REST
- **Lambda**: Lógica de negócio e Rekognition
- **DynamoDB**: Armazenamento de dados
- **S3**: Armazenamento de imagens
- **Cognito**: Autenticação (opcional)

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 Contato

Para contato/suporte ou reports, envie uma mensagem para www.linkedin.com/in/felipemacedo1/ ou abra uma issue no GitHub.

---

**RekognizeVote** - Votação Segura com Reconhecimento Facial 🗳️📱