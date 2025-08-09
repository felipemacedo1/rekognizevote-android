# Fontes do RekognizeVote

Para o funcionamento completo do app, baixe as seguintes fontes do Google Fonts e coloque na pasta `app/src/main/res/font/`:

## Montserrat
- `montserrat_regular.ttf`
- `montserrat_medium.ttf`  
- `montserrat_semibold.ttf`
- `montserrat_bold.ttf`

## Roboto
- `roboto_regular.ttf`
- `roboto_medium.ttf`
- `roboto_bold.ttf`

## Fira Code
- `fira_code_regular.ttf`
- `fira_code_medium.ttf`

### Como baixar:
1. Acesse https://fonts.google.com/
2. Busque por cada fonte
3. Baixe os pesos especificados
4. Renomeie os arquivos conforme listado acima
5. Coloque todos os arquivos .ttf na pasta `app/src/main/res/font/`

### Após adicionar as fontes:
1. Descomente o código no arquivo `app/src/main/java/com/rekognizevote/ui/theme/Type.kt`
2. Comente as linhas de fallback

### Fallbacks atuais:
O app funcionará mesmo sem as fontes, usando os fallbacks definidos no código:
- Montserrat → FontFamily.Default
- Roboto → FontFamily.Default  
- Fira Code → FontFamily.Monospace