#!/bin/bash


#Frontend
# Change directory to the frontend folder
cd frontend

# Run npm run build
npm run build

#Backend
cd ..
cd backend

./mvnw clean install


#start docker
docker compose up -d --build
