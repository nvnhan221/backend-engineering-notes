.PHONY: help install serve build deploy clean

help: ## Show this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Available targets:'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

install: ## Install dependencies
	pip install -r requirements.txt

serve: ## Start development server
	mkdocs serve

build: ## Build static site
	mkdocs build

deploy: ## Deploy to GitHub Pages
	mkdocs gh-deploy --force

clean: ## Clean build artifacts
	rm -rf site/
	rm -rf .mkdocs_cache/
