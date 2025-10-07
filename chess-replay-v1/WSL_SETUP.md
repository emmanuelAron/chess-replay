🚀 PySpark Setup Guide (Ubuntu WSL on Windows)
🧩 Objective

This guide explains how to install and configure PySpark on Ubuntu WSL,
while using your Windows D: drive to store data and projects efficiently.
It also covers fixing common permission, repository, and Java issues for Ubuntu 20.04 (Focal).

🪟 1. Check your WSL & Ubuntu version
lsb_release -a


Expected output:

Distributor ID: Ubuntu
Description:    Ubuntu 20.04.5 LTS
Codename:       focal


If your version is older, update Ubuntu from the Microsoft Store.

⚙️ 2. Enable correct permissions on Windows drives

Edit your WSL configuration file:

sudo nano /etc/wsl.conf


Paste the following:

[boot]
systemd=true

[user]
default=windowsUser

[automount]
options = "metadata,umask=22,fmask=11"


Save and exit with
👉 Ctrl + O, then Enter, then Ctrl + X

Restart WSL:

wsl --shutdown


Then reopen your Ubuntu terminal.

🧰 3. Update packages and install dependencies
sudo apt update && sudo apt upgrade -y
sudo apt install python3 python3-pip python3.12-venv openjdk-17-jdk -y


PySpark requires a JVM — Java 17 is the recommended stable choice.

🧱 4. Create a working folder on D:

This keeps heavy data away from your C: drive.

cd /mnt/d
mkdir pyspark_env
cd pyspark_env

🧪 5. Create and activate a Python virtual environment

⚠️ If you get errors like Operation not permitted,
it’s because /mnt/d is an NTFS partition.
So instead, create the venv in your Linux home directory:

cd ~
python3 -m venv pyspark_env
source ~/pyspark_env/bin/activate


You should now see:

(pyspark_env) emma@DESKTOP-V15S0QQ:~$

🧱 6. Install required Python packages
pip install --upgrade pip
pip install wheel
pip install pyspark


If you see:

ERROR: Failed building wheel for pyspark
Successfully installed py4j-0.10.9.7 pyspark-3.5.7


✅ You’re fine — PySpark is installed correctly.

🧩 7. Verify installation
python -c "import pyspark; print(pyspark.__version__)"


Expected output:

3.5.7

☕ 8. Test PySpark manually

Try running this in Python:

from pyspark.sql import SparkSession

spark = SparkSession.builder \
.appName("WSL Test") \
.master("local[*]") \
.getOrCreate()

print("Spark version:", spark.version)

spark.stop()


Expected output:

Spark version: 3.5.7

🌍 9. (Optional) Use a faster mirror for apt

If Ubuntu repositories are slow or outdated:

sudo nano /etc/apt/sources.list


Replace with one of these mirrors:

🇫🇷 Mirror Geek On Web Network

deb https://mirror.geekonweb.fr/ubuntu/ focal main restricted universe multiverse
deb-src https://mirror.geekonweb.fr/ubuntu/ focal main restricted universe multiverse
deb https://mirror.geekonweb.fr/ubuntu/ focal-updates main restricted universe multiverse
deb https://mirror.geekonweb.fr/ubuntu/ focal-security main restricted universe multiverse


or 🇫🇷 Mirror Scaleway

deb https://ubuntu.lafibre.info/ubuntu/ focal main restricted universe multiverse
deb-src https://ubuntu.lafibre.info/ubuntu/ focal main restricted universe multiverse
deb https://ubuntu.lafibre.info/ubuntu/ focal-updates main restricted universe multiverse
deb https://ubuntu.lafibre.info/ubuntu/ focal-security main restricted universe multiverse


Then refresh:

sudo apt update

🧹 10. Launch your PGN cleaner script

From your project directory:

cd /mnt/d/eclipse_wkspace/chess-replay-parent/pgn-cleaner
source ~/pyspark_env/bin/activate
python3 clean_pgn.py "/mnt/c/Users/windowsUser/Desktop/games1990/games_1990.pgn"


Expected output:

✅ Nettoyage terminé.
📁 Fichier sauvegardé : /mnt/c/Users/windowsUser/Desktop/games1990/games_1990_cleaned.pgn

🧭 11. Useful maintenance commands

Deactivate environment:

deactivate


Reinstall PySpark:

pip uninstall pyspark -y && pip install pyspark


Upgrade PySpark later:

pip install --upgrade pyspark

✅ Final Summary
Component	Status
Ubuntu WSL	20.04 or later
Java	17 (OpenJDK)
Python	3.12 + venv
PySpark	3.5.7
Virtual env	~/pyspark_env
Tested with	.pgn cleaner script
Output	/mnt/c/Users/windowsUser/Desktop/games1990/games_1990_cleaned.pgn