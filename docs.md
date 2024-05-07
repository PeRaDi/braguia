
# Estrutura do projeto

* Estruturar as pastas por features
* Para cada feature estruturar em camadas


* Estrutura da aplicação (com eventual recurso ao diagrama de classes)
* Mapa de navegação
* Padrões de software

# Features

## Utilizadores (Users) 

- ✅ A aplicação deve permitir efetuar **autenticação**
- ✅ A aplicação deve suportar 2 tipos de utilizadores:
  * **standard**
  * **premium**
- [ ] Página de informações acerca do utilizador atualmente autenticado

## Menu (Settings)

- [ ] Um menu com definições que o utilizador pode manipular

## Roteiros (Trails)

- ✅ A aplicação deve mostrar num ecrã, de forma responsiva, uma lista de roteiros disponíveis
- [] A aplicação deve mostrar, numa única página, informação acerca de um determinado **roteiro**:
  * ✅ descrição
  * ✅ galeria de imagens
  * ✅ informações sobre a mídia disponível para os seus pontos
  *  mapa do itinerário com pontos de interesse
- [ ] Capacidade de iniciar um roteiro
- [ ] Capacidade de interromper um roteiro

## Pontos de Interesse (Pins?)

- [ ] Página que mostra toda a informação disponível relativa a um ponto de interesse:
  * localização
  * descrição
  * galeria
  * mídia
  * propriedades
  * etc
- [ ] A aplicação deve guardar (localmente) o histórico de pontos de interesse visitados pelo utilizador

## Navegação 

- ✅ Assumir que o utilizador tem o **Google Maps** instalado no seu dispositivo 
  * ✅ Notificar o utilizador que este software é necessário
  * ✅ Reencaminhar o user para GMaps
- ✅ Utilizar o Google Maps para navegação no roteiro
- ✅ Para utilizadores **premium**, possibilitar a capacidade de navegação
- [ ] Deve poder ser feita de forma visual e com auxílio de voz
- [ ] Capacidade de ligar, desligar e configurar os serviços de localização
- [ ] A aplicação deve possuir um serviço (Android Service) capaz de monitorizar localização em background

## Media

- [ ] Para utilizadores **premium**, possibilitar a consulta e descarregamento de mídia
- [ ] A aplicação deve ter a capacidade de apresentar e produzir 3 tipos de mídia:
  * voz
  * imagem
  * vídeo
- [ ] Capacidade de descarregar mídia do backend e aloja-la localmente
  * De modo a poder ser usada em contextos de conectividade reduzida

## Notificação

- [ ] Emitir uma notificação quando o utilizador passa perto de um ponto de interesse:
  * Deve conter um atalho para o ecrã principal do ponto de interesse

## Chamadas

- [ ] Capacidade de efetuar chamadas para contactos de emergência da aplicação 
  * Através de um elemento gráfico facilmente acessível na aplicação
 
## Informação

- [ ] A aplicação deve possuir uma página inicial onde apresenta:
  * As principais funcionalidades do guia turístico
  * Descrição
  * etc.

# Metodologias de controlo de versão utilizadas

* Branchs:
  * dev
  * main

# CI/CD

- [ ] O repositório deverá ter na secção de Releases do GitHub o APK em versão release
- [ ] Automatização do processo de geração de releases através do uso de GitHub Actions

# Bibliotecas

* Navegação
  * https://developer.android.com/guide/navigation?hl=pt-br
  * https://developer.android.com/guide/navigation/design/editor?hl=pt-br
  * https://developer.android.com/guide/navigation/design?hl=pt-br
  * https://developer.android.com/guide/navigation/integrations/ui#bottom_navigation
  * https://developer.android.com/guide/navigation/integrations/ui#Tie-navdrawer
* Safe Args
  * https://developer.android.com/guide/navigation/use-graph/safe-args?hl=pt-br#java
