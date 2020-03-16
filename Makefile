builder:
	docker build -t banka/mvn-builder:latest --cache-from banka/mvn-builder:latest . -f Dockerfile-dep

start:
	docker-compose up --build

dev:
	docker-compose  -f docker-compose-dev.yml up --build