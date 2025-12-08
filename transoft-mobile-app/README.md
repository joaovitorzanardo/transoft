## Instruções para Executar

Na pasta raiz do projeto, rodar o seguinte comando:

```sh
docker build -t expo-manual .
```

Feita a criação da imagem, devemos executar o comando para rodar o container:

```sh
docker run -d \
  -p 8082:8082 \
  -e REACT_NATIVE_PACKAGER_HOSTNAME=<ip_do_computador_na_rede> \
  --name meu-expo-container \
  expo-manual
```

Por último devemos entrar dentro do container para executar o projeto:

```sh
docker exec -it meu-expo-container sh
```

Já dentro do container, subimos o servidor com o comando:

```sh
npx expo start --port 8082 --host lan
```