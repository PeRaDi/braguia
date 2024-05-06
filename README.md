# braguia
Trabalho prático para a UC de Tópicos de Desenvolvimento de Software

# Requirements
 - Android API 24 (7.0 - Nougat)
 - Java 21

# Authors
 - Edgar Guilherme, PG54469
 - João Rodrigues, PG52687
 - Pedro Braga, PG54471

# Requisitos técnicos obrigatórios
* A aplicação deve possuir um serviço (Android Service) capaz de monitorizar localização em background; 
* A aplicação deve ser codificada apenas com recurso a linguagens nativas Android; ✅
* A aplicação deve utilizar o Google Maps para navegação no roteiro; ✅
* O desenvolvimento colaborativo deverá ser feito através do uso de Git e GitHub; ✅

# Requisitos Funcionais
* A aplicação deve possuir a capacidade de iniciar um roteiro
* A aplicação deve possuir a capacidade de emitir uma notificação quando o utilizador passa perto de um ponto de interesse
* A notificação emitida quando o utilizador passa pelo ponto de interesse deve conter um atalho para o ecrã principal do ponto de interesse
* A aplicação deve possuir a capacidade de interromper um roteiro
* A aplicação deve guardar (localmente) o histórico de roteiros e pontos de interesse visitados pelo utilizador
* A aplicação deve possuir uma página que mostre toda a informação disponível relativa a um ponto de interesse: 
  * localização
  * descrição
  * galeria
  * mídia
  * propriedades
  * etc
* Para utilizadores premium (e apenas para estes) a aplicação deve possibilitar a capacidade de navegação, de consulta e descarregamento de mídia; 
* A navegação proporcionada pelo Google Maps deve poder ser feita de forma visual e com auxílio de voz, de modo a que possa ser utilizada por condutores;
* A aplicação deve possuir uma página de informações acerca do utilizador atualmente autenticado; 
* A aplicação deve possuir a capacidade de ligar, desligar e configurar os serviços de localização 
* A aplicação deve possuir a capacidade de descarregar mídia do backend e aloja-la localmente, de modo a poder ser usada em contextos de conectividade reduzida
* A aplicação deve possuir uma página inicial onde apresenta as principais funcionalidades do guia turístico, descrição, etc. ✅
* A aplicação deve mostrar num ecrã, de forma responsiva, uma lista de roteiros disponíveis; ✅
* A aplicação deve permitir efetuar autenticação; ✅
* A aplicação deve suportar 2 tipos de utilizadores: 
  * utilizadores standard ✅
  * utilizadores premium ✅
* A aplicação deve assumir que o utilizador tem o Google Maps instalado no seu dispositivo (e notificar o utilizador que este software é necessário); ✅
* A aplicação deve mostrar, numa única página, informação acerca de um determinado roteiro: 
  * galeria de imagens ✅
  * descrição ✅
  * mapa do itinerário com pontos de interesse ✅
  * informações sobre a mídia disponível para os seus pontos ✅
* A aplicação deve ter a capacidade de apresentar e produzir 3 tipos de mídia: 
  * voz ✅
  * imagem ✅
  * vídeo ✅
* A aplicação deve possuir um menu com definições que o utilizador pode manipular ✅
* A aplicação deve possuir a capacidade de efetuar chamadas para contactos de emergência da aplicação através de um elemento gráfico facilmente acessível na aplicação ✅

# Requisitos não Funcionais
* A aplicação não deve lançar erros do tipo ANR. 
* A aplicação deverá funcionar offline; 
* A aplicação deverá ter uma experiência de utilização intuitiva; 
* A aplicação deve aplicar os princípios de qualidade definidos pela Google: 
  * https://developer.android.com/docs/quality-guidelines/core-app-quality?hl=pt-br

# Notas importantes

* A entrega do trabalho será efetuada através do GitHub
* O projeto será extraído automaticamente do repositório de cada grupo imediatamente após a data de entrega.
* Para fins de avaliação funcional, cada repositório deverá ter na secção de Releases do GitHub o APK em versão release correspondente à última versão estável do código da aplicação.
* A automatização do processo de geração de releases através do uso de GitHub Actions será naturalmente devidamente valorizado.
* Detalhes de implementação:
  * Estrutura do projeto;
  * Soluções de implementação; 
  * Bibliotecas/dependências utilizadas; 
  * Padrões de software utilizados
* Mapa de navegação de GUI
* Gestão de projeto:
  * Gestão e Distribuição de trabalho;
  * Eventuais metodologias de controlo de versão utilizadas;
* Estrutura da aplicação (Com eventual recurso ao diagrama de classes)
* Estratégias utilizadas para: 
  * Autenticação;
  * Armazenamento de dados (cookies, mídia, perfil de utilizador, etc);
  * Navegação nos roteiros;
