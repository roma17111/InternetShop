#!/bin/bash

CONTAINER_TOOL=""

echo
echo "============================================"
# Check if Podman is installed
if command -v podman &>/dev/null; then
    echo "1. Podman is installed."
fi

# Check if Docker is installed
if command -v docker &>/dev/null; then
    echo "2. Docker is installed."
fi

echo
echo "============================================"
echo

# Prompt user to choose between Podman and Docker
printf "Please choose your container tool:\n1. podman\n2. docker\nEnter your choice (1/2): "
read CONTAINER_TOOL

if [[ "$CONTAINER_TOOL" == "1" ]]; then
    CONTAINER_TOOL="podman"
elif [[ "$CONTAINER_TOOL" == "2" ]]; then
    CONTAINER_TOOL="docker"
else
    echo "Invalid input. Exiting."
    exit 1
fi

# Check if the chosen container tool is installed
if ! command -v $CONTAINER_TOOL &>/dev/null; then
    echo "$CONTAINER_TOOL is not installed. Exiting."
    exit 1
fi

# Check if any container is using port 3000:3000
port_check=$($CONTAINER_TOOL ps --format "{{.ID}} {{.Names}} {{.Ports}}" | grep -E "3000->3000")

if [ -z "$port_check" ]; then
    # Create and run the first container
    echo "Creating and running the container on port 3000:3000..."
    $CONTAINER_TOOL run -d -p 3000:3000 --name shop ghcr.io/bizinmitya/front-react-avito:v1.10
else
    while true; do
        container_name=$(echo $port_check | awk '{print $2}')
        echo -e "\nA container is already running on port 3000:3000. Do you want to delete $container_name ? (y/n)"
        read delete_choice
        if [ "$delete_choice" == "y" ]; then
            # Get the name of the container running on port 3000:3000
            $CONTAINER_TOOL stop $container_name
            $CONTAINER_TOOL rm $container_name
            echo "Creating and running the 'shop' container on port 3000:3000..."
            $CONTAINER_TOOL run -d -p 3000:3000 --name shop ghcr.io/bizinmitya/front-react-avito:v1.10
            break
        elif [ "$delete_choice" == "n" ]; then
            break
        else
            echo "Invalid choice. Please enter 'y' or 'n'."
        fi
    done
fi

while true; do
    echo -e "\n\n"
    echo "============================================"
    echo "1. Stop the container"
    echo "2. Start the container again"
    echo "3. Open the link in the browser"
    echo "4. Remove the container (also running)"
    echo "5. Repull new v1.10 container image"
    echo "6. Remove the Docker(podman) image"
    echo "7. Restart init (this will restart script)"
    echo "8. Exit"
    echo "============================================"
    echo -e "\nEnter your choice:"
    read choice

    case $choice in
    1)
        while true; do
            echo -e "\nAre you sure you want to stop the container? (y/n)"
            read stop_choice

            if [ "$stop_choice" == "y" ]; then
                $CONTAINER_TOOL stop shop
                break
            elif [ "$stop_choice" == "n" ]; then
                break
            else
                echo "Invalid choice. Please enter 'y' or 'n'."
            fi
        done
        ;;
    2)
        $CONTAINER_TOOL start shop
        ;;
    3) xdg-open http://localhost:3000/ ;;
    4)
        while true; do
            echo -e "\nAre you sure you want to remove the container? (y/n)"
            read remove_choice

            if [ "$remove_choice" == "y" ]; then
                $CONTAINER_TOOL stop shop
                $CONTAINER_TOOL rm shop
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
        echo "Re:pulling front end with 1.10 version..."
        $CONTAINER_TOOL pull ghcr.io/bizinmitya/front-react-avito:v1.10
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
                        $CONTAINER_TOOL stop shop
                        $CONTAINER_TOOL rm shop
                        while true; do
                            echo -e "\nDo you also want to remove the Docker(podman) image for 'shop'? (y/n)"
                            read remove_image_choice

                            if [ "$remove_image_choice" == "y" ]; then
                                $CONTAINER_TOOL rmi ghcr.io/bizinmitya/front-react-avito:v1.10
                                echo -e "\nThe Docker(podman) image for 'shop' has been removed."
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
    7)
        exec "$0"
        ;;
    8) break ;;
    *)
        echo -e "\nInvalid choice. Do you want to exit? (y/n)"
        read exit_choice
        if [ "$exit_choice" == "y" ]; then
            break
        fi
        ;;
    esac
done
