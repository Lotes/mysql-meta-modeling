sudo docker run --name mmm -v ~/Projekte/00_ACTIVE/mysql-meta-modeling/data:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7.23
sudo docker start mmm
