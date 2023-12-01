# Use the official Nginx base image
FROM nginx:1.25.3

# Set the working directory to the Nginx configuration directory
WORKDIR /etc/nginx/conf.d

# Remove the default configuration file that comes with Nginx
RUN rm default.conf

# Copy your custom Nginx configuration file to the container
COPY nginx.conf .

# Set the working directory to the Nginx content directory
WORKDIR /usr/share/nginx/html

# Remove the default Nginx static content
RUN rm -rf ./*

# Copy the contents of the local dist directory to the Nginx content directory
COPY ./dist .

# Expose the port that Nginx will run on
EXPOSE 80

# Start Nginx when the container starts
CMD ["nginx", "-g", "daemon off;"]
