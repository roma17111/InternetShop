#!/bin/bash

# Check if Docker is already running
if ! systemctl is-active --quiet docker; then
    # Start Docker
    echo "Starting Docker..."
    sudo service docker start
fi

# Check if the docker image already exists
image_exists=$(docker images -q roman170692/shop)

if [ -z "$image_exists" ]; then
    # Pull the docker image
    echo "Pulling the docker image..."
    docker pull roman170692/shop
fi

# Check if a container with the name "shop" already exists
container_exists=$(docker ps -a --filter name=shop --format "{{.ID}}")

if [ -z "$container_exists" ]; then
    # Create and run the first container
    echo "Creating and running the container on port 3000:3000..."
    docker run -d -p 3000:3000 --name shop roman170692/shop
else
    # Check if the container is running on port 3000:3000
    port_check=$(docker port shop | grep 3000/tcp | grep 0.0.0.0:3000)
    
    if [ -z "$port_check" ]; then
        while true; do
            echo -e "\nThe existing 'shop' container is not running on port 3000:3000. Do you want to recreate it on port 3000:3000? (y/n)"
            read recreate_choice
            
            if [ "$recreate_choice" == "y" ]; then
                docker stop shop; docker rm shop
                echo "Recreating the container on port 3000:3000..."
                docker run -d -p 3000:3000 --name shop roman170692/shop
                break
                elif [ "$recreate_choice" == "n" ]; then
                break
            else
                echo "Invalid choice. Please enter 'y' or 'n'."
            fi
        done
    fi
fi

while true; do
    echo -e "\n\n"
    echo "1. Stop the container"
    echo "2. Start the container again"
    echo "3. Open the link in the browser"
    echo "4. Remove the container (also running)"
    echo "5. Check for a newer version of the image"
    echo "6. Remove the Docker image"
    echo "7. Exit"
    echo -e "\nEnter your choice:"
    read choice
    
    case $choice in
        1)
            while true; do
                echo -e "\nAre you sure you want to stop the container? (y/n)"
                read stop_choice
                
                if [ "$stop_choice" == "y" ]; then
                    docker stop shop
                    break
                    elif [ "$stop_choice" == "n" ]; then
                    break
                else
                    echo "Invalid choice. Please enter 'y' or 'n'."
                fi
            done
        ;;
        2)
            docker start shop
        ;;
        3) xdg-open http://localhost:3000/ ;;
        4)
            while true; do
                echo -e "\nAre you sure you want to remove the container? (y/n)"
                read remove_choice
                
                if [ "$remove_choice" == "y" ]; then
                    docker stop shop; docker rm shop
                    break
                    elif [ "$remove_choice" == "n" ]; then
                    break
                else
                    echo "Invalid choice. Please enter 'y' or 'n'."
                fi
            done
        ;;
        5)
            # Check for a newer version of the image
            echo "Checking for a newer version of the image..."
            docker pull roman170692/shop
        ;;
        6)
            while true; do
                echo -e "\nAre you sure you want to remove the container? This will delete all data stored inside the container. (y/n)"
                read remove_choice
                
                if [ "$remove_choice" == "y" ]; then
                    while true; do
                        echo -e "\nAre you really, really sure? This action cannot be undone. (y/n)"
                        read really_remove_choice
                        
                        if [ "$really_remove_choice" == "y" ]; then
                            docker stop shop; docker rm shop
                            while true; do
                                echo -e "\nDo you also want to remove the Docker image for 'shop'? (y/n)"
                                read remove_image_choice
                                
                                if [ "$remove_image_choice" == "y" ]; then
                                    docker rmi roman170692/shop
                                    echo -e "\nThe Docker image for 'shop' has been removed."
                                    break 3
                                    elif [ "$remove_image_choice" == "n" ]; then
                                    break 2
                                else
                                    echo "Invalid choice. Please enter 'y' or 'n'."
                                fi
                            done
                            elif [ "$really_remove_choice" == "n" ]; then
                            break
                        else
                            echo "Invalid choice. Please enter 'y' or 'n'."
                        fi
                    done
                    elif [ "$remove_choice" == "n" ]; then
                    break
                else
                    echo "Invalid choice. Please enter 'y' or 'n'."
                fi
            done
        ;;
        7) break ;;
        *) echo -e "\nInvalid choice. Do you want to exit? (y/n)"
            read exit_choice
            if [ "$exit_choice" == "y" ]; then
                break
            fi
        ;;
    esac
done
