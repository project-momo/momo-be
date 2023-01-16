# 가동중인 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=momoproject" | grep -q . && docker stop momoproject && docker rm momoproject | true

# 기존 이미지 삭제
sudo docker rmi dodannnn/momoproject:1.0

# 도커허브 이미지 pull
sudo docker pull dodannnn/momoproject:1.0

# 도커 run
docker run -d -p 8080:8080 --network my-network -v /home/ec2-user/here:/config -e "TZ=Asia/Seoul" --name momoproject dodannnn/momoproject:1.0

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않음
docker rmi -f $(docker images -f "dangling=true" -q) || true
