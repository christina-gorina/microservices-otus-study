Как внести изменение в приложение и запустить с деплоен на dockerhub

ВНЕСТИ ИЗМЕНЕНИЯ В jar И СБИЛДИТЬ

kubectl create namespace homework-8

cd "C:\Kris\CodeBase\Сourses\Otus\Microservices\homeworks\microservices-otus-study\homework-8\usersformetrics"

docker build -t  homework-8:1.0.0 .

docker tag homework-8:1.0.0 christinagorina/homework-8:1.0.0

docker push christinagorina/homework-8:1.0.0

docker pull christinagorina/homework-8:1.0.0

cd "C:\Kris\CodeBase\Сourses\Otus\Microservices\homeworks\microservices-otus-study\homework-8\files\appUsers"

ПОПРАВИТЬ ВЕРСИЮ В DEPLOYMENT

kubectl apply -f .

kubectl -n homework-8 logs ИМЯ ПОДА

http://arch.homework/api/v1/health

----------------------------------------------
База postgres уже должна быть в кубе, как ее установить, смотри instruction.txt из hw6
Если что, непосредственно к БД через консоль можно подключиться так
export POSTGRES_USER_PASSWORD=$(kubectl get secret --namespace postgres hw6-db-secrets -o jsonpath="{.data.userPwd}" | base64 -d)
echo $POSTGRES_USER_PASSWORD  вывсти эту переменную
winpty приставку писать только если заходим через bash под виндой
winpty kubectl run otus-postgresql-client --rm --tty -i --restart='Never' --namespace postgres --image docker.io/bitnami/postgresql:14.4.0-debian-11-r13 --env="PGPASSWORD=$POSTGRES_USER_PASSWORD"  --command -- psql --host psqldb-postgresql -U regUser -d usershw6 -p 5432

Провалиться внутрь контейнера с БД
winpty kubectl -n postgres exec -ti otus-postgresql-0 bash

приложение генерит свой секреты типа sh.helm.release.v1........, посмотреть их можно так
kubectl --namespace postgres get secrets sh.helm.release.v1.otus.v2 -o jsonpath='{.data.release}' | base64 -d | base64 -d | gzip -d


