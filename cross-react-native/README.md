# Como correr
Depois de clonar basta instalar as dependências e correr o projeto. Irá abrir um menu com as opções possíveis para correr o projeto e o emulador inicializará automaticamente se tiver tudo bem configurado.
```bash
cd ~/braguia/cross-react-native
npm i
npm start
```

# Elementos de equipa
* Pedro Braga
* Edgar Wchua
* João Rodrigues

# Backend

* Endereço publico 1: https://39b6-193-137-92-72.ngrok-free.app/
* Endereço publico 2: https://4b87ebd1e4ffbe8dffedd3aa0ea88e21.serveo.net/
* IP: 192.168.85.186; (requer VPN)

## Users

* premium user:
  * username: premium_user 
  * pwd: paiduser
* standard user:
  * username: standard_user
  * pwd: cheapuser

## Rotas

- /app: informação geral acerca da app;
- /trails: lista de trilhos;
- /trail/<int:pk>: informação sobre um trilho específico (identificado pelo seu id único);
- /pins: lista de pontos de interesse existentes no sistema;
- /pin/<int:pk>: Dados relativos a um ponto de interesse específico (identificado pelo
  seu id único);
- /content: lista de mídia disponível;
- /media/P<path>.*): obter mídia específica;
- /user: informação do utilizador;
- /login: autenticação utilizador (devolve JWT token)
- /logout


# Links úteis

- Comunicação React Native ⇔ Codigo nativo: https://reactnative.dev/docs/native-modules-android
- React Native Maps: https://github.com/react-native-maps/react-native-maps
- Redux (gestão de estado global, acesso a dados): https://enappd.com/blog/redux-in-react-native-app/92/
- WatermelonDB (em caso de reimplentação de acesso a BD): https://watermelondb.dev/docs

