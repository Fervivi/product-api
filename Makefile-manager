# Makefile for Spring Boot Microservices v0.1-manager

DEBUG_PORT ?= 5005

.PHONY: run clean

default: run

help:
	@echo "Available targets:"
	@echo "  run    Start the project through Maven"
	@echo "  clean  Clean the project"
	@echo "  debug  Start the project through Maven with enabled debug on port $(DEBUG_PORT)"

run:
	./mvnw spring-boot:run

clean:
	docker compose down -v
	./mvnw clean

debug:
	./mvnw spring-boot:run -Pdebug
