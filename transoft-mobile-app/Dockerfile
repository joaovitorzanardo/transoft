FROM node:20-alpine

WORKDIR /app

COPY package*.json ./

RUN npm install
RUN npm install expo@54.0.13

COPY . .

EXPOSE 8082

CMD ["tail", "-f", "/dev/null"]