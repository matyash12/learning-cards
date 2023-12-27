#!/bin/bash
##Define the source and destination file paths
#Easy startup
example_file_application_properties="backend/src/main/resources/application_example.properties"
destination_file_application_properties="backend/src/main/resources/application.properties"

example_file_env=".env.example"
destination_file_env=".env"

# Check if the "-example-run" argument is provided
if [[ "$1" == "-example" ]]; then
  # Copy the example file to the destination
  cp "$example_file_application_properties" "$destination_file_application_properties"
   cp "$example_file_env" "$destination_file_env"
  echo "Example files copied successfully."
else
  echo "For running as example run it using: $0 -example-run"
fi



##Installing everything
#Frontend
# Change directory to the frontend folder
cd frontend
# Run npm run build
npm install 
npm run build

#Backend
cd ..
cd backend

./mvnw clean install


#start docker
docker compose up -d --build
