horbar_addr=$1
horbar_repo=$2
project=$3
version=$4
container_port=$5
host_port=$6


imageName=$horbar_addr/$horbar_repo/$project:$version

echo $imageName

containerId=`docker ps -a | grep ${project} | awk '{print $1}'`

echo $containerId

if [ "$containerId" != "" ] ; then
    docker stop $containerId
    docker rm $containerId
fi

tag=`docker images | grep ${project} | awk '{print $2}'`

echo $tag

if [[ "$tag" =~ "$version" ]] ; then
    docker rmi $imageName
fi

docker login -u admin -p Harbor12345 $horbar_addr

docker pull $imageName

docker run -d -p $host_port:$container_port --name $project $imageName

echo "SUCCESS"
