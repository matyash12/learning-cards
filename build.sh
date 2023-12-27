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
  echo "running docker"
  docker compose up -d --build

  echo """
  FYI: This is only for example.
  These passwords are hardcoded.
  Check README.md on how to make it secure.
  Some things may not work in this example (email = password recovery)

  Link to website
    Register to create account
    http://localhost/user/login

  Link to api (java backend)
    Check docs for more info
    http://localhost/api/user/login

  Link to Minio console (image storage)
    Username: spring
    Password: ZmenitHeslo007
    http://localhost:9001
  
  """
else
  echo "For running as example run it using: $0 -example-run"
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
fi


